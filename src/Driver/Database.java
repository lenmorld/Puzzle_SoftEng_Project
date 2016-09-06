package Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;
import components.Puzzle;

/**
 * 
 * Database class
 * 
 * includes static methods and attributes for
 * saving puzzles to database from PuzzleMaker
 * and loading puzzles from database  to PuzzleSolver
 * 
 * @author team t
 *
 */
public class Database {
	
	//define attributes of puzzle to be included to the database table
	
	private static Puzzle puzzle;
	private static boolean[][] hints;
	private static String hints_str;
	
	
	private static int id;
	private static String name;
	private static int rows;
	private static int cols;
	private static int complexity;
	
	private static String[] headerLeft;
	private static String[] headerTop;
	private static String hleft;
	private static String htop;
	
	private static String pRow;		//row priorities array
	private static String pCol;		//col priorities array
	

	/**
	 * @return a puzzle object configured from DB
	 */
	public static Puzzle loadPuzzle(int index) {
		
		Connection connection = null;
		
		//get puzzle by random
		String sql = "SELECT * FROM SavedPuzzle WHERE ID = " + index + ";";
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:Puzzle.db");
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setQueryTimeout(30); //set timeout to 30 sec.
			
			ResultSet rs = statement.executeQuery();
			
				
			// while there's a next matching record
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				complexity = rs.getInt("complexity");
				rows = rs.getInt("rows");
				cols = rs.getInt("cols");
				hleft = rs.getString("headerleft");
				htop = rs.getString("headertop");
				hints_str = rs.getString("hints");		
				
				//-------------- US4 ---------------
				pRow = rs.getString("PriorityRow");
				pCol = rs.getString("PriorityCol");
				//----------------------------------
			}
			
			//parse headers from string to an array
			headerLeft = hleft.split(";");		
			headerTop = htop.split(";");
			
			//parse hints string to seperate rows
			hints = new boolean[rows][cols];
			
			//init. hints array
			String[] hints_temp;
			
			//if no hints
			if (hints_str.length() == 0)
				hints_temp = null;
			else
				hints_temp = hints_str.split(";");
			
