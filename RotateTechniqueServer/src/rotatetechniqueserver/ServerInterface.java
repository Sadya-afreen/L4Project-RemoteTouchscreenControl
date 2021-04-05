package rotatetechniqueserver;
import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.lang.Runnable;
/**
 *
 * @author sadya
 */

/** Class to initiate client-server connection and start a thread to receive and send messages to RotateTechnique Server Class */
public class ServerInterface {
    interface MessageReceiveInterface
	{
		void msgReceived(String msg);
	}
    private ServerSocket connectionSocket;              // Server socket intialised
    private Socket serverSocket;
    private BufferedReader reciever;
    private static final int port = 5000;               // Port number the server socket is connected at.
    Thread receiverThread;                              // thread to execute receivng messages from the client side of the application
    MessageReceiveInterface callback;

    public ServerInterface(MessageReceiveInterface callback)
    {
            this.callback = callback;
            try
    {
        connectionSocket = new ServerSocket(port);
    }
            catch (IOException e) 
            {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }

    }
    public void start()
    {

    	receiverThread = new Thread(new Runnable()
    	{
    		@Override
    		public void run()
    		{
    			try
    			{
    				serverSocket = connectionSocket.accept();          // Client connected
    				reciever = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));  // Client input received
    				while (!Thread.interrupted())
    				{
    					final String msg = reciever.readLine();
    					if (msg==null)
    						break;
    					new Thread(new Runnable()
    					{
    						@Override
    						public void run()
    						{
    							callback.msgReceived(msg);
    						}
    					}).start();
    				}
    			}
    			catch(SocketException e)
    			{
    			    System.out.println("Error Occured: "+e.getMessage());
    			    e.printStackTrace();
    			}
    			catch(IOException e)
    			{
    			    System.out.println("Error Occured: "+e.getMessage());
    			    e.printStackTrace();
    			}
    		}
    	});
    	receiverThread.start();
    }

	public void stop()
	{
		receiverThread.interrupt();
		try 
		{
			reciever.close();
			serverSocket.close();
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Wait()
	{
		try {
                      //  receiverThread.start();
			receiverThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    
}
