package com.nchl.JWT.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreditorRoleDto {
    private Long id;
    private String name;
    private boolean enable;
    private String allowedAction;

}
