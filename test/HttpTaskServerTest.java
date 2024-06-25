import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import service.EmptyTaskManager;
import service.InMemoryTaskManager;
import service.TaskManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static server.HttpTaskServer.getGson;

public class HttpTaskServerTest {
    TaskManager taskManager;
    HttpTaskServer taskServer;
    Gson gson = getGson();
    private Task task;

    @BeforeEach
    void init() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        task = taskManager.createTask(new Task(1, TypeTask.TASK, "задача1", "описание1"));
        taskServer.start();
    }

    @Test
    void testHashMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put("message", "Test task");
        String expected = "{\"message\":\"Test task\"}";
        String message = gson.toJson(params);
        assertEquals(expected, message);
    }

    @Test
    void testErrorResponse() {
        ErrorResponse response = new ErrorResponse("Test task");
        String expected = "{\"message\":\"Test task\"}";
        String message = gson.toJson(response);
        assertEquals(expected, message);
    }

    @AfterEach
    void stop() {
        taskServer.stop();
    }
}