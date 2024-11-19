import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class TicketBooking {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        // Set the look and feel of the UI for better consistency across platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the frame
        JFrame frame = new JFrame("Ticket Booking");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.setResizable(false);

        // Create JPanel with GridBagLayout for better component arrangement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margin to the panel

        // Title Label
        JLabel titleLabel = new JLabel("Ticket Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 122, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);  // Add space after title
        panel.add(titleLabel, gbc);

        // Route Name
        JLabel routeNameLabel = new JLabel("Route Name:");
        routeNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 0);  // Add space between rows
        panel.add(routeNameLabel, gbc);
        
        JTextField routeNameField = new JTextField(30);  // Wider field
        routeNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        routeNameField.setBackground(Color.WHITE);  // Set background color
        routeNameField.setBorder(new LineBorder(new Color(0, 122, 204), 2));  // Set border color
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(routeNameField, gbc);

        // Start Point
        JLabel startPointLabel = new JLabel("Start Point:");
        startPointLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(startPointLabel, gbc);

        JTextField startPointField = new JTextField(30);  // Wider field
        startPointField.setFont(new Font("Arial", Font.PLAIN, 16));
        startPointField.setBackground(Color.WHITE);  // Set background color
        startPointField.setBorder(new LineBorder(new Color(0, 122, 204), 2));  // Set border color
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(startPointField, gbc);

        // Travel Date
        JLabel travelDateLabel = new JLabel("Travel Date (YYYY-MM-DD):");
        travelDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(travelDateLabel, gbc);

        JTextField travelDateField = new JTextField(30);  // Wider field
        travelDateField.setFont(new Font("Arial", Font.PLAIN, 16));
        travelDateField.setBackground(Color.WHITE);  // Set background color
        travelDateField.setBorder(new LineBorder(new Color(0, 122, 204), 2));  // Set border color
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(travelDateField, gbc);

        // User ID
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(userIdLabel, gbc);

        JTextField userIdField = new JTextField(30);  // Wider field
        userIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        userIdField.setBackground(Color.WHITE);  // Set background color
        userIdField.setBorder(new LineBorder(new Color(0, 122, 204), 2));  // Set border color
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(userIdField, gbc);

        // Number of Seats
        JLabel numberOfSeatsLabel = new JLabel("Number of Seats:");
        numberOfSeatsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(numberOfSeatsLabel, gbc);

        JTextField numberOfSeatsField = new JTextField(30);  // Wider field
        numberOfSeatsField.setFont(new Font("Arial", Font.PLAIN, 16));
        numberOfSeatsField.setBackground(Color.WHITE);  // Set background color
        numberOfSeatsField.setBorder(new LineBorder(new Color(0, 122, 204), 2));  // Set border color
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(numberOfSeatsField, gbc);

        // Book Ticket Button
        JButton bookTicketButton = new JButton("Book Ticket");
        bookTicketButton.setFont(new Font("Arial", Font.BOLD, 18));
        bookTicketButton.setBackground(new Color(0, 122, 204));
        bookTicketButton.setForeground(Color.WHITE);
        bookTicketButton.setBorderPainted(false);
        bookTicketButton.setFocusPainted(false);
        bookTicketButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(bookTicketButton, gbc);

        // Message Label
        JLabel messageLabel = new JLabel("Please enter the details above.");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        // Go to Login Page Button
        JButton goToLoginButton = new JButton("Go to Login Page");
        goToLoginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        goToLoginButton.setBackground(Color.WHITE);
        goToLoginButton.setForeground(new Color(0, 122, 204));
        goToLoginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        goToLoginButton.setFocusPainted(false);
        goToLoginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(goToLoginButton, gbc);

        // Action listener for booking ticket
        bookTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from input fields
                String routeName = routeNameField.getText();
                String startPoint = startPointField.getText();
                String travelDate = travelDateField.getText();
                String userIdInput = userIdField.getText();
                String seatsInput = numberOfSeatsField.getText();

                if (routeName.isEmpty() || startPoint.isEmpty() || travelDate.isEmpty() ||
                        userIdInput.isEmpty() || seatsInput.isEmpty()) {
                    messageLabel.setText("All fields are required.");
                    return;
                }

                if (!isValidDate(travelDate)) {
                    messageLabel.setText("Invalid date format. Use YYYY-MM-DD.");
                    return;
                }

                try {
                    int userId = Integer.parseInt(userIdInput);
                    int numberOfSeats = Integer.parseInt(seatsInput);

                    int routeId = getRouteId(routeName, startPoint);

                    if (routeId != -1) {
                        double farePerSeat = getRouteFare(routeId);
                        double totalFare = farePerSeat * numberOfSeats;

                        insertBooking(userId, routeId, travelDate, numberOfSeats, totalFare, messageLabel);
                    } else {
                        messageLabel.setText("Invalid route name or start point.");
                    }

                } catch (NumberFormatException ex) {
                    messageLabel.setText("Invalid input. Please enter valid numbers for User ID and Seats.");
                }
            }
        });

        // Action listener for the 'Go to Login Page' button
        goToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false); // Hide the current window
                login.main(null); // Assuming you have a Login class
            }
        });

        // Show the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private static void insertBooking(int userId, int routeId, String travelDate, int numberOfSeats, double totalFare, JLabel messageLabel) {
        String query = "INSERT INTO booking (user_id, route_id, booking_date, travel_date, number_of_seats, total_fare, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, routeId);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));  // Current date
            preparedStatement.setDate(4, Date.valueOf(travelDate));  // Travel date
            preparedStatement.setInt(5, numberOfSeats);
            preparedStatement.setDouble(6, totalFare);
            preparedStatement.setString(7, "Booked");  // Default status

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                messageLabel.setText("Booking successful! Total fare: ₹" + totalFare);
            } else {
                messageLabel.setText("Booking failed. Try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    private static int getRouteId(String routeName, String startPoint) {
        String query = "SELECT route_id FROM routes WHERE route_name = ? AND start_point = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, routeName);
            preparedStatement.setString(2, startPoint);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("route_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no route found
    }

    private static double getRouteFare(int routeId) {
        String query = "SELECT fare FROM routes WHERE route_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("fare");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Return 0.0 if no fare found
    }

    public static void main(String[] args) {
        open();  // Open the Ticket Booking GUI initially
    }
}
