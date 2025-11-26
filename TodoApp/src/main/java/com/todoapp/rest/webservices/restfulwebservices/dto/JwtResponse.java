package com.todoapp.rest.webservices.restfulwebservices.dto;

public record JwtResponse(String token, String expiresAt, String type) {
}