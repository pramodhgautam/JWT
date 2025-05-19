package com.nchl.JWT.service;

import com.nchl.JWT.dto.CreditorUserDto;
import com.nchl.JWT.model.CreditorUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    CreditorUserDto toDto(CreditorUser user);
}