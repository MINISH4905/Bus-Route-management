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
        frame.setSize(400, 400); // Adjusted size
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true); // Removes window border for cleaner look
        frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG); // Adds rounded edges

        // Create the panel with custom background color
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));

        // Title label with stylish font and color
        JLabel titleLabel = new JLabel("Delete Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 122, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20)); // Adds spacing

        // Input field for booking_id
        JLabel bookingIdLabel = new JLabel("Enter Booking ID:");
        bookingIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bookingIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(bookingIdLabel);
        panel.add(Box.createVerticalStrut(10)); // Adds spacing

        JTextField bookingIdField = new JTextField();
        bookingIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        bookingIdField.setPreferredSize(new Dimension(300, 30));
        bookingIdField.setHorizontalAlignment(JTextField.CENTER);
        bookingIdField.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2)); // Blue border
        panel.add(bookingIdField);
        panel.add(Box.createVerticalStrut(20)); // Adds spacing

        // Message label to show status
        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(20)); // Adds spacing

        // Delete button with modern look
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(0, 122, 204));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2, true)); // Rounded corners with LineBorder
        deleteButton.setPreferredSize(new Dimension(200, 40));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
                    messageLabel.setForeground(Color.GREEN); // Success message in green
                } else {
                    messageLabel.setText("Booking ID not found.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid Booking ID.");
            }
        });
        panel.add(deleteButton);
        panel.add(Box.createVerticalStrut(20)); // Adds spacing

        // Back button with hover effect
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(0, 122, 204));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.setVisible(false); // Hide the current window
            PostLoginMenu.main(null); // Assuming `PostLoginMenu` is your previous menu
        });
        panel.add(backButton);

        // Add the panel to the frame and show it
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
