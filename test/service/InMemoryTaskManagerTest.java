package service;
import exception.ValidationException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@DisplayName("ИнМеморитэскменеджер")
public class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void init() {
        taskManager = new InMemoryTaskManager(historyManager);
        historyManager = new InMemoryHistoryManager();
    }

    @DisplayName("утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров")
    @Test
    public void testInitializedReadyInstancesManagers() {
        assertNotNull(taskManager);
    }

    @DisplayName("задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    @Test
    public void testTaskNotConflictWithinManager() throws ValidationException {
        Task task1 = new Task(4, TypeTask.TASK, "задача1", "описаниеТаска1", Status.NEW,
                LocalDateTime.of(2024,06,07,19,00,00), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        Task task2 = new Task(5, TypeTask.TASK, "задача2", "описаниеТаска2", Status.NEW,
                LocalDateTime.of(2024,06,07,19,20,00), Duration.ofMinutes(15));
        taskManager.createTask(task2);
        assertNotEquals(task1.getId(), task2.getId());
    }

    @DisplayName("задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных")
    @Test
    public void testHistoryManagerTask() {
        Task task = new Task(1,TypeTask.TASK,"task1", "desc1", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
        historyManager.add(task);
        List<Task> historyBrowsing = historyManager.getHistory();
        assertTrue(historyBrowsing.contains(task));
    }

    public Task createTask() {
        return new Task(1,TypeTask.TASK,"task", "desc", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
    }

    public Epic createEpic() {
        return new Epic(1,TypeTask.EPIC,"epic", "epic desc", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
    }

    public Task createSubTask() {
        Epic epic = new Epic(1,TypeTask.EPIC,"epic1", "epic desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        return new SubTask(2,TypeTask.SUBTASK,"subtask", "subtask desc", Status.NEW,1,
                LocalDateTime.now(), Duration.ofMinutes(15));
    }

    TaskManager manager = Managers.getDefault();
    TaskManager taskServiceReload = FileBackedTaskManager.loadFromFile(new File("resources/task.csv"));
    @DisplayName("Пересечение задач по времени, их продолжительность")
    @Test
    public void testDuration() {
        Epic epic = new Epic(0, TypeTask.EPIC, "эпик", "описание эпика", Status.NEW);
        manager.createEpic(epic);

        SubTask s1 = new SubTask(1,TypeTask.SUBTASK,"подзадача1", "описаниеСабтаска", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,18,00,00),
                Duration.ofMinutes(15));
        manager.createSubtask(s1);
        epic.addSubTask(s1);
        SubTask s2 = new SubTask(2,TypeTask.SUBTASK,"подзадача2", "описаниеСабтаска", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,18,20,00),
                Duration.ofMinutes(15));
        manager.createSubtask(s2);
        epic.addSubTask(s2);

        assertEquals(s1.getDuration().plus(s2.getDuration()), epic.getDuration(), "Длительность эпика равна " +
                "сумма длительностей подзадач");

        assertEquals(s1.getStartTime(), epic.getStartTime(), "Начало эпика - начало первой подзадачи");
        assertEquals(s2.getEndTime(), epic.getEndTime(), "Завершение эпика - завершение последней подзадачи");
    }
}
