//Nikshith Singh Varma
//Id:-1001667758
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.util.Date;
import java.text.*;  
import java.util.LinkedList; 
import java.util.Queue;

class Request extends Thread
{
	Socket socket;
	DataOutputStream output;
	int time;
	String username;
	int waitingtime;

	public Request(Socket socket,int time,String username,DataOutputStream output,int waitingtime)
	{
		this.socket=socket;
		this.output=output;
		this.time=time;
		this.username=username;
		this.waitingtime=waitingtime;
	}
	public void run()
	{
		try
		{
			//calculating total time from processing time and waiting time
			
			int totaltime=waitingtime+time;
			
			//Message that is to be sent to Client.
			String message="Server waited "+totaltime+" seconds(processing time:"+" "+time+" and waiting time:"+" "+waitingtime+") for client "+username+".";
					
			//generating date 
			Date date=new Date();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String dates = formatter.format(date);

			//writing the message into output buffer
			output.writeUTF("POST HTTP/1.1"+"\n"+
	                       "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36\n"+
	                       "Host: "+socket.getInetAddress().getHostName()+"\n"+
	                       "Content-Type: "+"String"+"\n"+
	                       "Content-Length: 4\n"+
	                       "Date: "+date+"."+"\n"+
	                       "\n"+message+"."+"\n");
		}
		catch(Exception e)
		{

		}
	}
}