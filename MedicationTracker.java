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

    private DefaultTableModel patientTableModel;
    private DefaultTableModel medicineTableModel;

    public MedicationTracker() {
        connectToDatabase();
        createGUI();
        loadPatientData();
        loadMedicineData();
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
        headerPanel.setBackground(new Color(4, 178, 217));
        JLabel titleLabel = new JLabel("Clinic Medication Tracker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane for Patients and Medicines Dashboard
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Patients panel with table and delete button
        JPanel patientPanel = new JPanel(new BorderLayout());
        patientTableModel = new DefaultTableModel();
        JTable patientTable = new JTable(patientTableModel);
        patientTableModel.addColumn("Patient ID");
        patientTableModel.addColumn("First Name");
        patientTableModel.addColumn("Middle Name");
        patientTableModel.addColumn("Last Name");
        patientTableModel.addColumn("Age");
        patientTableModel.addColumn("Birthdate");
        patientTableModel.addColumn("Sex");
        patientTableModel.addColumn("Contact Number");
        patientTableModel.addColumn("Address");

        JScrollPane patientScrollPane = new JScrollPane(patientTable);
        patientPanel.add(patientScrollPane, BorderLayout.CENTER);

        JButton deletePatientButton = new JButton("Delete Selected Patient");
        deletePatientButton.setBackground(new Color(220, 53, 69));
        deletePatientButton.setForeground(Color.WHITE);
        deletePatientButton.setFont(new Font("Arial", Font.BOLD, 16));
        deletePatientButton.addActionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a patient to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the selected patient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int patientId = (int) patientTableModel.getValueAt(selectedRow, 0);
                deletePatient(patientId);
                loadPatientData();
            }
        });
        JPanel patientButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        patientButtonPanel.add(deletePatientButton);
        patientPanel.add(patientButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Patients", patientPanel);

        // --- Medicines panel with table and delete button
        JPanel medicinePanel = new JPanel(new BorderLayout());
        medicineTableModel = new DefaultTableModel();
        JTable medicineTable = new JTable(medicineTableModel);
        medicineTableModel.addColumn("Medicine ID");
        medicineTableModel.addColumn("Name");
        medicineTableModel.addColumn("Brand");
        medicineTableModel.addColumn("Days Of Intake");
        medicineTableModel.addColumn("Start Date");
        medicineTableModel.addColumn("Time");
        medicineTableModel.addColumn("Frequency");

        JScrollPane medicineScrollPane = new JScrollPane(medicineTable);
        medicinePanel.add(medicineScrollPane, BorderLayout.CENTER);

        JButton deleteMedicineButton = new JButton("Delete Selected Medicine");
        deleteMedicineButton.setBackground(new Color(220, 53, 69));
        deleteMedicineButton.setForeground(Color.WHITE);
        deleteMedicineButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteMedicineButton.addActionListener(e -> {
            int selectedRow = medicineTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a medicine to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the selected medicine?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int medicineId = (int) medicineTableModel.getValueAt(selectedRow, 0);
                deleteMedicine(medicineId);
                loadMedicineData();
            }
        });
        JPanel medicineButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        medicineButtonPanel.add(deleteMedicineButton);
        medicinePanel.add(medicineButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Medicines", medicinePanel);

        frame.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPatientButton = createStyledButton("Add Patient", new Color(79, 195, 247));
        JButton addMedicineButton = createStyledButton("Add Medicine", new Color(79, 195, 247));

        addPatientButton.addActionListener(this::showAddPatientDialog);
        addMedicineButton.addActionListener(this::showAddMedicineDialog);

        buttonPanel.add(addPatientButton);
        buttonPanel.add(addMedicineButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    private void loadPatientData() {
        if (connection == null) return;
        patientTableModel.setRowCount(0);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patient_table")) {
            while (rs.next()) {
                patientTableModel.addRow(new Object[]{
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
    }

    private void loadMedicineData() {
        if (connection == null) return;
        medicineTableModel.setRowCount(0);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medicine_table")) {
            while (rs.next()) {
                medicineTableModel.addRow(new Object[]{
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getInt("days_of_intake"),
                        rs.getDate("start_date"),
                        rs.getTime("time"),
                        rs.getInt("frequency")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deletePatient(int patientId) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM patient_table WHERE patient_id = ?")) {
            stmt.setInt(1, patientId);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(frame, "✅ Patient Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Patient delete failed, patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine(int medicineId) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM medicine_table WHERE medicine_id = ?")) {
            stmt.setInt(1, medicineId);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(frame, "✅ Medicine Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Medicine delete failed, medicine not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting medicine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddPatientDialog(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField contactNumberField = new JTextField();

        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl birthdatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JTextField addressField = new JTextField();

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
                loadPatientData(); // Refresh the patient table after insert
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

        panel.add(new JLabel("Medicine Name:")); panel.add(nameField);
        panel.add(new JLabel("Medicine Brand:")); panel.add(brandField);
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
                java.sql.Time sqlTime = new java.sql.Time(selectedTime.getTime());
                stmt.setTime(5, sqlTime);
                stmt.setInt(6, Integer.parseInt(frequencyField.getText()));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "✅ Medicine Added Successfully!");
                loadMedicineData(); // Refresh the medicine table after insert
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding medicine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicationTracker::new);
    }
}

