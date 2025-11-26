package com.todoapp.rest.webservices.restfulwebservices.jwtsecurty;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author SinghShubham
 */
@ConfigurationProperties("jwt")
public record JwtProperties(String issuer, long expirationMinutes) {

}
