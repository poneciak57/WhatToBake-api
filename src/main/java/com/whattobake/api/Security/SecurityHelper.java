package com.whattobake.api.Security;

import com.whattobake.api.Model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class SecurityHelper {
    static public User UserFromPrincipal(Principal principal){
        return (User)((UsernamePasswordAuthenticationToken)principal).getDetails();
    }
}
