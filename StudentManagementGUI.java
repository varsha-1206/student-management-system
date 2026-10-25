
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StudentManagementGUI extends JFrame {

    private final StudentDAO dao = new StudentDAO();

    private final JTextField idField = new JTextField(12);
    private final JTextField nameField = new JTextField(12);
    private final JTextField marksField = new JTextField(12);

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"Student ID", "Name", "Marks"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    public StudentManagementGUI() {

        setTitle("Student Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel(
            "STUDENT MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            "Student Details"
        ));

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Marks (0-100):"));
        formPanel.add(marksField);

        JButton addButton = new JButton("Add Student");
        JButton displayButton = new JButton("Display All");
        JButton searchButton = new JButton("Search");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            "Student Records"
        ));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addStudent());
        displayButton.addActionListener(e -> displayStudents());
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row >= 0) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    marksField.setText(model.getValueAt(row, 2).toString());
                }
            }
        });

        displayStudents();
    }

    private int readId() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            if (id <= 0) {
                throw new NumberFormatException();
            }

            return id;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid positive student ID.",
                "Invalid ID",
                JOptionPane.WARNING_MESSAGE
            );

            return -1;
        }
    }

    private String readName() {
        String name = nameField.getText().trim();

        if (name.isEmpty() || name.contains("|")) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid student name.",
                "Invalid Name",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return name;
    }

    private Double readMarks() {
        try {
            double marks = Double.parseDouble(marksField.getText().trim());

            if (!Double.isFinite(marks) || marks < 0 || marks > 100) {
                throw new NumberFormatException();
            }

            return marks;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Marks must be a number between 0 and 100.",
                "Invalid Marks",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addStudent() {
        int id = readId();
        if (id == -1) return;

        String name = readName();
        if (name == null) return;

        Double marks = readMarks();
        if (marks == null) return;

        try {
            if (dao.getStudentById(id) != null) {
                JOptionPane.showMessageDialog(
                    this, "This student ID already exists."
                );
                return;
            }

            boolean added = dao.addStudent(new Student(id, name, marks));

            if (added) {
                JOptionPane.showMessageDialog(
                    this, "Student added successfully!"
                );
                clearFields();
                displayStudents();
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(
                this, "Student ID already exists."
            );

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void displayStudents() {
        try {
            List<Student> students = dao.getAllStudents();
            model.setRowCount(0);

            for (Student student : students) {
                model.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getMarks()
                });
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void searchStudent() {
        int id = readId();
        if (id == -1) return;

        try {
            Student student = dao.getStudentById(id);
            model.setRowCount(0);

            if (student == null) {
                JOptionPane.showMessageDialog(
                    this, "Student not found."
                );
                return;
            }

            model.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getMarks()
            });

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void updateStudent() {
        int id = readId();
        if (id == -1) return;

        String name = readName();
        if (name == null) return;

        Double marks = readMarks();
        if (marks == null) return;

        try {
            Student existing = dao.getStudentById(id);

            if (existing == null) {
                JOptionPane.showMessageDialog(
                    this, "Student not found."
                );
                return;
            }

            existing.setName(name);
            existing.setMarks(marks);

            dao.updateStudent(existing);

            JOptionPane.showMessageDialog(
                this, "Student details updated successfully!"
            );

            clearFields();
            displayStudents();

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deleteStudent() {
        int id = readId();
        if (id == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete student with ID " + id + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean deleted = dao.deleteStudent(id);

            if (deleted) {
                JOptionPane.showMessageDialog(
                    this, "Student deleted successfully!"
                );
                clearFields();
                displayStudents();

            } else {
                JOptionPane.showMessageDialog(
                    this, "Student not found."
                );
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        marksField.setText("");
        table.clearSelection();
        idField.requestFocusInWindow();
    }

    private void showDatabaseError(SQLException e) {
        JOptionPane.showMessageDialog(
            this,
            "Database operation failed:\n" + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI();
            gui.setVisible(true);
        });
    }
}