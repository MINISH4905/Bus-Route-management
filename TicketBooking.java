import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TicketBooking {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open(Scanner scanner) {
        while (true) { // Loop to allow user to re-enter details or go back
            try {
                System.out.println("Type 'back' anytime to return to the previous menu.");

                // Prompt user for route details
                System.out.print("Enter route_name: ");
                String routeName = scanner.nextLine();
                if (routeName.equalsIgnoreCase("back")) return; // Exit method if "back"

                System.out.print("Enter start_point: ");
                String startPoint = scanner.nextLine();
                if (startPoint.equalsIgnoreCase("back")) return; // Exit method if "back"

                System.out.print("Enter travel_date (YYYY-MM-DD): ");
                String travelDate = scanner.nextLine();
                if (travelDate.equalsIgnoreCase("back")) return; // Exit method if "back"

                System.out.print("Enter user_id: ");
                String userIdInput = scanner.nextLine();
                if (userIdInput.equalsIgnoreCase("back")) return; // Exit method if "back"
                int userId = Integer.parseInt(userIdInput); // Assuming user_id is an integer

                System.out.print("Enter number_of_seats: ");
                String seatsInput = scanner.nextLine();
                if (seatsInput.equalsIgnoreCase("back")) return; // Exit method if "back"
                int numberOfSeats = Integer.parseInt(seatsInput);

                // Get route_id based on route_name and start_point
                int routeId = getRouteId(routeName, startPoint);

                if (routeId != -1) {
                    // Insert the booking details into the booking table
                    insertBooking(userId, routeId, travelDate, numberOfSeats);
                } else {
                    System.out.println("Invalid route name or start point.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Method to retrieve the route_id based on route_name and start_point
     * @param routeName The name of the route
     * @param startPoint The starting point of the route
     * @return route_id or -1 if the route is not found
     */
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

    /**
     * Method to insert a booking record into the booking table
     * @param userId The ID of the user making the booking
     * @param routeId The ID of the route for the booking
     * @param travelDate The date of travel
     * @param numberOfSeats The number of seats booked
     */
    private static void insertBooking(int userId, int routeId, String travelDate, int numberOfSeats) {
        double fare = getRouteFare(routeId);
        double totalFare = fare * numberOfSeats;

        String query = "INSERT INTO booking (user_id, route_id, booking_date, travel_date, number_of_seats, total_fare, status) " +
                       "VALUES (?, ?, NOW(), ?, ?, ?, 'Booked')";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, routeId);

            // Use java.sql.Date explicitly
            preparedStatement.setDate(3, java.sql.Date.valueOf(travelDate));
            preparedStatement.setInt(4, numberOfSeats);
            preparedStatement.setDouble(5, totalFare);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking successfully created!");
            } else {
                System.out.println("Failed to create booking.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Method to get the fare of the route by route_id
     * @param routeId The route ID for which to fetch the fare
     * @return fare The fare for the route
     */
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
        return 0;  // Return 0 if fare is not found
    }
}
