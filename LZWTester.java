import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class LZWTester 
{
	public static void main (String [] args) throws IOException
	{
		Encoder en = new Encoder ();
		Scanner scanner = new Scanner (System.in) ;
		String fileName; 

		System.out.println ("Enter the file you wish to decode: ");
		fileName = scanner.nextLine (); 

		en.encode(fileName);
		System.out.println ("encoding finished"); 
		
		en.decode();
		System.out.println ("decoding finished"); 
	}
}
