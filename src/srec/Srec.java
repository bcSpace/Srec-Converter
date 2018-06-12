package srec;

import java.util.ArrayList;
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
		String label = line.substring(0,2); //record type
		String data = line.substring(add.get(label), line.length()-2); //data
		byte[] bytes = DatatypeConverter.parseHexBinary(data);
		String result = new String(bytes);
		return result;
	}
	
	public byte[] getBytes(String lines[]) {
		Rec[] recs = makeRecs(lines);

		int totalBytes = recs[recs.length-1].addressStart+recs[recs.length-1].data.length;
		byte[] bytes = new byte[totalBytes]; 
		
		int currentCount = 0;
		for(Rec rec : recs) {
			byte[] data = rec.data;
			for(int i = currentCount; i < data.length+currentCount; i++) 
				bytes[i] = data[i-currentCount]; 
			
			currentCount+=data.length;
		}
		return bytes;
	}
	
	private Rec[] makeRecs(String lines[]) {
		ArrayList<Rec> dataRecs = new ArrayList<>();
		
		int nextAdd=-1;
		for(int i = lines.length-1; i >= 0; i--) {
			//get the data needed
			String line = lines[i];
			String label = line.substring(0,2); //record type
			int addressStart = Integer.parseInt(line.substring(4, add.get(label)), 16); 

			//last line will be a Termination record so its okay to continue
			if(nextAdd == -1) {
				nextAdd = addressStart;
				continue;
			}
			
			//skip and skip parsing if it is not a data record
			if(exclude.contains(label)) continue;
			
			byte[] bytes = DatatypeConverter.parseHexBinary(line.substring(add.get(label), line.length()-2));
			
			dataRecs.add(new Rec(addressStart, nextAdd, bytes)); 
			nextAdd = addressStart;
		}
		
		return flipRec(dataRecs.toArray(new Rec[dataRecs.size()]));
	}
	
	private Rec[] flipRec(Rec[] rec) {
		for(int i = 0; i < rec.length/2; i++) {
			Rec temp = rec[i]; 
			rec[i] = rec[rec.length-i-1];
			rec[rec.length-i-1] = temp;
		}
		return rec;
	}
	
}
