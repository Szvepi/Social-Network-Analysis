package pstReader;

import java.util.*;

import com.pff.*;

/**
 * Read datas from .pst file.
 *
 * @version  0.2
 * @author   Istvan Fodor
 */
public class PSTReader extends Reader {
	
		private PSTFile pstFile;
		private List<PSTMessage> emails;
		private Vector<PSTFolder> childFolders;
		
		/**
		 * Initialized new array list
		 */
		public PSTReader() {
			emails = new ArrayList<PSTMessage>();
		}
	
		/** 
		 * read the email's from files and put to list 
		 */
		public void read(Vector<String> fileName) {
			try {
				for ( int i=0; i < fileName.size(); i++) {
					pstFile = new PSTFile(fileName.elementAt(i));
					processFolder(pstFile.getRootFolder());
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		
		/** 
		 * read the email's from file and put to list
		 * @param fileName
		 */
		public void read(String fileName) {
			try {
				pstFile = new PSTFile(fileName);
				processFolder(pstFile.getRootFolder());
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
			
		/** 
		 * get the email's subject
		 */
		public String getSubject(int number) {
			return emails.get(number).getSubject();
		}
		
		/** 
		 * return the sender's email address
		 * @param number
		 * @return
		 */
		public String getSenderEmail(int number) {
			return emails.get(number).getSenderEmailAddress();
		}
			
		/** 
		 * get the email's sender
		 */
		public String getSender(int number) {
			return emails.get(number).getSenderName();
		}
			
		/** 
		 * get the email's receiver
		 */
		public Vector<String> getReceived(int number) {
			Vector<String> cimzettek = new Vector<String>();
			try {
				for (int i = 0; i< getEmail(number).getNumberOfRecipients(); i++) {
					cimzettek.add(getEmail(number).getRecipient(i).getDisplayName());
				}
				
			} catch (Exception e)
			{
				System.out.println("Hiba a getReceived-nél");
				e.printStackTrace();
			}
			return cimzettek;
		}
			
		/**  
		 * get the email's sending time
		 */
		public Date getTime(int number) {
			return emails.get(number).getMessageDeliveryTime();
		}
			
		/** 
		 * get the email's body 
		 */
		public String getBody(int number) {
			return emails.get(number).getBody();
		}
			
		/**  
		 * return list size
		 */
		public int listSize() {
			return emails.size();
		}
		
		/** 
		 * list is empty? 
		 */
		public boolean isEmpty() {
			return emails.isEmpty();
		}
		
		/** 
		 * get the list's element
		 */
		public PSTMessage getEmail(int number) {
			return emails.get(number);
		}
		
		/** 
		 * get names of recipient 
		 */
		public Vector<String> getRecipient(int number) {
			Vector<String> cimzettek = new Vector<String>();
			try {
				for (int i = 0; i < getEmail(number).getNumberOfRecipients(); i++) {
					cimzettek.add(getEmail(number).getRecipient(i).getEmailAddress());
				}
				
			} catch (Exception e) {
				System.out.println("Hiba a getRecipient-nél");
				e.printStackTrace();
			}
			return cimzettek;
		}
		
		/**
		 *	upload the list 
		 */
		public void processFolder(Object folders) 
		{
			try {
			PSTFolder folder = (PSTFolder)folders;
			{
				// go through the folders...
				if (folder.hasSubfolders()) {
					childFolders = folder.getSubFolders();
					for (PSTFolder childFolder : childFolders) {
						processFolder(childFolder);
					}
				}
				
				//
				if (folder.getContentCount() > 0) {
					PSTMessage email = (PSTMessage)folder.getNextChild();
					while ( email != null) {
						emails.add(email);
						email = (PSTMessage)folder.getNextChild();
					}
				}
			}
			} catch (PSTException PstEx) {
				PstEx.printStackTrace();
			} catch (java.io.IOException IOEx) {
				IOEx.printStackTrace();
			}
		}

}
