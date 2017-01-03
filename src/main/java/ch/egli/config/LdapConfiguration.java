/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.egli.config;

import ch.egli.auth.CachingLdapAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
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
        contextSource.setBase("dc=example,dc=com");
        contextSource.setUserDn(env.getRequiredProperty("ldap.managerDn"));
        contextSource.setPassword(env.getRequiredProperty("ldap.managerPassword"));
        return contextSource;
    }

    @Bean
    FilterBasedLdapUserSearch userSearch() {
        return new FilterBasedLdapUserSearch("ou=people", "(uid={0})", contextSource());
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
        DefaultLdapAuthoritiesPopulator ldapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource(), "ou=groups");
        ldapAuthoritiesPopulator.setGroupRoleAttribute("ou");
        return ldapAuthoritiesPopulator;
    }

    @Bean
    public CachingLdapAuthenticationProvider ldapAuthenticationProvider() {
        return new CachingLdapAuthenticationProvider(bindAuthenticator(), ldapAuthoritiesPopulator());
    }

/*
    @Bean
    public LdapAuthenticationProvider authenticationProvider() {
        return new LdapAuthenticationProvider(bindAuthenticator(), ldapAuthoritiesPopulator());
    }
*/

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("ehcache-auth.xml"));
        factory.setShared(true);
        return factory;
    }

}
