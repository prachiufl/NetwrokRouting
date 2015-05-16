

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BinaryConversion {

	public static ArrayList<String> convertIP(File input) throws IOException {
		/*Reads the input file line by line skipping empty lines and call processIP to convert IP to binary*/
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
		ArrayList<String> result = new ArrayList<String>();

		while((line = reader.readLine() )!= null)
		{
			if("".equals(line))
				continue;
			result.add(processIP(line)) ;
		}
		reader.close();
		return result;
	}

	public static String processIP(String ip) {
		/*Splits the line with . and converts all four parts to corresponding 8 bit binary number.
		 *  Thus a 32 bit binary IP is returned*/
		StringBuilder bStringBuilder = new StringBuilder();
		String ipParts[] = ip.split("\\.");

		for (String ipPart : ipParts) {

			String binString = Integer.toBinaryString(Integer.parseInt(ipPart));
			int length = 8 - binString.length();
			char[] padArray = new char[length];
			Arrays.fill(padArray, '0');
			bStringBuilder.append(padArray).append(binString);
		}
		return bStringBuilder.toString();
	}


}
