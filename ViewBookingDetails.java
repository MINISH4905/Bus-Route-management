import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewBookingDetails {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        // Set up the frame
        JFrame frame = new JFrame("Booking Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        // Create the panel
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Booking Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 122, 204));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add columns to the table model
        model.addColumn("Booking ID");
        model.addColumn("User ID");
        model.addColumn("Route ID");
        model.addColumn("Booking Date");
        model.addColumn("Travel Date");
        model.addColumn("Seats");
        model.addColumn("Total Fare");
        model.addColumn("Status");

        // Populate table with data from database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM booking")) {

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("booking_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("route_id"),
                    resultSet.getDate("booking_date"),
                    resultSet.getDate("travel_date"),
                    resultSet.getInt("number_of_seats"),
                    resultSet.getDouble("total_fare"),
                    resultSet.getString("status")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Style the table
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(0, 122, 204));
        table.getTableHeader().setForeground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(0, 122, 204));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current window
            PostLoginMenu.main(null);
            System.out.println("Back button pressed. Implement logic to go back to the previous screen.");
        });

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(new Color(0, 122, 204));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> frame.dispose());

        // Create a panel to hold both buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}
