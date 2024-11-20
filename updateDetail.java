import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class updateDetail {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bus Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new CardLayout());

        // Theme colors
        Color bgColor = Color.decode("#f5f5f5");
        Color btnColor = Color.decode("#007bff");
        Color btnTextColor = Color.WHITE;

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        mainPanel.setBackground(bgColor);
        JLabel lblMain = new JLabel("Bus Booking Management", SwingConstants.CENTER);
        lblMain.setFont(new Font("SansSerif", Font.BOLD, 24));
        JButton btnCreateBooking = new JButton("Create New Booking");
        JButton btnUpdateBooking = new JButton("Update Booking");

        styleButton(btnCreateBooking, btnColor, btnTextColor);
        styleButton(btnUpdateBooking, btnColor, btnTextColor);

        mainPanel.add(lblMain);
        mainPanel.add(btnCreateBooking);
        mainPanel.add(btnUpdateBooking);
        frame.add(mainPanel);

        // Create Booking Panel
        JPanel createBookingPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        createBookingPanel.setBackground(bgColor);

        JLabel lblRouteName = new JLabel("Route Name:");
        JTextField txtRouteName = new JTextField();
        JLabel lblStartPoint = new JLabel("Start Point:");
        JTextField txtStartPoint = new JTextField();
        JLabel lblTravelDate = new JLabel("Travel Date (YYYY-MM-DD):");
        JTextField txtTravelDate = new JTextField();
        JLabel lblUserId = new JLabel("User ID:");
        JTextField txtUserId = new JTextField();
        JLabel lblSeats = new JLabel("Number of Seats:");
        JTextField txtSeats = new JTextField();

        JButton btnSubmitCreate = new JButton("Submit Booking");
        JButton btnBackCreate = new JButton("Back");
        styleButton(btnSubmitCreate, btnColor, btnTextColor);
        styleButton(btnBackCreate, btnColor, btnTextColor);

        createBookingPanel.add(lblRouteName);
        createBookingPanel.add(txtRouteName);
        createBookingPanel.add(lblStartPoint);
        createBookingPanel.add(txtStartPoint);
        createBookingPanel.add(lblTravelDate);
        createBookingPanel.add(txtTravelDate);
        createBookingPanel.add(lblUserId);
        createBookingPanel.add(txtUserId);
        createBookingPanel.add(lblSeats);
        createBookingPanel.add(txtSeats);
        createBookingPanel.add(btnSubmitCreate);
        createBookingPanel.add(btnBackCreate);
        frame.add(createBookingPanel);

        // Update Booking Panel
        JPanel updateBookingPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        updateBookingPanel.setBackground(bgColor);

        JLabel lblBookingId = new JLabel("Booking ID:");
        JTextField txtBookingId = new JTextField();
        JLabel lblUpdateField = new JLabel("Field to Update:");
        JComboBox<String> cmbUpdateField = new JComboBox<>(new String[]{"Travel Date", "Number of Seats"});
        JLabel lblNewValue = new JLabel("New Value:");
        JTextField txtNewValue = new JTextField();

        JButton btnSubmitUpdate = new JButton("Update Booking");
        JButton btnBackUpdate = new JButton("Back");
        styleButton(btnSubmitUpdate, btnColor, btnTextColor);
        styleButton(btnBackUpdate, btnColor, btnTextColor);

        updateBookingPanel.add(lblBookingId);
        updateBookingPanel.add(txtBookingId);
        updateBookingPanel.add(lblUpdateField);
        updateBookingPanel.add(cmbUpdateField);
        updateBookingPanel.add(lblNewValue);
        updateBookingPanel.add(txtNewValue);
        updateBookingPanel.add(btnSubmitUpdate);
        updateBookingPanel.add(btnBackUpdate);
        frame.add(updateBookingPanel);

        // Switch Panels
        btnCreateBooking.addActionListener(e -> {
            mainPanel.setVisible(false);
            TicketBooking.open();
        });

        btnUpdateBooking.addActionListener(e -> {
            mainPanel.setVisible(false);
            updateBookingPanel.setVisible(true);
        });

        btnBackCreate.addActionListener(e -> {
            createBookingPanel.setVisible(false);
            mainPanel.setVisible(true);
        });

        btnBackUpdate.addActionListener(e -> {
            updateBookingPanel.setVisible(false);
            PostLoginMenu.main(null);
        });

        // Handle Create Booking
        btnSubmitCreate.addActionListener(e -> {
            String routeName = txtRouteName.getText();
            String startPoint = txtStartPoint.getText();
            String travelDate = txtTravelDate.getText();
            int userId = Integer.parseInt(txtUserId.getText());
            int numberOfSeats = Integer.parseInt(txtSeats.getText());

            try {
                int routeId = getRouteId(routeName, startPoint);
                if (routeId != -1) {
                    insertBooking(userId, routeId, travelDate, numberOfSeats);
                    JOptionPane.showMessageDialog(frame, "Booking successfully created!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid route name or start point.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Handle Update Booking
        btnSubmitUpdate.addActionListener(e -> {
            int bookingId = Integer.parseInt(txtBookingId.getText());
            String updateField = (String) cmbUpdateField.getSelectedItem();
            String newValue = txtNewValue.getText();

            try {
                if ("Travel Date".equals(updateField)) {
                    updateTravelDate(bookingId, newValue);
                } else if ("Number of Seats".equals(updateField)) {
                    int newSeats = Integer.parseInt(newValue);
                    updateNumberOfSeats(bookingId, newSeats);
                }
                JOptionPane.showMessageDialog(frame, "Booking successfully updated!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
            System.err.println("Database error: " + e.getMessage());
        }
        return -1;
    }

    private static void insertBooking(int userId, int routeId, String travelDate, int numberOfSeats) {
        double fare = getRouteFare(routeId);
        double totalFare = fare * numberOfSeats;

        String query = "INSERT INTO booking (user_id, route_id, booking_date, travel_date, number_of_seats, total_fare, status) " +
                       "VALUES (?, ?, NOW(), ?, ?, ?, 'Booked')";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, routeId);
            preparedStatement.setDate(3, Date.valueOf(travelDate));
            preparedStatement.setInt(4, numberOfSeats);
            preparedStatement.setDouble(5, totalFare);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void updateTravelDate(int bookingId, String newTravelDate) {
        String query = "UPDATE booking SET travel_date = ? WHERE booking_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(newTravelDate));
            preparedStatement.setInt(2, bookingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void updateNumberOfSeats(int bookingId, int newSeats) {
        String query = "UPDATE booking SET number_of_seats = ? WHERE booking_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newSeats);
            preparedStatement.setInt(2, bookingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
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
            System.err.println("Database error: " + e.getMessage());
        }
        return 0;
    }
}
