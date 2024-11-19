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
        // Create the login frame
        JFrame frame = new JFrame("Login Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        frame.add(panel);

        // Add components
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JLabel statusLabel = new JLabel("", JLabel.CENTER);

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty cell for alignment
        panel.add(loginButton);
        panel.add(statusLabel);

        // Add login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("All fields are required!");
                    return;
                }

                if (authenticate(email, password)) {
                    statusLabel.setText("Login successful!");
                    frame.dispose(); // Close login window
                    showPostLoginMenu(); // Open post-login menu
                } else {
                    statusLabel.setText("Invalid email or password.");
                }
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    /**
     * Method to authenticate a user using the signup table.
     */
    private static boolean authenticate(String email, String password) {
        // SQL query to verify the user in the signup table
        String query = "SELECT * FROM signup WHERE email = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a matching record exists
            return resultSet.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    /**
     * This method shows the post-login menu for the user in a new window.
     */
    private static void showPostLoginMenu() {
        // Create a new frame for the post-login menu
        JFrame menuFrame = new JFrame("Post-Login Menu");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        menuFrame.add(panel);

        // Add buttons
        JButton routeDetailsButton = new JButton("Route Details");
        JButton bookTicketButton = new JButton("Update booking Details");
        JButton updateBookingButton = new JButton("Book tickets");
        JButton logoutButton = new JButton("Logout");

        panel.add(routeDetailsButton);
        panel.add(bookTicketButton);
        panel.add(updateBookingButton);
        panel.add(logoutButton);

        // Add action listeners for redirection
        routeDetailsButton.addActionListener(e -> {
            menuFrame.dispose(); // Close the menu frame
            RouteDetails.open(); // Redirect to RouteDetails functionality
        });

        // Update: Redirecting to BookingRoutesJoin when the "Book a Ticket" button is clicked
        bookTicketButton.addActionListener(e -> {
            menuFrame.dispose(); // Close the menu frame
            BookingRoutesJoin.open(); // Redirect to BookingRoutesJoin functionality
        });

        updateBookingButton.addActionListener(e -> {
            menuFrame.dispose(); // Close the menu frame
            TicketBooking.open();
            // Add redirection to Update Booking if implemented
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(menuFrame, "Logging out...");
            menuFrame.dispose(); // Close the post-login menu
            main(null); // Redirect to login page
        });

        // Show the menu frame
        menuFrame.setVisible(true);
    }
}
