package com.cards;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableEncryptableProperties
public class CardsApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
    }

}
