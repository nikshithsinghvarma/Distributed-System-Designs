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
  
public class Log { 
  /*
  Appends into log file
  */
    public static void append(String fileName, 
                                       String str) 
    { 
        try { 
  
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter(fileName, true)); 
            out.write(str); 
            //System.out.println("in append");
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
    } 
  
  /*
  Creating an empty og file
  */
    public static File create(String username) 
        throws Exception 
    { 
        // Let us create a sample file with some text 
        String fileName = username+"_log.txt"; 
        try { 
            BufferedWriter out = new BufferedWriter( 
                          new FileWriter(fileName)); 
            out.write(""); 
            out.close(); 
            File f=new File(fileName);
            return f;
        } 
        catch (IOException e) { 
            System.out.println("Exception Occurred" + e); 
        }  
        return null;
    } 

    /*
    retrive contents of file.
    */
    public static String get_contents(String filename)
    { 
        // Let us print modified file 
       try { 
            BufferedReader in = new BufferedReader( 
                        new FileReader(filename)); 
  
            String mystring; 
            if ((mystring = in.readLine()) != null) { 
                return mystring; 
            } 
        } 
        catch (IOException e) { 
            System.out.println("Exception Occurred" + e); 
        } return "";
    }
}