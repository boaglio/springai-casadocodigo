package com.boaglio.cinema;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BilheteriaAPI {

    private final FilmeService filmeService;

    public BilheteriaAPI(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @Operation(summary = "Lista todos os filmes com bilheteria")
    @GetMapping("/api/filmes")
    public ResponseEntity<List> getAllMovies() {
        var filmes = filmeService.getAllMovies();
        return ResponseEntity.ok(filmes);
    }

    @Operation(summary = "Verifica se o filme é famoso")
    @PostMapping("/api/filme")
    public ResponseEntity<Boolean> isFamousMovie(@RequestBody String titulo) {
        var isFamousMovie = filmeService.isFamousMovie(titulo);
        return ResponseEntity.ok(isFamousMovie);
    }

}