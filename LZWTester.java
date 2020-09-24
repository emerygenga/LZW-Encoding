import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LZWTester {
	public static void main (String [] args) throws IOException
	{
		Encoder en = new Encoder ();
		en.encode("lzw-file1.txt");
		
		en.check();
	}
}
