package com.jwt.jwt.DTOs;

import com.jwt.jwt.enums.Role;

public record UsuarioResponseDto(String login, Role role) {}
