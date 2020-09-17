//import all required tools
import java.io.*;
import java.util.*;

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
			pw.print("x");
			for (int i = 0; i < dictionary.size(); i++) {
				pw.print("" + (i + 256) + ":" + dictionary.get(i).length() + "-" + dictionary.get(i));
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
		ArrayList<String> newDictionary = new ArrayList<String>();
		try
		{
			// File Reader for Encoded Text
			FileReader fr = new FileReader("encoded.txt");

			// Buffered Reader for File
			BufferedReader br = new BufferedReader(fr);

			// PrintWriter for New Decoded Text File
			PrintWriter pw = new PrintWriter ( "decoded.txt ");
			
			
			int a;
			boolean foundX = false;
			String currentString = "";
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			int thisCode = 0;
			int lengthCounter = 0;
			int codeLength = 0;
			boolean startedReading = false;
			while ((a = br.read()) != -1) {
				if (foundX == true) {
					if (startedReading == true) {
						currentString += ((char)a);
						lengthCounter++;
						if (lengthCounter == codeLength) {
							map.put(thisCode, currentString);
							currentString = "";
							lengthCounter = 0;
							startedReading = false;
						}
					}
					else {
						if (String.valueOf((char)a).equals(":")) {
							thisCode = Integer.parseInt(currentString);
							currentString = "";
						}
						else if (String.valueOf((char)a).equals("-")) {
							codeLength = Integer.parseInt(currentString);
							startedReading = true;
							currentString = "";
						}
						else {
							currentString += ((char)a);
						}
					}
				}
				else if (String.valueOf((char)a).equals("x")) {
					foundX=true;
				}
			}
//			for (Integer key : map.keySet()){
//				System.out.println(key + ": " + map.get(key));
//			}
			fr.close();
			br.close();
			// File Reader for Encoded Text
			FileReader f = new FileReader("encoded.txt");

			// Buffered Reader for File
			BufferedReader bf = new BufferedReader(f);
			String thisCharacter = "";
			String currentCode = "";
			int code = 0;
			int b;
			while ((b = bf.read()) != -1 && String.valueOf((char)b).equals("x") == false) {
				thisCharacter = String.valueOf((char)b);
				if (thisCharacter.equals(" ")) {
					code = Integer.parseInt(currentCode);
					if (code <= 255) {
						decodedMessage += (char)code;
					}
					else {
						decodedMessage += map.get(code);	
					}
					currentCode = "";
				}
				else {
					currentCode += thisCharacter;
				}
			}
			System.out.println(decodedMessage);
		}
		catch (Exception e)
		{
			System.out.println(e);

		}
		
		//System.out.println(decodedMessage);
	}
}
