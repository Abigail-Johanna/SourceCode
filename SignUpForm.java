package medicationtracker;

import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

/**
 *
 * @author Lorenzo
 */

public class SignUpForm extends JFrame{

    private Connection connection;
    public SignUpForm() {
        initComponents();
        connectToDatabase();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/dump";
            String user = "root";
            String password = "Lorenzo0910";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to MySQL database.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Database connection failed.");
            System.exit(0);
        }
    }
    
    public void signUpForm() {
            String firstname = firstname_Field.getText();
            String lastname = LName_TextField.getText();
            String username = UName_TextField.getText();
            String email = Email_text.getText();
            String contactnumber = CNumber_text.getText();
            String position = Pos_box.getSelectedItem().toString();
            String password = new String(Pass_text.getPassword());
            String confirmpassword = new String(CPass_text.getPassword());
            
            if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() ||
            email.isEmpty() || contactnumber.isEmpty() || password.isEmpty() ||
            confirmpassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required. Please fill in all information.");
            return;
        }
        
            if (!password.equals(confirmpassword)) {
                JOptionPane.showMessageDialog(this, "❌ Passwords do not match.");
                return;
            }
            
                String sql="INSERT INTO users(first_name, last_name, username , email , contact_number, position, password) VALUES(?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, firstname);
                stmt.setString(2, lastname);
                stmt.setString(3, username);
                stmt.setString(4, email);
                stmt.setString(5, contactnumber);
                stmt.setString(6, position);
                stmt.setString(7, password); 
               
                
                
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "✅ Sign Up successful!");
                SwingUtilities.invokeLater(() -> new LogInForm(connection));
                this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Database error: " + e.getMessage());
        }
        
    }
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        SignUpForm_label = new javax.swing.JLabel();
        FName_label = new javax.swing.JLabel();
        LName_label = new javax.swing.JLabel();
        UName_label = new javax.swing.JLabel();
        Email_label = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        firstname_Field = new javax.swing.JTextField();
        LName_TextField = new javax.swing.JTextField();
        UName_TextField = new javax.swing.JTextField();
        Email_text = new javax.swing.JTextField();
        CNumber_text = new javax.swing.JTextField();
        Pos_box = new javax.swing.JComboBox<>();
        Pass_text = new javax.swing.JPasswordField();
        CPass_text = new javax.swing.JPasswordField();

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SignUpForm_label.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        SignUpForm_label.setText("Sign Up Form");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(SignUpForm_label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SignUpForm_label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FName_label.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        FName_label.setText("First Name:");

        LName_label.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        LName_label.setText("Last Name:");

        UName_label.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        UName_label.setText("Username:");

        Email_label.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Email_label.setText("Email:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Contact Number:");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("Position:");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setText("Password:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setText("Confirm Password:");

        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setText("Sign Up");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        firstname_Field.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        firstname_Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstname_FieldActionPerformed(evt);
            }
        });

        LName_TextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        LName_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LName_TextFieldActionPerformed(evt);
            }
        });

        UName_TextField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        UName_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UName_TextFieldActionPerformed(evt);
            }
        });

        Email_text.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Email_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Email_textActionPerformed(evt);
            }
        });

        CNumber_text.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        CNumber_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CNumber_textActionPerformed(evt);
            }
        });

        Pos_box.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Pos_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Doctor", "Secretary" }));
        Pos_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Pos_boxActionPerformed(evt);
            }
        });

        Pass_text.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        CPass_text.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(LName_label, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(FName_label)
                        .addComponent(UName_label, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(Email_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(UName_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addComponent(Email_text)
                        .addComponent(CNumber_text)
                        .addComponent(LName_TextField, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(firstname_Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pass_text, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CPass_text, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pos_box, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(firstname_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FName_label))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LName_label))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UName_label)
                    .addComponent(UName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Email_label)
                    .addComponent(Email_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(CNumber_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(Pos_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(Pass_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(CPass_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void firstname_FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstname_FieldActionPerformed
        String firstname = firstname_Field.getText();
    }//GEN-LAST:event_firstname_FieldActionPerformed

    private void Email_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Email_textActionPerformed
         String email = Email_text.getText();
    }//GEN-LAST:event_Email_textActionPerformed

    private void LName_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LName_TextFieldActionPerformed
         String lastname = LName_TextField.getText();
    }//GEN-LAST:event_LName_TextFieldActionPerformed

    private void UName_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UName_TextFieldActionPerformed
        String username = UName_TextField.getText();
    }//GEN-LAST:event_UName_TextFieldActionPerformed

    private void CNumber_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CNumber_textActionPerformed
        String contactnumber = CNumber_text.getText();
    }//GEN-LAST:event_CNumber_textActionPerformed

    private void Pos_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Pos_boxActionPerformed
        String position = Pos_box.getToolTipText();
    }//GEN-LAST:event_Pos_boxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        signUpForm();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CNumber_text;
    private javax.swing.JPasswordField CPass_text;
    private javax.swing.JLabel Email_label;
    private javax.swing.JTextField Email_text;
    private javax.swing.JLabel FName_label;
    private javax.swing.JTextField LName_TextField;
    private javax.swing.JLabel LName_label;
    private javax.swing.JPasswordField Pass_text;
    private javax.swing.JComboBox<String> Pos_box;
    private javax.swing.JLabel SignUpForm_label;
    private javax.swing.JTextField UName_TextField;
    private javax.swing.JLabel UName_label;
    private javax.swing.JTextField firstname_Field;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

   
}