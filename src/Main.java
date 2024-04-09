import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = taskManager.createTask(new Task("Переезд", "Купить дом", Status.IN_PROGRESS));
        Task task2 = taskManager.createTask(new Task("Приготовить обед", "Купить продукты", Status.NEW));
        System.out.println("createTask:" + task1);
        System.out.println("createTask:" + task2);

        Epic epic1 = taskManager.createEpic(new Epic("Сделать ремонт", "Ремонт новой квартиры",
                Status.NEW));
        SubTask subTask1 = taskManager.createSubtask(new SubTask("Купить материалы", "Стройматериалы",
                Status.DONE, epic1, 3));
        SubTask subTask2 = taskManager.createSubtask(new SubTask("Нанять рабочих", "Плиткоукладчики",
                Status.DONE, epic1, 3));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        System.out.println("createEpic:" + epic1);

        Epic epic2 = taskManager.createEpic(new Epic("Испечь торт", "Торт для семьи", Status.NEW));
        SubTask subTask3 = taskManager.createSubtask(new SubTask("найти рецепт", "Торт-Птичье молоко",
                Status.NEW, epic2,6));
        epic2.addSubTask(subTask3);
        System.out.println("createEpic:" + epic2);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllEpics());

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(6);


        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubTasks());

        System.out.println(taskManager.calculateEpicStatus());
    }
}
