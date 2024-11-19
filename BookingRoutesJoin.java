import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class BookingRoutesJoin {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Update Booking Details");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Update Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 122, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Booking ID
        JLabel bookingIdLabel = new JLabel("Enter Booking ID:");
        bookingIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 10, 10);
        panel.add(bookingIdLabel, gbc);

        JTextField bookingIdField = new JTextField(20);
        bookingIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        bookingIdField.setBorder(new LineBorder(new Color(0, 122, 204), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(bookingIdField, gbc);

        // Fetch Details Button
        JButton fetchButton = new JButton("Fetch Details");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 16));
        fetchButton.setBackground(new Color(0, 122, 204)); // Blue background
        fetchButton.setForeground(Color.WHITE);           // White text
        fetchButton.setFocusPainted(false);
        fetchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(fetchButton, gbc);

        // Status Label
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        // Travel Date
        JLabel travelDateLabel = new JLabel("Travel Date (YYYY-MM-DD):");
        travelDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 10, 10);
        panel.add(travelDateLabel, gbc);

        JTextField travelDateField = new JTextField(20);
        travelDateField.setFont(new Font("Arial", Font.PLAIN, 16));
        travelDateField.setBorder(new LineBorder(new Color(0, 122, 204), 2));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(travelDateField, gbc);

        // Seats
        JLabel seatsLabel = new JLabel("Number of Seats:");
        seatsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(seatsLabel, gbc);

        JTextField seatsField = new JTextField(20);
        seatsField.setFont(new Font("Arial", Font.PLAIN, 16));
        seatsField.setBorder(new LineBorder(new Color(0, 122, 204), 2));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(seatsField, gbc);

        // Status Update
        JLabel statusUpdateLabel = new JLabel("Status (e.g., Confirmed, Cancelled):");
        statusUpdateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(statusUpdateLabel, gbc);

        JTextField statusField = new JTextField(20);
        statusField.setFont(new Font("Arial", Font.PLAIN, 16));
        statusField.setBorder(new LineBorder(new Color(0, 122, 204), 2));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(statusField, gbc);

        // Update Booking Button
        JButton updateButton = new JButton("Update Booking");
        updateButton.setFont(new Font("Arial", Font.BOLD, 18));
        updateButton.setBackground(new Color(34, 139, 34)); // Green background
        updateButton.setForeground(Color.WHITE);            // White text
        updateButton.setFocusPainted(false);
        updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);

        // Go to Login Page Button
        JButton goToLoginButton = new JButton("Go to Login Page");
        goToLoginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        goToLoginButton.setBackground(Color.WHITE);          // White background
        goToLoginButton.setForeground(new Color(0, 122, 204)); // Blue text
        goToLoginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        goToLoginButton.setFocusPainted(false);
        goToLoginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(goToLoginButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}
