//import all required tools
import java.io.*;
import java.util.ArrayList;

public class Encoder {
	
	//initialize dictionary and previous, current, and previous + current variables
	private ArrayList <String> dictionary = new ArrayList <String> (); 
	private String p = "";
	private char c = 0;
	private String pc = "";

	public Encoder ()
	{
	}


	public void encode (String fileName) throws IOException
	{
		try {
			//reading in a text file and creating print writer
			FileReader fr = new FileReader (fileName);
			BufferedReader br = new BufferedReader(fr);
			PrintWriter pw = new PrintWriter ("encoded.txt");
			while (br.ready())
			{
				c = (char)br.read();
				pc = p+c;
				//dictionary excludes characters 0-255 in the ascii table
				//if pc is already in the dictionary or if it's in the ascii table
				if (dictionary.indexOf(pc) >= 0 || pc.length() == 1)
				{
					p = pc;
				}
				//print out value for previous character
				else
				{

					//if p is already in the ascii table
					if (p.length()==1)
					{
						pw.print((int)p.charAt(0) + " ");
					}
					//if only in dictionary
					else 
					{
						pw.print(256+dictionary.indexOf(p) + " ");
					}
					if (dictionary.size()<=1000)
					{
						dictionary.add(pc);
					}
					p= "" + c;
				}

			}
			//edge case
			//if previous is just one character then convert it to an int
			if (p.length() == 1 )
			{
				pw.print((int)p.charAt(0)+ " ");
			}
			//if previous is a longer String, then find it in the dictionary
			else
			{
				pw.print(256+dictionary.indexOf(p) + " ");
			}
			pw.close();
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println ("can't read");
		}
	}
<<<<<<< HEAD

	public void decode () throws IOException
	{
		String decodedMessage = "";
		ArrayList<String> newDictionary = new ArrayList<String>();
		try
		{
=======
	
	public void decode () {
		String decodedMessage = "";
		try
		{
			
>>>>>>> 84d7301a03e66f67c6e98836fe86c5b4fa731ed1
			// File Reader for Encoded Text
			FileReader fr = new FileReader("encoded.txt");

			// Buffered Reader for File
			BufferedReader br = new BufferedReader(fr);

			// PrintWriter for New Decoded Text File
			PrintWriter pw = new PrintWriter ( "decoded.txt ");

			int a;
			String thisCharacter = "";
			String currentCode = "";
			String c = "";
			String p = "";
			String pc = "";
			
			int code;
			while ((a = br.read()) != -1) {
				thisCharacter = String.valueOf((char)a);
				if (thisCharacter.equals(" ")) {
					code = Integer.parseInt(currentCode);
					if (code <= 255) {
						decodedMessage += p;
						if (c.equals("") && p.equals("") && pc.equals("")) {
							p += String.valueOf((char)code);
						}
						else{
							c = String.valueOf((char)code);
							pc = p + c;
							newDictionary.add(pc);
							p = c;
						}
						
						
					}
					else {
						c = newDictionary.get(code-256);
						pc = p + c.charAt(0);
						newDictionary.add(pc);
						decodedMessage += c;
						p = c;
						
					}
					currentCode = "";
				}
				else {
					currentCode += thisCharacter;
				}
<<<<<<< HEAD

			}
		}
		catch (Exception e)
		{
			System.out.println(e);
=======
				
			}
		}
		catch (Exception e) {
			System.out.println("An error occured.");
>>>>>>> 84d7301a03e66f67c6e98836fe86c5b4fa731ed1
		}
		
		System.out.println(decodedMessage);
	}
}