package com.lifeos.ui;

import java.util.List;
import java.util.regex.Pattern;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.lifeos.db.models.Task;
import com.lifeos.services.TaskService;

public class TodoScreen extends Panel {

    private final TaskService taskService;
    private final ActionListBox taskListBox;

    public TodoScreen() {
        super(new BorderLayout());
        this.taskService = new TaskService();
        this.taskListBox = new ActionListBox();

        // Painel de input
        Panel inputPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        TextBox newTaskBox = new TextBox().setValidationPattern(Pattern.compile(".+")); // Não pode ser vazio
        inputPanel.addComponent(newTaskBox.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));
        inputPanel.addComponent(new Button("Add", () -> {
            addTask(newTaskBox.getText());
            newTaskBox.setText("");
        }));
        
        addComponent(inputPanel, BorderLayout.Location.TOP);

        // Lista de tarefas
        loadTasks();
        addComponent(this.taskListBox, BorderLayout.Location.CENTER);
    }

    private void loadTasks() {
        taskListBox.clearItems();
        List<Task> tasks = taskService.getPendingTasks();
        for (Task task : tasks) {
            // Adiciona uma "ação" (Runnable) a cada item da lista
            taskListBox.addItem(
                String.format("[P%d] %s", task.prioridade(), task.descricao()),
                () -> markTaskAsDone(task) // Ação a correr ao pressionar "Enter"
            );
        }
    }

    private void addTask(String description) {
        // Lógica para chamar o taskService.addTask(...)
        // ...
        loadTasks(); // Recarrega a lista
    }

    private void markTaskAsDone(Task task) {
        // Lógica para chamar o taskService.completeTask(task.id())
        // ...
        loadTasks(); // Recarrega a lista
        // Mostra uma popup de confirmação
        MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Sucesso", "Tarefa completada!");
    }
}