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
			
			// keep track of the index of the file
			int index = 0;
			
			//the string that will be printed
			String toPrint = "";
			
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
						toPrint = (int)p.charAt(0) + " ";
						index += toPrint.length();
						pw.print(toPrint);
					}
					//if only in dictionary
					else 
					{
						toPrint = 256 + dictionary.indexOf(p) + " ";
						index +=toPrint.length();
						pw.print(toPrint);
					}
					if (dictionary.size()<=1000)
					{
						dictionary.add(pc);
					}
					else 
					{
						System.out.println ("Dictionary excedes limit"); 
						br.close (); 
						break; 
					}
					p= "" + c;
				}

			}
			//edge case
			//if previous is just one character then convert it to an int
			if (p.length() == 1 )
			{
				toPrint = (int)p.charAt(0)+ " ";
				index += toPrint.length();
				pw.print(toPrint);
			}
			//if previous is a longer String, then find it in the dictionary
			else
			{
				toPrint = 256+dictionary.indexOf(p) + " ";
				index += toPrint.length();
				pw.print(toPrint);
			}
			// Include the dictionary at the end of the encoded file
			// Print an x to represent the end of the code and the start of the dictionary
			pw.print("x");
			// print each the index of each dictionary entry, then the length of the entry so when reading it in, it is easy to know when to stop, then print the entry itself
			// these are delimited by a ":" between the index and the length and a "-" between the length and the entry itself
			for (int i = 0; i < dictionary.size(); i++) {
				pw.print("" + (i + 256) + ":" + dictionary.get(i).length() + "-" + dictionary.get(i));
			}
			
			//creates the end of the file in the order of: "[index of delimieter] [how long that index is]
			String delimiterIndexString = "" + index;
			int lengthOfIndex = delimiterIndexString.length();
			pw.print(delimiterIndexString);
			pw.print(" ");
			pw.print(lengthOfIndex);
			//close all writers and readers
			pw.close();
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			System.out.println ("can't read");
		}
	}
	
	public void check () throws IOException
	{
		//read in the file
		RandomAccessFile fileReader = new RandomAccessFile ("encoded.txt", "r");

		// PrintWriter for New Decoded Text File
		PrintWriter pw = new PrintWriter ( "decoded.txt ");
		
		//find the index where the dictionary begins
		
		//go to the end of the file
		int index = (int) fileReader.length() - 1;
		fileReader.seek(index);
		
		//currentCharacter represents the current char being read
		int currentCharacter = fileReader.read();
		
		//A string representing the # of digits in the index
		String digitsInIndexString = "";
		
		//we read the file backwards till we hit a space (indicating the end of the number representing
		//the num of digits
		while (currentCharacter!= 32)
		{
			//add the current character to the front of the digitsInIndex string
			digitsInIndexString = (char)currentCharacter + digitsInIndexString;
			
			//read the next char to the left
			index --;
			fileReader.seek(index);
			currentCharacter = fileReader.read();
		}
		
		//change the digits in index string to an int
		int digitsInIndex = Integer.parseInt(digitsInIndexString);
		System.out.println("Number of digits in the delimiter's index: " + digitsInIndex);

		//move one backwards from the space to the first char in the index of the delimiter
		index--;
		
		//save this char as the end of the index of the delimiter
		int endIndexOfDelimiterIndex = index;
		
		//this string represents the index of the delimiter
		String indexOfDelimiterString = "";
		
		//read through as many characters as there are digits in the index of the delimiter
		while (index > endIndexOfDelimiterIndex - digitsInIndex)
		{
			//read in each digit and then move to the char to the left
			fileReader.seek(index);
			indexOfDelimiterString = (char)fileReader.read() + indexOfDelimiterString;
			index --;
		}
		
		//parse the index of the delimiter into a string
		int indexOfDelimiter = Integer.parseInt(indexOfDelimiterString);
		System.out.println("Index of delimiter: " + indexOfDelimiter);
		
		int numUselessCharacters = indexOfDelimiterString.length() + digitsInIndexString.length() + 1;
		System.out.println ("There are " + numUselessCharacters + " at the end that shouldn't be read in as part of the dictionary");
	}

	public void decode () throws IOException
	{
		// New String for Message
		String decodedMessage = "";
				
		try
		{
			// First, the dictionary alone is read from the encoded file
			// File Reader for Encoded Text
			RandomAccessFile fileReader = new RandomAccessFile ("encoded.txt", "r");

			// PrintWriter for New Decoded Text File
			PrintWriter pw = new PrintWriter ( "decoded.txt ");
			
			//find the index where the dictionary begins
			//go to the end of the file
			fileReader.seek(fileReader.length());
			
			//read in the cha
			int currentCharacter = fileReader.read();
			String digitsInIndex = "";
			
			while (currentCharacter!= 32)
			{
				digitsInIndex = (char)currentCharacter + digitsInIndex;
				currentCharacter = fileReader.read();
			}
			
			// Boolean for whether or not the Letter X has been read in yet, which indicates the start of the dictionary
			boolean foundX = false;
			
			// String for the Current String being constructed
			String currentString = "";
			
			// HashMap for reconstructed Dictionary
			HashMap < Integer, String > map = new HashMap < Integer, String > ();
			
			// Int for the Current Code
			int thisCode = 0;
			
			// Int for Current Counter (counts the number of characters read so far for each dictionary entry)
			int lengthCounter = 0;
			
			// Int for Current Length (stores the correct length of each dictionary entry)
			int codeLength = 0;
			
			// Boolean for Reading (whether or not the characters currently being read are part of a combination of characters that is stored in the dictionary)
			boolean startedReading = false;
			
			// While There Is a Char in the Buffered Reader
			while ( ( currentCharacter = br.read () ) != -1 )
			{
				// If the letter X has been found yet
				if ( foundX == true )
				{
					// If We Have Started Reading a combination that is in the dictionary
					if ( startedReading == true )
					{
						// Add the Char Version of the Letter from the Buffered Reader to the current String that is being constructed
						currentString += ( ( char ) currentCharacter );
						// counter for the length of the String constructed so far Increases by One
						lengthCounter++;
						//if the current String has hit the specified length of the dictionary entry
						if ( lengthCounter == codeLength )
						{
							// Add to Dictionary
							map.put ( thisCode, currentString );
							// Reset currentString
							currentString = "";
							// Reset lengthCounter
							lengthCounter = 0;
							// Reset startedReading
							startedReading = false;
						}
					}
					else
					{
						// If Statement for Delimiter ":", which represents the end of the index of the dictionary entry and the start of its length
						if ( String.valueOf ( ( char ) currentCharacter).equals ( ":" ) )
						{
							thisCode = Integer.parseInt(currentString);
							currentString = "";
						}
						// If Statement for Delimiter "-", which indicates the end of the length of the dictionary entry and the start of the actual character combination
						else if (String.valueOf ( ( char ) currentCharacter ).equals ( "-" ) )
						{
							//parse the length of the entry to an int
							codeLength = Integer.parseInt(currentString);
							//set startedReading to true
							startedReading = true;
							//reset currentString
							currentString = "";
						}
						else
						{
							//append the current character to currentString
							currentString += ( ( char ) currentCharacter );
						}
					}
				}
				// If a = x, Then We Found X
				else if ( String.valueOf ( ( char ) currentCharacter ).equals ( "x" ) )
				{
					//set foundX to true
					foundX = true;
				}
			}

			//close the file reader and the buffered reader
			fr.close();
			br.close();
			
			// Now that the dictionary is fully reconstructed, the file is read again to translate the encoded section
			// File Reader for Encoded Text
			FileReader f = new FileReader("encoded.txt");

			// Buffered Reader for File
			BufferedReader bf = new BufferedReader(f);
			
			// String Character to store the current character
			String thisCharacter = "";
			
			// String Current Code to store the current code
			String currentCode = "";
			
			// int to store the currentCode after it is parsed to an integer
			int code = 0;
			
			// String for the Buffered Reader
			int b;
			
			// While b != "x" and the end of the file hasn't been reached
			while ( ( b = bf.read() ) != -1 && String.valueOf ( (char ) b ).equals ( "x" ) == false )
			{
				// thisCharacter Becomes the current character
				thisCharacter = String.valueOf ( ( char ) b );
				
				// If Statement for Delimiter " ", which indicates the end of the current code
				if (thisCharacter.equals ( " " ) )
				{
					// parse the current code to an integer 
					code = Integer.parseInt(currentCode);
					
					// If the code represents a single ASCII character
					if ( code <= 255  )
					{
						// the character is added to the message
						decodedMessage += (char)code;
					}
					else
					// If the code does not represent a single character, Use Dictionary We Created to find its value
					{
						// add the decoded combination to the decoded message
						decodedMessage += map.get(code);	
					}
					//reset the currentCode after each individual code has been decoded
					currentCode = "";
				}
				else
				{
					// Add Character to Current Code
					currentCode += thisCharacter;
				}
			}
			
			// Print Decoded Message to File
			for ( int counter = 0; counter < decodedMessage.length (); counter++ )
			{
				pw.print ( decodedMessage.charAt ( counter ) );
			}
			
			// close the print writer
			pw.close();
		}
		
		// Catch for Errors
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}
