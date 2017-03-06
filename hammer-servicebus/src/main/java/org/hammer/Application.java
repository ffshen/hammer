package org.hammer;


import io.vertx.core.Vertx;

import org.hammer.proxy.verticle.ProxyVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
	

  @Autowired
  private ProxyVerticle proxyVerticle ;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void deployVerticle() {
    Vertx.vertx().deployVerticle(proxyVerticle);
  }
}
