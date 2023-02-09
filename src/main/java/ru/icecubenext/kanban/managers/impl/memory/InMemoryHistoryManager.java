package ru.icecubenext.kanban.managers.impl.memory;

import ru.icecubenext.kanban.managers.HistoryManager;
import ru.icecubenext.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private Node head = null;
    private Node tail = null;

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        nodeMap.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Node node = nodeMap.remove(id);
            if (node == head && node == tail) {
                head = null;
                tail = null;
            } else if (node == head) {
                head = node.next;
                head.prev = null;
            } else if (node == tail) {
                tail = node.prev;
                tail.next = null;
            } else {
                Node prevNode = node.prev;
                Node nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node {
        Node next;
        Node prev;
        Task task;

        public Node(Task task) {
            this.next = null;
            this.prev = null;
            this.task = task;
        }

        @Override
        public  String toString() {
            return "Node{" +
                    "Task='" + this.task + '\'' +
                    "next='" + this.next + '\'' +
                    "prev='" + this.prev + "'}";
        }
    }

    private Node linkLast(Task task) {
        Node newNode = new Node(task);
        Node oldTail = tail;
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
            tail.prev = oldTail;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }
}
