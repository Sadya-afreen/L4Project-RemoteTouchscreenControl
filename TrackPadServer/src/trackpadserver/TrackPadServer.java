
package trackpadserver;

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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author sadya
 */


public class TrackPadServer {
    private static ServerSocket server = null;               // Server socket initiated
    private static Socket client = null;                     // Client socket initiated
    private static BufferedReader in = null;
    private static String line;
    private static boolean isConnected = true;
    private static Robot robot;                             // Robot instance to execute various message requests received
    private static final int SERVER_PORT = 5000;            // Port number the server socket is connected at.
    private static final float MOVE_RATIO = 3.2f;           // Cursor movement (sensitivity) ratio 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        System.out.println("Server Running!");
            try {
                    robot = new Robot();
                    server = new ServerSocket(SERVER_PORT);
                    client = server.accept();                                  // Client connected
                   
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));    // Receiving client input
                    
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
                          
                            if (line.contains(",")) {       // Smartphone touchscreen x and y values received from client side of the application.
                               
                                   int movex = Integer.parseInt(line.split(",")[0]);   // x and y values received parsed
                                    int mx = (int) (movex * MOVE_RATIO);
                                
                                   
                                    int movey = Integer.parseInt(line.split(",")[1]);
                                    int my = (int) (movey * MOVE_RATIO);
                                 
                                    Point point = MouseInfo.getPointerInfo().getLocation(); // gets current screen cursor location
                                    int x=0;
                                    int y=0;
                                     if(point==null){
                                        point.x=0;
                                        point.y=0;

                                    }
                                    int nowx = point.x;
                                    int nowy = point.y;
                                 
                                    robot.mouseMove((nowx+mx),(nowy+my)); // Robot controls display cursor and moves it based on values received.

                                            
                   
                            }
                            else if(line.contains("leftclick")){             // Execute Left click once message received.
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        
                            }
                            else if(line.contains("leftrelease")){
                             
		               robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            }
                            else if(line.contains("rightclick")){               // Execute Right click once message received.
                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
				
                            }
                            else if(line.contains("rightrelease")){
                               robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                            }
                             else if(line.contains("stop")){                          // Disconnect server once 'stop' message recieved.
                                String text = "Press OK to disconnect the server";
                                String title = "Server Connection";
                                int optionType = JOptionPane.OK_CANCEL_OPTION;
                                int result = JOptionPane.showConfirmDialog(null, text, title, optionType);
                                if (result == JOptionPane.OK_OPTION) {
                                    System.exit(0);
                                }
//                                 System.out.println("here");
//                                JFrame f;
//                                f=new JFrame();
//                                JOptionPane.showMessageDialog(f,"The Server has shut down.");
                            }
                         
               
                    } catch (IOException e) {                   // Exception raised if client values not read in the input stream.
                            System.out.println("Read failed");
                            System.exit(-1);
                    }
            }
      
            
    }
    
}


    

