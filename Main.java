
package medicationtracker;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main method started..."); 
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}
