import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;
import service.InMemoryHistoryManager;
import service.HistoryManager;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = taskManager.createTask(new Task("Переезд", "Купить дом", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Приготовить обед", "Купить продукты", Status.NEW));

        Epic epic1 = taskManager.createEpic(new Epic("Сделать ремонт", "Ремонт новой квартиры",
                Status.NEW));
        SubTask subTask1 = taskManager.createSubtask(new SubTask("Составить план", "Просчитать расходы",
                Status.NEW, epic1,2));
        SubTask subTask2 = taskManager.createSubtask(new SubTask("Нанять рабочих", "Плиткоукладчики",
                Status.NEW, epic1, 2));
        SubTask subTask3 = taskManager.createSubtask(new SubTask("Купить материалы", "Стройматериалы",
                Status.NEW, epic1, 2));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        epic1.addSubTask(subTask3);

        Epic epic2 = taskManager.createEpic(new Epic("Испечь торт", "Торт для семьи", Status.NEW));

        List<Task> history = historyManager.getHistory();

        System.out.println("История:");

        history.add(task1);
        System.out.println(history.get(0));

        history.add(task2);
        System.out.println(history.get(1));

        history.add(epic1);
        System.out.println(history.get(2));

        history.add(epic2);
        System.out.println(history.get(3));

        history.add(task2);
        System.out.println(history.get(1));

        history.add(task1);
        System.out.println(history.get(0));

        history.add(epic1);
        System.out.println(history.get(2));

        history.add(epic2);
        System.out.println(history.get(3));

        history.add(task2);
        System.out.println(history.get(1));

        history.add(task1);
        System.out.println(history.get(0));

        history.remove(9);

        history.remove(2);

        Set<Task> uniqueHistory = new HashSet<>();
        uniqueHistory.addAll(history);

        System.out.println(uniqueHistory);

    }
}











