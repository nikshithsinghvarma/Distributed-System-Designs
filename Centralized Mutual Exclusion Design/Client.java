//Nikshith Singh Varma
//Id:-1001667758

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.*;
 
public class Client implements ActionListener,Runnable
{
    static JFrame clientframe;
    static JTextArea client_display_area;
    static JScrollPane scroller;
    static JButton connect_button;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    int time; 
    static String username;
    static int flag=0;

    /*
    This is running part of thread which deals with generating random and sending it to client
    until the client exits.
    */
    public void run()
    {
        try
            {
                //Establishing connection with server.
                socket = new Socket("127.0.0.1", 2000);
                
                if(socket.isConnected()==false)
                    {
                      client_display_area.append("Client is disconnected");  
                    }
                else
                {
                    client_display_area.append("Connected.\n");
                }
                
                output = new DataOutputStream(socket.getOutputStream());
                input  = new DataInputStream(socket.getInputStream()); 
                
                //Registering user with server.
                output.writeUTF(username);
            }
            catch(Exception e)
            {
                    client_display_area.append("\n---------Try connecting again---------\n");
            }

            // Generates random number and sends to Server.
            while(flag==0)
                {
                    Date dates       = new Date();
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date      = formatter.format(dates);

                    Random random = new Random();
                    int max = 15;
                    int min = 5;
                    time=random.nextInt(max-min+1)+min;
                    String time_in_string=Integer.toString(time);
                    
                    //sending message to Server.
                    try
                    {
                        output.writeUTF("GET HTTP/1.1"+"\n"+
                                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\n"+
                                        "Host: "+socket.getInetAddress().getHostName()+"\n"+
                                        "Content-Type: "+"Integer"+"\n"+
                                         "Content-Length: "+Integer.toString(time_in_string.length())+"\n");
                        //reply from server.
                        String response_from_server=input.readUTF();
                        
                        String[] http_message=response_from_server.split("\n\n");
                        String message=http_message[1];
                        
                        //displaying message from server.
                        client_display_area.append(message);
                        
                    }
                    catch(Exception i)
                    {
                        client_display_area.append("\n---------Server Not Found---------\n");
                        break;
                    }
                }
        }
    /*
    This method reads the username and launches the GUI if username is not empty.
    */
    public void client1()
    {

        try
        {
            //Getting username of client.
            username = JOptionPane.showInputDialog(null, "Enter User Name for client");
            
            if(username.isEmpty()||username==null)
            {
                System.out.println("\n---------Enter Valid Username and try again---------\n");
            }
            else
            {
                clientframe = new JFrame("Client");
                 
                connect_button = new JButton("Connect "+username);
                connect_button.setBounds(100, 300, 200, 50);
                connect_button.addActionListener(new Client());
                clientframe.add(connect_button);
                client_display_area = new JTextArea();
                scroller = new JScrollPane(client_display_area);
                scroller.setBounds(50,50,500,200);
                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
                    public void adjustmentValueChanged(AdjustmentEvent e) {  
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
                    }
                    });
                clientframe.getContentPane().add(scroller);
                JButton exit_button = new JButton("Exit");
                exit_button.setBounds(300, 300, 200, 50);
                clientframe.add(exit_button);
                exit_button.addActionListener(new Client());
                clientframe.setSize(600, 500);
                clientframe.setLayout(null);
                clientframe.setVisible(true);
                clientframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
                
        }
        catch(Exception i)
        {
        
            client_display_area.append("\n---------Try again.---------\n");
                    
        }
    
    }
    /* 
    used to check event occured and ation that is to be performed after the button click on frame.
    @param  e contains infor mation about event occured.
    @return void
    */ 
    public void actionPerformed(ActionEvent e)
    {

        Thread thread=new Thread(new Client());
        String button_name=e.getActionCommand();
        
        if(button_name.equals("Connect "+username))
        {
            //Starting client connection request on clicking button.
            
            flag=0;
            thread.start();
            
        }
        if(button_name.equals("Exit"))
        {
            //Stopping the Connection.
            flag=1;
            client_display_area.append("\n$$$$$$$$$ This Client has disconnected $$$$$$$$$\n");
            //clientframe.dispose();
        }
    }
 
    public static void main(String args[])
    {
        try
        {
            //Starting Client GUI.
            Client client = new Client();
            client.client1();
        }
        catch(Exception e)
        {
            System.out.println("\n---------Something went wrong.Please Try again---------\n");
        }
    }
}
//<Author-Rishabh Mahrsee><Title-Introducing Threads in Socket Programming in Java>
// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
