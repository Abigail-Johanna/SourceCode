package medicationtracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Authentication auth = new Authentication();
        if (auth.login(username, password)) {
            openMedicationTracker(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public class Authentication {
    private final String storedDoctorPassword = BCrypt.hashpw("1234", BCrypt.gensalt());
    private final String storedSecretaryPassword = BCrypt.hashpw("5678", BCrypt.gensalt());

    public boolean login(String username, String password) {
        if ("Doctor".equals(username) && BCrypt.checkpw(password, storedDoctorPassword)) {
            return true;
        } else if ("Secretary".equals(username) && BCrypt.checkpw(password, storedSecretaryPassword)) {
            return true;
        }
        return false;
    }
}


    private void openMedicationTracker(String role) {
        dispose();
        String patientName = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        if (patientName != null && !patientName.trim().isEmpty()) {
            new MedicationTracker(patientName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}
