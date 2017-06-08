package org.bongiorno.sdrss;

import java.util.Set;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.bongiorno.sdrss.domain.security.AclClass;
import org.bongiorno.sdrss.repositories.security.AclClassRepository;
import org.bongiorno.sdrss.repositories.security.AclSidRepository;
import org.bongiorno.sdrss.repositories.security.UserRepository;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @RequiredArgsConstructor
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        private final UserRepository userRepo;

        private final AclClassRepository classRepo;

        private final AclSidRepository aclSidRepo;

        @PostConstruct
        @Profile("init")
        private void init() {
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("system", "system",
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
//
//            aclSidRepo.save(new AclSid(true,"root"));
//            aclSidRepo.save(new AclSid(true,"christian"));
//            aclSidRepo.save(new AclSid(true,"gail"));

            Iterable<AclClass> all = classRepo.findAll();
            Reflections reflections = new Reflections(this.getClass().getPackage().getName());

            Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
            for (AclClass aclClass : all) {
                entities.remove(aclClass.getClazz());
            }
            entities.stream().map(AclClass::new).forEach(classRepo::save);

            SecurityContextHolder.clearContext();

        }
        @Autowired
        public void configureGlobalSecurity(UserDetailsService uds, AuthenticationManagerBuilder auth,
                                            PasswordEncoder encoder, AuthenticationProvider authProvider) throws Exception {
            auth.userDetailsService(uds).passwordEncoder(encoder);
            auth.authenticationProvider(authProvider);

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().authorizeRequests().//
                    antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN").//
                    antMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN").//
                    antMatchers(HttpMethod.GET, "/**").authenticated().//
                    antMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN").//
                    and().csrf().disable();
        }

    }

    @Bean
    PasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder pwdEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(pwdEncoder);
        return authenticationProvider;
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
        roleHierarchy.setHierarchy("ROLE_ROOT > ROLE_ADMIN ROLE_ADMIN > ROLE_USER  ROLE_USER > ROLE_VISITOR");
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
