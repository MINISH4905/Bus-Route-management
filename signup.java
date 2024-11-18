import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class signup {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome! Please select an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Login Page");
            System.out.println("3. Exit");

            System.out.print("Enter your choice (1/2/3): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    performSignup(scanner);
                    break;
                case "2":
                    System.out.println("Redirecting to login page...");
                    // Here you can call your login class or method
                    login.main(null); // Replace with the correct call for your login page
                    return; // Exit the signup process after redirecting
                case "3":
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void performSignup(Scanner scanner) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/bus"; // Replace 'bus' with your database name
        String user = "root"; // Replace with your MySQL username
        String password = "moni2626"; // Replace with your MySQL password

        // SQL INSERT query
        String sql = "INSERT INTO signup (username, email, password) VALUES (?, ?, ?)";

        try {
            // Collect user input
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String plainPassword = scanner.nextLine();

            // For security, hash the password (this is a placeholder, replace with a real hashing function)
            String hashedPassword = hashPassword(plainPassword);

            // Establish database connection and insert user data
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Set parameters for the query
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashedPassword);

                // Execute the query
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Signup successful! You can now log in.");
                }
            } catch (Exception e) {
                System.err.println("Error during signup: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    // Placeholder method for hashing passwords
    public static String hashPassword(String password) {
        // Implement a proper password hashing algorithm like BCrypt here
        return password; // Return plain text password for now (NOT secure)
    }
}
