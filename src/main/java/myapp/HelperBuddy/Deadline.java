package myapp.helperbuddy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private final LocalDateTime deadlineBy;

    /**
     * Constructs a {@code Deadline} task with the specified description and deadline.
     *
     * @param description The description of the task.
     * @param deadlineBy  The deadline date and time for the task.
     */
    public Deadline(String description, LocalDateTime deadlineBy) {
        super(description);
        this.deadlineBy = deadlineBy;
    }

    /**
     * Parses a string to create a Deadline task.
     * The string should be in the format used for saving to a file, with fields separated by " | ".
     * The fields are: task type ("D"), completion status (1 for done, 0 for not done), description, and optionally, the deadline.
     * If the deadline is missing or incorrectly formatted, a warning is printed, and the deadline will be null.
     *
     * @param taskData The string representing the Deadline task.
     * @return A Deadline object created from the string data.
     */
    public static Deadline parseTask(String taskData) {
        String[] parts = taskData.split(" \\| ");
        String description = parts[2].trim();
        LocalDateTime deadlineBy = null;
        if (parts.length > 3) {
            try {
                deadlineBy = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"));
            } catch (DateTimeParseException e) {
                System.out.println("Warning: There is no date format provided");
            }
        }
        Deadline deadline = new Deadline(description, deadlineBy);
        if (parts[1].trim().equals("1")) {
            deadline.markDone();
        }
        return deadline;
    }

    /**
     * Returns a string representation of the Deadline task.
     * The string representation includes the task type ("D"), completion status, description, and the deadline formatted as "MMM dd yyyy HH:mm".
     * For example, a completed task might be represented as: "[D][X] Finish assignment (by: Oct 10 2024 15:30)".
     *
     * @return A string representing the Deadline task.
     */
    @Override
    public String toString() {
        return "[D][" + (this.getDone() ? "X" : " ") + "] " + this.getDescription() +
                (deadlineBy != null ? " (by: " + deadlineBy.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")) + ")" : "");
    }

    /**
     * Converts the Deadline task into a format suitable for saving to a file.
     * The format includes the task type ("D"), completion status (1 for done, 0 for not done), description, and deadline formatted as "dd/MM/yyyy HHmm".
     * For example, a saved task might be represented as: "D | 1 | Finish assignment | 10/10/2024 1530"
     *
     * @return A string representing the Deadline task in file format.
     */
    @Override
    public String toFileFormat() {
        return "D | " + (this.getDone() ? "1" : "0") + " | " + this.getDescription() +
                (deadlineBy != null ? " | " + deadlineBy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm")) : "");
    }
}
