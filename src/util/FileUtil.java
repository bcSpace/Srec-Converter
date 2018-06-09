package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import error.CustomError;

public class FileUtil {
	
	public static String[] loadData(String path) 
		throws CustomError {
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			fileReader.close();
		} catch (IOException e) {
			throw new CustomError("Failure to load file: " + e);
		}
		
		String a[] = new String[lines.size()];
		a = lines.toArray(a);
		return a;
	}
	
	public static void write(String cotents, String path) 
			throws CustomError {
		
		//in case the dirs don't exist
		File file = new File(path);
		file = file.getParentFile();
		if(!file.exists()) file.mkdirs();
		
		try {
			new FileOutputStream(path).write(cotents.getBytes());
		} catch(Exception e) {
			throw new CustomError("Failure to write file: " + e);
		}
	}

}
