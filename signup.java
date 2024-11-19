import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class signup {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Replace 'bus' with your database name
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "moni2626"; // Replace with your MySQL password

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Signup Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        frame.add(panel);

        // Add components
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton signupButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Login Page");

        JLabel statusLabel = new JLabel("", JLabel.CENTER);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signupButton);
        panel.add(loginButton);
        panel.add(statusLabel);

        // Add signup button action
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
                    usernameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");
                } else {
                    statusLabel.setText("Signup failed. Try again.");
                }
            }
        });

        // Add login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Redirecting to login page...");
                // Implement or call your login page here
                frame.dispose(); // Close the current window
                login.main(null); // Assuming you have a Login class
            }
        });

        // Show the frame
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
