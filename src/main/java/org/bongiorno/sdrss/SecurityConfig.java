package org.bongiorno.sdrss;

import lombok.RequiredArgsConstructor;
import org.bongiorno.sdrss.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    @Component
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final SecurityExpressionHandler<FilterInvocation> webExpressionHandler;

        @Autowired
        public void configureGlobalSecurity(UserDetailsService uds, AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(uds);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().authorizeRequests().//
                    antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN").//
                    antMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN").//
                    antMatchers(HttpMethod.GET, "/users/**").authenticated().//
                    antMatchers(HttpMethod.GET, "/authorities/**").hasRole("ADMIN").//
                    antMatchers(HttpMethod.PATCH, "/users/**").authenticated().//
                    antMatchers(HttpMethod.GET, "/candidates/**").authenticated().
                    antMatchers(HttpMethod.GET, "/reports/**").authenticated().
                    antMatchers(HttpMethod.GET, "/forms/**").hasRole("USER").and().//
                    csrf().disable();
        }
    }

    @Bean
    UserDetailsService uds(UserRepository users) {
        return users::findOne;
    }

    @Bean
    RoleHierarchyVoter hierarchyVoter(RoleHierarchy roleHierarchy) {

        return new RoleHierarchyVoter(roleHierarchy);
    }

    @Bean
    SecurityExpressionHandler<FilterInvocation> webExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER  ROLE_USER > ROLE_VISITOR");
        return  roleHierarchy;
    }

    //    http://krams915.blogspot.com/2011/01/spring-security-3-full-acl-tutorial_30.html
    /*
    public AclAuthorizationStrategyImpl(GrantedAuthority[] auths)
And here's what auths represent:
auths - an array of GrantedAuthoritys that have special permissions (index 0 is the authority needed to change ownership, index 1 is the authority needed to modify auditing details, index 2 is the authority needed to change other ACL and ACE details)

     */
    @Bean
    AclPermissionEvaluator aclEvaluator(DataSource source, LookupStrategy lookupStrategy) {
        return new AclPermissionEvaluator(new JdbcAclService(source, lookupStrategy));
    }

    @Bean
    LookupStrategy lookupStrategy(DataSource source, AclCache aclCache, AclAuthorizationStrategy aclStrategy, AuditLogger logger) {
        return new BasicLookupStrategy(source, aclCache, aclStrategy, logger);
    }

    @Bean
    AclCache aclCache(AclAuthorizationStrategy aclStrategy, AuditLogger auditLogger) {
        return new SpringCacheBasedAclCache(new ConcurrentMapCache("aclcache"),
                new DefaultPermissionGrantingStrategy(auditLogger), aclStrategy);
    }

    @Bean
    AclAuthorizationStrategy aclStrategy() {
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        return new AclAuthorizationStrategyImpl(admin, admin, admin);
    }

    @Bean
    AuditLogger auditLogger() {
        return new ConsoleAuditLogger();
    }
}
