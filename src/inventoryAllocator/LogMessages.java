package inventoryAllocator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogMessages{
	
	private static Logger logger = Logger.getLogger("MyLog"); 
	private static FileHandler fh;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Date date = new Date();
	
	static {
		try{
			fh = new FileHandler("Log.txt");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			Handler conHdlr = new ConsoleHandler();
		    conHdlr.setFormatter(new Formatter() {
		      public String format(LogRecord record) {
		        return dateFormat.format(date) + " :" + record.getLevel() + " : "
		             + record.getMessage() + "\n";
		      }
		    });
		    logger.addHandler(conHdlr);
		    fh.setFormatter(new SimpleFormatter());
	        System.out.println("Static block executed");
		} catch (IOException io){
			
		}
	}
	
	public static void printMessage(String theMessage) throws IOException {
		logger.info(theMessage); 
	}
	
	public static void printWithOutFormatting(String theMessage) throws IOException {
		Handler conHdlr = new ConsoleHandler();
		conHdlr.setFormatter(new Formatter() {
		      public String format(LogRecord record) {
		        return record.getMessage();
		      }
		    });
		    logger.addHandler(conHdlr);
		logger.info(theMessage); 
	}
	
	public static void printWithOutFormatting(Long number) throws IOException {
		Handler conHdlr = new ConsoleHandler();
		conHdlr.setFormatter(new Formatter() {
		      public String format(LogRecord record) {
		        return record.getMessage();
		      }
		    });
		    logger.addHandler(conHdlr);
		logger.info(number.toString()); 
	}


	
	public static void main (String args[]) throws IOException{
		LogMessages.printMessage("This is my test message");
		LogMessages.printMessage("This is my another test message");		
	}


	public static void printMessage(Long itemCount) throws IOException {
		logger.info(itemCount.toString()); 
		
	}
	
	
	
	
	
	
	
	
	
	
}
