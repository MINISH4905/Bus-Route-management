import java.sql.*;
import java.util.Scanner;

public class BookingRoutesJoin {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Update with your DB URL
    private static final String DB_USER = "root"; // Update with your DB username
    private static final String DB_PASSWORD = "moni2626"; // Update with your DB password

    public static void open(Scanner scanner) {
        // Prompt user for booking ID to update
        System.out.println("=== Update Booking Details ===");
        
        // Get user input for booking ID
        System.out.print("Enter Booking ID to update: ");
        int bookingId = Integer.parseInt(scanner.nextLine());

        // Display current booking details
        displayBookingDetails(bookingId);

        // Get user input for the field to update
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Travel Date");
        System.out.println("2. Number of Seats");
        System.out.println("3. Status");
        System.out.print("Enter choice (1-3): ");
        int choice = Integer.parseInt(scanner.nextLine());

        // Call update method based on user choice
        switch (choice) {
            case 1:
                updateTravelDate(bookingId, scanner);
                break;
            case 2:
                updateNumberOfSeats(bookingId, scanner);
                break;
            case 3:
                updateStatus(bookingId, scanner);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }

        displayBookingDetails(bookingId);
    }

    /**
     * Display current booking details before updating.
     * 
     * @param bookingId The ID of the booking to display.
     */
    private static void displayBookingDetails(int bookingId) {
        String query = "SELECT booking.booking_id, booking.user_id, booking.booking_date, booking.travel_date, " +
                "booking.number_of_seats, booking.total_fare, booking.status, routes.route_name, routes.start_point, " +
                "routes.end_point, routes.distance_km, routes.estimated_time, routes.fare " +
                "FROM booking " +
                "JOIN routes ON booking.route_id = routes.route_id " +
                "WHERE booking.booking_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the booking ID parameter in the query
            preparedStatement.setInt(1, bookingId);

            // Execute the query and get the results
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display booking details
                System.out.println("\n=== Current Booking Details ===");
                System.out.printf("Booking ID: %d%n", resultSet.getInt("booking_id"));
                System.out.printf("User ID: %d%n", resultSet.getInt("user_id"));
                System.out.printf("Booking Date: %s%n", resultSet.getString("booking_date"));
                System.out.printf("Travel Date: %s%n", resultSet.getString("travel_date"));
                System.out.printf("Number of Seats: %d%n", resultSet.getInt("number_of_seats"));
                System.out.printf("Total Fare: %.2f%n", resultSet.getDouble("total_fare"));
                System.out.printf("Booking Status: %s%n", resultSet.getString("status"));

                System.out.printf("\nRoute Name: %s%n", resultSet.getString("route_name"));
                System.out.printf("Start Point: %s%n", resultSet.getString("start_point"));
                System.out.printf("End Point: %s%n", resultSet.getString("end_point"));
                System.out.printf("Distance (km): %.2f%n", resultSet.getDouble("distance_km"));
                System.out.printf("Estimated Time: %s%n", resultSet.getString("estimated_time"));
                System.out.printf("Route Fare: %.2f%n", resultSet.getDouble("fare"));
            } else {
                System.out.println("Booking not found.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Update the travel date of the booking.
     * 
     * @param bookingId The ID of the booking to update.
     * @param scanner The scanner object to read user input.
     */
    private static void updateTravelDate(int bookingId, Scanner scanner) {
        // Get new travel date from the user
        System.out.print("Enter new Travel Date (YYYY-MM-DD): ");
        String newTravelDate = scanner.nextLine();

        String updateQuery = "UPDATE booking SET travel_date = ? WHERE booking_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameters for the update query
            preparedStatement.setString(1, newTravelDate);
            preparedStatement.setInt(2, bookingId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Travel Date updated successfully.");
            } else {
                System.out.println("Failed to update Travel Date.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Update the number of seats in the booking.
     * 
     * @param bookingId The ID of the booking to update.
     * @param scanner The scanner object to read user input.
     */
    private static void updateNumberOfSeats(int bookingId, Scanner scanner) {
        // Get new number of seats from the user
        System.out.print("Enter new number of seats: ");
        int newNumberOfSeats = Integer.parseInt(scanner.nextLine());

        // Get the route fare to calculate the new total fare
        String fareQuery = "SELECT fare FROM routes WHERE route_id = (SELECT route_id FROM booking WHERE booking_id = ?)";
        double newTotalFare = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement fareStatement = connection.prepareStatement(fareQuery)) {

            // Set the booking ID to find the fare
            fareStatement.setInt(1, bookingId);
            ResultSet resultSet = fareStatement.executeQuery();

            if (resultSet.next()) {
                double routeFare = resultSet.getDouble("fare");
                newTotalFare = routeFare * newNumberOfSeats;
            }

            // Update the number of seats and total fare in the booking
            String updateQuery = "UPDATE booking SET number_of_seats = ?, total_fare = ? WHERE booking_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, newNumberOfSeats);
                preparedStatement.setDouble(2, newTotalFare);
                preparedStatement.setInt(3, bookingId);

                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Number of Seats updated successfully.");
                    System.out.printf("New Total Fare: %.2f%n", newTotalFare);
                } else {
                    System.out.println("Failed to update Number of Seats.");
                }

            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Update the status of the booking.
     * 
     * @param bookingId The ID of the booking to update.
     * @param scanner The scanner object to read user input.
     */
    private static void updateStatus(int bookingId, Scanner scanner) {
        // Get new status from the user
        System.out.print("Enter new Status (e.g., 'Confirmed', 'Cancelled', etc.): ");
        String newStatus = scanner.nextLine();

        String updateQuery = "UPDATE booking SET status = ? WHERE booking_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameters for the update query
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, bookingId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update Status.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
