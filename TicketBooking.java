import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TicketBooking {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        // Create a JFrame window
        JFrame frame = new JFrame("Ticket Booking");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(9, 2, 10, 10));  // Updated layout to add one more button

        // Create components for booking
        JLabel routeNameLabel = new JLabel("Route Name:");
        JTextField routeNameField = new JTextField();
        JLabel startPointLabel = new JLabel("Start Point:");
        JTextField startPointField = new JTextField();
        JLabel travelDateLabel = new JLabel("Travel Date (YYYY-MM-DD):");
        JTextField travelDateField = new JTextField();
        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField();
        JLabel numberOfSeatsLabel = new JLabel("Number of Seats:");
        JTextField numberOfSeatsField = new JTextField();

        JButton bookTicketButton = new JButton("Book Ticket");
        JLabel messageLabel = new JLabel("Please enter the details above.");

        // Add components to the frame
        frame.add(routeNameLabel);
        frame.add(routeNameField);
        frame.add(startPointLabel);
        frame.add(startPointField);
        frame.add(travelDateLabel);
        frame.add(travelDateField);
        frame.add(userIdLabel);
        frame.add(userIdField);
        frame.add(numberOfSeatsLabel);
        frame.add(numberOfSeatsField);
        frame.add(bookTicketButton);
        frame.add(messageLabel);

        // Button to go to login page
        JButton goToLoginButton = new JButton("Go to Login Page");
        frame.add(goToLoginButton);  // Add the button to the frame

        // Action listener for booking ticket
        bookTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from the input fields
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

                try {
                    int userId = Integer.parseInt(userIdInput); // Assuming user_id is an integer
                    int numberOfSeats = Integer.parseInt(seatsInput);

                    // Get route_id based on route_name and start_point
                    int routeId = getRouteId(routeName, startPoint);

                    if (routeId != -1) {
                        // Insert the booking details into the booking table
                        insertBooking(userId, routeId, travelDate, numberOfSeats, messageLabel);
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
                frame.setVisible(false);  // Hide the current TicketBooking window
                login.main(null); // Assuming you have a Login class
            }
        });

        // Display the window
        frame.setVisible(true);
    }

    // The rest of your existing code (getRouteId, insertBooking, getRouteFare) remains the same.

    public static void main(String[] args) {
        open();  // Open the Ticket Booking GUI initially
    }
}
