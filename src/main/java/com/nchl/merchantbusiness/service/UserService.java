package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.dto.CreditorUserDto;
import com.nchl.merchantbusiness.exception.UserNotFoundException;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.entity.CreditorUserRoleMap;
import com.nchl.merchantbusiness.constant.ResponseCode;
import com.nchl.merchantbusiness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {

                    Set<GrantedAuthority> authorities = user.getCreditorUserRoleMap().stream()
                            .filter(Objects::nonNull)
                            .map(CreditorUserRoleMap::getCreditorRole)
                            .filter(Objects::nonNull)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                            .collect(Collectors.toSet());

                    if (authorities.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    return new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public APIResponse<List<CreditorUserDto>> getAllUsers() {
        try {
            List<CreditorUserDto> users = userRepository.findAll()
                    .stream()
                    .map(userMapper::toDto)
                    .toList();

            return APIResponse.<List<CreditorUserDto>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Users retrieved successfully")
                    .data(users)
                    .timestamp(Instant.now())
                    .build();

        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            return APIResponse.<List<CreditorUserDto>>builder()
                    .code(ResponseCode.SERVER_ERROR.getCode())
                    .message("Failed to retrieve users")
                    .timestamp(Instant.now())
                    .build();
        }
    }

    public APIResponse<CreditorUserDto> getUserById(Integer id) {
        log.info("🟠 Service fetching user by ID: {}", id);
        try {

            CreditorUserDto userDto = userRepository.findById(id)
                    .map(userMapper::toDto)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            return APIResponse.<CreditorUserDto>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("User retrieved successfully")
                    .data(userDto)
                    .timestamp(Instant.now())
                    .build();

        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return APIResponse.<CreditorUserDto>builder()
                    .code(ResponseCode.NOT_FOUND.getCode())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return APIResponse.<CreditorUserDto>builder()
                    .code(ResponseCode.SERVER_ERROR.getCode())
                    .message("Failed to retrieve user")
                    .build();
        }
    }

    public APIResponse<CreditorUser> findUserEntityById(Integer id) {
        try {
            CreditorUser user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return APIResponse.<CreditorUser>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("User entity retrieved successfully")
                    .data(user)
                    .build();

        } catch (UserNotFoundException e) {
            return APIResponse.<CreditorUser>builder()
                    .code(ResponseCode.NOT_FOUND.getCode())
                    .message(e.getMessage())
                    .build();
        }
    }
}
