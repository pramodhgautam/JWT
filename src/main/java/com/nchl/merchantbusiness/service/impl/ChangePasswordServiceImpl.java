package com.nchl.merchantbusiness.service.impl;

import com.nchl.merchantbusiness.constant.ResponseDetail;
import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.entity.ChangePasswordRequest;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.exception.UserNotFoundException;
import com.nchl.merchantbusiness.repository.CreditorUserRepository;
import com.nchl.merchantbusiness.service.ChangePasswordService;
import com.nchl.merchantbusiness.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final CreditorUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public APIResponse changePassword(ChangePasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {

            return APIResponse.apiResponse(ResponseDetail.FAILURE,"New password and confirmation password don't match", Collections.emptyMap());
        }


        CreditorUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if ("Y".equalsIgnoreCase(user.getPasswordChangeStatus())) {

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setPasswordChangeStatus("N");
            userRepository.save(user);

            return APIResponse.apiResponse(ResponseDetail.SUCCESS,"First login password change completed successfully",Collections.emptyMap());
        }

        else {

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {

                return APIResponse.apiResponse(ResponseDetail.FAILURE,"Wrong credentials",Collections.emptyMap());
            }


            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return APIResponse.apiResponse(ResponseDetail.SUCCESS,"Password changed successfully",Collections.emptyMap());
        }
    }
}
