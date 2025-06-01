package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.dto.CreditorUserDto;
import com.nchl.merchantbusiness.exception.UserNotFoundException;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.entity.CreditorUserRoleMap;
import com.nchl.merchantbusiness.constant.ResponseDetail;
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
import java.time.LocalDateTime;
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
                    .code(ResponseDetail.SUCCESS.getRespCode())
                    .message("Users retrieved successfully")
                    .data(users)
                    .build();

        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            return APIResponse.<List<CreditorUserDto>>builder()
                    .code(ResponseDetail.SERVER_ERROR.getRespCode())
                    .message("Failed to retrieve users")
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
                    .code(ResponseDetail.SUCCESS.getRespCode())
                    .message("User retrieved successfully")
                    .data(userDto)
                    .timeStamp(String.valueOf(LocalDateTime.now()))
                    .build();

        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return APIResponse.<CreditorUserDto>builder()
                    .code(ResponseDetail.NOT_FOUND.getRespCode())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return APIResponse.<CreditorUserDto>builder()
                    .code(ResponseDetail.SERVER_ERROR.getRespCode())
                    .message("Failed to retrieve user")
                    .build();
        }
    }

    public APIResponse<CreditorUser> findUserEntityById(Integer id) {
        try {
            CreditorUser user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return APIResponse.<CreditorUser>builder()
                    .code(ResponseDetail.SUCCESS.getRespCode())
                    .message("User entity retrieved successfully")
                    .data(user)
                    .build();

        } catch (UserNotFoundException e) {
            return APIResponse.<CreditorUser>builder()
                    .code(ResponseDetail.NOT_FOUND.getRespCode())
                    .message(e.getMessage())
                    .build();
        }
    }
}
