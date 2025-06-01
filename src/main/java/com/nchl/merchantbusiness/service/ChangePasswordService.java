package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.entity.ChangePasswordRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChangePasswordService {
    APIResponse changePassword(ChangePasswordRequest request);
}
