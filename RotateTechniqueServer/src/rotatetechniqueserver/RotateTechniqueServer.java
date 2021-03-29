package rotatetechniqueserver;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JOptionPane;

/**
 *
 * @author sadya
 */

/** Class to execute the display cursor control functionality based on the client cursor updates received.*/
public class RotateTechniqueServer  implements ServerApp_AirMouse.IGetMessage {
 
    private ServerApp_AirMouse server;       // Server initiated from ServerApp_Airmouse interface
    private int x, y;
    private Robot robot;                     // Robot instance to execute various message requests received             
    enum Button	{ LEFT, RIGHT }

    private final int MAX_X;                // Max x value to set the cursor not beyond the screen size
    private final int MAX_Y;                // Max y value to set the cursor not beyond the screen size
    private final int MIN_X;                // Man x value to set the cursor not beyond the screen size
    private final int MIN_Y;                // Min y value to set the cursor not beyond the screen size
    
    public RotateTechniqueServer() throws AWTException
	{
		server = new ServerApp_AirMouse(this);
		server.start();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MAX_X = (int)screenSize.getWidth();
		MAX_Y = (int)screenSize.getHeight();
		MIN_X = 0;
		MIN_Y = 0;
		x = MAX_X/2;
		y = MAX_Y/2;
		robot = new Robot();
		move (0,0);
	}

	public void move(int dx,int dy)
	{
		x = (x+dx)>MAX_X?MAX_X:((x+dx)<MIN_X?MIN_X:x+dx);
		y = (y+dy)>MAX_Y?MAX_Y:((y+dy)<MIN_Y?MIN_Y:y+dy);
		robot.mouseMove(x,y);                                  // Robot controls display cursor and moves it based on values received.
	}

	private void clickButton(String b)
	{
		switch(b)
		{
                    case "leftclick":                                         // Execute Left click once message received.
                        System.out.println(true);
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        break;
                    case "leftrelease":
                        System.out.println(false);
		        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        break;
                           
                    case "rightclick":                                       // Execute Right click once message received.
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		        break;
                            
                    case "rightrelease":
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        break;
                    case "stop":                                             // Disconnect server once 'stop' message recieved.
                        String text = "Press OK to disconnect the server";
                        String title = "Server Connection";
                        int optionType = JOptionPane.OK_CANCEL_OPTION;
                        int result = JOptionPane.showConfirmDialog(null, text, title, optionType);
                        if (result == JOptionPane.OK_OPTION) {
                            System.exit(0);
                        }
                            break;
//			case RIGHT:
//				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
//				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
//				break;
		}
	}	

	private void processSensorData(float[] values)
	{
		float magnitude = (float)Math.sqrt(values[0]*values[0] + 
							values[1]*values[1] + values[2]*values[2]);
		if(magnitude < 0.25)
			return;
		final float sensitivity = 10.2f;
//                int movex =(int) ((int) 1244+((27-1244)*((values[0]-values[1])/(values[1]-values[2]))));
//                int movey =(int) ((int) 72+((598-72)*((values[0]-values[1])/(values[1]-values[2]))));
                int movex = -(int)(values[2]*sensitivity);                       // x and z vlaues received from the client are parsed
		int movey = -(int)(values[0]*sensitivity);
		//int movey = -(int)(values[2]*sensitivity);
		move(movex,movey);
	}
        String s = "";
        
	@Override
	public void msgReceived(String msg)
	{
		String dataParts[] = msg.split(",");          // Receiving client input
                
                //System.out.print(dataParts[0]);
		if(dataParts.length==1)
			switch(dataParts[0])
			{
				case "leftclick":
                                        s = "leftclick";
					clickButton(s);
					break;
                                case "leftrelease":
                                        s = "leftrelease";
                                        clickButton(s);
                                        break;
				case "rightclick":
                                        s = "rightclick";
                                        clickButton(s);
					break;
                                case "rightrelease":
                                        s = "rightrelease";
					clickButton(s);
					break;
                                case "stop":
                                        s = "stop";
					clickButton(s);
					break;
			}
		else
		{
			float[] values = new float[dataParts.length];
			for (int i = 0;i<dataParts.length;i++)
				values[i] = Float.parseFloat(dataParts[i]);
                               
			processSensorData(values);
                        
		}
	}

	public void waitForServer()
	{
		server.waitForServer();
	}

	public static void main(String []args) throws AWTException
	{
		RotateTechniqueServer a = new RotateTechniqueServer();
		System.out.println("Server Started");
		a.waitForServer();
	}
    
}

  
    