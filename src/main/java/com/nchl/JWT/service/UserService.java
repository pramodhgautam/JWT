package com.nchl.JWT.service;

import com.nchl.JWT.dto.ApiResponse;
import com.nchl.JWT.dto.UserDto;
import com.nchl.JWT.exception.UserNotFoundException;
import com.nchl.JWT.model.CreditorUser;
import com.nchl.JWT.model.ResponseCode;
import com.nchl.JWT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ApiResponse<List<UserDto>> getAllUsers() {
        try {
            List<UserDto> users = userRepository.findAll()
                    .stream()
                    .map(userMapper::toDto)
                    .toList();

            return ApiResponse.<List<UserDto>>builder()
                    .responseCode(ResponseCode.SUCCESS.getCode())
                    .responseMessage("Users retrieved successfully")
                    .data(users)
                    .timestamp(Instant.now())
                    .build();

        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            return ApiResponse.<List<UserDto>>builder()
                    .responseCode(ResponseCode.SERVER_ERROR.getCode())
                    .responseMessage("Failed to retrieve users")
                    .timestamp(Instant.now())
                    .build();
        }
    }

    public ApiResponse<UserDto> getUserById(Integer id) {
        log.info("🟠 Service fetching user by ID: {}", id);
        try {
            UserDto userDto = userRepository.findById(id)
                    .map(userMapper::toDto)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            return ApiResponse.<UserDto>builder()
                    .responseCode(ResponseCode.SUCCESS.getCode())
                    .responseMessage("User retrieved successfully")
                    .data(userDto)
                    .timestamp(Instant.now())
                    .build();

        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return ApiResponse.<UserDto>builder()
                    .responseCode(ResponseCode.NOT_FOUND.getCode())
                    .responseMessage(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return ApiResponse.<UserDto>builder()
                    .responseCode(ResponseCode.SERVER_ERROR.getCode())
                    .responseMessage("Failed to retrieve user")
                    .build();
        }
    }

    // Additional helper method with builder pattern
    public ApiResponse<CreditorUser> findUserEntityById(Integer id) {
        try {
            CreditorUser user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return ApiResponse.<CreditorUser>builder()
                    .responseCode(ResponseCode.SUCCESS.getCode())
                    .responseMessage("User entity retrieved successfully")
                    .data(user)
                    .build();

        } catch (UserNotFoundException e) {
            return ApiResponse.<CreditorUser>builder()
                    .responseCode(ResponseCode.NOT_FOUND.getCode())
                    .responseMessage(e.getMessage())
                    .build();
        }
    }
}
