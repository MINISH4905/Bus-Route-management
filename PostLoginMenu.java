import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PostLoginMenu {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "moni2626"; // Replace with your DB password

    public static void showMenu() {
        // Create the menu frame
        JFrame menuFrame = new JFrame("Main Menu");
        menuFrame.setSize(450, 400);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title label
        JLabel menuTitleLabel = new JLabel("Welcome to the Main Menu", JLabel.CENTER);
        menuTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuTitleLabel.setForeground(new Color(0, 122, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(menuTitleLabel, gbc);

        // Buttons for options
        JButton routeDetailsButton = createMenuButton("Route Details");
        JButton bookTicketButton = createMenuButton("Book a Ticket");
        JButton updateBookingButton = createMenuButton("Update Booking");
        JButton viewBooking = createMenuButton("View Booking");
        JButton deleteBooking = createMenuButton("Delete Booking");
        JButton Check = createMenuButton("Members");
        JButton update = createMenuButton("Update Details");
        JButton logoutButton = createMenuButton("Logout");

        // Arrange the buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(routeDetailsButton, gbc);

        gbc.gridy = 2;
        panel.add(bookTicketButton, gbc);


        gbc.gridy = 3;
        panel.add(viewBooking, gbc);

        gbc.gridy = 4;
        panel.add(deleteBooking, gbc);

        gbc.gridy = 5;
        panel.add(Check, gbc);

        gbc.gridy = 6;
        panel.add(update, gbc);

        gbc.gridy = 7;
        panel.add(logoutButton, gbc);

        // Add button actions
        routeDetailsButton.addActionListener(e -> {
            menuFrame.dispose();
            RouteDetails.open(); // Display route details from the database
        });

        bookTicketButton.addActionListener(e -> {
            menuFrame.dispose();
            // Replace with appropriate functionality
            TicketBooking.open();
        });


        viewBooking.addActionListener(e -> {
            menuFrame.dispose();
            ViewBookingDetails.open(); // Display route details from the database
        });

        deleteBooking.addActionListener(e -> {
            menuFrame.dispose();
            DeleteBooking.open(); // Display route details from the database
        });

        Check.addActionListener(e -> {
            menuFrame.dispose();
            PrintMembers.main(null);// Display route details from the database
        });

        update.addActionListener(e -> {
            menuFrame.dispose();
            updateDetail.main(null);
        });

        // ActionListener for the "Go to Login" button
        logoutButton.addActionListener(e -> {
            menuFrame.dispose();
            login.main(null);
        });


        // Show the menu frame
        menuFrame.add(panel);
        menuFrame.setVisible(true);
    }

    /**
     * Utility function to create a menu button with styling
     */
    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 255)); // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 122, 204)); // Default color
            }
        });

        return button;
    }

    /**
     * Function to display route details from the database
     */
    private static void displayRouteDetails() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM routes"; // Assuming a table named 'routes'
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder routesData = new StringBuilder("Route Details:\n\n");
            while (rs.next()) {
                routesData.append("Route ID: ").append(rs.getInt("route_id")).append("\n")
                        .append("Start: ").append(rs.getString("start")).append("\n")
                        .append("Destination: ").append(rs.getString("destination")).append("\n")
                        .append("Fare: ").append(rs.getDouble("fare")).append("\n\n");
            }

            JOptionPane.showMessageDialog(null, routesData.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching route details: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        showMenu();
    }
}
