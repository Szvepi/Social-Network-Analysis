package database;

import java.sql.*;
import java.util.*;
import java.util.Date;

import pstReader.PSTReader;

public class EmailDB {
	private String USER = "username";
	private String PASS = "password";
	private static int id = 0;
	private String strUrl = "jdbc:derby:Graph";
	   
	Connection dbConnection = null;
	
	public EmailDB(PSTReader emailList) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
						
			Properties props = new Properties();
			props.put("create", "true");
			props.put("user", USER);
			props.put("password", PASS);

			dbConnection = DriverManager.getConnection(strUrl, props);
			createTable();
			insertData(emailList);
			selectData();
		} catch (SQLException e) {
			System.out.println("SQL Hiba:");
			e.printStackTrace();
		} catch ( ClassNotFoundException e ) {
			System.out.println("ClassNotFound Hiba:");
			e.printStackTrace();
		} finally{
		      //finally block used to close resources
		      try{
		         if(dbConnection!=null)
		        	 dbConnection.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		}
	}
	
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
			System.out.println("SQL Hiba:");
			e.printStackTrace();
		}
	}
	
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
	        															"', 'subject', " +
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
			System.out.println("SQL Hiba!");
			//e.printStackTrace();
		}
		
	}
	
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
			System.out.println("SQL Hiba:");
			e.printStackTrace();
		} catch ( Exception e ) {
			System.out.println("ClassNotFound Hiba:");
			e.printStackTrace();
		} finally{
		      //finally block used to close resources
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		}
	}
}
