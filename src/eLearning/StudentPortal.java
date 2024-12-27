package eLearning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class StudentPortal {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Portal");

        // Set layout for better component arrangement
        frame.setLayout(new BorderLayout());

        // Adding a Top Panel for the Image
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/Student.jpg")); // Ensure the image exists
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT); // Scale the image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        topPanel.add(imageLabel);

        // Adding a Center Panel for Buttons and Labels
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Grid layout for alignment and spacing
        JLabel welcomeLabel = new JLabel("Welcome to the Student Portal", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bold font for welcome text
        JButton viewCoursesButton = new JButton("View Courses");
        JButton takeQuizButton = new JButton("Take Quiz");
        JButton cancelButton = new JButton("Cancel");

        centerPanel.add(welcomeLabel);
        centerPanel.add(viewCoursesButton);
        centerPanel.add(takeQuizButton);
        centerPanel.add(cancelButton);

        // Add action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the StudentPortal frame
                new Login(); // Redirect to the Login window
            }
        });

        // Add action listener for View Courses button
        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch courses from the database
                ArrayList<Course> courses = getCoursesFromDatabase();

                if (courses.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No courses available.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Create a new frame to show courses
                    JFrame coursesFrame = new JFrame("Available Courses");
                    coursesFrame.setLayout(new BorderLayout());

                    // Display courses in a list with a custom renderer for title and description
                    JList<Course> courseList = new JList<>(courses.toArray(new Course[0]));
                    courseList.setCellRenderer(new CourseListCellRenderer());
                    JScrollPane scrollPane = new JScrollPane(courseList);
                    coursesFrame.add(scrollPane, BorderLayout.CENTER);

                    // Close button for courses frame
                    JButton closeButton = new JButton("Close");
                    closeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            coursesFrame.dispose(); // Close the courses frame
                        }
                    });
                    coursesFrame.add(closeButton, BorderLayout.SOUTH);

                    // Frame properties
                    coursesFrame.setSize(400, 300);
                    coursesFrame.setLocationRelativeTo(frame); // Center relative to main frame
                    coursesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    coursesFrame.setVisible(true);
                }
            }
        });

        // Adding panels to the frame
        frame.add(topPanel, BorderLayout.NORTH); // Image at the top
        frame.add(centerPanel, BorderLayout.CENTER); // Buttons and label in the center

        // Frame properties
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        frame.setVisible(true);
    }

    // Method to fetch courses from the database (using your existing connection)
    private static ArrayList<Course> getCoursesFromDatabase() {
        ArrayList<Course> courses = new ArrayList<>();

        // SQL query to fetch courses (title and description)
        String query = "SELECT title, description FROM courses";

        // Use the connect() method from DatabaseConnection to get the connection
        try (Connection con = DatabaseConnection.connect();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                courses.add(new Course(title, description)); // Adding a new Course object
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching courses: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return courses;
    }
}

// Create a class to represent Course objects
class Course {
    private String title;
    private String description;

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title; // Only the title will appear in the JList by default
    }
}

// Custom renderer to display both title and description in the JList
class CourseListCellRenderer extends JPanel implements ListCellRenderer<Course> {
    private JLabel titleLabel;
    private JTextArea descriptionTextArea;

    public CourseListCellRenderer() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        descriptionTextArea = new JTextArea(3, 20); // Multi-line area for description
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(descriptionTextArea), BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Course> list, Course course, int index, boolean isSelected, boolean cellHasFocus) {
        titleLabel.setText(course.getTitle());
        descriptionTextArea.setText(course.getDescription());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
