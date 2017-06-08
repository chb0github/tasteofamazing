package org.bongiorno.sdrss;

import lombok.RequiredArgsConstructor;
import org.bongiorno.sdrss.domain.security.*;
import org.bongiorno.sdrss.repositories.security.*;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.bongiorno.sdrss.domain.security.AclEntry.Permission.*;

@SpringBootApplication
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SdrExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdrExampleApplication.class, args);
    }

    @Configuration
    @RequiredArgsConstructor
    public static class ValidationConfiguration extends RepositoryRestConfigurerAdapter {

        private final Validator jsr303Validator;

        @Override
        public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
            //bean validation always before save and create
            validatingListener.addValidator("beforeCreate", jsr303Validator);
            validatingListener.addValidator("beforeSave", jsr303Validator);

        }


    }

    @Component
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class PublishOnCreateEventListener extends AbstractRepositoryEventListener {

        private final AclObjectIdentityRepository objects;
        private final AclEntryRepository aclEntries;

        @Override
        protected void onBeforeSave(Object entity) {
            System.out.println("*********Saving " + entity);
        }

        @Override
        protected void onAfterCreate(Object entity) {
            System.out.format("*********Created %s%n", entity);
        }

        @Override
        protected void onBeforeCreate(Object entity) {
            System.out.println(entity);
        }

        @Override
        protected void onAfterSave(Object entity) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AclSid aclSid = new AclSid(true, authentication.getPrincipal() + "");
            AclObjectIdentity objectEntry = objects.save(new AclObjectIdentity(entity.getClass(), ((Identifiable<Long>) entity).getId(), aclSid));

            aclEntries.save(new AclEntry(objectEntry,aclSid, READ,WRITE));
        }
    }

}
