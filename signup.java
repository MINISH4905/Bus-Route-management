import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class signup {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Replace 'bus' with your database name
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "moni2626"; // Replace with your MySQL password

    public static void main(String[] args) {
        // Set the look and feel of the UI for better consistency across platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and setup the signup frame
        JFrame frame = new JFrame("Signup Page");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setResizable(false);

        // Create a JPanel with GridBagLayout for better control of component placement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Add title label
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 122, 204)); // A vibrant color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);  // Spacing after the title
        panel.add(titleLabel, gbc);

        // Add Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Add Email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(emailField, gbc);

        // Add Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        // Add Sign Up button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setBackground(new Color(0, 122, 204));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add a hover effect on the button
        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(new Color(0, 150, 255));  // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(new Color(0, 122, 204));  // Default color
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(signupButton, gbc);

        // Add Login button (Redirect to login page)
        JButton loginButton = new JButton("Login Page");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setBackground(new Color(255, 255, 255));
        loginButton.setForeground(new Color(0, 122, 204));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add a hover effect on the button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 122, 204));  // Lighter on hover
                loginButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Color.WHITE);  // Default color
                loginButton.setForeground(new Color(0, 122, 204));  // Default text color
            }
        });

        gbc.gridy = 5;
        panel.add(loginButton, gbc);

        // Add status label for feedback
        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(statusLabel, gbc);

        // Add ActionListener for SignUp button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("All fields are required!");
                    return;
                }

                if (performSignup(username, email, password)) {
                    statusLabel.setText("Signup successful! You can now log in.");
                    statusLabel.setForeground(Color.GREEN);
                    usernameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");
                } else {
                    statusLabel.setText("Signup failed. Try again.");
                }
            }
        });

        // Add ActionListener for Login Page button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Redirecting to login page...");
                frame.dispose(); // Close the current window
                login.main(null); // Assuming you have a Login class
            }
        });

        // Show the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    // Perform signup logic
    private static boolean performSignup(String username, String email, String plainPassword) {
        // SQL INSERT query
        String sql = "INSERT INTO signup (username, email, password) VALUES (?, ?, ?)";

        try {
            // For security, hash the password (this is a placeholder, replace with a real hashing function)
            String hashedPassword = hashPassword(plainPassword);

            // Establish database connection and insert user data
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashedPassword);

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (Exception e) {
            System.err.println("Error during signup: " + e.getMessage());
            return false;
        }
    }

    // Placeholder method for hashing passwords
    private static String hashPassword(String password) {
        // Implement a proper password hashing algorithm like BCrypt here
        return password; // Return plain text password for now (NOT secure)
    }
}
