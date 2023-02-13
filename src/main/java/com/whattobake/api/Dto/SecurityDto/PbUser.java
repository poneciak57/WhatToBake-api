package com.whattobake.api.Dto.SecurityDto;

import com.whattobake.api.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PbUser {
    private String id;
    private String email;
    private String name;
    private String username;
    private List<UserRole> roles;
}
