package org.hammer;

import io.vertx.core.Vertx;

import org.hammer.wfl.rule.WflRule;
import org.hammer.wfl.verticle.HttpVerticle;
import org.hammer.wfl.verticle.IOrderVerticle;
import org.hammer.wfl.verticle.IStepVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;;

@SpringBootApplication
public class Application {
	
  @Autowired
  Vertx vertx;
  
  @Autowired
  WflRule rule ;

  @Autowired
  private HttpVerticle httpVerticle ;
  @Autowired
  private IOrderVerticle orderVerticle ;
  @Autowired
  private IStepVerticle stepVerticle ;
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void deployVerticle() { 
	    vertx.deployVerticle(httpVerticle);
	    vertx.deployVerticle(orderVerticle);
	    vertx.deployVerticle(stepVerticle);
  }
}
