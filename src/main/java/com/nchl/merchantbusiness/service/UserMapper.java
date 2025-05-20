package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.CreditorUserDto;
import com.nchl.merchantbusiness.model.CreditorUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    CreditorUserDto toDto(CreditorUser user);
}