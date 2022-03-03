package it.unibo.disi.harvesteradapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, "eu.arrowhead.common", "it.unibo"})
public class HarvesterAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(HarvesterAdapterApplication.class, args);
    }
}