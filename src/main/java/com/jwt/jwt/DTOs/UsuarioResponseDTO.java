package com.jwt.jwt.DTOs;

import com.jwt.jwt.enums.Role;

public record UsuarioResponseDTO(String login, Role role) {}
