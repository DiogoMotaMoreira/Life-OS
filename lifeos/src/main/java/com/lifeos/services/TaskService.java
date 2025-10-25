package com.lifeos.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lifeos.db.DatabaseManager;
import com.lifeos.db.models.Task;

public class TaskService {

    public List<Task> getPendingTasks() {
        String sql = "SELECT * FROM tarefas WHERE status = 'pendente' ORDER BY prioridade DESC";
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
                    rs.getDate("data_limite") != null ? rs.getDate("data_limite").toLocalDate() : null
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ... (métodos para addTask, completeTask, etc.)

}
