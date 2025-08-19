package org.example.apigateway.config;

import org.example.apigateway.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
  @Value("${banks-service.url}") private String banksServiceUrl;
  @Value("${banks-service.id}") private String banksServiceId;
  @Value("${banks-service.path}") private String banksServicePath;

  @Value("${accounts-service.url}") private String accountsServiceUrl;
  @Value("${accounts-service.id}") private String accountsServiceId;
  @Value("${accounts-service.path}") private String accountsServicePath;

  @Value("${transactions-service.url}") private String transactionsServiceUrl;
  @Value("${transactions-service.id}") private String transactionsServiceId;
  @Value("${transactions-service.path}") private String transactionsServicePath;

  @Value("${auth-service.url}")
  private String authServiceUrl;
  @Value("${auth-service.id}")
  private String authServiceId;
  @Value("${auth-service.path}")
  private String authServicePath;

  private final JwtAuthenticationFilter filter;

  public RouteConfig(JwtAuthenticationFilter filter) {
    this.filter = filter;
  }


  @Bean
  public RouteLocator createRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route(banksServiceId, route -> route.path(banksServicePath)
                    .filters(gtf -> gtf.filter(filter)).uri(banksServiceUrl))
            .route(accountsServiceId, route -> route.path(accountsServicePath)
                    .filters(gtf -> gtf.filter(filter)).uri(accountsServiceUrl))
            .route(transactionsServiceId, route -> route.path(transactionsServicePath)
                    .filters(gtf -> gtf.filter(filter)).uri(transactionsServiceUrl))
            .route(authServiceId, route -> route.path(authServicePath).uri(authServiceUrl))
      .build();
  }
}
