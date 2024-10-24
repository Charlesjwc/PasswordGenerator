package utils;

import java.util.*;

public class FileUtil {
	//	Filepath to info on accounts
	public static final String ACCOUNTS = "../files/accountInfos.txt";
	
	//	Ensure that only one fileUtil gets instanciated
	public static FileUtil fileUtil = null;
	
	//	Private constructor to prevent duplicates
	private FileUtil() {}
	
	/**	Initializes singleton object if it doesn't already exist	*/
	public static void init() {
		if (fileUtil == null)
			fileUtil = new FileUtil();
		else
			System.out.println("FileUtil already exists");
	}
}

