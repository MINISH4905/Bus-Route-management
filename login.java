import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void main(String[] args) {
        // Set the look and feel of the UI for better consistency across platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and setup the login frame
        JFrame frame = new JFrame("Login Page");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setResizable(false);

        // Create a JPanel with GridBagLayout for better control of component placement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Add title label
        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 122, 204)); // A vibrant color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);  // Spacing after the title
        panel.add(titleLabel, gbc);

        // Add Email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Add Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Add Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add a hover effect on the button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 150, 255));  // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 122, 204));  // Default color
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(loginButton, gbc);

        // Add Status label for feedback
        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(statusLabel, gbc);

        // Add ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Basic validation
                if (email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Both fields are required!");
                    return;
                }

                // Authenticate user
                if (authenticate(email, password)) {
                    statusLabel.setText("Login successful!");
                    statusLabel.setForeground(Color.GREEN);
                    frame.dispose();  // Close the login window
                    showPostLoginMenu(); // Proceed to post-login menu
                } else {
                    statusLabel.setText("Invalid email or password.");
                }
            }
        });

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * Authenticate user credentials against the database
     */
    private static boolean authenticate(String email, String password) {
        String query = "SELECT * FROM signup WHERE email = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();  // Check if a matching record exists
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    /**
     * Display the post-login menu with various options
     */
    private static void showPostLoginMenu() {
        // Create the menu frame
        JFrame menuFrame = new JFrame("Post-Login Menu");
        menuFrame.setSize(450, 400);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title label
        JLabel menuTitleLabel = new JLabel("Welcome to the Post-Login Menu", JLabel.CENTER);
        menuTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuTitleLabel.setForeground(new Color(0, 122, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(menuTitleLabel, gbc);

        // Buttons for options
        JButton routeDetailsButton = createMenuButton("Route Details");
        JButton bookTicketButton = createMenuButton("Book a Ticket");
        JButton updateBookingButton = createMenuButton("Update Booking");
        JButton logoutButton = createMenuButton("Logout");

        // Arrange the buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(routeDetailsButton, gbc);

        gbc.gridy = 2;
        panel.add(bookTicketButton, gbc);

        gbc.gridy = 3;
        panel.add(updateBookingButton, gbc);

        gbc.gridy = 4;
        panel.add(logoutButton, gbc);

        // Add button actions
        routeDetailsButton.addActionListener(e -> {
            menuFrame.dispose();
            RouteDetails.open();  // Open RouteDetails window
        });

        bookTicketButton.addActionListener(e -> {
            menuFrame.dispose();
            BookingRoutesJoin.open();  // Open BookingRoutesJoin window
        });

        updateBookingButton.addActionListener(e -> {
            menuFrame.dispose();
            TicketBooking.open();  // Open TicketBooking window
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(menuFrame, "Logging out...");
            menuFrame.dispose();
            main(null);  // Redirect to login page
        });

        // Show the menu frame
        menuFrame.add(panel);
        menuFrame.setVisible(true);
    }

    /**
     * Utility function to create a menu button with styling
     */
    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 255));  // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 122, 204));  // Default color
            }
        });

        return button;
    }
}
