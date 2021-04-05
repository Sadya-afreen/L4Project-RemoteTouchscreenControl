/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author sadya
 */
package pkg3dmodel;

import com.sun.j3d.utils.applet.MainFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.List;
import java.awt.Point;
import static javafx.scene.paint.Color.color;
import javax.swing.JButton;
//import javax.swing.Timer;

import java.util.Random;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import static pkg3dmodel.Experiment_window.indicator;
import pkg3dmodel.Main.*;

public class exp1 extends javax.swing.JFrame implements ActionListener,MouseListener {
    
    /**
     * Creates new form exp1
     */
    public exp1() throws IOException {

        initComponents();
        this.populateJButtonList();
        centre_button.addActionListener(centre); 
        jButton1.addMouseListener(this);
        this.addMouseListener(this);
        centre_button.removeMouseListener(this);
        int score = Integer.parseInt(Score.getText());
        //Timer t = new Timer(3000,this);
        
        user_data.add(participant_id);
        user_data.add(technique_name);
        user_data.add(task_blockname);
        user_data.add(individual_taskname);
    }

    timer tt = new timer();
    
  //  Experiment_window ew = new Experiment_window();
    String participant_id = Experiment_window.getParticipantid();
    String technique_name = Experiment_window.getTechniquename();
    String task_blockname = Experiment_window.gettaskblockname();
    String individual_taskname = Experiment_window.getindividualtaskname();
    
    
    static int target_score = 0;
    static int centre_target_score = 0;
    long startTime = System.currentTimeMillis();
    private Random randomGenerator = new Random();
    ArrayList<JButton> jbuttons = new ArrayList<>();
    private static ArrayList<Integer> target_positions = new ArrayList<Integer>();
    private static ArrayList<Integer> mouse_positions = new ArrayList<Integer>();
    private static ArrayList<Integer> target_size = new ArrayList<Integer>();
    private static ArrayList<Date> time = new ArrayList<Date>();
    private static ArrayList<String> user_data = new ArrayList<String>();
    
    private static HashMap<String,ArrayList> dict = new HashMap<String,ArrayList>();
    private static HashMap<Integer,String> mouse_pos_dict = new HashMap<Integer,String>();
    
    public void populateJButtonList() {
        // Gets the class attributes, e.g. JButton, String, Integer types, everything.
        // In this case it is this class, but can define any other class in your project.
        Field[] fields = exp1.class.getDeclaredFields();

        // Loop over each field to determine if it is a JButton.
        for (Field field : fields) {
            //System.out.println(field.getName().toString());
            // If it is a JButton then add it to the list.
            if (field.getType().equals(JButton.class) && !field.getName().toString().equals("centre_button" ) &&!field.getName().toString().equals("start_btn" ) && !field.getName().toString().equals("stop_btn" )  ) {
                try {
                    // De-reference the field from the object (ColorButtons) and cast it to a JButton and add it to the list.
                    jbuttons.add((JButton) field.get(this));
                    System.out.println(field.getName().toString());
                
                } catch (IllegalArgumentException | IllegalAccessException
                        | SecurityException e) {
                    e.printStackTrace();
                }
            }
            
        }
        
    }

   
   public void set_yellow_color(JButton yellow_btn){
       yellow_btn.setBackground(Color.YELLOW);
   }
   public void set_green_color(JButton green_btn){
       green_btn.setBackground(Color.GREEN);
   }
   
   public void set_red_color(JButton red_btn){
       red_btn.setBackground(Color.RED);
   }


    int size_h = 0;
    int size_v = 0;
    int index = 0;
    Random rn = new Random();
    Random A = new Random();
    Random B = new Random();
  
    ActionListener centre = new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
        centre_button.setBackground(Color.GREEN);
        centre_target_score++;

        JButton next_button_toclick = jbuttons.get(0);

        rn = new Random();
        size_h = 30 + rn.nextInt(150 - 50 + 1);
        size_v = 30 + rn.nextInt(150 - 50 + 1);

        A = new Random();
        //int x = A.nextInt(5);
        int x = A.nextInt((1050-0+1)) + 0;
        B = new Random();
       // int y = A.nextInt(5);
        int y = B.nextInt((350-0+1)) + 0;
        
     //   float x_f = jbuttons.get(index).getAlignmentX();
     //   float y_f = jbuttons.get(index).getAlignmentY();
        
        float x_f = next_button_toclick.getAlignmentX();
        float y_f = next_button_toclick.getAlignmentY();
        Point xp = next_button_toclick.getLocation();

       //int randomOfTwoInts = new Random().nextBoolean() ? a : b;

       //  next_button_toclick.setBounds(18, 16, 29, 20);
        next_button_toclick.setBounds(size_h, size_v, size_h, size_v);
        //jbuttons.get(index).setLocation((int)x_f*(x),((int)y_f+y));
        next_button_toclick.setLocation((int)x_f*(x)+size_h,((int)y_f+y)+size_v);
        next_button_toclick.setBackground(Color.YELLOW);
       // jbuttons.get(index).setBackground(Color.YELLOW);
         int r1 = (int)x_f+(x); 
         
