package service;
import model.Task;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyBrowsing = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyBrowsing.size() == 10) {
                historyBrowsing.remove(0);
            }
            historyBrowsing.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyBrowsing;
    }
}
