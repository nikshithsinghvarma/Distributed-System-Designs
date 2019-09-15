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
 
public class Parse 
{
/*
checks if string starts with operator
*/
	public static boolean check_operator(String input)
	{
	char[] input_array = input.toCharArray();
	int i=0;

	//ignore spaces.
	while(input_array[i]==' ')
	{
		i++;
	}

	//check for operator
	if(input_array[i] == '+' || input_array[i] == '-' || 
                     input_array[i] == '*' || input_array[i] == '/' || input_array[i] == '%')
                     {
                     	return true;
                     }	
                     else
                     {
                     	return false;
                     }
	}

	/*
	adds space as delimiter for input string
	*/
	public static String add_space(String input)
	{
		 char[] input_array = input.toCharArray();
		 String new_input_array="";
		 int i;
		 //check the string
		 for(i=0;i<input.length();++i)
		 {
		 	//check if operator and if it is an operator add spaces
		 	if (input_array[i] == '+' || input_array[i] == '-' || 
                     input_array[i] == '*' || input_array[i] == '/')
                     {
                     	new_input_array=new_input_array+" "+input_array[i]+" ";
                     } 
                     else
                     {
                     	new_input_array=new_input_array+input_array[i];  
                     	                   }
		 }
		 return new_input_array;
	}

	/*
	makes calculations easier for compatible srings starting with negative number.
	*/
	public static String check_negative(String input)
	{
		char[] input_array = input.toCharArray();
                if(input_array[0]=='-' || (input_array[1]=='-' && input_array[0]==' '))
               	{
                    input="0 "+input;
                }
                return input;
	}
}
