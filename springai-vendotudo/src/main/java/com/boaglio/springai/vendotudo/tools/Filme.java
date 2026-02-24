package com.boaglio.springai.vendotudo.tools;

public record Filme(
        Integer id,
        String titulo,
        Integer ano,
        String atoresPrincipais
) {
    public static class Builder {
        private Integer id;
        private String titulo;
        private Integer ano;
        private String atoresPrincipais;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder ano(Integer ano) {
            this.ano = ano;
            return this;
        }

        public Builder atoresPrincipais(String atoresPrincipais) {
            this.atoresPrincipais = atoresPrincipais;
            return this;
        }

        public Filme build() {
            return new Filme(id, titulo, ano, atoresPrincipais);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}