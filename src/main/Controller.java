package main;

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
		String[] sourceData = FileUtil.loadData(sourcePath);
		FileUtil.write(writePath, srec.getBytes(sourceData));
		return srec.decodeForDisplay(sourceData);
	}
	
}
