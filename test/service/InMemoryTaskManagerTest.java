package service;
import exception.ValidationException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

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
        Task task1 = new Task(1,TypeTask.TASK,"task1","desc1", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
        Task task2 = new Task(2,TypeTask.TASK,"task2", "desc2", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
        Task taskAdd1 = taskManager.createTask(task1);
        Task taskAdd2 = taskManager.createTask(task2);
        assertNotEquals(task1.getId(), task2.getId());
    }

    @DisplayName("InMemoryTaskManager добавляет задачи разного типа и может найти их по id")
    @Test
    public void testInMemoryManager() throws ValidationException {
        Task task = new Task(1,TypeTask.TASK,"task1", "desc1", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(15));
        Task addedTask = taskManager.createTask(task);
        Task foundTask = taskManager.getTaskById(0);
        assertEquals(task, addedTask);
        assertEquals(task, foundTask);
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

    @BeforeEach
    public void createHistoryManager() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = new InMemoryHistoryManager();
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

    @DisplayName("проверка добавления просмотров в историю")
    @Test
    public void testInAdd() {
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
        Task task = createTask();

        historyManager.add(task);
        history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
    }
}
