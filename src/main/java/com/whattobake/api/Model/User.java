package com.whattobake.api.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whattobake.api.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;
import java.util.Map;

@Node("USER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String pbId;
    private String email;
    private String name;
    private String username;
    private List<UserRole> roles;

    public static User fromPBAction(Map<String,Object> map) throws JsonProcessingException {
        return User.builder()
                .pbId((String) map.get("id"))
                .email((String) map.get("email"))
                .name((String) map.get("name"))
                .username((String) map.get("username"))
                .roles((List<UserRole>) map.get("roles"))
                .build();
    }
}
