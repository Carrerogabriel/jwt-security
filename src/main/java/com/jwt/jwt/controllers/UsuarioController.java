package com.jwt.jwt.controllers;

import com.jwt.jwt.DTOs.UsuarioDto;
import com.jwt.jwt.DTOs.UsuarioResponseDto;
import com.jwt.jwt.entities.Usuario;
import com.jwt.jwt.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<UsuarioResponseDto> responseList = usuarios.stream()
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getLogin(),
                        usuario.getRole()
                ))
                .toList();

        return ResponseEntity.status(200).body(responseList);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@RequestBody UsuarioDto usuarioDTO) {

        if(this.usuarioRepository.findByLogin(usuarioDTO.login()) != null) return ResponseEntity.status(400).build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.senha());

        Usuario usuario = new Usuario();
        usuario.setLogin(usuarioDTO.login());
        usuario.setSenha(senhaCriptografada);
        usuario.setRole(usuarioDTO.role());

        try {
            Usuario novoUsuario = usuarioRepository.save(usuario);

            var responseDTO = new UsuarioResponseDto(
                    novoUsuario.getLogin(),
                    novoUsuario.getRole()
            );

            return ResponseEntity.status(201).body(responseDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{login}")
    public ResponseEntity<UsuarioResponseDto> listarPorLogin(@PathVariable String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario != null) {
            var responseDTO = new UsuarioResponseDto(
                    usuario.getLogin(),
                    usuario.getRole()
            );

            return ResponseEntity.status(200).body(responseDTO);
        }

        return ResponseEntity.status(404).body(null);
    }
}
