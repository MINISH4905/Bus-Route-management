import java.sql.*;
import java.util.Scanner;

public class RouteDetails {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open(Scanner scanner) {
        System.out.println("=== Route Details Finder ===");

        boolean exit = false;

        while (!exit) {
            System.out.print("\nEnter Route Name (or type 'back' to return to the menu): ");
            String routeName = scanner.nextLine();

            if (routeName.equalsIgnoreCase("back")) {
                break; // Return to post-login menu
            }

            displayRouteDetails(routeName);
        }
    }

    private static void displayRouteDetails(String routeName) {
        String query = "SELECT * FROM routes WHERE route_name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("\n=== Route Details ===");
                System.out.printf("Route ID: %d%n", resultSet.getInt("route_id"));
                System.out.printf("Route Name: %s%n", routeName);
                System.out.printf("Start Point: %s%n", resultSet.getString("start_point"));
                System.out.printf("End Point: %s%n", resultSet.getString("end_point"));
                System.out.printf("Distance (km): %.2f%n", resultSet.getDouble("distance_km"));
                System.out.printf("Estimated Time: %s%n", resultSet.getTime("estimated_time"));
                System.out.printf("Fare: %.2f%n", resultSet.getDouble("fare"));
            } else {
                System.out.println("No route found with the name: " + routeName);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
