package org.bongiorno.sdrss;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import java.util.Map;

@SpringBootApplication
@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SdrExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdrExampleApplication.class, args);
    }

    @Configuration
    public static class ValidationConfiguration extends RepositoryRestConfigurerAdapter {

        @Autowired
        private Validator jsr303Validator;

        @Override
        public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
            //bean validation always before save and create
            validatingListener.addValidator("beforeCreate", jsr303Validator);
            validatingListener.addValidator("beforeSave", jsr303Validator);

        }


    }

    @Component
    public static class PublishOnCreateEventListener extends AbstractRepositoryEventListener {
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
    }

}
