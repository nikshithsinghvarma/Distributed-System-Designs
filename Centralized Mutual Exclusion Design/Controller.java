//Nikshith Singh Varma
//Id:-1001667758
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.util.*;
import java.text.*;  


class Controller extends Thread
{
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String username;
	JTextArea server_textarea;
	JTextArea server_textarea1;
	//Queue<String> usernameQueue;
	Queue<Integer> timeQueue;
	Queue<Thread> requestQueue;

	public Controller(Socket socket, String username,DataInputStream input,
					  DataOutputStream output,JTextArea server_textarea,JTextArea server_textarea1,
					  Queue<Thread> requestQueue,Queue<Integer> timeQueue)
	{
		this.socket=socket;
		this.output=output;
		this.input=input;
		this.username=username;
		this.server_textarea=server_textarea;
		this.server_textarea1=server_textarea1;
		this.timeQueue=timeQueue;
		this.requestQueue=requestQueue;
	}
	
	/*
	This thread running is used to handle different clients by parsing messages received 
	from clients and sending the responses to client
	*/
	public void run()
	{
		while(true)
		{
			//To show that client is connected to server dynamically.
			server_textarea1.append("====="+username+" is connected.=====\n");
			
			try
			{
				//Getting HTTP request message from Client.	
				String request_from_client=input.readUTF();
				
				//Displaying the HTTP request
				
				server_textarea.append("\n\tRequest from "+username+"\n:"+request_from_client+"\t\nRequest end\n");
			
				//Retriving the body of message.
				String[] http_message=request_from_client.split("\n\n");
				int time=Integer.parseInt(http_message[1]);
				
				int sum = 0;

				//Iterating through the queue to calculate waiting time.
				Iterator iterator = timeQueue.iterator();
				while(iterator.hasNext())
				{
  					int element = (int) iterator.next();
  					sum=sum +element;
				}

				//add current time to time Queue.
				timeQueue.add(time);
				
				//start processing the request.
				Thread clientRequest=new Request(socket,time,username,output,sum);

				//adding requested thread to queue.
				requestQueue.add(clientRequest);
				
				server_textarea.append("\n->->->->->-> Time "+time+" seconds to the Queue as requested by "+username+".<-<-<-<-<-<-\n");
				System.out.println(timeQueue+" "+username+" "+time+" "+sum);
				
			
				if(requestQueue.peek().equals(clientRequest))
				{
								
				}
				else
				{
					//making other requests wait.
					System.out.println(username+" "+time+" on wait");
					requestQueue.peek().join();
				}
				
				//processing the current request.
				Thread.sleep(1000*time);
				clientRequest.start();
				
				//remove the elements from queue
				requestQueue.remove();
				timeQueue.remove();
				
				//printing on the server side. 
				server_textarea.append("\n<-<-<-<-<-<- Server Waited for "+time+" seconds  as requested by "+username+".->->->->->->\n");
				System.out.println(username+" "+time+" completed execution");
   				
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