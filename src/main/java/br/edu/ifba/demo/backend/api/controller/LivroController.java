package br.edu.ifba.demo.backend.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifba.demo.backend.api.dto.LivroDTO;
import br.edu.ifba.demo.backend.api.model.LivroModel;
import br.edu.ifba.demo.backend.api.repository.LivroRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    //Metodo para adicionar um livro
    @PostMapping
    public ResponseEntity<LivroModel> addLivro(@RequestBody LivroModel livro) {
        System.out.println("Adicionando livro: " + livro);
        LivroModel savedLivro = livroRepository.save(livro);
        return new ResponseEntity<>(savedLivro, HttpStatus.CREATED);
    }

    //Metodo para listar todos os livros
    @GetMapping("/listall")
    public List<LivroModel> listall() {
        var livros = livroRepository.findAll();
        return livros;
    }

    //Metodo para buscar um livro por id
    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Long id) {
        var livro = livroRepository.findById(id);
        if (livro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LivroDTO.converter(livro.get()));
    }

    //Metodo para deletar um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!livroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        livroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Metodo para buscar um livro por isbn
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LivroDTO> buscarPorISBN(@PathVariable String isbn) {
        var livro = livroRepository.findByIsbn(isbn);
        if (livro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LivroDTO.converter(livro));
    }

    //Metodo para buscar um livro por titulo
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroDTO>> buscarPorTitulo(@PathVariable String titulo) {
        var livros = livroRepository.findByTituloContainingIgnoreCase(titulo);
        if (livros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LivroDTO.converter(livros));
    }
}
