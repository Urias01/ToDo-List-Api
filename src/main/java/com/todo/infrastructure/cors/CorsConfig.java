package com.todo.infrastructure.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.todo.config.AppProperties;

@Configuration
public class CorsConfig {

  private AppProperties appProperties;

  public CorsConfig(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(appProperties.getAllowedOrigins().split(","))
            .allowedMethods("*")
            .allowedHeaders("*");
      }
    };
  }
}