package medicationtracker;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Properties;

public class MedicationTracker {
    private JFrame frame;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dump";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Lorenzo0910";

    public MedicationTracker() {
        connectToDatabase();
        createGUI();
    }

    MedicationTracker(Connection connection) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("✅ Connected to MySQL database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void createGUI() {
        frame = new JFrame("Medication Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Medication Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPatientButton = createStyledButton("Add Patient", new Color(0, 153, 76));
        JButton viewPatientsButton = createStyledButton("View Patients", new Color(0, 102, 204));
        JButton addMedicineButton = createStyledButton("Add Medicine", new Color(153, 76, 0));
        JButton addMedicationButton = createStyledButton("View Medication", new Color(0, 76, 153));

        addPatientButton.addActionListener(this::showAddPatientDialog);
        viewPatientsButton.addActionListener(this::showPatientList);
        addMedicineButton.addActionListener(this::showAddMedicineDialog);
        addMedicationButton.addActionListener(this::showAddMedicationList);

        buttonPanel.add(addPatientButton);
        buttonPanel.add(viewPatientsButton);
        buttonPanel.add(addMedicineButton);
        buttonPanel.add(addMedicationButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    private void showAddPatientDialog(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField contactNumberField = new JTextField();
        JTextField addressField = new JTextField();

        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl birthdatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        panel.add(new JLabel("First Name:")); panel.add(firstNameField);
        panel.add(new JLabel("Middle Name:")); panel.add(middleNameField);
        panel.add(new JLabel("Last Name:")); panel.add(lastNameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Sex:")); panel.add(genderComboBox);
        panel.add(new JLabel("Birthdate:")); panel.add(birthdatePicker);
        panel.add(new JLabel("Contact Number:")); panel.add(contactNumberField);
        panel.add(new JLabel("Address:")); panel.add(addressField);

        int option = JOptionPane.showConfirmDialog(frame, panel, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO patient_table (first_name, middle_name, last_name, age, sex, birthdate, contact_number, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
            ) {
                stmt.setString(1, firstNameField.getText());
                stmt.setString(2, middleNameField.getText());
                stmt.setString(3, lastNameField.getText());
                stmt.setInt(4, Integer.parseInt(ageField.getText()));
                stmt.setString(5, (String) genderComboBox.getSelectedItem());
                if (dateModel.getValue() != null) {
                    java.sql.Date birthdate = new java.sql.Date(dateModel.getValue().getTime());
                    stmt.setDate(6, birthdate);
                } else {
                    stmt.setNull(6, java.sql.Types.DATE);
                }
                stmt.setString(7, contactNumberField.getText());
                stmt.setString(8, addressField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "✅ Patient Added Successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showPatientList(ActionEvent e) {
        JFrame tableFrame = new JFrame("Patient List");
        tableFrame.setSize(800, 400);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("patient_id");
        model.addColumn("First Name");
        model.addColumn("Middle Name");
        model.addColumn("Last Name");
        model.addColumn("Age");
        model.addColumn("Birthdate");
        model.addColumn("Sex");
        model.addColumn("Contact");
        model.addColumn("Address");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patient_table")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDate("birthdate"),
                        rs.getString("sex"),
                        rs.getString("contact_number"),
                        rs.getString("address")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM patient_table WHERE patient_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "✅ Patient Deleted Successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableFrame.add(new JScrollPane(table), BorderLayout.CENTER);
        tableFrame.add(deleteButton, BorderLayout.SOUTH);
        tableFrame.setVisible(true);
    }

    private void showAddMedicineDialog(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField daysOfIntakeField = new JTextField();
        JTextField frequencyField = new JTextField();

        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl birthdatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Brand:")); panel.add(brandField);
        panel.add(new JLabel("Days Of Intake:")); panel.add(daysOfIntakeField);
        panel.add(new JLabel("Start Date:")); panel.add(birthdatePicker);
        panel.add(new JLabel("Time:")); panel.add(timeSpinner);
        panel.add(new JLabel("Frequency:")); panel.add(frequencyField);

        int option = JOptionPane.showConfirmDialog(frame, panel, "Add Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO medicine_table (Name, Brand, Days_of_Intake, Start_Date, Time, Frequency) VALUES (?, ?, ?, ?, ?, ?)")
            ) {
                stmt.setString(1, nameField.getText());
                stmt.setString(2, brandField.getText());
                stmt.setInt(3, Integer.parseInt(daysOfIntakeField.getText()));
                if (dateModel.getValue() != null) {
                    java.sql.Date startdate = new java.sql.Date(dateModel.getValue().getTime());
                    stmt.setDate(4, startdate);
                } else {
                    stmt.setNull(4, java.sql.Types.DATE);
                }
                  java.util.Date selectedTime = (java.util.Date) timeSpinner.getValue();
        long timeInMillis = selectedTime.getTime();
        java.sql.Time sqlTime = new java.sql.Time(timeInMillis);
        stmt.setTime(5, sqlTime);
                stmt.setInt(6, Integer.parseInt(frequencyField.getText()));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "✅ Medicine Added Successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showAddMedicationList(ActionEvent e) {
        JFrame tableFrame = new JFrame("Medication List");
        tableFrame.setSize(800, 400);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("medicine_id");
        model.addColumn("Name");
        model.addColumn("Brand");
        model.addColumn("Days of Intake");
        model.addColumn("Start Date");
        model.addColumn("Time");
        model.addColumn("Frequency");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medicine_table")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("days_of_intake"),
                        rs.getDate("start_date"),
                        rs.getString("time"),
                        rs.getString("frequency")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM medicine_table WHERE medicine_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "✅ Medication Deleted Successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableFrame.add(new JScrollPane(table), BorderLayout.CENTER);
        tableFrame.add(deleteButton, BorderLayout.SOUTH);
        tableFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicationTracker::new);
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
