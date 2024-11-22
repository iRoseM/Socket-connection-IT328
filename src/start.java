
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class start extends javax.swing.JFrame {
    static String playername;
    static String connectedNames;
    static Socket socket;
    BufferedReader in;
    PrintWriter out;
    private static String playerName;
    static String serverAddress="192.168.100.168";
    static final int Server_port= 6789;
    Client clientFrame= new Client();
   
        public start() throws Exception{
        initComponents();
          
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(200, 44, 115));
        jButton1.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(245, 245, 255));
        jButton1.setText("Connect");
        jButton1.setToolTipText("Click To Connect");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 0, 102), new java.awt.Color(102, 0, 102)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 370, 220, 35));

        jPanel1.setBackground(new java.awt.Color(123, 98, 187));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 0, 102), new java.awt.Color(102, 0, 102)));

        jLabel3.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(245, 245, 255));
        jLabel3.setText("Player username:");

        jTextField1.setBackground(new java.awt.Color(237, 235, 250));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 540, 110));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/startBackground.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        playername = jTextField1.getText().trim();  // Get the player name from the text field
        if (!playername.isEmpty()) {
            connectionProcess(playername);  // Call method to connect to the server
        } else {
            JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

       public void connectionProcess(String playername) {
 
    try {
        // Connect to the server
        socket = new Socket(serverAddress, Server_port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Send the player name to the server
        out.println(playername);
        out.flush();

        // Create a Client instance and pass the socket, in, and out
        Client clientFrame = new Client(socket, in, out, playername);

        // Pass the client instance to the ServerConnection
        ServerConnection serverConnection = new ServerConnection(socket, clientFrame);
        new Thread(serverConnection).start();

        clientFrame.setVisible(true); // Make the client frame visible
        this.dispose(); // Close the current start window

        JOptionPane.showMessageDialog(this, "Welcome " + playername + "! You are connected!");

    } catch (UnknownHostException ex) {
        JOptionPane.showMessageDialog(this, "Unknown host: " + serverAddress, "Connection Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new start().setVisible(true);  // Create and display the form
                } catch (Exception ex) {
                    Logger.getLogger(start.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }



    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
