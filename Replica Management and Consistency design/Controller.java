  //Nikshith Singh Varma
//Id:-1001667758
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.util.Date;
import java.text.*;  
import java.util.Scanner;
import java.text.DecimalFormat;


class Controller extends Thread
{
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String username;
	JTextArea server_textarea;
	DecimalFormat df;
	/*
	Initialising the elements from 
	*/
	public Controller(Socket socket,String username,DataInputStream input,DataOutputStream output,JTextArea server_textarea)
	{
		this.socket=socket;
		this.output=output;
		this.input=input;
		this.username=username;
		this.server_textarea=server_textarea;
	}

	
	/*
	This thread running is used to handle different clients by parsing messages received 
	from clients and sending the responses to client
	*/
	public void run()
	{
		df = new DecimalFormat("###.####");
		while(true)
		{
			try
			{
				if(Server.server_flag==true)
				{
					//Getting HTTP request message from Client.	
					String request_from_client=input.readUTF();
					 
					
					//Retriving the body of message.
					//String[] http_message=request_from_client.split("\n\n");
					//int time=Integer.parseInt(http_message[1]);
					//String ops=http_message[1];

					Server.operation=Server.operation+""+request_from_client;
					//Displaying the HTTP request
					//System.out.println(Server.operation);

					server_textarea.append("\nRequest from "+username+" to do operations:"+request_from_client+" on server instance.");
				
					//Printing the random generated integer sent by Client.
					//server_textarea.append("\n->->->->->-> Server calculates for "+Server.operation+" seconds as requested by "+username+".<-<-<-<-<-<-\n");
					
					//Waiting for the number of seconds specified.
					//Thread.sleep(1000);
					float result;
					System.out.println(Server.operation);
					if(Parse.check_operator(Server.operation))
            {
            String expression=Float.toString(Server.server_value)+Server.operation;
            
            
            
            result=Float.parseFloat(df.format(Calculator.evaluate(expression)));
            server_textarea.append("\nServer instance changed from "+Float.toString(Server.server_value)+" to "+result+".\nMessage sent to "+username+" to update its value.\n");
            Server.server_value=result;
            Server.operation="";
            //server_textarea.append("Server instance changed from.");
        }
            		
					//Message that is to be sent to Client.
					//String message="Server value: $"+Integer.toString(Server.server_value)+"$ for client "+username+".";
					
					//generating date
					Date date=new Date();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                String dates = formatter.format(date);
					
					//writing the message into output buffer
			        output.writeUTF(Float.toString(Server.server_value));
	             
	            	//making server flag false for next itera
			        Server.server_flag=false;
			    }
		    }	
			catch(Exception e)
			{ 
				break;
			}
		}
	}
}
//<Author-Rishabh Mahrsee><Title-Introducing Threads in Socket Programming in Java>
// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/