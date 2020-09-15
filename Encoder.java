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

	public void decode () throws IOException
	{
		String decodedMessage = "";
		try
		{
			// File Reader for Encoded Text
			FileReader fr = new FileReader("encoded.txt");

			// Buffered Reader for File
			BufferedReader br = new BufferedReader(fr);

			// PrintWriter for New Decoded Text File
			PrintWriter pw = new PrintWriter ( "decoded.txt ");

			int a;
			
			// String for This Character with Initialization
			String thisCharacter = "";
			
			// String for Current Code with Initialization
			String currentCode = "";

			int code;
			while ( (a = br.read () ) != -1 )
			{
				thisCharacter = String.valueOf((char)a);
				if (thisCharacter.equals(" ")) {
					code = Integer.parseInt(currentCode);
					if (code <= 255) {
						decodedMessage += ((char)code);
					}
					else {
						decodedMessage += dictionary.get(code-256);
					}
					currentCode = "";
				}
				else {
					currentCode += thisCharacter;
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("An error occured.");
		}
		
		// PrintWriter for New Decoded Text File
		PrintWriter pw = new PrintWriter ( "decoded.txt ");
		
		for ( int counter = 0; counter < decodedMessage.length (); counter++ )
		{
			pw.print ( decodedMessage.charAt ( counter ) );
		}
		
		pw.close ();
	}
}
