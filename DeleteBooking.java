import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class DeleteBooking {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        // Set up the frame
        JFrame frame = new JFrame("Delete Booking");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 350); // Adjusted size to fit the back button
        frame.setLocationRelativeTo(null);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title label
        JLabel titleLabel = new JLabel("Delete Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 122, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Input field for booking_id
        JLabel bookingIdLabel = new JLabel("Enter Booking ID:");
        bookingIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(bookingIdLabel);

        JTextField bookingIdField = new JTextField();
        bookingIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(bookingIdField);

        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(0, 122, 204));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(deleteButton);

        // Message label
        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(0, 122, 204));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        panel.add(backButton);

        // Action listener for delete button
        deleteButton.addActionListener(e -> {
            String bookingIdInput = bookingIdField.getText();

            if (bookingIdInput.isEmpty()) {
                messageLabel.setText("Booking ID is required.");
                return;
            }

            try {
                int bookingId = Integer.parseInt(bookingIdInput);
                if (deleteBookingFromDatabase(bookingId)) {
                    messageLabel.setText("Booking deleted successfully.");
                } else {
                    messageLabel.setText("Booking ID not found.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid Booking ID.");
            }
        });

        // Action listener for back button
        backButton.addActionListener(e -> {
            frame.setVisible(false); // Hide the current window
            PostLoginMenu.main(null);
            System.out.println("Back button pressed. Implement logic to go back to the previous screen.");
        });

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean deleteBookingFromDatabase(int bookingId) {
        String query = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;  // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if an error occurs or no rows were deleted
    }

    public static void main(String[] args) {
        open();
    }
}
