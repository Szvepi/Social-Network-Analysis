package database;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.sql.*;
import java.util.*;
import java.util.Date;

import pstReader.PSTReader;

/**
 * This class create the new database and upload email's data.
 *
 * @version  0.4
 * @author   Istvan Fodor
 */
public class EmailDB {
	static final Logger logger = Logger.getLogger(EmailDB.class);
	
	private String USER = "username";
	private String PASS = "password";
	private static int id = 0;
	private String strUrl = "jdbc:derby:Graph";
	   
	Connection dbConnection = null;
	
	/**
	 * If the database is not exist, It is create it, then it insert data.
	 * @param emailList
	 */
	public EmailDB(PSTReader emailList) {
		BasicConfigurator.configure();
		
		Properties props2 = new Properties();
		Properties props = new Properties();
		props.put("user", USER);
		props.put("password", PASS);
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			
			dbConnection = DriverManager.getConnection(strUrl, props);

		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( ClassNotFoundException e ) {
			//System.out.println("ClassNotFound Hiba:");
			logger.error(e.getMessage());
		} finally {
			props2.put("create", "true");
			props2.put("user", USER);
			props2.put("password", PASS);
		}
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");			
			dbConnection = DriverManager.getConnection(strUrl, props);
			createTable();
			insertData(emailList);
			selectData();
		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( ClassNotFoundException e ) {
			//System.out.println("ClassNotFound Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally{
		      //finally block used to close resources
		      try{
		         if(dbConnection!=null)
		        	 dbConnection.close();
		      }catch(SQLException se2){
		    	  logger.error(se2.getMessage());
		      }// nothing we can do
		}
	}
	
	/**
	 * If the database is not exist, It is create it, then it insert data.
	 * It filters data.
	 * @param emailList
	 */
	public EmailDB(PSTReader emailList, Date from, Date to) {
		
		Properties props2 = new Properties();
		Properties props = new Properties();
		props.put("user", USER);
		props.put("password", PASS);
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			
			dbConnection = DriverManager.getConnection(strUrl, props);

		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( ClassNotFoundException e ) {
			//System.out.println("ClassNotFound Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			props2.put("create", "true");
			props2.put("user", USER);
			props2.put("password", PASS);
		}
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");			
			dbConnection = DriverManager.getConnection(strUrl, props);
			if (from.before(to)) {
				createTable();
				insertData(emailList, from, to);
				selectData();
			}
		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( ClassNotFoundException e ) {
			//System.out.println("ClassNotFound Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally{
		      //finally block used to close resources
		      try{
		         if(dbConnection!=null)
		        	 dbConnection.close();
		      }catch(SQLException se2){
		    	  logger.error(se2.getMessage());
		      }// nothing we can do
		}
	}
	
