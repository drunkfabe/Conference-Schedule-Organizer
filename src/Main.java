import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static final String FILE_NAME = "conferences.csv";
    static final String LOG_FILE = "activity_log.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Conference Schedule Organizer ===");
            System.out.println("1. Add Session");
            System.out.println("2. View All Sessions");
            System.out.println("3. Update Session");
            System.out.println("4. Delete Session");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");
            System.out.println("7. View Sessions Sorted by Title");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": addSession(); break;
                case "2": viewSessions(); break;
                case "3": updateSession(); break;
                case "4": deleteSession(); break;
                case "5": generateReport(); break;
                case "6": System.exit(0);
                case "7": sortSessionsByTitle(); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static class Session {
        String id, title, speaker, date, startTime, endTime, location;

        public Session(String id, String title, String speaker, String date,
                       String startTime, String endTime, String location) {
            this.id = id;
            this.title = title;
            this.speaker = speaker;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }

        public String toCSV() {
            return String.join(",", id, title, speaker, date, startTime, endTime, location);
        }

        public static Session fromCSV(String line) {
            String[] parts = line.split(",", -1);
            return new Session(parts[0], parts[1], parts[2], parts[3],
                    parts[4], parts[5], parts[6]);
        }

        @Override
        public String toString() {
            return String.format("ID: %s | Title: %s | Speaker: %s | Date: %s | Time: %s-%s | Location: %s",
                    id, title, speaker, date, startTime, endTime, location);
        }
    }

    static void addSession() {
        try {
            String id = generateNextId();

            System.out.print("Enter Title: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) throw new Exception("Title cannot be empty.");

            System.out.print("Enter Speaker: ");
            String speaker = scanner.nextLine().trim();
            if (speaker.isEmpty()) throw new Exception("Speaker cannot be empty.");

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            if (!isValidFutureDate(date)) throw new Exception("Invalid date format or date is not in the future.");

            System.out.print("Enter Start Time (HH:MM): ");
            String startTime = scanner.nextLine().trim();
            if (!isValidTime(startTime)) throw new Exception("Invalid start time.");

            System.out.print("Enter End Time (HH:MM): ");
            String endTime = scanner.nextLine().trim();
            if (!isValidTime(endTime)) throw new Exception("Invalid end time.");

            System.out.print("Enter Location: ");
            String location = scanner.nextLine().trim();
            if (location.isEmpty()) throw new Exception("Location cannot be empty.");

            Session session = new Session(id, title, speaker, date, startTime, endTime, location);
            appendToFile(session.toCSV(), FILE_NAME);
            log("Added session: " + id);
            System.out.println("Session added successfully with ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewSessions() {
        List<Session> sessions = readAllSessions();
        if (sessions.isEmpty()) {
            System.out.println("No sessions found.");
        } else {
            for (Session s : sessions) System.out.println(s);
        }
    }

    static void updateSession() {
        List<Session> sessions = readAllSessions();
        System.out.print("Enter Session ID to update: ");
        String id = scanner.nextLine().trim();

        Optional<Session> sessionOpt = sessions.stream().filter(s -> s.id.equals(id)).findFirst();
        if (sessionOpt.isEmpty()) {
            System.out.println("Session not found.");
            return;
        }

        Session session = sessionOpt.get();
        try {
            System.out.print("Enter new Title [" + session.title + "]: ");
            String title = scanner.nextLine().trim();
            if (!title.isEmpty()) session.title = title;

            System.out.print("Enter new Speaker [" + session.speaker + "]: ");
            String speaker = scanner.nextLine().trim();
            if (!speaker.isEmpty()) session.speaker = speaker;

            System.out.print("Enter new Date [" + session.date + "]: ");
            String date = scanner.nextLine().trim();
            if (!date.isEmpty() && isValidFutureDate(date)) session.date = date;

            System.out.print("Enter new Start Time [" + session.startTime + "]: ");
            String startTime = scanner.nextLine().trim();
            if (!startTime.isEmpty() && isValidTime(startTime)) session.startTime = startTime;

            System.out.print("Enter new End Time [" + session.endTime + "]: ");
            String endTime = scanner.nextLine().trim();
            if (!endTime.isEmpty() && isValidTime(endTime)) session.endTime = endTime;

            System.out.print("Enter new Location [" + session.location + "]: ");
            String location = scanner.nextLine().trim();
            if (!location.isEmpty()) session.location = location;

            writeAllSessions(sessions);
            log("Updated session: " + id);
            System.out.println("Session updated successfully.");
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    static void deleteSession() {
        List<Session> sessions = readAllSessions();
        System.out.print("Enter Session ID to delete: ");
        String id = scanner.nextLine().trim();

        boolean removed = sessions.removeIf(s -> s.id.equals(id));
        if (removed) {
            writeAllSessions(sessions);
            log("Deleted session: " + id);
            System.out.println("Session deleted.");
        } else {
            System.out.println("Session not found.");
        }
    }

    static void generateReport() {
        List<Session> sessions = readAllSessions();
        System.out.println("=== Report ===");
        System.out.println("Total sessions: " + sessions.size());

        Map<String, Long> speakerCount = sessions.stream()
                .collect(Collectors.groupingBy(s -> s.speaker, Collectors.counting()));

        if (!speakerCount.isEmpty()) {
            String topSpeaker = Collections.max(speakerCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            System.out.println("Most frequent speaker: " + topSpeaker);
        } else {
            System.out.println("No speakers found.");
        }

        System.out.println("Activity Log:");
        try {
            Files.lines(Paths.get(LOG_FILE)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("No log available.");
        }
    }


    static boolean isValidFutureDate(String dateStr) {
        try {
            LocalDate inputDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            return inputDate.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isValidTime(String timeStr) {
        try {
            LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static void appendToFile(String line, String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(line);
            bw.newLine();
        }
    }

    static void writeAllSessions(List<Session> sessions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Session s : sessions) {
                bw.write(s.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to write sessions: " + e.getMessage());
        }
    }

    static List<Session> readAllSessions() {
        List<Session> sessions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    sessions.add(Session.fromCSV(line));
                }
            }
        } catch (IOException e) {
            // File may not exist yet
        }
        return sessions;
    }

    static String generateNextId() {
        List<Session> sessions = readAllSessions();
        int maxId = 0;
        for (Session s : sessions) {
            try {
                int num = Integer.parseInt(s.id);
                if (num > maxId) maxId = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.valueOf(maxId + 1);
    }

    static void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            bw.write("[" + timestamp + "] " + message);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Failed to write log.");
        }
    }

    static void sortSessionsByTitle() {
        List<Session> sessions = readAllSessions();
        if (sessions.isEmpty()) {
            System.out.println("No sessions available to sort.");
            return;
        }

        sessions.sort(Comparator.comparing(s -> s.title.toLowerCase()));

        System.out.println("=== Sessions Sorted by Title (A-Z) ===");
        for (Session session : sessions) {
            System.out.println(session);
        }
    }

}
