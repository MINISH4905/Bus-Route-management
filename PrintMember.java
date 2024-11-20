import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class PrintMembers {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus"; // Replace with your database URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "moni2626"; // Replace with your DB password

    public static void main(String[] args) {
        // Call the method to create the GUI and fetch members
        SwingUtilities.invokeLater(PrintMembers::createAndShowGUI);
    }

    // Method to create the GUI
    public static void createAndShowGUI() {
        // Create the main JFrame
        JFrame frame = new JFrame("Signed-Up Members");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);  // Adjusted frame size to accommodate longer lists
        frame.setLocationRelativeTo(null);

        // Create a panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240)); // Light gray background for the panel

        // Create the title label
        JLabel titleLabel = new JLabel("List of Signed-Up Members", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 122, 204));  // Title color
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(240, 240, 240)); // Same background color as panel
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // Padding for title

        // Create a text area to display members
        JTextArea memberListTextArea = new JTextArea();
        memberListTextArea.setEditable(false);
        memberListTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        memberListTextArea.setLineWrap(true);  // Allows text wrapping
        memberListTextArea.setWrapStyleWord(true);  // Wraps at word boundaries
        memberListTextArea.setBackground(new Color(255, 255, 255)); // White background for text area
        memberListTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));  // Border for text area

        // Add the text area inside a scroll pane for scrollable content
        JScrollPane scrollPane = new JScrollPane(memberListTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch the members and update the text area
        String membersList = getMembersListFromDatabase();
        memberListTextArea.setText(membersList);

        // Add a Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(200, 200, 200)); // Light gray button
        backButton.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for the Back button: Close the current frame
                frame.dispose();
                PostLoginMenu.main(null);
            }
        });

        // Create a footer panel for the Back button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240)); // Match panel background
        footerPanel.add(backButton);

        // Add the title, panel, and footer to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    // Method to fetch members from the database and return a formatted string
    public static String getMembersListFromDatabase() {
        String query = "SELECT * FROM signup"; // Modify this if you need specific columns
        StringBuilder membersList = new StringBuilder();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Check if there are no results
            if (!resultSet.next()) {
                return "No members found in the database.";
            }

            // Get column count for formatting
            int columnCount = resultSet.getMetaData().getColumnCount();
            membersList.append("-------------------------------------------------\n");

            // Print the column names (for readability) with alignment
            int[] columnWidths = new int[columnCount]; // Track the width of each column
            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                membersList.append(String.format("%-" + columnName.length() + "s", columnName));
                columnWidths[i - 1] = columnName.length(); // Initialize width to column name length
                if (i < columnCount) membersList.append("  "); // Add spacing between columns
            }
            membersList.append("\n-------------------------------------------------\n");

            // Print the member details with proper alignment
            do {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    int width = columnWidths[i - 1];
                    membersList.append(String.format("%-" + width + "s", value)); // Format each value to align with the column width
                    if (i < columnCount) membersList.append("  "); // Add spacing between columns
                }
                membersList.append("\n");
            } while (resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching members from the database.";
        }

        return membersList.toString();
    }
}
