package com.iworks.hateoas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

@ComponentScan
@EnableAutoConfiguration
@EnableHypermediaSupport(type = {HypermediaType.HAL})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}