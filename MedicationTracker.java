package medicationtracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

class Medication {
    private String name;
    private String dosage;
    private LocalDateTime startDateTime;
    private int frequency;
    private LocalDateTime nextDoseTime;
    private int days;

    public Medication(String name, String dosage, LocalDateTime startDateTime, int frequency, int days) {
        this.name = name;
        this.dosage = dosage;
        this.startDateTime = startDateTime;
        this.frequency = frequency;
        this.days = days;
        this.nextDoseTime = startDateTime.plusHours(frequency);
    }

    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public LocalDateTime getNextDoseTime() { return nextDoseTime; }
    public int getDays() { return days; }

    public void updateNextDose() { nextDoseTime = nextDoseTime.plusHours(frequency); }
    public String getNextDoseFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return nextDoseTime.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return String.format("<html><b>%s</b> (%s)<br>Start: %s<br>Every %d hours for %d days<br>Next dose: %s</html>",
                name, dosage, startDateTime.format(formatter), frequency, days, getNextDoseFormatted());
    }
}

public class MedicationTracker {
    private JFrame frame;
    private DefaultListModel<String> medicationListModel;
    private List<Medication> medications;
    private String patientName;
    private DefaultListModel<String> historyListModel;

   public MedicationTracker(String patientName) {
    this.patientName = patientName;
    medications = new ArrayList<>();
    historyListModel = new DefaultListModel<>();
    createGUI();
}


    private void askForPatientName() {
        patientName = JOptionPane.showInputDialog(null, "Enter patient's name:", "Patient Name", JOptionPane.PLAIN_MESSAGE);
        if (patientName == null || patientName.trim().isEmpty()) {
            patientName = "Unknown";
        }
    }

    private void createGUI() {
        frame = new JFrame("Medication Tracker - " + patientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel patientLabel = new JLabel("Patient: " + patientName, SwingConstants.CENTER);
        patientLabel.setFont(new Font("Arial", Font.BOLD, 24));
        patientLabel.setForeground(Color.WHITE);
        headerPanel.add(patientLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        medicationListModel = new DefaultListModel<>();
        JList<String> medicationList = new JList<>(medicationListModel);
        medicationList.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(new JScrollPane(medicationList), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel historyLabel = new JLabel("History Bin", SwingConstants.CENTER);
        historyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(historyLabel, BorderLayout.NORTH);
        JList<String> historyList = new JList<>(historyListModel);
        rightPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = createStyledButton("Add Medication", new Color(0, 76, 153));
        addButton.addActionListener(e -> showAddMedicationDialog());
        buttonPanel.add(addButton);
        
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

private void showAddMedicationDialog() {
    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    JTextField nameField = new JTextField();
    JTextField dosageField = new JTextField();
    JTextField dateField = new JTextField("yyyy-MM-dd");
    JTextField timeField = new JTextField("hh:mm a"); 
    JTextField frequencyField = new JTextField();
    JTextField daysField = new JTextField();

    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Dosage:"));
    panel.add(dosageField);
    panel.add(new JLabel("Start Date (yyyy-MM-dd):"));
    panel.add(dateField);
    panel.add(new JLabel("Start Time (hh:mm AM/PM):")); 
    panel.add(timeField);
    panel.add(new JLabel("Frequency (hours):"));
    panel.add(frequencyField);
    panel.add(new JLabel("Days of Medication:"));
    panel.add(daysField);

    int option = JOptionPane.showConfirmDialog(frame, panel, "Add Medication", JOptionPane.OK_CANCEL_OPTION);
    if (option == JOptionPane.OK_OPTION) {
        try {
            String name = nameField.getText();
            String dosage = dosageField.getText();
            String dateTimeStr = dateField.getText().trim() + " " + timeField.getText().trim();
            
           
            System.out.println("Parsing DateTime: " + dateTimeStr);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            LocalDateTime startDateTime = LocalDateTime.parse(dateTimeStr, formatter);

            int frequency = Integer.parseInt(frequencyField.getText().trim());
            int days = Integer.parseInt(daysField.getText().trim());

            addMedication(new Medication(name, dosage, startDateTime, frequency, days));
        } catch (Exception e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(frame, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

   public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new MedicationTracker("Unknown"));
    }

    private void addMedication(Medication medication) {
       medications.add(medication);
       medicationListModel.addElement(medication.toString());
       JOptionPane.showMessageDialog(frame, "Medication Added:\n" + medication.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}

