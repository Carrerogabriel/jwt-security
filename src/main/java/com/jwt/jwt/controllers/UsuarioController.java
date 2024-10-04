package com.jwt.jwt.controllers;

import com.jwt.jwt.DTOs.UsuarioDTO;
import com.jwt.jwt.DTOs.UsuarioResponseDTO;
import com.jwt.jwt.entities.Usuario;
import com.jwt.jwt.repositories.UsuarioRepository;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<UsuarioResponseDTO> responseList = usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getLogin(),
                        usuario.getRole()
                ))
                .toList();

        return ResponseEntity.status(200).body(responseList);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario( @RequestBody UsuarioDTO usuarioDTO) {

        if(this.usuarioRepository.findByLogin(usuarioDTO.login()) != null) return ResponseEntity.status(400).build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.senha());

        Usuario usuario = new Usuario();
        usuario.setLogin(usuarioDTO.login());
        usuario.setSenha(senhaCriptografada);
        usuario.setRole(usuarioDTO.role());

        try {
            Usuario novoUsuario = usuarioRepository.save(usuario);

            var responseDTO = new UsuarioResponseDTO(
                    novoUsuario.getLogin(),
                    novoUsuario.getRole()
            );

            return ResponseEntity.status(201).body(responseDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{login}")
    public ResponseEntity<UsuarioResponseDTO> listarPorLogin(@PathVariable String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario != null) {
            var responseDTO = new UsuarioResponseDTO(
                    usuario.getLogin(),
                    usuario.getRole()
            );

            return ResponseEntity.status(200).body(responseDTO);
        }

        return ResponseEntity.status(404).body(null);
    }
}
