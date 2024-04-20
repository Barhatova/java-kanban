package model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Эпик")
class EpicTest {

    @DisplayName("Эпики должны совпадать")
    @Test
    void shouldEqualsWithCopy() {
        Epic epic1 = new Epic("name", "desc", Status.NEW);
        Epic epic2 = new Epic("name", "desc", Status.NEW);
        assertEquals(epic2, epic1, "Эпики должны совпадать");
    }

    private static void testAssertEqualsTask(Task expected, Task actual, String message) {
        assertEquals(expected.getName(), actual.getName(), message + ", name");
        assertEquals(expected.getDescription(), actual.getDescription(), message + ", description");
        assertEquals(expected.getStatus(), actual.getStatus(), message + ", status");
        assertEquals(expected.getId(), actual.getId(), message + ", id");
    }
}