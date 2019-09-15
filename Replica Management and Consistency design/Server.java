 //Nikshith Singh Varma
//Id:-1001667758
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList; 
import java.util.Queue;
import java.text.DecimalFormat;

public class Server implements ActionListener,Runnable
{
    static JFrame f;
    static JTextArea d;
    static JTextArea d1;
    static JButton b1;
    static JButton b2;
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;
    DataOutputStream out;
    static int num=0;
    public static volatile boolean server_flag=false;
    public static volatile String operation="";
    public static volatile float server_value=0;
    static String server_logfile_name;
    static File server_logfile; 

    public static void Server1()
    {
        f=new JFrame("server");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        b1=new JButton("Start Server");
        b1.setBounds(100, 600, 200, 50);
        b2=new JButton("Poll");
        b2.setBounds(300, 600, 200, 50);
        d=new JTextArea();
        JScrollPane s=new JScrollPane(d);
        s.setBounds(50,0,500,150);
        s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        s.setVisible(true);
        s.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
        public void adjustmentValueChanged(AdjustmentEvent e) {  
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        }
        });
        d1=new JTextArea();
        JScrollPane s1=new JScrollPane(d1);
        s1.setBounds(50,160,500,440);
        s1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        s1.setVisible(true);
        s1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
        public void adjustmentValueChanged(AdjustmentEvent e) {  
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        }
        });
        f.add(b1);
        f.add(b2);
        b1.addActionListener(new Server());
        b2.addActionListener(new Server());
        f.getContentPane().add(s);
        f.getContentPane().add(s1);
        f.setSize(600,700);
        f.setLayout(null);
        f.setVisible(true);
        
    }
   
   /*
    This running thread is used to start the sender and pass the information
    to controller to handle the messages
   */
    public void run()
    {
        try
        {
            //Server gets started and is Listening.
            server = new ServerSocket(2000);
            d.append("########### Server is waiting for Client ###########\n");
            DecimalFormat df = new DecimalFormat("###.####");
            server_value=10;
            d.append("Server instance value is initialized to : "+server_value+".\n");

            while(true)
            {
                try
                {
                    //Client got connected.
                    socket = server.accept();
                   
                    //establishing input and output streams.
                    in = new DataInputStream(socket.getInputStream());
                    out=new DataOutputStream(socket.getOutputStream());
                    server_logfile=Log.create("server");
                    
                    //Getting the username of Client.
                    String username= in.readUTF();

                    //Registering username at server.
                    d.append("\n************ New Client with Username "+username+" Registered. ************\n");
                   
                    //Calling Controller to perform action that is required.
                    Controller c=new Controller(socket,username,in,out,d1);
                    c.start();
                    
                }
                catch(Exception e)
                {
                    d.append("\n------------ Something went wrong ------------\n");
                    break;
                }
            }          
        }
        catch(IOException i)
        {
            System.out.println(i);

        }
    }
    /*
    Starts the Server when button is clicked.
    */
    public void actionPerformed(ActionEvent e)
    { 
        String button_name=e.getActionCommand();
        if(button_name.equals("Start Server"))
        {
        //Starting the server when button is Clicked.
            Thread thread=new Thread(new Server());
            thread.start();
        }
        if(button_name.equals("Poll"))
        {
            Server.server_flag=true;
        }
    }

    public static void main(String args[])
    {
       //Starting the GUI. 
        Server1();
    }
}
//<Author-Rishabh Mahrsee><Title-Introducing Threads in Socket Programming in Java>
// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/