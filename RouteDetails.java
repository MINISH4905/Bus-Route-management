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
        // Set the look and feel of the UI for better consistency across platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and setup the frame for Route Details Finder
        JFrame frame = new JFrame("Route Details Finder");
        frame.setSize(900, 700);  // Adjusted frame size for better content display
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the frame on the screen
        frame.setResizable(false);

        // Create a JPanel with GridBagLayout for better control of component placement
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Added margin for better spacing

        // Title Label
        JLabel titleLabel = new JLabel("Route Details Finder");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 122, 204));  // Vibrant color for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);  // Spacing after the title
        panel.add(titleLabel, gbc);

        // Route Name Label and TextField
        JLabel routeLabel = new JLabel("Enter Route Name:");
        routeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(routeLabel, gbc);

        JTextField routeField = new JTextField(30);  // Adjusted width of the text field
        routeField.setFont(new Font("Arial", Font.PLAIN, 16));
        routeField.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;  // Allow the field to span two columns
        panel.add(routeField, gbc);

        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchButton.setBackground(new Color(0, 122, 204));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect for Search button
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 150, 255)); // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 122, 204)); // Default color
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;  // Button spans all columns
        panel.add(searchButton, gbc);

        // Result Area for displaying the route details
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);  // Make the result area read-only
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        JScrollPane scrollPane = new JScrollPane(resultArea); // Added scroll pane for better result area visibility
        scrollPane.setPreferredSize(new Dimension(700, 200));  // Adjusted size for result area
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 0, 20, 0);  // Added space between result area and buttons
        panel.add(scrollPane, gbc);

        // Buttons panel (Back and Login buttons)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(0, 122, 204));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect for Back button
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0, 122, 204));  // Lighter on hover
                backButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.WHITE);  // Default color
                backButton.setForeground(new Color(0, 122, 204));  // Default text color
            }
        });

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setBackground(new Color(255, 255, 255));
        loginButton.setForeground(new Color(0, 122, 204));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 2));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect for Login button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 122, 204));  // Lighter on hover
                loginButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Color.WHITE);  // Default color
                loginButton.setForeground(new Color(0, 122, 204));  // Default text color
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

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
        frame.add(panel);
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
