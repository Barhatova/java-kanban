import exception.ValidationException;
import model.*;
import service.*;
import java.io.File;
import java.time.*;

public class Main {

    public static void main(String[] args) throws ValidationException {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task(0, TypeTask.TASK, "задача1", "описаниеТаска1", Status.NEW,
                LocalDateTime.of(2024,06,06,10,00,00), Duration.ofMinutes(15));
        manager.createTask(task1);
        Task task2 = new Task(1, TypeTask.TASK, "задача2", "описаниеТаска2", Status.NEW,
                LocalDateTime.of(2024,06,06,10,20,00), Duration.ofMinutes(15));
        manager.createTask(task2);

        Epic epic = new Epic(2, TypeTask.EPIC, "эпик", "описание эпика", Status.NEW);
        manager.createEpic(epic);

        SubTask s1 = new SubTask(3,TypeTask.SUBTASK,"подзадача1", "описаниеСабтаска1", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,10,40,00),
                Duration.ofMinutes(15));
        manager.createSubtask(s1);
        epic.addSubTask(s1);
        SubTask s2 = new SubTask(4,TypeTask.SUBTASK,"подзадача2", "описаниеСабтаска2", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,11,00,00),
                Duration.ofMinutes(15));
        manager.createSubtask(s2);
        epic.addSubTask(s2);
        SubTask s3 = new SubTask(5,TypeTask.SUBTASK,"подзадача3", "описаниеСабтаска3", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,11,20,00),
                Duration.ofMinutes(15));
        manager.createSubtask(s3);
        epic.addSubTask(s3);

        TaskManager taskServiceReload = FileBackedTaskManager.loadFromFile(new File("resources/task.csv"));
        System.out.println("Все задачи:");
        System.out.println(taskServiceReload.getAllTasks());
        System.out.println("Все сабтакси:");
        System.out.println(taskServiceReload.getAllSubTasks());
        System.out.println("Все эпики:");
        System.out.println(taskServiceReload.getAllEpics());
        System.out.println("Задачи из приоритизированного списка:");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("Продолжительность эпика:");
        System.out.println(epic.getDuration());
        System.out.println("Начало эпика:");
        System.out.println(epic.getStartTime());
        System.out.println("Окончание эпика:");
        System.out.println(epic.getEndTime());
        System.out.println("Продолжительность таска:");
        System.out.println(task1.getDuration());
        System.out.println("Начало таска:");
        System.out.println(task1.getStartTime());
        System.out.println("Окончание таска:");
        System.out.println(task1.getEndTime());
    }
}






