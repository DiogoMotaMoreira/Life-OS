package com.lifeos.db.models;

import java.time.LocalDate;

public record Project(
    Integer id,
    String titulo,
    LocalDate criado_em
) {
    // Construtor para criar novas tarefas
    public Project(String titulo) {
        this(null, titulo, null);
    }
}