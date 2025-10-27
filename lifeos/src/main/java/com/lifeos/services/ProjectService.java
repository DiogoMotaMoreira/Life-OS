package com.lifeos.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.lifeos.db.DatabaseManager;
import com.lifeos.db.models.Project;

public class ProjectService {

    public List<Project> getProjects() {
        String sql = "Select * FROM projetos";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                projects.add(new Project(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getDate("criado_em") != null ? rs.getDate("criado_em").toLocalDate() : null
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }
}