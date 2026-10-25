
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentDAO dao = new StudentDAO();

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");

            int choice = readInt(sc, "Enter your choice: ");

            try {
                switch (choice) {

                    case 1: {
                        int id = readInt(sc, "Enter Student ID: ");

                        if (id <= 0) {
                            System.out.println(
                                "Student ID must be positive."
                            );
                            break;
                        }

                        if (dao.getStudentById(id) != null) {
                            System.out.println(
                                "Student ID already exists."
                            );
                            break;
                        }

                        System.out.print("Enter Student Name: ");
                        String name = sc.nextLine().trim();

                        if (name.isEmpty()) {
                            System.out.println(
                                "Student name cannot be empty."
                            );
                            break;
                        }

                        double marks =
                            readDouble(sc, "Enter Student Marks: ");

                        if (!Double.isFinite(marks)
                                || marks < 0 || marks > 100) {
                            System.out.println(
                                "Marks must be between 0 and 100."
                            );
                            break;
                        }

                        Student student =
                            new Student(id, name, marks);

                        if (dao.addStudent(student)) {
                            System.out.println(
                                "Student added successfully!"
                            );
                        }

                        break;
                    }

                    case 2: {
                        List<Student> students = dao.getAllStudents();

                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            for (Student student : students) {
                                student.display();
                                System.out.println("--------------------");
                            }
                        }

                        break;
                    }

                    case 3: {
                        int id = readInt(
                            sc, "Enter Student ID to search: "
                        );

                        Student student = dao.getStudentById(id);

                        if (student != null) {
                            student.display();
                        } else {
                            System.out.println("Student not found.");
                        }

                        break;
                    }

                    case 4: {
                        int id = readInt(
                            sc, "Enter Student ID to update: "
                        );

                        Student existing = dao.getStudentById(id);

                        if (existing == null) {
                            System.out.println("Student not found.");
                            break;
                        }

                        System.out.print("Enter new Student Name: ");
                        String name = sc.nextLine().trim();

                        if (name.isEmpty()) {
                            System.out.println(
                                "Student name cannot be empty."
                            );
                            break;
                        }

                        double marks = readDouble(
                            sc, "Enter new Student Marks: "
                        );

                        if (!Double.isFinite(marks)
                                || marks < 0 || marks > 100) {
                            System.out.println(
                                "Marks must be between 0 and 100."
                            );
                            break;
                        }

                        existing.setName(name);
                        existing.setMarks(marks);

                        if (dao.updateStudent(existing)) {
                            System.out.println(
                                "Student updated successfully!"
                            );
                        } else {
                            System.out.println(
                                "No changes were made."
                            );
                        }

                        break;
                    }

                    case 5: {
                        int id = readInt(
                            sc, "Enter Student ID to delete: "
                        );

                        if (dao.deleteStudent(id)) {
                            System.out.println(
                                "Student deleted successfully!"
                            );
                        } else {
                            System.out.println("Student not found.");
                        }

                        break;
                    }

                    case 6:
                        running = false;
                        System.out.println("Exiting application.");
                        break;

                    default:
                        System.out.println(
                            "Invalid choice. Enter 1 to 6."
                        );
                }

            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println(
                    "Database constraint violation. Check the student ID."
                );

            } catch (SQLException e) {
                System.out.println("Database operation failed.");
                System.out.println(e.getMessage());
            }
        }

        sc.close();
    }

    public static int readInt(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Please enter a valid whole number."
                );
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Please enter a valid number."
                );
            }
        }
    }
}