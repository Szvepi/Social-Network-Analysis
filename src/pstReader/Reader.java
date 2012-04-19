package pstReader;

import java.util.*;

/**
 * This class is the PSTReader's super class.
 *
 * @version  0.1
 * @author   Istvan Fodor
 */
public abstract class Reader {
	
	/** read the email's from file and put to list */
	public abstract void read(Vector<String> filename);
		
	/** get the email's subject*/
	public abstract String getSubject(int number);
		
	/** get the email's sender */
	public abstract String getSender(int number);
		
	/** get the email's receiver */
	public abstract Vector<String> getReceived(int number);
		
	/** get the email's sending time */
	public abstract Date getTime(int number);
		
	/** get the email's body */
	public abstract String getBody(int number);
		
	/** list size */
	public abstract int listSize();
	
	/** list is empty? */
	public abstract boolean isEmpty();
	
	/** get the list's element */
	public abstract Object getEmail(int number);
	
	/** get names of recipient */
	public abstract Object getRecipient(int number);
	
	/** upload the list */
	public abstract void processFolder(Object folder);

}
