package com.lifeos;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.lifeos.db.DatabaseManager;
import com.lifeos.ui.AppWindow;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializa a Base de Dados
        DatabaseManager.initializeDatabase();

        // 2. Configura o Terminal e o Ecrã (Screen) do Lanterna
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            Terminal terminal = terminalFactory.createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen(); // Inicia o modo TUI

            // 3. Cria a "Window Based GUI" (o gestor de janelas)
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

            // 4. Cria a tua Janela Principal (definida em ui/AppWindow.java)
            AppWindow mainWindow = new AppWindow();
            
            // 5. Adiciona a janela à GUI e espera que feche
            gui.addWindowAndWait(mainWindow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}