package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Node prev;
        Node next;
        Task task;

        public Node(Node prev, Node next, Task task) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }
    }

    Node first;
    Node last;
    Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void addHistory(Task task) {
        if (task == null) {
            return;
        }

        remove(task.getId());
        Node lastNode = last;
        Node node = new Node(last, null, task);
        last = node;

        if (lastNode == null) {
            first = node;
        } else {
            lastNode.next = node;
        }

        nodeMap.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node current = first;

        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node remove = nodeMap.remove(id);

        if  (remove == null) {
            return;
        }

        if (nodeMap.isEmpty()) {
            first = null;
            last = null;
        } else if (remove.prev == null) {
            first = remove.next;
            remove.next.prev = null;
        } else if (remove.next == null) {
            last = remove.prev;
            remove.prev.next = null;
        } else {
            remove.next.prev = remove.prev;
            remove.prev.next = remove.next;
        }
    }
}
