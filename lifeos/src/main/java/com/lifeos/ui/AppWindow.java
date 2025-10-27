package com.lifeos.ui;

import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;

public class AppWindow extends BasicWindow {

    private Panel mainContentPanel; // O painel que vai mudar de conteúdo

    public AppWindow() {
        super("LIFE OS");
        setHints(Arrays.asList(Hint.CENTERED, Hint.EXPANDED));

        // Layout principal
        Panel mainPanel = new Panel(new BorderLayout());

        // 1. Menu Lateral (exemplo)
        Panel menuPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        menuPanel.addComponent(new Button("Dashboard", () -> showDashboard()));
        menuPanel.addComponent(new Button("Tarefas", () -> showTodoScreen()));
        menuPanel.addComponent(new Button("Notas", () -> showNotesScreen()));
        menuPanel.addComponent(new Button("Projetos"));
        menuPanel.addComponent(new Separator(Direction.HORIZONTAL));
        menuPanel.addComponent(new Button("Sair", () -> close()));
        
        mainPanel.addComponent(menuPanel, BorderLayout.Location.LEFT);

        // 2. Contentor Principal (onde os "ecrãs" vão)
        this.mainContentPanel = new Panel();
        mainPanel.addComponent(this.mainContentPanel, BorderLayout.Location.CENTER);

        // Define o ecrã inicial
        showDashboard();

        setComponent(mainPanel);
    }

    private void showDashboard() {
        mainContentPanel.removeAllComponents();
        mainContentPanel.addComponent(new Label("Bem-vindo ao teu Dashboard!"));
        // Aqui irias carregar um painel de "Dashboard" mais complexo
    }

    private void showTodoScreen() {
        mainContentPanel.removeAllComponents();
        mainContentPanel.addComponent(new TodoScreen());
    }

    private void showNotesScreen() {
        mainContentPanel.removeAllComponents();
        mainContentPanel.addComponent(new Label("Módulo de Notas (em construção)"));
    }

    private void showProjectsScreen() {
        mainContentPanel.removeAllComponents();
        mainContentPanel.addComponent(new Label("Os meus Projetos"));
        
    }
}