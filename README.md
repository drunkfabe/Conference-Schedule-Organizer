# Fatima Kasmaly kyzy
## Presentation
https://www.canva.com/design/DAGnN3kL9q8/P7BJXcJjyP3EK68ZvCXQJg/edit?utm_content=DAGnN3kL9q8&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton

#  Conference Schedule Organizer

A simple command-line Java application that allows users to manage conference sessions. It supports adding, viewing, updating, and deleting session records, along with generating reports and maintaining an activity log. The system uses CSV for data storage and logs all actions for tracking.

---

##  Features

###  1. Add Session

* Allows users to add new conference sessions.
* Requires: Title, Speaker, Date (must be future), Start Time, End Time, Location.
* Validates date and time formats.
* Automatically generates a unique ID for each session.
* Appends the session to `conferences.csv` and logs the action.

###  2. View All Sessions

* Displays all saved sessions in a formatted output.
* Shows ID, Title, Speaker, Date, Time Range, and Location.
* Useful for checking session details or finding session IDs for update/delete.

###  3. Update Session

* Lets users modify an existing session by entering its ID.
* Each field shows the current value; the user can choose to update or skip it.
* Validates new entries before applying changes.
* Saves updates and logs the activity.

###  4. Delete Session

* Removes a session using its ID.
* Deletes the session from `conferences.csv` and records the action in the log.
* No confirmation prompt—deletion happens immediately.

###  5. Generate Report

* Summarizes key conference statistics:

  * Total number of sessions.
  * Most frequent speaker (speaker with the most sessions).
  * Full activity history from `activity_log.txt`.

###  6. Exit

* Exits the application cleanly.

---

##  File Structure

```
.
├── Main.java              # Main application file
├── conferences.csv        # Data storage (created automatically)
├── activity_log.txt       # Action log (created automatically)
└── README.md              # Project documentation
```

---

##  How to Run

1. **Compile the program**:

   ```bash
   javac Main.java
   ```

2. **Run the program**:

   ```bash
   java Main
   ```

3. **Follow on-screen instructions** to manage sessions.

---

##  Sample Input & Output

###  Adding a Session

```
Enter Title: Future of AI  
Enter Speaker: Nazira Satybaldieva
Enter Date (YYYY-MM-DD): 2025-05-20  
Enter Start Time (HH:MM): 10:00  
Enter End Time (HH:MM): 11:30  
Enter Location: Hall
```

 Output:

```
Session added successfully with ID: 1
```

---

###  Viewing Sessions

```
=== Conference Schedule Organizer ===
2. View All Sessions
```

 Output:

```
ID: 1 | Title: Future of AI | Speaker: Dr. Alice Smith | Date: 2025-06-01 | Time: 10:00-11:30 | Location: Room A
```

---

###  Updating a Session

```
Enter Session ID to update: 1  
Enter new Title [Future of AI]: Future of AI and Ethics  
Enter new Speaker [Dr. Alice Smith]:   
Enter new Date [2025-06-01]:  
Enter new Start Time [10:00]:  
Enter new End Time [11:30]: 12:00  
Enter new Location [Room A]: Auditorium  
```

 Output:

```
Session updated successfully.
```

---

###  Deleting a Session

```
Enter Session ID to delete: 1
```

 Output:

```
Session deleted.
```

---

###  Generating a Report

```
=== Report ===
Total sessions: 3
Most frequent speaker: Dr. Bob Jones

Activity Log:
[2025-05-12T10:30:20.123] Added session: 2
[2025-05-12T10:35:11.456] Updated session: 2
[2025-05-12T10:36:00.789] Deleted session: 1
```
