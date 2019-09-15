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
import java.util.Scanner;
import java.text.DecimalFormat;
 
public class Client implements ActionListener,Runnable
{
    static JFrame clientframe;
    static JTextArea client_display_area;
    static JScrollPane scroller;
    static JTextArea evaluate_area;
    static JTextArea value_area;
    static JScrollPane evaluate_scroller;
    static JButton connect_button;
    static JButton evaluate_button;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    static float client_value=0; 
    static String username;
    static int flag=1;
    static float result;
    static int time;
    static File logfile;
    static volatile String logfile_name;
    static volatile String  inputlog="";
    static Scanner sc;
    static DecimalFormat df;

    /*
    This is running part of thread which deals with generating random and sending it to client
    until the client exits.
    */
    public void run()
    {
        String message="";
        try
            {
                //Establishing connection with server.
                socket = new Socket("127.0.0.1", 2000);
                df = new DecimalFormat("###.####");
                
                if(socket.isConnected()==false)
                {
                    //if not connected
                    client_display_area.append("Client is disconnected");  
                }
                else
                {
                    client_value=10;
                    client_display_area.append(" Initializing a local copy of the shared value to : "+client_value+".\n");
                    client_display_area.append("\nConnected to Server.\n");
                }
                
                //setting input and output streams
                output = new DataOutputStream(socket.getOutputStream());
                input  = new DataInputStream(socket.getInputStream()); 
                
                //creating log file
                logfile=Log.create(username);
                
                //initializing Client value
                

                

                //Registering user with server.
                output.writeUTF(username);
            
                while(flag==0)
                {
                    //setting scanner to read log file
                    sc = new Scanner(logfile); 

                    //if log file is not empty
                    if(sc.hasNextLine())
                   {
                    //read message from log file
                    message=sc.nextLine();

                    //send to server
                    output.writeUTF(message); 

                    //refesh logfile
                    logfile=Log.create(username);

                    //response from server
                    String server_message=input.readUTF();

                    //display server message and change value
                    client_display_area.append("\nServer modifies client instance from "+client_value+" to "+server_message+".\n");
                    client_value=Float.parseFloat(server_message);
                   }
                }
                }
            catch(Exception e)
            {
                System.out.println(e);
                    client_display_area.append("\n---------Try connecting again---------\n");
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
                connect_button.setBounds(150, 400, 200, 50);
                connect_button.addActionListener(new Client());
                evaluate_button = new JButton("Evaluate");
                evaluate_button.setBounds(350, 400, 200, 50);
                evaluate_button.addActionListener(new Client());
                clientframe.add(connect_button);
                clientframe.add(evaluate_button);
                client_display_area = new JTextArea();
                scroller = new JScrollPane(client_display_area);
                scroller.setBounds(200,50,500,200);
                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
                    public void adjustmentValueChanged(AdjustmentEvent e) {  
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
                    }
                    });
                evaluate_area = new JTextArea();
                evaluate_area.setBounds(200,300,500,25);
                value_area = new JTextArea();
                value_area.setBounds(400,350,100,25);
                clientframe.getContentPane().add(scroller);
                clientframe.getContentPane().add(evaluate_area);
                clientframe.getContentPane().add(value_area);
                JButton exit_button = new JButton("Exit");
                exit_button.setBounds(550, 400, 200, 50);
                clientframe.add(exit_button);
                exit_button.addActionListener(new Client());
                clientframe.setSize(900, 500);
                clientframe.setLayout(null);
                clientframe.setVisible(true);
                clientframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
                
        }
        catch(Exception i)
        {
        
            client_display_area.append("\n---------Try-again.---------\n");
                    
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
        if(button_name.equals("Evaluate"))
        {
            if(flag==0)
            {
                String input=evaluate_area.getText();
                String message;

                
                    log_evaluate(input);
            }
            else
            {
                client_display_area.append("\n(((((((((( Check if Client is connected )))))))))))))\n");
            }
            evaluate_area.setText(" ");
        }
        if(button_name.equals("Exit"))
        {
            //Stopping the Connection.
            flag=1;
            client_display_area.append("\n$$$$$$$$$ This Client has disconnected $$$$$$$$$\n");
            //clientframe.dispose();
        }
    }
    
    public static void log_evaluate(String input)
    {
        try
        {
            if(Parse.check_operator(input))
            {
            String expression=Float.toString(client_value)+input;
            
            
            
            result=Calculator.evaluate(expression);

            //System.out.println(expression);
            //System.out.println(result);
            
            value_area.setText(df.format(result));
            client_value=Float.parseFloat(df.format(result));
            Log.append(logfile.getName(),input);

            client_display_area.append("\n~~~~~~~~~~~~ Client value updated after operation : "+input+" to "+client_value+".~~~~~~~~~~");
            }
            else
            {
                 client_display_area.append("\nXXXXXXXXX Start input with operator. XXXXXXXXX\n");
            }
        }
        catch(UnsupportedOperationException e)
        {
            client_display_area.append("\nXXXXXXXXX The result of this operation leads to Arithematic exception.XXXXXXXXX\n");
        }
        catch(Exception e)
        {
           client_display_area.append("\nXXXXXXXXX Enter correct string to evaluate XXXXXXXXX\n"); 
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

