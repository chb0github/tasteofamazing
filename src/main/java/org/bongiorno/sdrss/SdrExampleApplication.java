package org.bongiorno.sdrss;

import static org.bongiorno.sdrss.domain.security.AclEntry.Permission.READ;
import static org.bongiorno.sdrss.domain.security.AclEntry.Permission.WRITE;

import lombok.RequiredArgsConstructor;
import org.bongiorno.sdrss.domain.resources.Candidate;
import org.bongiorno.sdrss.domain.security.AclEntry;
import org.bongiorno.sdrss.domain.security.AclObjectIdentity;
import org.bongiorno.sdrss.domain.security.AclSid;
import org.bongiorno.sdrss.repositories.security.AclEntryRepository;
import org.bongiorno.sdrss.repositories.security.AclObjectIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

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
    @RepositoryEventHandler
    public static class CanidateEventHandler {
        // 2 different ways to handle events
        @HandleBeforeCreate
        public void handleBeforeCreate(Candidate c) {
            System.out.println(c);
        }
    }
    // 2 different ways to handle events
    @Component
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class PublishOnCreateEventListener extends AbstractRepositoryEventListener<Identifiable<Long>> {

        private final AclObjectIdentityRepository objects;
        private final AclEntryRepository aclEntries;

        @Override
        protected void onBeforeSave(Identifiable<Long> entity) {
            System.out.println("*********Saving " + entity);
        }

        @Override
        protected void onAfterCreate(Identifiable<Long> entity) {
            System.out.format("*********Created %s%n", entity);
        }

        @Override
        protected void onBeforeCreate(Identifiable<Long> entity) {
            System.out.println(entity);
        }

        @Override
        protected void onAfterSave(Identifiable<Long> entity) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AclSid aclSid = new AclSid(true, authentication.getPrincipal() + "");
            AclObjectIdentity objectEntry = objects.save(new AclObjectIdentity(entity.getClass(), entity.getId(), aclSid));

            aclEntries.save(new AclEntry(objectEntry,aclSid, READ,WRITE));
        }
    }

}
