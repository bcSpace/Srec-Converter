package srec;

import java.util.HashMap;
import java.util.HashSet;

import javax.xml.bind.DatatypeConverter;

public class Srec {
	
	private final HashMap<String, Integer> add;
	private final HashSet<String> exclude;
	
	public Srec() {
		
		//used for conversion to binary
		exclude = new HashSet<String>();
		exclude.add("S0"); //header ignore
		exclude.add("S5"); //non data field ignore
		exclude.add("S6"); //non data field ignore
		exclude.add("S7"); //non data field ignore
		exclude.add("S8"); //non data field ignore
		exclude.add("S9"); //non data field ignore
		
		//S4 seems to be unused
		add = new HashMap<String, Integer>(); //used to determine size of address field	
		add.put("S0", 8);
		add.put("S1", 8);
		add.put("S5", 8);
		add.put("S9", 8);
		
		add.put("S2", 10);
		add.put("S6", 10);
		
		add.put("S3", 12);
		add.put("S7", 12);
	}
	
	public String decodeForDisplay(String s[]) {
		String output = "";
		for(String ss : s) output+=decodeLine(ss)+"\n";
		return output;
	}
	
	private String decodeLine(String line) {
		String label = line.substring(0,2);
		String data = line.substring(add.get(label), line.length()-2);
		byte[] bytes = DatatypeConverter.parseHexBinary(data);
		String result = new String(bytes);
		return result;
	}
	
	public String getRawDataLine(String line) {
		String label = line.substring(0,2);
		if(exclude.contains(label)) return ""; //skip non-data lines
		String data = line.substring(add.get(label), line.length()-2);
		return data.toLowerCase(); //seems that binary output only uses lower case
	}
}
