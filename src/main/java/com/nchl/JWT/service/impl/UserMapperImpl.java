//package com.nchl.JWT.service.impl;
//
//import com.nchl.JWT.dto.CreditorUserDto;
//import com.nchl.JWT.model.CreditorUser;
//import javax.annotation.processing.Generated;
//
//import com.nchl.JWT.service.UserMapper;
//import org.springframework.stereotype.Component;
//
//@Generated(
//        value = "org.mapstruct.ap.MappingProcessor",
//        date = "2025-05-19T08:59:41+0545",
//        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
//)
//@Component
//public class UserMapperImpl implements UserMapper {
//
//    @Override
//    public CreditorUser toDto(CreditorUser user) {
//        if ( user == null ) {
//            return null;
//        }
//
//        CreditorUserDto.UserDtoBuilder userDto = CreditorUserDto.builder();
//
//        userDto.id( user.getId() );
//        userDto.firstname( user.getFirstname() );
//        userDto.lastname( user.getLastname() );
//        userDto.email( user.getEmail() );
//        userDto.role( user.getRole() );
//
//        return userDto.build();
//    }
//}
//
