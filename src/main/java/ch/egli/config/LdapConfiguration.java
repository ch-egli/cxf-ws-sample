/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.egli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

/**
 * @author u210150 (Christian Egli)
 * @since 01.12.2016
 */
@Configuration
public class LdapConfiguration {

    @Autowired
    Environment env;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.managerDn"));
        contextSource.setPassword(env.getRequiredProperty("ldap.managerPassword"));
        return contextSource;
    }

    @Bean
    FilterBasedLdapUserSearch userSearch() {
        return new FilterBasedLdapUserSearch("dc=example,dc=com", "(uid={0})", contextSource());
    }

    @Bean
    public BindAuthenticator bindAuthenticator() {
        //String[] userDnPatterns = {"uid={0}"};
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource());
        //bindAuthenticator.setUserDnPatterns(userDnPatterns);
        bindAuthenticator.setUserSearch(userSearch());
        return bindAuthenticator;
    }

    @Bean
    public DefaultLdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        DefaultLdapAuthoritiesPopulator ldapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource(), "dc=example,dc=com");
        ldapAuthoritiesPopulator.setGroupRoleAttribute("member");
        return ldapAuthoritiesPopulator;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        return new LdapAuthenticationProvider(bindAuthenticator(), ldapAuthoritiesPopulator());
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}