	/**
	 * This function create the new table
	 */
	private void createTable() {
		Statement statement = null;
		String dropTable = "Drop table EMAILS";
		String strCreateTable = "CREATE TABLE EMAILS " +
                					"(id INTEGER not NULL, " +
                					" senderEmail VARCHAR(100), " + 
                					" senderName VARCHAR(50)," +
                					" receiverEmail VARCHAR(100), " +
                					" receiverName VARCHAR(50)," +
                					" emailSubject VARCHAR(300)," +
                					" sendingTime VARCHAR(50)," +
                					" PRIMARY KEY ( id ))"; 
			
		try {
			statement = dbConnection.createStatement();
			try {
				statement.execute(dropTable);
			} catch ( SQLException sql) {
			} finally {
				statement.execute(strCreateTable);
			}
			if (statement!=null) {
				statement.close();
			}
		} catch (SQLException e ) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * This function insert data to table
	 * @param List
	 */
	private void insertData(PSTReader List) {
		Statement statement = null;
			
		try {
			statement = dbConnection.createStatement();
			
			for ( int i=0;i<List.listSize();i++) {
				String time = List.getTime(i).getYear()+1900 + "-" + List.getTime(i).getMonth() + "-" +
						List.getTime(i).getDay() + " " + List.getTime(i).getHours() + ":" + 
						List.getTime(i).getMinutes() + ":" + List.getTime(i).getSeconds();
				Vector<String> receiver = new Vector<String>();
	        	Vector<String> recName = new Vector<String>();
	        	receiver = List.getRecipient(i);
	        	recName = List.getReceived(i);
	        	for ( int j=0; j < receiver.size(); ++j) {
	        		String strInsertTable = "insert into EMAILS values( " + id + ", '" + List.getSenderEmail(i) + 
	        															"', '" + List.getSender(i) + 
	        															"', '" + receiver.elementAt(j) + 
	        															"', '" + recName.elementAt(j) + 
	        															"', '"+ List.getSubject(i) +"', " +
	        															"'" + time + "')";
	        		statement.addBatch(strInsertTable);
	        		++id;
	        	}
			}		
			statement.executeBatch();

			if (statement!=null) {
				statement.close();
			}
		} catch (SQLException e ) {
			//System.out.println("SQL Hiba!");
			logger.error(e.getMessage());
			//e.printStackTrace();
		}
		id = 0;
	}
	
	/**
	 * This function insert data to table
	 * @param List
	 */
	private void insertData(PSTReader List, Date from, Date to) {
		Statement statement = null;
			
		try {
			statement = dbConnection.createStatement();
			
			for ( int i=0;i<List.listSize();i++) {
				if (from.before(List.getTime(i))) {
					if (List.getTime(i).before(to)) {
						String time = List.getTime(i).getYear()+1900 + "-" + List.getTime(i).getMonth() + "-" +
								List.getTime(i).getDay() + " " + List.getTime(i).getHours() + ":" + 
								List.getTime(i).getMinutes() + ":" + List.getTime(i).getSeconds();
						Vector<String> receiver = new Vector<String>();
						Vector<String> recName = new Vector<String>();
						receiver = List.getRecipient(i);
						recName = List.getReceived(i);
						for ( int j=0; j < receiver.size(); ++j) {
							String strInsertTable = "insert into EMAILS values( " + id + ", '" + List.getSenderEmail(i) + 
	        															"', '" + List.getSender(i) + 
	        															"', '" + receiver.elementAt(j) + 
	        															"', '" + recName.elementAt(j) + 
	        															"', '"+ List.getSubject(i) +"', " +
	        															"'" + time + "')";
							statement.addBatch(strInsertTable);
							++id;
						}
					}
				}
			}		
			statement.executeBatch();

			if (statement!=null) {
				statement.close();
			}
		} catch (SQLException e ) {
			//System.out.println("SQL Hiba!");
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		id = 0;
	}
	
	/**
	 * This function retrieves data from database and print to consol
	 */
	public void selectData() {
		Connection conn = null;
		Statement statement = null;
		
		Properties props = new Properties();
		props.put("create", "true");
		props.put("user", USER);
		props.put("password", PASS);
		
		try {
			conn = DriverManager.getConnection(strUrl, props);
			
			statement = conn.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM Emails");
			while (rs.next()) {
				int p = rs.getInt(1);
				String q = rs.getString(2);
				String k = rs.getString(3);
				String l = rs.getString(4);
				String m = rs.getString(5);
				String n = rs.getString(6);
				String date = rs.getString(7);
				System.out.print(p + "    " );
				System.out.print(q + "    " );
				System.out.print(k + "    " );
				System.out.print(l + "    " );
				System.out.print(m + "    " );
				System.out.print(n + "    " );
				System.out.println(date);
			}
			if (statement!=null) {
				statement.close();
			}
		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( Exception e ) {
			//System.out.println("ClassNotFound Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally{
		      //finally block used to close resources
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se2){
		    	  logger.error(se2.getMessage());
		      }// nothing we can do
		}
	}
	
	/**
	 * Get all data from table
	 * @return Vector<Object>
	 */
	public Vector<Object> getData() {
		Connection conn = null;
		Statement statement = null;
		Vector<Object> datas = new Vector<Object>();
		
		
		Properties props = new Properties();
		props.put("create", "true");
		props.put("user", USER);
		props.put("password", PASS);
		
		try {
			conn = DriverManager.getConnection(strUrl, props);
			
			statement = conn.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM Emails");
			int i=0;
			while (rs.next()) {
				int p = rs.getInt(1);
				String q = rs.getString(2);
				String k = rs.getString(3);
				String l = rs.getString(4);
				String m = rs.getString(5);
				String n = rs.getString(6);
				String date = rs.getString(7);
				datas.add(p);
				datas.add(q);
				datas.add(k);
				datas.add(l);
				datas.add(m);
				datas.add(n);
				datas.add(date);
				i++;
			}
			if (statement!=null) {
				statement.close();
			}
		} catch (SQLException e) {
			//System.out.println("SQL Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} catch ( Exception e ) {
			//System.out.println("ClassNotFound Hiba:");
			//e.printStackTrace();
			logger.error(e.getMessage());
		} finally{
		      //finally block used to close resources
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se2){
		    	  logger.error(se2.getMessage());
		      }// nothing we can do
		}
		return datas;
	}
	
	/**
	 * Get the table's column name
	 * @return Vector<String>
	 */
	public Vector<String> getColumnName() {
		Vector <String> columnName = new Vector<String>();
		columnName.add("id");
		columnName.add("senderEmail");
		columnName.add("senderName");
		columnName.add("receiverEmail");
		columnName.add("receiverName");
		columnName.add("emailSubject");
		columnName.add("sendingTime");
		return columnName;
	}
	
	/**
	 * write data from database to file
	 * @param fileName
	 */
	public void writeData(File fileName) {
		PrintWriter pw;
		try {
			pw = new PrintWriter( new FileWriter(fileName) );
			for ( int i=0; i< getData().size(); i++ ) {
				if (i == 0) {
					pw.print(getData().elementAt(i) + "     ");
				}
				else {				
					if (i%7 == 6) {
						pw.println(getData().elementAt(i));
					}
					else {
						pw.print(getData().elementAt(i) + "     ");
					}
				}
			}
    		if (pw != null) {
    			pw.close();
    		}
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		} catch (Exception exc) {
			logger.error(exc.getMessage());
		}
	}
}
