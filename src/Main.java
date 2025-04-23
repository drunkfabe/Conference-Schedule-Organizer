import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static class Session implements Serializable {
        String id, title, speaker, time;

        Session(String id, String title, String speaker, String time) {
            this.id = id;
            this.title = title;
            this.speaker = speaker;
            this.time = time;
        }

        @Override
        public String toString() {
            return "ID: " + id + " | Title: " + title + " | Speaker: " + speaker + " | Time: " + time;
        }
    }

    static List<Session> sessions = new ArrayList<>();
    static final String FILE_NAME = "schedule.csv";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadSessions();

        while (true) {
            System.out.println("\n--- Conference Schedule Organizer ---");
            System.out.println("1. Add Session");
            System.out.println("2. View Sessions");
            System.out.println("3. Update Session");
            System.out.println("4. Delete Session");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addSession();
                case "2" -> viewSessions();
                case "3" -> updateSession();
                case "4" -> deleteSession();
                case "5" -> generateReport();
                case "6" -> {
                    saveSessions();
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addSession() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Speaker: ");
        String speaker = scanner.nextLine();
        System.out.print("Enter Time: ");
        String time = scanner.nextLine();

        if (id.isEmpty() || title.isEmpty() || speaker.isEmpty() || time.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        sessions.add(new Session(id, title, speaker, time));
        saveSessions();
        System.out.println("Session added.");
    }

    static void viewSessions() {
        if (sessions.isEmpty()) {
            System.out.println("No sessions available.");
        } else {
            sessions.forEach(System.out::println);
        }
    }

    static void updateSession() {
        System.out.print("Enter ID to update: ");
        String id = scanner.nextLine();
        for (Session s : sessions) {
            if (s.id.equals(id)) {
                System.out.print("New Title: ");
                s.title = scanner.nextLine();
                System.out.print("New Speaker: ");
                s.speaker = scanner.nextLine();
                System.out.print("New Time: ");
                s.time = scanner.nextLine();
                saveSessions();
                System.out.println("Session updated.");
                return;
            }
        }
        System.out.println("Session not found.");
    }

    static void deleteSession() {
        System.out.print("Enter ID to delete: ");
        String id = scanner.nextLine();
        if (sessions.removeIf(s -> s.id.equals(id))) {
            saveSessions();
            System.out.println("Session deleted.");
        } else {
            System.out.println("Session not found.");
        }
    }

    static void generateReport() {
        System.out.println("Total Sessions: " + sessions.size());
    }

    static void saveSessions() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(sessions);
        } catch (IOException e) {
            System.out.println("Error saving sessions: " + e.getMessage());
        }
    }

    static void loadSessions() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            sessions = (List<Session>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            sessions = new ArrayList<>();
        }
    }
}
