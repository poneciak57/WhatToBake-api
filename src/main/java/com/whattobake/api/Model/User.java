package com.whattobake.api.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whattobake.api.Dto.SecurityDto.PbUser;
import com.whattobake.api.Enum.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Node("USER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Principal {

    @Id
    @NotNull
    private String pbId;

    @NotNull
    @Size(max = 30)
    private String email;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 10)
    private String username;

    @NotNull
    private List<@Valid UserRole> roles;

    public static User fromPBAction(Map<String,Object> map) throws JsonProcessingException {
        return User.builder()
                .pbId((String) map.get("id"))
                .email((String) map.get("email"))
                .name((String) map.get("name"))
                .username((String) map.get("username"))
                .roles((List<UserRole>) map.get("roles"))
                .build();
    }

    public static User fromPbUser(PbUser pbUser) {
        return User.builder()
                .pbId(pbUser.getId())
                .username(pbUser.getUsername())
                .name(pbUser.getName())
                .email(pbUser.getEmail())
                .roles(pbUser.getRoles())
                .build();
    }

}
