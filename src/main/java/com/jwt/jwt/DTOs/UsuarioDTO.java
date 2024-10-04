package com.jwt.jwt.DTOs;

import com.jwt.jwt.enums.Role;


public record UsuarioDTO(String login, String senha, Role role) {}
