import model.*;
import service.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Epic epic1 = new Epic(0, TypeTask.EPIC,"эпик1", "описание1", Status.NEW);
        manager.createEpic(epic1);

        SubTask subTask1 = new SubTask(1,TypeTask.SUBTASK,"подзадача1", "описание",
                Status.NEW, epic1.getId());
        manager.createSubtask(subTask1);
        epic1.addSubTask(subTask1);

        Task task1 = new Task(2, TypeTask.TASK, "задача1", "описание1", Status.NEW);
        manager.createTask(task1);
        Task task2 = new Task(3, TypeTask.TASK, "задача2", "описание2", Status.NEW);
        manager.createTask(task2);

        TaskManager taskServiceReload = FileBackedTaskManager.loadFromFile(new File("resources/task.csv"));
        System.out.println("Все задачи:");
        System.out.println(taskServiceReload.getAllTasks());
        System.out.println("Все сабтакси:");
        System.out.println(taskServiceReload.getAllSubTasks());
        System.out.println("Все эпики:");
        System.out.println(taskServiceReload.getAllEpics());
    }
}






