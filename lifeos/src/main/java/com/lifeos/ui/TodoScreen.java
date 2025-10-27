package com.lifeos.ui;

import java.util.List;
import java.util.regex.Pattern;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import com.lifeos.db.models.Task;
import com.lifeos.db.models.Project;
import com.lifeos.services.TaskService;
import com.lifeos.services.ProjectService;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;

public class TodoScreen extends Panel {

    private final TaskService taskService;
    private final TaskService taskDoneService;
    private final ActionListBox taskListBox;
    private final ActionListBox taskDoneListBox;

    private final ProjectService projectService;

    // Campos de input do formulário
    private final TextBox tituloBox;
    private final TextBox descBox;
    private final TextBox projetoBox;
    private final TextBox prioridadeBox;

    private static int tamanhoHorizontalFormInput =50;

    private int projetosLength = 0;

    public TodoScreen() {

        super(new BorderLayout());
        this.taskService = new TaskService();
        this.taskDoneService = new TaskService();
        this.taskListBox = new ActionListBox();
        this.taskDoneListBox = new ActionListBox();

        this.projectService = new ProjectService();

        LinearLayout contentLayout = new LinearLayout();
        this.setLayoutManager(contentLayout);
        contentLayout.setSpacing(1);


        // ------------ FORM PANEL -----------------
        Panel formPanel = new Panel(new GridLayout(2));

        Panel gridPanel = new Panel(new GridLayout(2));
        formPanel.addComponent(gridPanel);

        gridPanel.addComponent(new Label("Inserir Nova Tarefa")); 
        gridPanel.addComponent(new Label(""));  

        //Título
        gridPanel.addComponent(new Label("Titulo* -> "));
        tituloBox = new TextBox().setPreferredSize(new TerminalSize(tamanhoHorizontalFormInput, 1)).setValidationPattern(Pattern.compile(".+"));
        gridPanel.addComponent(tituloBox.setLayoutData(GridLayout.createLayoutData(
        GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false)));

        // Descrição
        gridPanel.addComponent(new Label("Descricao -> "));
        descBox = new TextBox().setPreferredSize(new TerminalSize(tamanhoHorizontalFormInput, 1));
        gridPanel.addComponent(descBox.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false)));

        //ID Projeto
        gridPanel.addComponent(new Label("Projeto -> "));
        projetoBox = new TextBox().setPreferredSize(new TerminalSize(tamanhoHorizontalFormInput, 1));
        gridPanel.addComponent(projetoBox.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false)));

        //Prioridade
        gridPanel.addComponent(new Label("Prioridade* -> "));
        prioridadeBox = new TextBox().setPreferredSize(new TerminalSize(tamanhoHorizontalFormInput, 1));
        gridPanel.addComponent(prioridadeBox.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false)));


        Button addButton = new Button("add", this::handleTask);
        gridPanel.addComponent(addButton);
        
        // -------------- Instruction Painel --------------------
        Panel instPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        instPanel.addComponent(new Label("Instruções"));
        instPanel.addComponent(new Label("  * -> Elementos Obrigatórios"));
        instPanel.addComponent(new Label("  Prioridades: 1- Baixa  5- Elevada"));
        instPanel.addComponent(new Label("  Projetos (Digite o número identificador): "));
        loadProjects(instPanel);


        formPanel.addComponent(instPanel);

        addComponent(formPanel, BorderLayout.Location.TOP);


        // ---------------- Lista de Tarefas ------------
        Panel tarefasPanel = new Panel(new LinearLayout());
        tarefasPanel.addComponent(new Label("Tarefas: "));
        tarefasPanel.addComponent(this.taskListBox);

        Panel tarefasConcluidasPanel = new Panel(new LinearLayout());
        tarefasConcluidasPanel.addComponent(new Label("Tarefas Concluídas: "));
        tarefasConcluidasPanel.addComponent(this.taskDoneListBox);

        addComponent(tarefasPanel);
        addComponent(tarefasConcluidasPanel);

        loadTasks();
        

    }


    private void handleTask(){
        try{
            String titulo = tituloBox.getText();
            String descricao = descBox.getText();
            String status = "Pendente";
            int projeto = projetoBox.getText().isEmpty() ? 0 : Integer.parseInt(projetoBox.getText());
            int prioridade = prioridadeBox.getText().isEmpty() ? 0 : Integer.parseInt(prioridadeBox.getText());

            if (titulo.isEmpty()) {
                MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Erro de Validação", "O campo 'Título' é obrigatório.");
                return;
            }

            if (projeto < 0 || projeto > projectService.getProjects().size()) {
                MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Erro de Validação", "Número inválido em projeto");
                return;
            }

            if (prioridade < 0 || prioridade > 5) {
                MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Erro de Validação", "Número inválido em prioridade");
                return;
            }


            taskService.addTask(titulo, descricao, status, projeto, prioridade);

            tituloBox.setText("");
            descBox.setText("");
            projetoBox.setText("");
            prioridadeBox.setText("");

            loadTasks();

        } catch (NumberFormatException e) {
            // Isto acontece se o utilizador conseguir contornar a validação (ex: campo vazio que não devia)
            MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Erro de Input", "Projeto e Prioridade devem ser números válidos.");
        } catch (Exception e) {
            // Captura outros erros (ex: falha da BD)
            MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Erro Inesperado", "Não foi possível adicionar a tarefa: " + e.getMessage());
            e.printStackTrace(); // Debbug
        }
    }

    private void loadTasks() {
        taskListBox.clearItems();
        taskDoneListBox.clearItems();


        List<Task> tasks = taskService.getPendingTasks();
        for (Task task : tasks) {
            // Adiciona uma "ação" (Runnable) a cada item da lista
            taskListBox.addItem(
                String.format("[P%d] %s", task.prioridade(), task.titulo()),
                () -> showTaskOptions(task, false) // Ação a correr ao pressionar "Enter"
            );
        }


        List<Task> tasksDone = taskDoneService.getDoneTasks();
        for (Task task : tasksDone) {
            // Adiciona uma "ação" (Runnable) a cada item da lista
            taskDoneListBox.addItem(
                String.format("[P%d] %s", task.prioridade(), task.titulo()),
                () -> showTaskOptions(task, true) // Ação a correr ao pressionar "Enter"
            );
        }
    }

    private void loadProjects(Panel panel) {

        List<Project> projects = projectService.getProjects();
        for (Project project : projects) {
            
            panel.addComponent(new Label(String.format("    [%d] %s", project.id(), project.titulo())));
        }


        List<Task> tasksDone = taskDoneService.getDoneTasks();
        for (Task task : tasksDone) {
            // Adiciona uma "ação" (Runnable) a cada item da lista
            taskDoneListBox.addItem(
                String.format("[P%d] %s", task.prioridade(), task.titulo()),
                () -> showTaskOptions(task, true) // Ação a correr ao pressionar "Enter"
            );
        }
    }

    private void showTaskOptions(Task task, Boolean done) {

        final WindowBasedTextGUI textGUI = (WindowBasedTextGUI) this.getTextGUI();
        Runnable actionConcluir = new Runnable() {
            @Override
            public void run(){
            // logica 
            taskService.completeTask(task.id());
            loadTasks();
            MessageDialog.showMessageDialog( textGUI , "Sucesso", "Tarefa '" + task.titulo() + "' completada!");
            }

            @Override
            public String toString(){
                return "Concluir";
            }
        };

        Runnable actionEliminar = new Runnable() {
            
            @Override
            public void run(){          
            taskService.deleteTask(task.id()); 

            loadTasks();
            MessageDialog.showMessageDialog(textGUI, "Sucesso", "Tarefa '" + task.titulo() + "' eliminada!");
            }

            @Override
            public String toString(){return "Eliminar";}
        };

        
        if (!done){ 
            ActionListDialog.showDialog(
            textGUI,
            task.titulo(),
            task.descricao(),

            //butões
            actionConcluir ,
            actionEliminar
        );
        } else {
            ActionListDialog.showDialog(
            textGUI,
            task.titulo(),
            task.descricao(),

            //butões
            actionEliminar
        );
        }
    }

    private void markTaskAsDone(Task task) {
        // Lógica para chamar o taskService.completeTask(task.id())
        // ...
        loadTasks(); // Recarrega a lista
        // Mostra uma popup de confirmação
        MessageDialog.showMessageDialog((WindowBasedTextGUI) this.getTextGUI(), "Sucesso", "Tarefa completada!");
    }
}
