package com.lifeos.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.lifeos.db.DatabaseManager;
import com.lifeos.db.models.Task;

public class TaskService {

    public List<Task> getPendingTasks() {
        String sql = "SELECT * FROM tarefas WHERE status = 'Pendente' ORDER BY prioridade DESC";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                tasks.add(new Task(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("status"),
                    rs.getString("projeto"),
                    rs.getInt("prioridade"),
                    rs.getDate("dataLimite") != null ? rs.getDate("dataLimite").toLocalDate() : null
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void addTask(String titulo, String descricao, String status, int projeto, int prioridade) {
        String sql = "INSERT INTO tarefas (titulo, descricao, status, projeto, prioridade) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titulo);
            pstmt.setString(2, descricao);
            pstmt.setString(3, status);
            pstmt.setInt(4, projeto);
            pstmt.setInt(5, prioridade);
            int linhasAfetadas = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ... (métodos para addTask, completeTask, etc.)

}
