package myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"myapp.repository"})
@ComponentScan(basePackages = {"myapp.controller", "myapp.auth", "myapp.service"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
