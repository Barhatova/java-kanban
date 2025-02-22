package service;
import model.Task;
import java.util.List;
import java.util.LinkedList ;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyBrowsing = new LinkedList<>();
    static final int MAX_VALUE = 10;

    @Override
    public void add(Task task) {
    if (task != null) {
            if (historyBrowsing.size() == MAX_VALUE) {
                historyBrowsing.removeFirst();
            }
        historyBrowsing.add(task);
        }
    }


    @Override
    public List<Task> getHistory() {
        return historyBrowsing;
    }
}
