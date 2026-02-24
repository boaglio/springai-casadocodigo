package com.boaglio.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmeRepository extends JpaRepository<Filme,Long> {

    List<Filme> findTop20ByOrderByBilheteriaDesc();

    Optional<Filme> findByTituloIgnoreCase(String titulo);
}