package com.jwt.jwt.controllers;

import com.jwt.jwt.DTOs.ProdutoDto;
import com.jwt.jwt.entities.Produto;
import com.jwt.jwt.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/produto")
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();

        return ResponseEntity.status(200).body(produtos);
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto( @RequestBody ProdutoDto produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());

        try {
            Produto novoProduto = produtoRepository.save(produto);
            return ResponseEntity.status(201).body(novoProduto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id,  @RequestBody ProdutoDto produtoDTO) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDTO.nome());
                    produto.setDescricao(produtoDTO.descricao());
                    produto.setPreco(produtoDTO.preco());

                    Produto produtoAtualizado = produtoRepository.save(produto);
                    return ResponseEntity.status(200).body(produtoAtualizado);
                })
                .orElse(ResponseEntity.status(404).body(null));
    }
}