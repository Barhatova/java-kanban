package service;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node> historyBrowsing = new HashMap<>();
    Node head;
	Node tail;

    private static class Node {
	Node prev;
	Task item;
	Node next;

    Node(Node prev, Task item, Node next) {
        this.prev = prev;
        this.item = item;
        this.next = next;
    }
    }

    @Override
	public void add(Task task) {
        Node node = historyBrowsing.get(task.getId());
    if (node != null) {
        removeNode(node);
    }
        linkLast(task);
    }

    @Override
	public void remove(int id) {
        Node node = historyBrowsing.get(id);
        	if (node != null) {
            	removeNode(node);
            	historyBrowsing.remove(id);
            	}
        	}

    void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(head, task, oldTail);
        tail = newNode;
        	if (oldTail == null) {
                head = newNode;
            	} else {
                oldTail.prev = newNode;
            	}
            historyBrowsing.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node.next == null && node.prev == null) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.next;
            node.next.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            node.prev.next = null;
        } else if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
        }
    }

    @Override
	public List<Task> getHistory() {
        List<Task> history = new LinkedList<>();
        Node current = head;
        while (current != null) {
            history.add(current.item);
            current = current.next;
        }
        return history;
    }
}
