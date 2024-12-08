package Java_Codes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PatientHeap {
    static class Node {
        Patient patient;
        Node left, right, parent;

        public Node(Patient patient) {
            this.patient = patient;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private Node root;
    private int size;
    private int totalAppointmentTime; 
    private final int MAX_APPOINTMENT_TIME = 420; 
    private ArrayList<Patient> removedPatients;

    public PatientHeap() {
        root = null;
        size = 0;
        totalAppointmentTime = 0;
        removedPatients = new ArrayList<>();
    }

    public void insert(Patient patient) {
        Node newNode = new Node(patient);
        size++;

        if (root == null) {
            root = newNode;
            return;
        }

        Node parent = findParent(size);
        newNode.parent = parent;

        if (parent.left == null) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        heapifyUp(newNode);
    }

    private Node findParent(int index) {
        int parentIndex = index / 2;
        String path = Integer.toBinaryString(parentIndex).substring(1);
        Node current = root;

        for (char direction : path.toCharArray()) {
            current = (direction == '0') ? current.left : current.right;
        }

        return current;
    }

    private void heapifyUp(Node node) {
        while (node.parent != null && node.patient.getPriority() < node.parent.patient.getPriority()) {
            Patient temp = node.patient;
            node.patient = node.parent.patient;
            node.parent.patient = temp;
            node = node.parent;
        }
    }

    private void heapifyDown(Node node) {
        while (true) {
            Node smallest = node;

            if (node.left != null && node.left.patient.getPriority() < smallest.patient.getPriority()) {
                smallest = node.left;
            }

            if (node.right != null && node.right.patient.getPriority() < smallest.patient.getPriority()) {
                smallest = node.right;
            }

            if (smallest == node) {
                break;
            }

            Patient temp = node.patient;
            node.patient = smallest.patient;
            smallest.patient = temp;

            node = smallest;
        }
    }

    private Node getLastNode() {
        String path = Integer.toBinaryString(size).substring(1);
        Node current = root;

        for (char direction : path.toCharArray()) {
            current = (direction == '0') ? current.left : current.right;
        }

        return current;
    }

    private void removeLastNode() {
        String path = Integer.toBinaryString(size).substring(1);
        Node current = root;
        Node parent = null;

        for (char direction : path.toCharArray()) {
            parent = current;
            current = (direction == '0') ? current.left : current.right;
        }

        if (parent != null) {
            if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }

        size--;
    }

    public void removeRoot() {
        if (totalAppointmentTime >= MAX_APPOINTMENT_TIME) {
            System.out.println("Total appointment time limit reached (420 minutes). Cannot remove more patients.");
            return;
        }

        if (root == null) {
            System.out.println("Heap is empty. Cannot remove root.");
            return;
        }

        // Add the appointment time to the total
        totalAppointmentTime += root.patient.getAppointmentTime();

        // Add the removed patient to the list
        removedPatients.add(root.patient);

        // Replace root with the last node
        Node lastNode = getLastNode();
        if (lastNode == root) {
            root = null; // If there's only one node, just make root null
        } else {
            root.patient = lastNode.patient;
            removeLastNode();
            heapifyDown(root);
        }
    }

    public ArrayList<Patient> getRemovedPatients() {
        return removedPatients;
    }

    public void printLevelOrder() {
        if (root == null) {
            System.out.println("Heap is empty");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.patient.toString() + " ");

            if (current.left != null)
                queue.add(current.left);
            if (current.right != null)
                queue.add(current.right);
        }

        System.out.println();
    }
}
