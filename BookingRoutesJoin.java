import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BookingRoutesJoin {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Update with your DB URL
    private static final String DB_USER = "root"; // Update with your DB username
    private static final String DB_PASSWORD = "moni2626"; // Update with your DB password

    public static void open() {
        // Create the frame for the booking update UI
        JFrame frame = new JFrame("Update Booking Details");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2, 10, 10));  // Updated grid to fit the new button

        // Create components
        JLabel bookingIdLabel = new JLabel("Enter Booking ID: ");
        JTextField bookingIdField = new JTextField();

        JButton fetchButton = new JButton("Fetch Details");
        JLabel statusLabel = new JLabel("");

        // Fields for updating details
        JLabel travelDateLabel = new JLabel("Travel Date (YYYY-MM-DD): ");
        JTextField travelDateField = new JTextField();

        JLabel seatsLabel = new JLabel("Number of Seats: ");
        JTextField seatsField = new JTextField();

        JLabel statusUpdateLabel = new JLabel("Status (e.g., Confirmed, Cancelled): ");
        JTextField statusField = new JTextField();

        // Add components to the frame
        frame.add(bookingIdLabel);
        frame.add(bookingIdField);
        frame.add(fetchButton);
        frame.add(statusLabel);

        frame.add(travelDateLabel);
        frame.add(travelDateField);
        frame.add(seatsLabel);
        frame.add(seatsField);
        frame.add(statusUpdateLabel);
        frame.add(statusField);

        // Action listener for fetch button
        fetchButton.addActionListener(e -> {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            displayBookingDetails(bookingId, statusLabel, travelDateField, seatsField, statusField);
        });

        // Add action listener to update the booking details
        JButton updateButton = new JButton("Update Booking");
        updateButton.addActionListener(e -> {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            String travelDate = travelDateField.getText();
            int numberOfSeats = Integer.parseInt(seatsField.getText());
            String status = statusField.getText();

            // Update booking details
            updateBookingDetails(bookingId, travelDate, numberOfSeats, status, statusLabel);
        });
        frame.add(updateButton);

        // Add button to go back to login page
        JButton goToLoginButton = new JButton("Go to Login Page");
        goToLoginButton.addActionListener(e -> {
            frame.setVisible(false);  // Hide the current frame
            login.main(null); // Assuming you have a Login class
        });
        frame.add(goToLoginButton);  // Add the "Go to Login Page" button to the frame

        // Show the frame
        frame.setVisible(true);
    }

    /**
     * Display current booking details before updating.
     */
    private static void displayBookingDetails(int bookingId, JLabel statusLabel, JTextField travelDateField, 
                                              JTextField seatsField, JTextField statusField) {
        String query = "SELECT booking_id, travel_date, number_of_seats, status " +
                       "FROM booking WHERE booking_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display booking details in the fields
                travelDateField.setText(resultSet.getString("travel_date"));
                seatsField.setText(String.valueOf(resultSet.getInt("number_of_seats")));
                statusField.setText(resultSet.getString("status"));
                statusLabel.setText("Booking found! You can now update.");
            } else {
                statusLabel.setText("Booking not found.");
            }

        } catch (SQLException e) {
            statusLabel.setText("Database error: " + e.getMessage());
        }
    }

    /**
     * Update the booking details in the database.
     */
    private static void updateBookingDetails(int bookingId, String travelDate, int numberOfSeats, String status,
                                             JLabel statusLabel) {
        String updateQuery = "UPDATE booking SET travel_date = ?, number_of_seats = ?, status = ? WHERE booking_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, travelDate);
            preparedStatement.setInt(2, numberOfSeats);
            preparedStatement.setString(3, status);
            preparedStatement.setInt(4, bookingId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                statusLabel.setText("Booking updated successfully.");
            } else {
                statusLabel.setText("Failed to update booking.");
            }

        } catch (SQLException e) {
            statusLabel.setText("Database error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        open();  // Open the Booking Routes Join GUI
    }
}
