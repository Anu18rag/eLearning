package eLearning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherPortal {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Teacher Portal");

        // Set up the main panel and layout manager (similar to StudentPortal)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout

        // Adding an image to the frame (logo or header image)
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/Teacher.jpg")); // Replace with your image path
        Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT); // Scale the image
        ImageIcon scaledIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center image horizontally
        panel.add(imageLabel);

        // Create the welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Teacher Portal");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold font similar to StudentPortal
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center text horizontally
        panel.add(Box.createVerticalStrut(20)); // Add spacing between components
        panel.add(welcomeLabel);

        // Create buttons with uniform appearance and size
        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCourseButton.setPreferredSize(new Dimension(250, 40)); // Make the buttons consistent with StudentPortal size
        panel.add(addCourseButton);

        JButton uploadResourceButton = new JButton("Upload Resource");
        uploadResourceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadResourceButton.setPreferredSize(new Dimension(250, 40));
        panel.add(Box.createVerticalStrut(10)); // Add space between buttons
        panel.add(uploadResourceButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setPreferredSize(new Dimension(250, 40));
        panel.add(Box.createVerticalStrut(20)); // Add space between buttons
        panel.add(cancelButton);

        // Add the panel to a JScrollPane to make the content scrollable
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the Teacher Portal frame
                new Login(); // Redirect to the Login window
            }
        });

        // Action listener for "Add Course" button (similar logic as StudentPortal)
        addCourseButton.addActionListener(e -> {
            JFrame addCourseFrame = new JFrame("Add New Course");
            JLabel titleLabel = new JLabel("Course Title:");
            JLabel descriptionLabel = new JLabel("Description:");
            JTextField titleField = new JTextField();
            JTextArea descriptionArea = new JTextArea();
            JButton saveCourseButton = new JButton("Save Course");

            // Set layout manager and bounds for Add Course window
            addCourseFrame.setLayout(new BoxLayout(addCourseFrame.getContentPane(), BoxLayout.Y_AXIS));
            addCourseFrame.setSize(400, 300);
            addCourseFrame.setLocationRelativeTo(null); // Center the frame
            addCourseFrame.setVisible(true);

            // Add components to Add Course window
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleField.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            saveCourseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            addCourseFrame.add(titleLabel);
            addCourseFrame.add(titleField);
            addCourseFrame.add(descriptionLabel);
            addCourseFrame.add(descriptionArea);
            addCourseFrame.add(saveCourseButton);

            // Action listener for saving the course
            saveCourseButton.addActionListener(ev -> {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                try (Connection con = DatabaseConnection.connect();
                     PreparedStatement ps = con.prepareStatement("INSERT INTO courses (title, description, teacher_id) VALUES (?, ?, ?)")) {
                    // Assuming teacher_id is available after login (retrieve it from the session)
                    int teacherId = 1;  // Example teacher ID, replace with actual teacher ID from session or database

                    ps.setString(1, title);
                    ps.setString(2, description);
                    ps.setInt(3, teacherId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(addCourseFrame, "Course added successfully!");
                    addCourseFrame.dispose(); // Close the Add Course window
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addCourseFrame, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        });

        // Action listener for "Upload Resource" button (similar logic as StudentPortal)
        uploadResourceButton.addActionListener(e -> {
            JFrame uploadResourceFrame = new JFrame("Upload Resource");
            JLabel courseLabel = new JLabel("Course:");
            JLabel fileLabel = new JLabel("Resource File:");
            JComboBox<String> courseComboBox = new JComboBox<>();
            JTextField filePathField = new JTextField();
            JButton uploadButton = new JButton("Upload");

            // Set layout manager and bounds for Upload Resource window
            uploadResourceFrame.setLayout(new BoxLayout(uploadResourceFrame.getContentPane(), BoxLayout.Y_AXIS));
            uploadResourceFrame.setSize(400, 300);
            uploadResourceFrame.setLocationRelativeTo(null); // Center the frame
            uploadResourceFrame.setVisible(true);

            // Add components to Upload Resource window
            courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            courseComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
            filePathField.setAlignmentX(Component.CENTER_ALIGNMENT);
            uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            uploadResourceFrame.add(courseLabel);
            uploadResourceFrame.add(courseComboBox);
            uploadResourceFrame.add(fileLabel);
            uploadResourceFrame.add(filePathField);
            uploadResourceFrame.add(uploadButton);

            // Populate the courses dropdown
            try (Connection con = DatabaseConnection.connect();
                 PreparedStatement ps = con.prepareStatement("SELECT title FROM courses WHERE teacher_id = ?")) {
                int teacherId = 1;  // Example teacher ID, replace with actual teacher ID from session or database
                ps.setInt(1, teacherId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    courseComboBox.addItem(rs.getString("title"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Action listener for "Upload" button
            uploadButton.addActionListener(ev -> {
                String course = (String) courseComboBox.getSelectedItem();
                String filePath = filePathField.getText();

                try (Connection con = DatabaseConnection.connect();
                     PreparedStatement ps = con.prepareStatement("INSERT INTO resources (course_id, resource_type, file_path) VALUES ((SELECT course_id FROM courses WHERE title = ?), ?, ?)")) {
                    // Assuming the resource type is either 'PDF' or 'Video', for simplicity, we'll use PDF as default
                    ps.setString(1, course);
                    ps.setString(2, "PDF");
                    ps.setString(3, filePath);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(uploadResourceFrame, "Resource uploaded successfully!");
                    uploadResourceFrame.dispose(); // Close the Upload Resource window
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(uploadResourceFrame, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        });

        // Set up the main Teacher Portal window (same as StudentPortal window)
        frame.setSize(400, 400);  // Set size to a reasonable value
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
