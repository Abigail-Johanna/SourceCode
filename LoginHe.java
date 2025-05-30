package loginhe;

import javax.swing.*;
import java.awt.*;

public class LoginHe extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> positionCombo;

    public LoginHe() {
        setTitle("Medical Staff Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(230, 230, 230)); 
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // username 
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        backgroundPanel.add(usernameField, gbc);

        // password 
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        backgroundPanel.add(passwordField, gbc);

        // position 
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        positionCombo = new JComboBox<>(new String[]{"Doctor", "Nurse"});
        backgroundPanel.add(positionCombo, gbc);

        // button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        backgroundPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String position = (String) positionCombo.getSelectedItem();

            JOptionPane.showMessageDialog(this, "Username: " + username +
                    "\nPosition: " + position + "\nPassword: " + password);
        });

        add(backgroundPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginHe().setVisible(true));
    }
}
