package com.transmi.remun.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.ContractRepository;
import com.transmi.remun.backend.data.repositories.HistoryItemRepository;
import com.transmi.remun.backend.data.repositories.ParmRepository;
import com.transmi.remun.backend.data.repositories.UserRepository;
import com.transmi.remun.backend.service.UserService;
import com.transmi.remun.frontend.main.MainView;
import com.transmi.remun.frontend.security.SecurityConfiguration;

/**
 * The entry point of the Spring Boot application.
 */
/*
 * @SpringBootApplication
 * (
 */
@SpringBootApplication(
    scanBasePackageClasses = {
        SecurityConfiguration.class,
        MainView.class,
        Application.class,
        UserService.class },
    exclude = ErrorMvcAutoConfiguration.class,
    scanBasePackages = {
        "com.transmi.remun.backend.main",
        "com.transmi.remun.backend.data",
        "com.transmi.remun.backend.data.entity",
        "com.transmi.remun.backend.data.repositories",
        "com.transmi.remun.backend.service",
        "com.transmi.remun.backend.service.security",
        "com.transmi.remun.service.main",
        "com.transmi.remun.frontend.security",
        "com.transmi.remun.frontend.liquidador",
        "com.transmi.remun.frontend.main",
        "com.transmi.remun.frontend.init"
    })

@EnableJpaRepositories(
    basePackageClasses = {
        UserRepository.class,
        ContractRepository.class,
        ParmRepository.class,
        HistoryItemRepository.class })
@EntityScan(
    basePackages = {
        "com.transmi.remun.backend.main",
        "com.transmi.remun.backend.data",
        "com.transmi.remun.backend.data.entity",
        "com.transmi.remun.backend.data.repositories",
        "com.transmi.remun.backend.service",
        "com.transmi.remun.backend.service.security",
        "com.transmi.remun.service.main",
        "com.transmi.remun.frontend.security",
        "com.transmi.remun.frontend.liquidador",
        "com.transmi.remun.frontend.main",
        "com.transmi.remun.frontend.init"
    },
    basePackageClasses = { User.class })

public class Application extends SpringBootServletInitializer
{

  public static void main(String[] args) { SpringApplication.run(Application.class, args); }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { return application.sources(Application.class); }

}// Application
