/*
 * Program for trouble ticketing system.
 * Trouble ticket (sometimes called a trouble report) is a mechanism used in an organization to create, track, report, and resolute technical and operational issues.
 * JFrame JAVA Swing.
 * File name : SqliteConnection.java
 */

package troubleTicketSystem;

/**
 * @author Abhijeet
 */

//package imports for the program
import java.sql.*;
import javax.swing.*;

public class SqliteConnection {

	// sqlite connection
	Connection conn = null;
	public static Connection dbConnector(){
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Abhijeet\\workspace\\troubleTicketSystem\\src\\troubleTicketSystem\\TTSdb.sqlite");
			// JOptionPane.showMessageDialog(null, "Connection successfull");
			return conn; 
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}