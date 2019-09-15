//Nikshith Singh Varma
//Id:-1001667758
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.util.Date;
import java.text.*;  


class Controller extends Thread
{
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String username;
	JTextArea server_textarea;
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
		while(true)
		{
			try
			{
				//Getting HTTP request message from Client.	
				String request_from_client=input.readUTF();
				
				//Retriving the body of message.
				String[] http_message=request_from_client.split("\n\n");
				int time=Integer.parseInt(http_message[1]);
				
				//Displaying the HTTP request
				//System.out.println(request_from_client);
				server_textarea.append("\n\tRequest from "+username+"\n:"+request_from_client+"\t\nRequest end\n");
			
				//Printing the random generated integer sent by Client.
				server_textarea.append("\n->->->->->-> Server going to wait for "+time+" seconds as requested by "+username+".<-<-<-<-<-<-\n");
				
				//Waiting for the number of seconds specified.
				Thread.sleep(1000*time);
				
				//Message that is to be sent to Client.
				String message="Server waited "+time+" seconds for client "+username+".";
				
				//generating date
				Date date=new Date();
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dates = formatter.format(date);
				
				//writing the message into output buffer
		        output.writeUTF("POST HTTP/1.1"+"\n"+
                             "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\n"+
                             "Host: "+socket.getInetAddress().getHostName()+"\n"+
                             "Content-Type: "+"String"+"\n"+
                             "Content-Length: "+Integer.toString(message.length())+"\n"+
                             "Date: "+date+"."+"\n"+
                             "\n"+message+"."+"\n");
   				
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