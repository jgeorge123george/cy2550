import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class xkcdpwgen {

	String WORD_FILE_NAME = "words.txt";
	ArrayList<String> words;

	public static void main(String[] args) {




		boolean HELP = false;
		int WORDS = 4;
		int CAPS = 0;
		int NUMBERS = 0;
		int SYMBOLS = 0;


		ArrayList<String> arraylistWordsForPassword = new ArrayList<String>();

		xkcdpwgen passwordGen = new xkcdpwgen();


		int argumentCount = args.length;
		ArrayList<String> agmtsList = new ArrayList<String>();
		boolean praseArgs = false;
		if (argumentCount < 0) {

		}else {
			for(int i=0; i < argumentCount; i++) {
				agmtsList.add(args[i]);
			}
			praseArgs = true;
		}

		if(praseArgs) {
		    try {

		    	if(
		    			(agmtsList.contains("-h") && agmtsList.contains("--help") ) ||
		    			(agmtsList.contains("-w") && agmtsList.contains("--words") ) ||
		    			(agmtsList.contains("-c") && agmtsList.contains("--caps") ) ||
		    			(agmtsList.contains("-n") && agmtsList.contains("--numbers") ) ||
		    			(agmtsList.contains("-s") && agmtsList.contains("--symbols") ) ) {
		    		throw new Exception("Duplicate arguments");
		    	}


		    	Iterator<String> agmtsItr = agmtsList.iterator();
		    	while(agmtsItr.hasNext()) {
		    		String option = agmtsItr.next();
		    		option = option.toLowerCase();
		    		switch(option) {
		    		    case "-h":
		    		    	HELP = true;
		    		    	break;
		    		    case "--help":
		    		    	HELP = true;
		    		    	break;
		    			case "-w" :
		    				WORDS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "--words" :
		    				WORDS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "-c" :
		    				CAPS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "--caps" :
		    				CAPS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "-n" :
		    				NUMBERS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "--numbers" :
		    				NUMBERS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "-s" :
		    				SYMBOLS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			case "--symbols" :
		    				SYMBOLS =  Integer.parseInt(agmtsItr.next());
		    				break;
		    			default:
		    				throw new Exception("Invalid Option");

		    		}
		    	}

		    } catch (Exception e) {
		    	//System.out.println(e.getMessage());
		    	passwordGen.help();
		        System.exit(0);
		    }

		}

		if(HELP) {
			passwordGen.help();
		}else {
			passwordGen.words = passwordGen.loadWordsFromFile();
			for(int i=0; i < WORDS; i++) {
				String wordForPassword = passwordGen.getRandomWord();
				if(i < CAPS) {
					wordForPassword = wordForPassword.substring(0, 1).toUpperCase() + wordForPassword.substring(1);
				}
				arraylistWordsForPassword.add(wordForPassword);
			}

			System.out.println(passwordGen.generatePassword(arraylistWordsForPassword,SYMBOLS,NUMBERS));
		}

	}

	private String generatePassword(ArrayList<String> arraylistWordsForPassword,int SYMBOLS,int NUMBERS) {
		String password = "";
		for(String word : arraylistWordsForPassword) {
			password += word;
		}

		//password = password.toLowerCase();

		if(SYMBOLS > 0) {
			Random random = new Random();
			String specialChars = "~?!-=+_@#$%^&*/<>";
			for(int i=0; i < SYMBOLS; i++) {
				int index = random.nextInt(specialChars.length());
				char splChar = specialChars.charAt(index);
				StringBuilder passwordStringBuilder = new StringBuilder(password);
				passwordStringBuilder.insert(random.nextInt(password.length()), splChar);
				password = passwordStringBuilder.toString();
			}
		}

		if(NUMBERS > 0) {
			Random random = new Random();
			String numberChars = "0123456789";
			for(int i=0; i < NUMBERS; i++) {
				int index = random.nextInt(numberChars.length());
				char splChar = numberChars.charAt(index);
				StringBuilder passwordStringBuilder = new StringBuilder(password);
				passwordStringBuilder.insert(random.nextInt(password.length()), splChar);
				password = passwordStringBuilder.toString();
			}
		}

		return password;
	}


	private  void help() {
		StringBuffer helpStringBuffer = new StringBuffer();
		helpStringBuffer.append("usage: xkcdpwgen [-h] [-w WORDS] [-c CAPS] [-n NUMBERS] [-s SYMBOLS]");
		helpStringBuffer.append("\n");
		helpStringBuffer.append("\nGenerate a secure, memorable password using the XKCD method");
		helpStringBuffer.append("\n");
		helpStringBuffer.append("\noptional arguments:");
		helpStringBuffer.append("\n\t-h, --help            show this help message and exit");
		helpStringBuffer.append("\n\t-w WORDS, --words WORDS");
		helpStringBuffer.append("\n\t\tinclude WORDS words in the password (default=4)");
		helpStringBuffer.append("\n\t-c CAPS, --caps CAPS  capitalize the first letter of CAPS random words");
		helpStringBuffer.append("\n\t\t\t\t(default=0)");
		helpStringBuffer.append("\n\t-n NUMBERS, --numbers NUMBERS");
		helpStringBuffer.append("\n\t\t\t\tinsert NUMBERS random numbers in the password");
		helpStringBuffer.append("\n\t\t\t\t(default=0)");
		helpStringBuffer.append("\n\t-s SYMBOLS, --symbols SYMBOLS");
		helpStringBuffer.append("\n\t\t\t\tinsert SYMBOLS random symbols in the password");
		helpStringBuffer.append("\n\t\t\t\t(default=0)");
		System.out.println(helpStringBuffer.toString());
	}


	private  ArrayList<String> loadWordsFromFile(){
		ArrayList<String> words = new ArrayList<String>();
		try {
			FileReader wordFileReader  = new FileReader(WORD_FILE_NAME);
			BufferedReader fileBufferReader = new BufferedReader(wordFileReader);
			while(fileBufferReader.readLine() != null) {
				words.add(fileBufferReader.readLine());
			}
			fileBufferReader.close();
		} catch (Exception e) {
			System.out.println("Not able to read " + WORD_FILE_NAME );
			System.exit(1);
		}
		return words;
	}

	private String getRandomWord() {
	    Random random = new Random();
		int wordIndex = random.nextInt(words.size()) + 0;
		return words.get(wordIndex).toLowerCase();
	}



}