         target_positions.add(target_score);
         target_positions.add((int)x_f+(x));
         target_positions.add((int)y_f+(y));
         target_size.add(size_h);
         target_size.add(size_v);
//         target_size.add(size_h);
//         target_size.add(size_v);
         
           for(JButton j : jbuttons){
            //   System.out.println(j.next_button_toclick));
               j.setVisible(true);
            if(!j.equals(next_button_toclick)){
                 j.setVisible(false);
                 System.out.println(j);
             // if(!j.equals(jbuttons.get(index))){
              // j.setBackground(Color.red);
             //  j.setVisible(false);
     }
           }
         //  index =0;
          // next_button_toclick = null;
    }
    };

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        targets_selected = new javax.swing.JLabel();
        Score = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        centre_button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        start_btn = new javax.swing.JButton();
        stop_btn = new javax.swing.JButton();
        countdown_timer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 0, 102));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 0, 51));

        targets_selected.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        targets_selected.setForeground(new java.awt.Color(255, 255, 255));
        targets_selected.setText("Targets Selected : ");

        Score.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        Score.setForeground(new java.awt.Color(255, 255, 255));
        Score.setText("0");

        jButton1.setForeground(new java.awt.Color(255, 0, 0));
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        centre_button.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        centre_button.setForeground(new java.awt.Color(51, 0, 51));
        centre_button.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(153, 153, 153)));
        centre_button.setFocusPainted(false);
        centre_button.setOpaque(false);
        centre_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centre_buttonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("EXPERIMENT TASK 1");

        start_btn.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        start_btn.setText("CLICK TO START");
        start_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                start_btnActionPerformed(evt);
            }
        });

        stop_btn.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        stop_btn.setText("CLICK TO STOP");
        stop_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stop_btnActionPerformed(evt);
            }
        });

        countdown_timer.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        countdown_timer.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(targets_selected, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Score, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(centre_button, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(360, 360, 360))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(408, 408, 408))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(724, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(stop_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(start_btn))
                    .addGap(19, 19, 19)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(countdown_timer, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(255, 255, 255))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(countdown_timer, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(centre_button, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(targets_selected, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Score)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(start_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(stop_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
             jButton1.setBackground(Color.GREEN);
              
             target_score++;
             //score = score+1;
             Score.setText(""+target_score);
             centre_button.setBackground(Color.YELLOW);
              for(JButton j : jbuttons){
                //  j.setVisible(true);
                if(!j.equals(jButton1)){

                 //j.setBackground(Color.red);
                j.setVisible(false);
                         
       }
   }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void centre_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_centre_buttonActionPerformed

    }//GEN-LAST:event_centre_buttonActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        
        jButton1.setBackground(Color.GRAY);
//        jButton2.setBackground(Color.GRAY); 
//        jButton3.setBackground(Color.GRAY);
//        jButton4.setBackground(Color.GRAY);
        centre_button.setBackground(Color.YELLOW); 
       
         
         
        
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        
        long totalTime = System.currentTimeMillis() - startTime;   
        System.out.println(totalTime + "total time");
    }//GEN-LAST:event_formWindowClosed

    private void start_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_start_btnActionPerformed
      //  start_btn.setActionCommand("start");
     //   Timer t = new Timer(1000,this);
        target_score = 0;
        Score.setText(""+target_score);
        centre_target_score = 0;
        mouse_positions.clear();
        target_size.clear();
        target_positions.clear();
        tt.runTimer();  
        tt.setLocation(900, 250);

        tt.setVisible(true);
        Date start_d = new Date();
        time.add(start_d);
        System.out.println("Timer task started at" + new Date());
        
 
     
       
      
    }//GEN-LAST:event_start_btnActionPerformed

    private void stop_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stop_btnActionPerformed
        // TODO add your handling code here:
//        Main task2 = new Main();
//        Frame frame = new MainFrame(task2, 800, 600);
        indicator = true;
      //  task2.setVisible(true);
        this.setVisible(false);
        tt.setVisible(false);
        Date end_d = new Date();
        time.add(end_d);
        System.out.println("Timer task stopped at" + new Date());
        
        System.out.println(mouse_positions);
        String s = "";
        String ss = "";
         
         for( int m=0; m<mouse_positions.size(); m+=3 ) {    
            
         //   mouse_pos_dict.
               if(mouse_pos_dict.containsKey(mouse_positions.get(m))){
                   ss = mouse_positions.get(m+1)+","+mouse_positions.get(m+2);
                   s+=","+ss;
                   mouse_pos_dict.put(mouse_positions.get(m),s);
               }
               else{
                   s=mouse_positions.get(m+1)+","+mouse_positions.get(m+2);
            mouse_pos_dict.put(mouse_positions.get(m),s);
               }
             
           //   mouse_pos_dict.put(mouse_positions.get(m),mouse_positions.get(m+2));
        
}
         System.out.println(mouse_pos_dict);
        
          try  ( PrintWriter writer = new PrintWriter(new FileWriter("test.csv", true));) {

            StringBuilder sb = new StringBuilder();
            sb.append("Participant");
            sb.append(',');
            sb.append("Condition");
            sb.append(',');            

            sb.append("Block");
            sb.append(',');
            sb.append("Task");
             sb.append(",");
            //sb.append('\n');
            sb.append("Trial");
            sb.append(',');
            sb.append("Target.x");
             sb.append(',');
            //sb.append('\n');
            sb.append("Target.y");
             sb.append(',');
            //sb.append(',');
            sb.append("Targetsize.b"); 
            sb.append(',');
            //sb.append('\n');
            sb.append("Targetsize.l");
            sb.append(',');
            sb.append("TargetArea");
             sb.append(',');
           // sb.append('\n');
            sb.append("Mousepositions.x");
            sb.append(',');
            sb.append("MousePositions.y");
           // sb.append('\n');
             sb.append(',');
            sb.append("error.x");
            sb.append(',');
            sb.append("error.y");
            sb.append('\n');
            int i =0;
            for (int k = 1; k<=target_score;k++){
            sb.append(participant_id);
            sb.append(',');
            sb.append(technique_name);
            sb.append(',');
            sb.append(task_blockname);
             sb.append(',');
            sb.append(individual_taskname);
            sb.append(',');
            sb.append(k);
            sb.append(',');
            sb.append(target_positions.get(i));
            sb.append(',');
            sb.append(target_positions.get(i+1));
            sb.append(',');
            sb.append(target_size.get(i));
            sb.append(',');
            sb.append(target_size.get(i+1));
            sb.append(',');
            sb.append(target_size.get(i+1)*target_size.get(i+1));
            sb.append(',');
            if(k==1 && mouse_pos_dict.containsKey(0) && mouse_pos_dict.containsKey(1)){
                   sb.append(mouse_pos_dict.get(0));
                    sb.append(mouse_pos_dict.get(1));
                  
            }
            else if(mouse_pos_dict.containsKey(k)){
                 sb.append(mouse_pos_dict.get(k));
            }
                    
            sb.append('\n');
            i+=2;
            }

            writer.write(sb.toString());

            System.out.println("done!");

          } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
    }   catch (IOException ex) {
            Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dict.put("Participant_input_data",user_data);
        dict.put("target_positions", target_positions);
        dict.put("target_sizes", target_size);
        dict.put("mouse_position",mouse_positions);
        dict.put("time",time);
        PrintWriter outFile=null;
        FileWriter ioFile = null;
        try {
            ioFile = new FileWriter( "output.txt" ,true);
        } catch (IOException ex) {
            Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
        }
        outFile = new PrintWriter( ioFile );
		
		outFile.printf( "target_score "+target_score );
                outFile.printf( " centre target_score "+centre_target_score );
                outFile.printf( "dictionary ,"+dict );
               // outFile.printf( "target_score "+target_score );
		outFile.close();
        //    pickandmove.task3d_cube_rot_mainmethod();
    
          java.util.Timer t = new java.util.Timer();
                 
                t.schedule(new TimerTask() {
                
                @Override
                public void run() {
                  // scene.removeAllChildren();
//                  Main m = new Main();
//                  Frame f = new MainFrame(m,800,600);
//                  f.setVisible(false);
                    try {
                        //   while(true){
                        Main.mainmethod();
                        indicator = false;
                        System.out.println("Executed...");
                        if(indicator==false){
                            t.cancel();
                        }
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Experiment_window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 
                   
                    }
               
                  }, 1000*3,1000*3);
//                
        // Reset Variables        
//        user_data.clear();
//        target_score = 0;
//        centre_target_score = 0;
//        target_positions.clear();
//        target_size.clear();
//        mouse_positions.clear();
//        time.clear();
                
        //System.exit(0);
        
    }//GEN-LAST:event_stop_btnActionPerformed

  //  jButton1.setBackground(Color.RED);
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(exp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(exp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(exp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(exp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        /* Create and display the form */
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                try {
                    new exp1().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
     

        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Score;
    private static javax.swing.JButton centre_button;
    private javax.swing.JLabel countdown_timer;
    private static javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton start_btn;
    private javax.swing.JButton stop_btn;
    private javax.swing.JLabel targets_selected;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y=  e.getY();
        mouse_positions.add(target_score);
        mouse_positions.add(x);
        mouse_positions.add(y);
        //System.out.print("clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        int x = e.getX();
//        int y=e.getY();
//        mouse_positions.add(x);
//        mouse_positions.add(y);
//        System.out.print("clicked");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}

