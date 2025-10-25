package com.lifeos.db.models;

import java.time.LocalDate;

public record Task(
    Integer id,
    String titulo,
    String descricao,
    String status,
    String projeto,
    int prioridade,
    LocalDate dataLimite
) {
    // Construtor para criar novas tarefas
    public Task(String titulo, String descricao, String projeto, int prioridade) {
        this(null, titulo, descricao, "pendente", projeto, prioridade, null);
    }
}