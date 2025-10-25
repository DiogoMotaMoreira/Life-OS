package com.lifeos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:life-os.db"; // O ficheiro da BD

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initializeDatabase() {
        String sqlNotas = """
            CREATE TABLE IF NOT EXISTS notas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                conteudo TEXT,
                criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String sqlProjetos = """
            CREATE TABLE IF NOT EXISTS projetos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;
        
        
        String sqlTarefas = """
                CREATE TABLE IF NOT EXISTS tarefas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    descricao TEXT,
                    status TEXT NOT NULL,
                    projeto INTEGER,
                    prioridade TEXT NOT NULL,
                    dataLimite DATE,
                    FOREIGN KEY (projeto) REFERENCES projetos(id)
                )
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlNotas);
            stmt.execute(sqlProjetos);
            stmt.execute(sqlTarefas);
            System.out.println("Base de dados inicializada com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}