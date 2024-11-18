import java.sql.*;
import java.util.Scanner;

public class login {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Login Page ===");

        // Get email and password input from the user
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Authenticate the user
        if (authenticate(email, password)) {
            System.out.println("Login successful! Welcome!");
            postLoginMenu(scanner); // After login, show the post-login menu
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }

        scanner.close();
    }

    /**
     * Method to authenticate a user using the signup table
     *
     * @param email    The email entered by the user
     * @param password The password entered by the user
     * @return boolean Whether the user is authenticated successfully
     */
    private static boolean authenticate(String email, String password) {
        // SQL query to verify the user in the signup table
        String query = "SELECT * FROM signup WHERE email = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a matching record exists
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return false;
    }

    /**
     * This method shows the post-login menu for the user.
     */
    private static void postLoginMenu(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Post-Login Menu ===");
            System.out.println("1. Route Details");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Update Booking Details");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            switch (choice) {
                case 1:
                    // Route Details functionality (Navigate to RouteDetails.java)
                    RouteDetails.open(scanner);
                    break;
                case 2:
                    // Book a Ticket functionality (Navigate to TicketBooking.java)
                    TicketBooking.open(scanner);
                    break;
                case 3:
                    // View Booking Details functionality (Navigate to BookingRoutesJoin.java)
                    BookingRoutesJoin.open(scanner);
                    break;
                case 4:
                    // Logout and exit
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
