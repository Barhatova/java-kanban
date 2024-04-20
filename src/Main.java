import model.*;
import service.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task("Переезд", "Купить дом", Status.NEW));

        Task task2 = taskManager.createTask(new Task("Приготовить обед", "Купить продукты", Status.NEW));

        Epic epic1 = taskManager.createEpic(new Epic("Сделать ремонт", "Ремонт новой квартиры",
                Status.NEW));
        SubTask subTask1 = taskManager.createSubtask(new SubTask("Купить материалы", "Стройматериалы",
                Status.DONE, epic1,2));
        SubTask subTask2 = taskManager.createSubtask(new SubTask("Нанять рабочих", "Плиткоукладчики",
                Status.NEW, epic1, 2));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        Epic epic2 = taskManager.createEpic(new Epic("Испечь торт", "Торт для семьи", Status.NEW));
        SubTask subTask3 = taskManager.createSubtask(new SubTask("Найти рецепт", "Торт-Птичье молоко",
                Status.NEW, epic2,5));
        epic2.addSubTask(subTask3);

        List<Task> historyBrowsing = taskManager.getHistory();

        System.out.println("История:");

        taskManager.getTaskById(0);
        System.out.println(historyBrowsing.get(0));

        taskManager.getTaskById(1);
        System.out.println(historyBrowsing.get(1));

        taskManager.getEpicById(2);
        System.out.println(historyBrowsing.get(2));

        taskManager.getSubtaskById(3);
        System.out.println(historyBrowsing.get(3));

        taskManager.getSubtaskById(4);
        System.out.println(historyBrowsing.get(4));

        taskManager.getEpicById(5);
        System.out.println(historyBrowsing.get(5));

        taskManager.getSubtaskById(6);
        System.out.println(historyBrowsing.get(6));

        taskManager.getTaskById(0);
        System.out.println(historyBrowsing.get(7));

        taskManager.getTaskById(1);
        System.out.println(historyBrowsing.get(8));

        taskManager.getEpicById(2);
        System.out.println(historyBrowsing.get(9));
        }
    }













