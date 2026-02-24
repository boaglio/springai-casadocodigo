package com.boaglio.cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bilheteria_anos_80")
public class Filme {

  @Id
  private long id;
  private String titulo;
  private int ano;
  @Column(name="bilheteria_mundial_usd")
  private long bilheteria;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public long getBilheteria() {
        return bilheteria;
    }

    public void setBilheteria(long bilheteria) {
        this.bilheteria = bilheteria;
    }
}