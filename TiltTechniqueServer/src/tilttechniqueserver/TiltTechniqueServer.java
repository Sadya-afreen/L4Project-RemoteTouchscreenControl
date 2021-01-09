/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tilttechniqueserver;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author sadya
 */
public class TiltTechniqueServer {
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    private static ServerSocket server = null;
    private static Socket client = null;
    private static BufferedReader in = null;
    private static String line;
    private static boolean isConnected = true;
    private static Robot robot;
    private static final int SERVER_PORT = 5000;
    
    public static void main(String[] args) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    float scale=1;
    float sensitivity=1;
    System.out.println("Server Running!");
            try {
                    robot = new Robot();
                    server = new ServerSocket(SERVER_PORT);
                    client = server.accept();
                   
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    
                    System.out.println("Socket Open successfully");
            } catch (IOException e) {
                    System.out.println("Error in opening Socket");
                    System.exit(-1);
            } catch (AWTException e) {
                    System.out.println("Error in creating robot instance");
                    System.exit(-1);
            }

            while (isConnected) {
                    try {
                  //      while(line!=null){
                        line = in.readLine();
                       
                           // line = in.readLine();
                            //System.out.println(line);

                            if (line.contains(",")) {
                           
                                    float movex = Float.parseFloat(line.split(",")[0]);
                                
                                    float movey = Float.parseFloat(line.split(",")[1]);
                               
                                    Point point = MouseInfo.getPointerInfo().getLocation();
                                    float movez = Float.parseFloat(line.split(",")[2]);
                                
//                                   point.x=(int) movex;
//                                   point.y=(int) movey;

                                    float nowx = point.x;
                                    float nowy = point.y;
                                 //   robot.mouseMove((int)(scale*x),(int)(scale*y));
                                    robot.mouseMove( point.x + (int) (-movex*sensitivity),point.y + (int) (-movey*sensitivity));
                              //         robot.mouseMove((int) ( -movex*6) , (int)movey*6);
                                            
                         //   }
                            }
                            else if(line.contains("leftclick")){
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                  
                            }
                            else if(line.contains("leftrelease")){
                              
		               robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            }
                            else if(line.contains("rightclick")){
                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
				
                            }
                            else if(line.contains("rightrelease")){
                               robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                            }
                            else if(line.contains("stop")){
                                String text = "Press OK to disconnect the server";
                                String title = "Server Connection";
                                int optionType = JOptionPane.OK_CANCEL_OPTION;
                                int result = JOptionPane.showConfirmDialog(null, text, title, optionType);
                                if (result == JOptionPane.OK_OPTION) {
                                    System.exit(0);
                                }

                            }
                
                    } catch (IOException e) {
                            System.out.println("Read failed");
                            System.exit(-1);
                    }
            }
}

    }
    

