package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import components.Puzzle;
import Driver.Database;

/**
 * 
 * unit test for database
 * specifically, checks if headers/constraints
 * are properly stored to database
 * when puzzle is saved by PuzzleMaker
 * 
 * @author team t
 *
 */
public class DatabaseTest {

	//initialize puzzle attributes to save
	
	Puzzle p1 = new Puzzle(5,5);
	Puzzle p2;
	String[] headerTopSave;
	String[] headerLeftSave;
	String[] headerLeftLoad;
	String[] headerTopLoad;
	boolean[][] solution;
	int rowid;
	
	//create complete Puzzle
	/*    0 1 2 3 4 
	 * 0  X _ X _ X  {true,false,true,false,true}
	 * 1  X X _ _ X  {true,true,false,false,true}
	 * 2  _ _ X _ X  {false,false,true,false,true}
	 * 3  X _ X _ _  {true,false,true,false,false}
	 * 4  _ X _ X X  {false,true,false,true,true}
	 */
	
	@Before
	public void makePuzzle() {
		
		//initialize solution array all to true
		solution = new boolean[][]{
			{true,false,true,false,true},
			{true,true,false,false,true},
			{false,false,true,false,true},
			{true,false,true,false,false},
			{false,true,false,true,true}};
			
		
		//set the array as its solution
		p1.setPuzzleSolution(solution);
		
		headerTopSave = new String[]{"2 1", "1 1", "1 2", "1", "3 1"};		// create constraints
		headerLeftSave = new String[]{"1 1 1", "2 1", "1 1", "1 1", "1 2"};
		p1.setPuzzleHints(solution);
		p1.setHeaderLeft(headerLeftSave);
		p1.setHeaderTop(headerTopSave);
		
		//save puzzle to DB
		Database.savePuzzle(p1);
		rowid = Database.getIdOfLastCreatedEntry();
		p2 = Database.loadPuzzle(rowid);
		
		//get headers - top and left
		headerLeftLoad = p2.getHeaderLeft();
		headerTopLoad = p2.getHeaderTop();
	}
	
	
	//test if the saved leftHeaders equals the one loaded
	@Test
	public void testSaveLoadHeaderLeft() {
		assertArrayEquals(headerLeftSave,headerLeftLoad);
		Database.deleteEntry(rowid);		//delete database entry after testing
	}
	
	//test if the saved topHeaders equals the one loaded
	@Test
	public void testSaveLoadHeaderTop() {
		assertArrayEquals(headerTopSave,headerTopLoad);
		Database.deleteEntry(rowid);		//delete database entry after testing	
	}
	
	//test if the hints equals the ones loaded
	@Test
	public void testHintsLoading() {
		assertArrayEquals(p1.getPuzzleHints(), p2.getPuzzleHints());
		Database.deleteEntry(rowid);		//delete database entry after testing	
	}

}
