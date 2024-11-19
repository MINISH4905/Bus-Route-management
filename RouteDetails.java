import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class RouteDetails {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "moni2626";

    public static void open() {
        // Create the frame for Route Details Finder
        JFrame frame = new JFrame("Route Details Finder");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        frame.add(panel);

        // Create input and output components
        JLabel routeLabel = new JLabel("Enter Route Name:");
        JTextField routeField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        JButton loginButton = new JButton("Login");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false); // Make the result area read-only
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        // Organize components in the panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(routeLabel);
        inputPanel.add(routeField);
        inputPanel.add(searchButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);  // Added login button

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routeName = routeField.getText().trim();
                if (routeName.isEmpty()) {
                    resultArea.setText("Please enter a route name.");
                } else {
                    String result = getRouteDetails(routeName);
                    resultArea.setText(result);
                }
            }
        });

        // Add action listener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the Route Details window
            }
        });

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the Route Details window
                login.main(null); // Assuming you have a Login class
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    /**
     * Method to retrieve route details from the database
     *
     * @param routeName The name of the route to search for
     * @return A string containing the route details or an error message
     */
    private static String getRouteDetails(String routeName) {
        String query = "SELECT * FROM routes WHERE route_name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return String.format(
                        "=== Route Details ===\n" +
                        "Route ID: %d\n" +
                        "Route Name: %s\n" +
                        "Start Point: %s\n" +
                        "End Point: %s\n" +
                        "Distance (km): %.2f\n" +
                        "Estimated Time: %s\n" +
                        "Fare: %.2f\n",
                        resultSet.getInt("route_id"),
                        resultSet.getString("route_name"),
                        resultSet.getString("start_point"),
                        resultSet.getString("end_point"),
                        resultSet.getDouble("distance_km"),
                        resultSet.getTime("estimated_time"),
                        resultSet.getDouble("fare")
                );
            } else {
                return "No route found with the name: " + routeName;
            }
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
}
