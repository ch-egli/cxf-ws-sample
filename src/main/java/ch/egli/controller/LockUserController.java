package ch.egli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Describe
 *
 * @author Christian Egli
 * @since 1/3/17.
 */
@RestController
public class LockUserController {

    @Autowired
    private LdapAuthenticationProvider authenticationProvider;

    @RequestMapping("/lock")
    public String lockUser(@RequestParam(value = "name", defaultValue = "ben") String username) {

        for (int i = 1; i < 21; i++) {
            try {
                Authentication authRequest = new UsernamePasswordAuthenticationToken(username, "wrong_password");
                authenticationProvider.authenticate(authRequest);
                System.out.println("### user authenticated!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("##### " + i + " " + e.getMessage());
            }
        }

        return "Hopefully user '" + username + "' is locked... :-)";
    }

}
