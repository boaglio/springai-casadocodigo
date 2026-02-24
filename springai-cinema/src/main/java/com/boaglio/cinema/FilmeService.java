package com.boaglio.cinema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FilmeService {

    private Logger log = LoggerFactory.getLogger(FilmeService.class.getName());

    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    public List<Filme> getAllMovies() {
        return filmeRepository.findTop20ByOrderByBilheteriaDesc();
    }

    @Tool(name = "isFamousMovie", description = "Check if it is a famous movie")
    public boolean isFamousMovie(
            @ToolParam(description = "the name of the movie") String titulo
    ) {

        log.info("Is "+titulo+" famous ?");

        if (Objects.isNull(titulo)) {
            log.info("No");
            return false;
        }

        var result =filmeRepository.findByTituloIgnoreCase(titulo.trim()).isPresent();

        log.info(" >> "+result);

        return result;
    }

}
