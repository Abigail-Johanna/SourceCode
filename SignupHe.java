package signuphe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupHe {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpForm());
    }
}
class SignUpForm extends JFrame {

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(230, 230, 230); // dirty white
            g2d.setColor(color1);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public SignUpForm() {
        setTitle("Sign Up Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // first
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        JTextField firstNameField = new JTextField(20);
        panel.add(firstNameField, gbc);

        // last
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        JTextField lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);

        // username
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // contact
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        JTextField contactField = new JTextField(20);
        panel.add(contactField, gbc);

        // position
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        String[] positions = {"Doctor", "Secretary"};
        JComboBox<String> positionComboBox = new JComboBox<>(positions);
        panel.add(positionComboBox, gbc);

        // password
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // confirm password
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);

        // button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(200, 255, 200));
        panel.add(signUpButton, gbc);

        signUpButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String contactNumber = contactField.getText().trim();
            String position = (String) positionComboBox.getSelectedItem();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                email.isEmpty() || contactNumber.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields must be filled out.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(panel, "Passwords do not match.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(panel, "Sign up successful for " + username,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        add(panel);
        setVisible(true);
    }
}
