package eLearning;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Registration extends JFrame {
    JTextField nameField, emailField;
    JPasswordField passField;
    JComboBox<String> roleBox;
    JButton registerButton, cancelButton; // Added Cancel button

    Registration() {
        setTitle("Registration");
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        nameField = new JTextField();
        emailField = new JTextField();
        passField = new JPasswordField();
        roleBox = new JComboBox<>(new String[]{"Student", "Teacher"});
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel"); // Initialize Cancel button

        // Setting up the components' bounds
        nameLabel.setBounds(50, 120, 100, 30);
        emailLabel.setBounds(50, 170, 100, 30);
        passLabel.setBounds(50, 220, 100, 30);
        roleLabel.setBounds(50, 270, 100, 30);
        nameField.setBounds(150, 120, 200, 30);
        emailField.setBounds(150, 170, 200, 30);
        passField.setBounds(150, 220, 200, 30);
        roleBox.setBounds(150, 270, 200, 30);
        registerButton.setBounds(150, 320, 100, 30);
        cancelButton.setBounds(270, 320, 100, 30); // Place Cancel button beside Register button

        // Adding an icon
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/icon9.png"));
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(150, 10, 100, 100);
        add(image);

        // Adding components to the frame
        add(nameLabel);
        add(emailLabel);
        add(passLabel);
        add(roleLabel);
        add(nameField);
        add(emailField);
        add(passField);
        add(roleBox);
        add(registerButton);
        add(cancelButton); // Add Cancel button to frame

        // Action listener for the Register button
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            String role = roleBox.getSelectedItem().toString();

            try (Connection con = DatabaseConnection.connect();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, role);
                ps.executeUpdate(); // Executes the insert query
                JOptionPane.showMessageDialog(this, "Registration Successful");
                dispose(); // Close the registration frame
                new Login(); // Redirect back to the Login screen
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Action listener for the Cancel button
        cancelButton.addActionListener(e -> {
            dispose(); // Close the registration frame
            new Login(); // Redirect back to the Login screen
        });

        // Frame properties
        setSize(400, 450);
        setLocation(500, 250);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Registration();
    }
}
