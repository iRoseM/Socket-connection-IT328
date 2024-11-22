import java.io.*;
import static java.lang.System.out;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Client extends javax.swing.JFrame { 

    String ServerAddress;
    String playername = "";
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    static String IP;
    static String serverAddress = start.serverAddress;
    static int port = start.Server_port;
   
    public Client() {
        initComponents();
        

    }
     public void connectToServer(String serverAddress, String playername) throws IOException {
        socket = new Socket(serverAddress, 6789);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println(playername);
        out.flush();

        ServerConnection serverConnection = new ServerConnection(socket, this);
        new Thread(serverConnection).start();
    }

    public void updateScores(String scores) {
        jTextArea3.setText(scores);
    }

    public void displayQuestion(String message) {
        String[] parts = message.split(";OPTIONS:");
        String question = parts[0];
        String options = parts.length > 1 ? parts[1] : "";

        jTextArea1.setText(question);
        choice1.removeAll();
        for (String option : options.split(",")) {
            choice1.add(option);
        }

        jButton1.setEnabled(true);
    }

   // دالة لتحديث واجهة العميل عند تلقي الرسالة "GAME_OVER"
public void displayGameOver(String message) {
    SwingUtilities.invokeLater(() -> {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // يمكن إضافة خطوات إضافية هنا مثل إعادة تعيين واجهة المستخدم أو إغلاق التطبيق.
        // يمكنك إضافة زر لإغلاق اللعبة أو إعادة تشغيلها إذا لزم الأمر.
    });
}


        public Client(Socket socket, BufferedReader in, PrintWriter out, String playername) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.playername = playername;
        initComponents();  // Initialize GUI components
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

      

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error setting up client", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        choice1 = new java.awt.Choice();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.darkGray, java.awt.Color.white));
        jPanel2.setMinimumSize(new java.awt.Dimension(790, 580));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea1.setBackground(new java.awt.Color(237, 235, 250));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 153, 0));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game Space", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Showcard Gothic", 3, 24), new java.awt.Color(214, 78, 129))); // NOI18N
        jTextArea1.setDisabledTextColor(new java.awt.Color(92, 57, 140));
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 383, 325));

        jTextArea3.setBackground(new java.awt.Color(237, 235, 250));
        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Showcard Gothic", 0, 14)); // NOI18N
        jTextArea3.setForeground(new java.awt.Color(214, 78, 129));
        jTextArea3.setRows(5);
        jTextArea3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Connected", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Showcard Gothic", 2, 24), new java.awt.Color(214, 78, 129))); // NOI18N
        jTextArea3.setDisabledTextColor(new java.awt.Color(92, 57, 140));
        jScrollPane3.setViewportView(jTextArea3);
        jTextArea3.getAccessibleContext().setAccessibleName("Score board");

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 292, 274, 160));

        jTextArea4.setBackground(new java.awt.Color(237, 235, 250));
        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Showcard Gothic", 0, 14)); // NOI18N
        jTextArea4.setForeground(new java.awt.Color(214, 78, 129));
        jTextArea4.setRows(5);
        jTextArea4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Waitign List", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Showcard Gothic", 2, 24), new java.awt.Color(214, 78, 129))); // NOI18N
        jTextArea4.setDisabledTextColor(new java.awt.Color(92, 57, 140));
        jScrollPane4.setViewportView(jTextArea4);
        jTextArea4.getAccessibleContext().setAccessibleName("Connection list");

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 130, 274, 150));

        jButton1.setBackground(new java.awt.Color(200, 44, 115));
        jButton1.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(245, 245, 255));
        jButton1.setText("SUBMIT");
        jButton1.setToolTipText("Click To Submit Your Answer");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 0, 102), new java.awt.Color(102, 0, 102)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 460, 89, 31));

        jButton2.setBackground(new java.awt.Color(200, 44, 115));
        jButton2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(245, 245, 255));
        jButton2.setText("PLAY");
        jButton2.setToolTipText("Click To Play");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 0, 102), new java.awt.Color(102, 0, 102)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, 274, 31));

        choice1.setBackground(new java.awt.Color(237, 235, 250));
        choice1.setForeground(new java.awt.Color(214, 78, 129));
        jPanel2.add(choice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 284, 31));

        jButton3.setBackground(new java.awt.Color(123, 98, 187));
        jButton3.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(245, 245, 255));
        jButton3.setText("Disconnect");
        jButton3.setToolTipText("Click To Disconnect");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 0, 102), new java.awt.Color(102, 0, 102)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 31));

        jLabel1.setBackground(new java.awt.Color(255, 102, 255));
        jLabel1.setForeground(new java.awt.Color(255, 51, 204));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Background.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 580));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //connect button
        
  if (socket != null && !socket.isClosed()) {
        // Send a message to the server to add this player to the playing list
        out.println("PLAY:" + playername);
        out.flush();
      updatePlayingClients( playername);
        
    } else {
        JOptionPane.showMessageDialog(this, "You are not connected to the server.");
    }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         String answer = choice1.getSelectedItem();
    out.println("ANSWER:" + answer);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    
    out.println("DISCONNECT");
    this.dispose();

    }//GEN-LAST:event_jButton3ActionPerformed

    

    public static void main(String args[]){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            } });
        
    }
 public void updateConnectedPlayers(String playerNames) {
    SwingUtilities.invokeLater(() -> {
        // Assuming playerNames is a comma-separated string of names
        String formattedNames = playerNames.replace(",", "\n");
        jTextArea3.setText(formattedNames);  // Display each name on a new line
    });
}
    

    public void updatePlayingClients(String playingClients) {
    SwingUtilities.invokeLater(() -> {
        String message = "Players currently in the game: \n" + playingClients.replace(",", "\n");
        jTextArea4.setText(message); // Display each playing client on a new line
    });
}
    public void startGame(String players) {
    JOptionPane.showMessageDialog(this, "The game is starting with players: " + players);
    // Update the game state on the client-side (e.g., switch to game mode or enable game UI components)
}
    public void displayRoomFullMessage() {
    JOptionPane.showMessageDialog(this, "The room is full. Please wait for the next round.", "Room Full", JOptionPane.WARNING_MESSAGE);
}
    public void displaykickedMessage() throws Exception {
    JOptionPane.showMessageDialog(this, "All players left the game you cant play solo.", "Solo", JOptionPane.WARNING_MESSAGE);
    this.dispose();
    start s= new start();
    s.setVisible(true);
    
}




     void connectionProcess(String playername) {
    try {
        // الاتصال بالخادم
        socket = new Socket(serverAddress, 6789);
        
        // تعيين المهلة (Timeout) للسوكيت
        socket.setSoTimeout(30000);  // تعيين مهلة 30 ثانية

        // قراءة البيانات من الخادم
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // إرسال اسم اللاعب إلى الخادم
        out.println(playername);
        out.flush();

        // بقية الكود هنا كما هو
        Client clientFrame = new Client(socket, in, out, playername);
        ServerConnection serverConnection = new ServerConnection(socket, clientFrame);
        new Thread(serverConnection).start();

        clientFrame.setVisible(true);  // جعل واجهة العميل مرئية
        this.dispose();  // إغلاق واجهة البداية بعد الاتصال

        JOptionPane.showMessageDialog(this, "Welcome " + playername + "! You are connected!");

    } catch (UnknownHostException ex) {
        JOptionPane.showMessageDialog(this, "Unknown host: " + serverAddress, "Connection Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice choice1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    // End of variables declaration//GEN-END:variables

    void showWrongAnswer() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
}