package eLearning;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {
    JButton login, cancel, register;
    JTextField tfusername;
    JPasswordField tfpassword;

    Login() {
        setLayout(null);

        // Frame Image at the Top
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(500, 200, Image.SCALE_DEFAULT); // Scale image to frame width
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 500, 150); // Spanning the top
        add(image);

        // Email Label
        JLabel lblusername = new JLabel("Email");
        lblusername.setBounds(50, 170, 100, 20);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(150, 170, 150, 20);
        add(tfusername);

        // Password Label
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(50, 210, 100, 20);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(150, 210, 150, 20);
        add(tfpassword);

        // Login Button
        login = new JButton("Login");
        login.setBounds(50, 260, 120, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Tahoma", Font.BOLD, 15));
        login.addActionListener(this);
        add(login);

        // Cancel Button
        cancel = new JButton("Cancel");
        cancel.setBounds(200, 260, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        // Register Button with Icon
        ImageIcon regIcon = new ImageIcon(ClassLoader.getSystemResource("icons/icon14.jpg")); // Ensure the icon exists
        Image regImage = regIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT); // Scale the icon
        ImageIcon scaledRegIcon = new ImageIcon(regImage);

        register = new JButton("Register", scaledRegIcon); // Add text with icon
        register.setBounds(350, 260, 130, 30);
        register.setBackground(Color.BLACK);
        register.setForeground(Color.WHITE);
        register.setFont(new Font("Tahoma", Font.BOLD, 15));
        register.setHorizontalTextPosition(SwingConstants.RIGHT); // Text to the right of the icon
        register.setVerticalTextPosition(SwingConstants.CENTER); // Center alignment for text vertically
        register.addActionListener(this);
        add(register);

        // Frame Settings
        setSize(510, 400); // Increased height for the top image
        setLocation(500, 250);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword());

            try (Connection con = DatabaseConnection.connect();
                 PreparedStatement ps = con.prepareStatement("SELECT role FROM users WHERE email = ? AND password = ?")) {

                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    if (role.equals("Student")) {
                        StudentPortal.main(null);
                    } else if (role.equals("Teacher")) {
                        TeacherPortal.main(null);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
            dispose();
        } else if (ae.getSource() == register) {
            new Registration();
            dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
