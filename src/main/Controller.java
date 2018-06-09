package main;

import java.util.ArrayList;

import error.CustomError;
import srec.Srec;
import util.FileUtil;

public class Controller {
	
	private final Srec srec;
	
	public Controller() {
		srec = new Srec();
	}
	
	public String run(String sourcePath, String writePath) 
		throws CustomError {
		
		String sourceData[] = FileUtil.loadData(sourcePath); //load data, each array index is a line
		ArrayList<String> rawData = new ArrayList<String>(); //raw encoded data
		
		for(String s : sourceData) {
			String decodedLine = srec.getRawDataLine(s);
			if(decodedLine.length()>0)rawData.add(decodedLine); //"" is returned if no data is pulled
		}
		
		if(rawData.size() == 0) throw new CustomError("No data"); 
		
		//filling, seems that there are a allocation of 50 chars per line, if the line does not use them all it should be filled with NULLs "00"
		for(int i = 0; i < rawData.size()-1; i++) {
			int currentLength = rawData.get(i).length();
			int needToFill = 100 - currentLength; 
			for(int j = 0; j < needToFill; j++) rawData.set(i, rawData.get(i)+"0"); //assume even number fill
		}
		
		//combine all the raw lines into a big string, so it can be chunked and lined
		String endContent = "";
		for(String ss : rawData) endContent+=ss; 
		endContent = this.formatOutput(endContent);
		
		//finally write the data
		FileUtil.write(endContent, writePath); 
		
		//return the deEncoded source for display 
		return srec.decodeForDisplay(sourceData);
	}
	
	private String formatOutput(String content) {
		ArrayList<String> chunks = new ArrayList<String>();
		getChunks(content, chunks); //recursion, group into 4's
		
		ArrayList<String> lines = new ArrayList<String>();
		getLines(chunks, lines); //recursion, group the chunks into lines of 8 chunks
		
		//throw all the lines together
		String end = "";
		for(int i = 0; i < lines.size()-1; i++) end+=lines.get(i)+"\n";
		end+=lines.get(lines.size()-1);
		
		return end;
	}
	
	private void getChunks(String source, ArrayList<String> chunks) {
		if(source.length()==0||source==null) return; //exit
		if(source.length() <= 4) { chunks.add(source); return;} //exit
		chunks.add(source.substring(0, 4));
		getChunks(source.substring(4), chunks);
	}
	
	private void getLines(ArrayList<String> chunks, ArrayList<String> lines) {
		if(chunks.size() == 0 || chunks == null) return;
		
		int size = 8;
		if(chunks.size() < 8) size = chunks.size();
		
		String line = "";
		for(int i = 0; i < size; i++) {
			line+=chunks.get(0)+" ";
			chunks.remove(0);
		}
		lines.add(line.substring(0, line.length()-1)); //remove the last space
		if(size < 8) return; //exit here if the size was changed
		getLines(chunks, lines);
	}
}
