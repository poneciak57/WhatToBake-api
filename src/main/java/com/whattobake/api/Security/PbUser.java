package com.whattobake.api.Security;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PbUser {
    private String id;
    private String username;
    private String email;
    private String name;
    private List<String> roles;
}
