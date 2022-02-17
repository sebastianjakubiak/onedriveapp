package com.onedrive.app;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    var app = new SpringApplication(Application.class);
    app.setDefaultProperties(Collections
        .singletonMap("server.port", "8082"));
    app.run(args);
  }
}
