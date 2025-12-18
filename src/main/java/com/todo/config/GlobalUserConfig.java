package com.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.todo.models.entities.User;
import com.todo.ports.user.IUserCommandRepository;
import com.todo.ports.user.IUserQueryRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GlobalUserConfig {

  private IUserQueryRepository userQueryRepository;
  private IUserCommandRepository userCommandRepository;
  private PasswordEncoder passwordEncoder;

  private AppProperties appProperties;

  @PostConstruct
  public void createGlobalUser() {
    String email = appProperties.getGlobalUserEmail();
    String password = appProperties.getGlobalUserPassword();

    if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
      System.out.println("Variáveis de ambiente do usuário global não definidas ou vazias. Ignorando criação.");
      return;
    }

    if (userQueryRepository.findByEmail(email).isEmpty()) {
      User globalUser = new User();
      globalUser.setEmail(email);
      globalUser.setName("Admin Global");
      globalUser.setPassword(passwordEncoder.encode(password));
      userCommandRepository.save(globalUser);

      System.out.println("Usuário global criado!");
    } else {
      System.out.println("Usuário global já existe.");
    }
  }
}