			// init. hints array
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					hints[i][j] = false;
				}
			}
			
			
			if (hints_temp != null)	// if no hints
			{
				//parse rows hints array to individual tiles 
				for (int i = 0; i < hints_temp.length;i++) {
					String[] coordinate = hints_temp[i].split(",");
					int crow = Integer.parseInt(coordinate[0]);
					int ccol = Integer.parseInt(coordinate[1]);
					hints[crow][ccol] = true;
				}
			}
			
			//create puzzle based on obtained attributes
			puzzle = new Puzzle(rows, cols, headerLeft, headerTop, hints, pRow, pCol);
			
			rs.close();				//close DB connection
			statement.close();
			connection.close();
				
		} catch (Exception e) {		//catch exception
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		finally {		//close DB connection if exceptions happen
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		System.out.println("Puzzle loaded from database successfully");
		return puzzle;
		
	}
	
	
	/**
	 * save puzzle to database
	 * 
	 * @param puzzle puzzle object
	 */
	public static void savePuzzle(Puzzle puzzle) {
		
		//setup SQL connection and SQLite prepared statements
		
		Connection connection = null;

		//prepare SQL statement
		String sql = "INSERT INTO SavedPuzzle (COMPLEXITY, ROWS, COLS, HeaderLeft, HeaderTop, HINTS, PriorityRow, PriorityCol) " +
				"VALUES (?,?,?,?,?,?,?,?)";
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:Puzzle.db");
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setQueryTimeout(30); //set timeout to 30 sec.
			
			//get hints array
			hints = puzzle.getPuzzleHints();
			
			//get num. of rows and columns
			rows = puzzle.getPuzzleSolution().length;
			cols = puzzle.getPuzzleSolution()[0].length;
			
			//get the header arrays
			headerLeft = puzzle.getHeaderLeft();
			headerTop = puzzle.getHeaderTop();
			
			//convert from array to string -------------------
			
			hleft = headerLeft[0];
			htop = headerTop[0];
			
			complexity = 0;
			hints_str = "";
			
			for (int i = 1; i < rows; i++) {
				hleft += ";" + headerLeft[i];	
			}
			
			for (int i = 1; i < cols; i++) {
				htop += ";" + headerTop[i];
			}
			
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++) {
					if (hints[i][j]) {
						complexity++;
						hints_str += i + "," + j + ";";
					}
				}
			
			/*********************/
			//row and column priorities
			
			pRow= "";
			
			for (int i=0; i<rows; i++) {
				pRow += (puzzle.getRowPriority(i) + " " );
			}
			
			pCol= "";
			
			for (int i=0; i<cols; i++) {
				pCol += (puzzle.getColPriority(i) + " " );
			}		
			/*******************/
			
			statement.setInt(1, complexity);
			statement.setInt(2, rows);
			statement.setInt(3, cols);
			statement.setString(4, hleft);
			statement.setString(5, htop);
			statement.setString(6, hints_str);	
			statement.setString(7, pRow);
			statement.setString(8, pCol);
			
			statement.executeUpdate();			//update database
			statement.close();					// close DB connections
			connection.close();			
						
			
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		finally {			//database connection/SQL query error
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		//System.out.println("Puzzle Saved To Database");
		JOptionPane.showMessageDialog(null, "Puzzle Saved to Database", "SAVED to DB", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	/**
	 * select puzzle from DB for Puzzle Solver
	 * includes SQL script for querying selected puzzle
	 * 
	 * @return
	 */
	public static int[][] selectPuzzle(){
		
		System.out.println("selectPuzzle :: begin");
		int[][] fail = null;
		Connection connection = null;
		
		String selectPuzzles = "SELECT ID, COMPLEXITY, ROWS, COLS FROM SavedPuzzle;";
		String countPuzzles = "SELECT COUNT(*) AS C FROM SavedPuzzle;";
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:Puzzle.db");
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(selectPuzzles);
			PreparedStatement statement2 = connection.prepareStatement(countPuzzles);
			
			statement.setQueryTimeout(30); //set timeout to 30 sec.
			statement2.setQueryTimeout(30); //set timeout to 30 sec.
			
			ResultSet cs = statement2.executeQuery();
			ResultSet rs = statement.executeQuery();
			
			int count = cs.getInt("C");
			System.out.println("cs: " + count);
			int[][] entries = new int[count][4];
					
			int i = 0;
			// while there's a next matching record
			while (rs.next()) {
				id = rs.getInt("id");
				entries[i][0] = id;
				complexity = rs.getInt("complexity");
				entries[i][1] = complexity;
				rows = rs.getInt("rows");
				entries[i][2] = rows;
				cols = rs.getInt("cols");
				entries[i][3] = cols;
				System.out.println(i + "> id: " + entries[i][0] + "  |  comp: " + entries[i][1] + "  |  rows: " + entries[i][2] + "  |  cols:" + entries[i][3] );
				i++;
			}
			
			
			cs.close();
			rs.close();				//close DB connection
			statement.close();
			connection.close();
			return entries;
		} catch (Exception e) {		//catch exception
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		finally {		//close DB connection if exceptions happen
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		System.out.println("Puzzle loaded from database successfully");
		return fail;
	}
	
	//Method used only for database tests
	public static int getIdOfLastCreatedEntry() {
		
		Connection connection = null;
		
		String countPuzzles = "SELECT MAX(id) AS R FROM SavedPuzzle;";
		int rowid = -1;
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:Puzzle.db");
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(countPuzzles);
			
			statement.setQueryTimeout(30); //set timeout to 30 sec.
			
			ResultSet cs = statement.executeQuery();
			
			rowid = cs.getInt("R");
						
			
			cs.close();				//close DB connection
			statement.close();
			connection.close();
			return rowid;
		} catch (Exception e) {		//catch exception
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		finally {		//close DB connection if exceptions happen
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return rowid;
	}
	
	//Method used only for database tests
	public static void deleteEntry(int rowid) {
		
		Connection connection = null;
		Statement statement = null;
		
		String deletePuzzle = "DELETE FROM SavedPuzzle WHERE ID= " + rowid + ";";
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			connection = DriverManager.getConnection("jdbc:sqlite:Puzzle.db");
			statement = connection.createStatement();
			
			statement.executeUpdate(deletePuzzle);
						
			statement.close();
			connection.close();
			
		} catch (Exception e) {		//catch exception
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		finally {		//close DB connection if exceptions happen
			try {
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	} // end method
	
}//end class
