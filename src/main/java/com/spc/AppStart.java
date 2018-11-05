package com.spc;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages={"com.spc"})
@Configurable(autowire = Autowire.BY_NAME)    //定义bean的注入方式
@SpringBootApplication
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class, args);
    }
}
