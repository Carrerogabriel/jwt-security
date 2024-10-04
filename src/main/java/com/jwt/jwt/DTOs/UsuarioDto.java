package com.jwt.jwt.DTOs;

import com.jwt.jwt.enums.Role;


public record UsuarioDto(String login, String senha, Role role) {}
