package test;

import static org.junit.Assert.*;
import java.util.Arrays;

import org.junit.*;

import Objects.*;
import components.*;

/**
 * unit test for Puzzle Solver
 * 
 * For the test case we use a 10x9 puzzle.
 * 
 * @author team t
 *
 */
public class PuzzleSolverTest {

	//initialize test objects
	
	Puzzle puzzle = new Puzzle(10, 9);
	String[] headerTop;
	String[] headerLeft;
	boolean[][] solution;
	PuzzleSolver ps;
	
	
	//create complete 10x9 Puzzle
	/*    0 1 2 3 4 5 6 7 8
	 * 0  X _ _ _ X _ X _ X
	 * 1  X X _ _ X _ _ _ X
	 * 2  _ X X _ X X X _ X
	 * 3  X _ X X _ _ X _ _
	 * 4  _ X _ X _ _ X X _
	 * 5  X _ _ _ _ _ _ _ _
	 * 6  X _ X X _ X X _ X
	 * 7  X _ X _ _ _ X _ X
	 * 8  X _ X X _ _ X X _
	 * 9  X _ _ X _ _ X _ _
	 */
	
	@Before
	public void makePuzzle() {
		
		//initialize 2D solution array for the 10x9 puzzle
		solution = new boolean[][]{{true, false,false, false, true, false,true, false, true},
					{true, true, false, false, true, false, false, false, true},
					{false, true, true, false, true, true, true, false, true},
					{true, false, true, true, false, false, true, false, false},
					{false, true, false, true, false, false, true, true, false},
					{true, false, false, false, false, false, false, false, false},
					{true, false, true, true, false, true, true, false, true},
					{true, false, true, false, false, false, true, false, true},
					{true, false, true, true, false, false, true, true, false},
					{true, false, false, true, false, false, true, false, false}};
		
		//set the array as its solution
		puzzle.setPuzzleSolution(solution);
		
		//Headers associated with the 10x9 test case puzzle
		headerTop = new String[]{"2 1 5", "2 1", "2 3", "2 1 2", "3", "1 1", "1 3 4", "1 1", "3 2"};		// create constraints
		headerLeft = new String[]{"1 1 1 1", "2 1 1", "2 3 1", "1 2 1", "1 1 2", "1", "1 2 2 1", "1 1 1 1", "1 2 2", "1 1 1"};
		puzzle.setHeaderLeft(headerLeft);
		puzzle.setHeaderTop(headerTop);
		
		
		//create PuzzleSolver object with the artificially create test objects
		ps = new PuzzleSolver(puzzle);
	}

	
	// Test the checkSingleRowOrColumn method used for user stories 3, 5 and 6.
	// Given the configuration of the solution of the 10x9 test case, the test checks that the method return true for every
	// row. Meaning that each rows are satisfied.
	@Test
	public void testCheckSingleRow() {
		
		String[] con = {"t t t t", "t t t", "t t t", "t t t", "t t t", "t", "t t t t", "t t t t", "t t t", "t t t"};
		
		//assert that it matches
		for (int i = 0; i < solution.length; i++) {
			assertTrue("Check Row : " + i, ps.checkSingleRowOrColumn(solution[i], headerLeft[i]).equals(con[i]));
		}
	}
	
	// Test the checkSingleRowOrColumn method used for user stories 3, 5 and 6.
	// Given a random configuration  of the 10x9 test case, the test checks that the method return false for at least 
	// one row. Meaning that at least one row is not satisfied.
	@Test(expected = AssertionError.class)
	public void testCheckSingleRowFails() {
		//boolean[] row = {true, false, true, true};		// get a solution row
		//String rowCon = headerLeft[3];					// get constraint
		
		String[] con = {"t t t t", "t t t", "t t t", "t t t", "t t t", "t", "t t t t", "t t t t", "t t t", "t t t"};
		
		//assert that it matches
		for (int i = 0; i < solution.length; i++) {
			int index = (i + 1) % 10;
			assertTrue((ps.checkSingleRowOrColumn(solution[index], headerLeft[i])).equals(con[i]));
		}
	}
	
	
	// Test the checkSingleRowOrColumn method used for user stories 3, 5 and 6.
	// Given the configuration of the solution of the 10x9 test case, the test checks that the method return true for every
	// columns. Meaning that each columns are satisfied.
	@Test
	public void testCheckSingleCol() {
		
		String[] con = {"t t t", "t t", "t t", "t t t", "t", "t t", "t t t", "t t", "t t"};
		
		
		boolean[][] cols = new boolean[9][10];  //we have to create the column array by looping through the rows 
		for (int i = 0; i < solution[0].length; i++) {
			for (int j = 0; j < solution.length; j++) {
				cols[i][j] = solution[j][i];
			}
		}
		
		//assert that it matches
		for (int i = 0; i < solution[0].length; i++) {
			assertEquals(ps.checkSingleRowOrColumn(cols[i], headerTop[i]), con[i]);
		}
	}
	
	
	// Test GetMatchingRows() method used for user stories 3,5 and 6
	// Given the complete or incomplete solution, the method should know exactly which rows are determined
	// and which one are not are returning the appropriate boolean.	
	@Test
	public void testGetMatchingRows() {
		
		//First test with the complete solution. All rows should return true.
		boolean[] allMatch = {true, true, true, true, true, true, true, true, true, true};		//setup array all-match
		boolean[] matches = ps.getMatchingRows(solution, headerLeft, 10);	// compare to header
		
		assertArrayEquals(allMatch, matches);		// assert match
		
		//Second test with some rows that are not completely determined
		//case where the rows are not all set to true
		allMatch[2] = false;
		allMatch[4] = false;
		allMatch[5] = false;
		//Changing some tiles in row 2, 4 ,5, so the method returns false.
		boolean[][] otherSolution = new boolean[][]{{true, false,false, false, true, false,true, false, true},
			{true, true, false, false, true, false, true, false, false}, 	//changed a boolean here, but still return true for the entire row as expected
			{false, true, false, false, true, true, true, false, true},
			{true, false, true, true, false, false, true, false, false},
			{false, true, true, true, false, false, true, true, false},
			{true, false, false, false, false, false, false, false, true}, 	//test fails here : when the last column on this row is set to true, it should return false, but does not
			{true, false, true, true, false, true, true, false, true},
			{true, false, true, false, false, false, true, false, true},
			{true, false, true, true, false, false, true, true, false},
			{true, false, false, true, false, false, true, false, false}};
			
		matches = ps.getMatchingRows(otherSolution, headerLeft, 10);	// compare to header
		
		assertArrayEquals(allMatch, matches);
	}
	
	// Test GetMatchingRows() method failure used for user stories 3,5 and 6
	// Changing the headers of the rows, but not the solution configuration,
	// then some row should return false, making the whole test a failure.
	@Test
	public void testGetMatchingRowsFail() {
		boolean[] allMatch = {true, true, true, true, true, true, true, true, true, true};		//setup array
		headerLeft = new String[]{"1 1 1 1 1", "2 1 1", "2 3 1", "1 2 1", "1 1 2", "1", "1 2 2 1", "1 1 1 1", "1 2 2", "1 1 1"};		// create artificial non-matching constraint
		boolean[] matches = ps.getMatchingRows(solution, headerLeft, 10);		//compare to header
		
		//assert non-match
		assertFalse(Arrays.equals(allMatch, matches));
	}
	
	
	// Test GetMatchingCols() method used for user stories 3,5 and 6
	// Given the complete or incomplete solution, the method should know exactly which columns are determined
	// and which one are not are returning the appropriate boolean.	
	@Test
	public void testGetMatchingCols() {
		boolean[] allMatch = {true, true, true, true, true, true, true, true, true};	//setup array all-match
		boolean[] matches = ps.getMatchingCols(solution, headerTop, 9);		//compare to header
		
		//assert match
		assertArrayEquals(allMatch, matches);
		
		//case where the cols are not all set to true
		allMatch[2] = false;
		allMatch[4] = false;
		allMatch[5] = false;
		
		boolean[][] otherSolution = new boolean[][]{{true, false, true, false, false, false,true, false, true},
			{true, true, false, false, true, false, false, false, true},
			{false, true, true, false, true, true, true, false, true},
			{true, false, true, true, false, false, true, false, false},
			{false, true, false, true, false, false, true, true, false},
			{true, false, false, false, false, false, false, false, false},
			{true, false, true, true, false, true, true, false, true},
			{true, false, true, false, false, false, true, false, true},
			{true, false, true, true, false, false, true, true, false},
			{true, false, false, true, false, true, true, false, false}}; //test fails here : when the 6th column of this row is set to true, it does not return false when it should
			
		matches = ps.getMatchingCols(otherSolution, headerTop, 9);	// compare to header
		
		assertArrayEquals(allMatch, matches);
	}
	
	// Test GetMatchingColumns() method failure used for user stories 3,5 and 6
	// Changing the headers of the rows, but not the solution configuration,
	// then some column should return false, making the whole test a failure.
	@Test
	public void testGetMatchingColsFail() {
		boolean[] allMatch = {true, true, true, true, true, true, true, true, true};		//setup array
		headerTop = new String[]{"2 1 5 1", "2 1", "2 3", "2 1 2", "3", "1 1", "1 3 4", "1 1", "3 2"};		// create artificial non-matching constraint
		boolean[] matches = ps.getMatchingCols(solution, headerTop, 9);		//compare to header
		
		//assert non-match
		assertFalse(Arrays.equals(allMatch, matches));
	}
	
	/*
	 * Test the checkSingleConstraint() method used for user stories 3,5 and 6.
	 */
	@Test
	public void testCheckSingleConstraint() {
		//Row/Column used for test case. This correspond to the header constraint (1,2,3)
		boolean[] rowOrColumn = {true, false, true, true, false, false, true, true, true};
		
		//Test the first constraint starting at index 0. Pass
		int con = ps.checkSingleConstraint(rowOrColumn, 1, 0);
		assertEquals(con, 1);
		//Test second constraint starting at index 1. Fail
		con = ps.checkSingleConstraint(rowOrColumn, 1, 1);
		assertEquals(con, -1);
		//Test the second constraint starting at index 0. Fail
		con = ps.checkSingleConstraint(rowOrColumn, 2, 0);
		assertEquals(con, -1);
		//Test the second constraint starting at index 1. Pass
		con = ps.checkSingleConstraint(rowOrColumn, 2, 1);
		assertEquals(con, 4);
		//Test the second constraint starting at index 3. Pass
		con = ps.checkSingleConstraint(rowOrColumn, 2, 3);
		assertEquals(con, -1);
		//Test the third constraint starting at index 2. Fail
		con = ps.checkSingleConstraint(rowOrColumn, 3, 2);
		assertEquals(con, -1);
		//Test the third constraint starting at index 4. Pass
		con = ps.checkSingleConstraint(rowOrColumn, 3, 4);
		assertEquals(con, 9);
		//Test the first constraint at the end or the array(out of bound). Fail.
		con = ps.checkSingleConstraint(rowOrColumn, 1, 9);
		assertEquals(con, -1);
	}
	
	/*
	 * Test rowSnapShot() method used in user story 5 (potential solution)
	 */	
	@Test
	public void testRowSnapShot() {
		
		//All 't' and 'f' represent a constraint that is either satisfied or not.
		String[] con = {"t t t f", "t t f", "f t f", "t t t", "t t t", "f", "t f t t", "t t t f", "f t t", "t t f"};
		
		//Given the configuration below the method should return the array above
		boolean[][] current_state = {{true, false,false, false, true, false,true, false, false},
			{true, true, false, false, true, false, true, false, true},
			{false, false, true, false, true, true, true, false, false},
			{true, false, true, true, false, false, true, false, false},
			{false, true, false, true, false, false, true, true, false},
			{false, false, false, false, false, false, false, false, false},
			{true, false, true, false, false, true, true, false, true},
			{true, false, true, false, false, false, true, false, false},
			{false, false, true, true, false, false, true, true, false},
			{true, false, false, true, false, false, true, false, true}};
		
		assertArrayEquals(con, ps.rowSnapShot(current_state, headerLeft, 10));		
	}
	
	/*
	 * Test colSnapShot() method used in user story 5 (potential solution)
	 */	
	@Test
	public void testColSnapShot() {
		
		//All 't' and 'f' represent a constraint that is either satisfied or not.
		String[] con = {"t f t", "f f", "f t", "f t t", "f", "t f", "f t t", "t t", "t f"};
		//Given the configuration below the method should return the array above
		boolean[][] current_state = 	{{true, 	false,	false, 	false, 	true, 	false,	false, 	false, 	true},
										{true, 		true, 	true, 	false,	true, 	false, 	false, 	false, 	true},
										{false, 	true, 	true, 	false, 	false, 	true, 	true, 	false, 	true},
										{false, 	true, 	true, 	false, 	true, 	false, 	true, 	false, 	false},
										{false, 	true, 	false, 	true, 	false, 	false, 	true, 	true, 	true},
										{true, 		false, 	false, 	false, 	false, 	false, 	false, 	false, 	false},
										{true, 		false, 	true, 	true, 	true, 	true, 	true, 	false, 	true},
										{true, 		false, 	true, 	false, 	false, 	false, 	true, 	false, 	true},
										{true, 		false, 	true, 	true, 	false, 	false, 	true, 	true, 	false},
										{true, 		false, 	false, 	true, 	false, 	true, 	true, 	false, 	false}};
		
		assertArrayEquals(con, ps.colSnapShot(current_state, headerTop, 9));	
		
	}
	
	/*
	 * Test the checkPotentialSolutionRows() method used for user story 5
	 * Test every row for potential solutions given the current configuration and compare it to the associated row of expected potential solution
	 */	
	@Test
	public void testCheckPotentialSolutionRows() {
		
		//This 2D array represent all the potential solution for rows given the current configuration of the 10x9 puzzle
		boolean[][] potential_solution = {{false, false,true, false, true, true, false, false, false},
										{false, false, false, false, false, false, false, false, false},
										{false, true, true, false, true, false, true, false, false},
										{true, true, true, false, false, false, false, true, true},
										{false, true, true, true, true, false, true, true, true},
										{false, false, false, false, false, false, false, false, false},
										{false, false, false, false, false, false, false, false, true},
										{false, false, true, false, true, false, false, false, true},
										{false, false, true, true, true, true, false, true, true},
										{false, false, true, true, true, false, false, false, true}};
		
		//This 2D array represent the current configuration of the 10x9 puzzle used to determine the potential solution above
		boolean[][] current_state = {{true, false,false, false, false, false,false, true, false},
									{true, false, false, true, false, false, false, false, false},
									{false, false, false, true, false, false, false, false, true},
									{false, false, false, false, true, true, false, false, false},
									{false, false, false, false, false, false, false, false, false},
									{true, false, false, false, false, false, false, false, false},
									{true, false, true, true, false, false, true, false, false},
									{true, false, false, false, false, false, true, false, false},
									{true, false, false, false, false, false, true, false, false},
									{true, false, false, false, false, false, true, false, false}};
		
		for (int i = 0; i < potential_solution.length; i++) {
			assertArrayEquals(Arrays.toString(ps.checkPotentialSolutionsRows(current_state, i, headerTop, headerLeft)), potential_solution[i], ps.checkPotentialSolutionsRows(current_state, i, headerTop, headerLeft));
		}
	}
	
	/*
	 * Test the checkPotentialSolutionCols() method used for user story 5
	 * Test every columns for potential solutions given the current configuration and compare it to the associated column of expected potential solution
	 */	
	@Test
	public void testCheckPotentialSolutionCols() {
		
		//This 2D array represent all the potential solution for columns given the current configuration of the 10x9 puzzle
		boolean[][] potential_solution = {{false, false, false, true, false, false, false, false, false, false},
				{false, false, true, true, true, false, false, false, false, false},
				{true, false, true, true, true, false, false, true, true, true},
				{false, false, false, false, true, false, false, false, true, true},
				{true, false, true, false, true, false, false, true, true, true},
				{true, false, false, false, false, false, false, false, true, false},
				{false, false, true, false, true, false, false, false, false, false},
				{false, false, false, true, true, false, false, false, true, false},
				{false, false, false, true, true, false, true, true, true, true}};

		//This 2D array represent the current configuration of the 10x9 puzzle used to determine the potential solution above
		boolean[][] current_state = {{true, false,false, false, false, false,false, true, false},
			{true, false, false, true, false, false, false, false, false},
			{false, false, false, true, false, false, false, false, true},
			{false, false, false, false, true, true, false, false, false},
			{false, false, false, false, false, false, false, false, false},
			{true, false, false, false, false, false, false, false, false},
			{true, false, true, true, false, false, true, false, false},
			{true, false, false, false, false, false, true, false, false},
			{true, false, false, false, false, false, true, false, false},
			{true, false, false, false, false, false, true, false, false}};

		for (int i = 0; i < potential_solution.length; i++) {
			assertArrayEquals(Arrays.toString(ps.checkPotentialSolutionsCols(current_state, i, headerTop, headerLeft)), potential_solution[i], ps.checkPotentialSolutionsCols(current_state, i, headerTop, headerLeft));
		}
		
	}
	
	/*
	 * Test the compareBeforeAndAfter() method used in user story 5
	 * Compare the set of constraint that are satisfied and not satisfied for rows and columns, before and after making some arbitrary change
	 * in the configuration of the grid.
	 * 
	 * To pass the test, some 't' in the 'before' arrays must also be 't' in the 'after'.
	 * Some 'f' in the 'before' array can be either 't' or 'f'
	 */
	@Test
	public void testCompareBeforeAndAfter() {
		
		String[] rowBefore = {"t t f f", "t t t", "f t t", "t f f", "f f f", "t", "t f t t", "t t f t", "t f t", "f t t"};
		
		String[] colBefore = {"t t f", "f t", "t f", "t f t", "f", "f t", "t f t", "t t", "t t"};
		
		String[] rowAfter = {"t t f t", "t t t", "t t t", "t t t", "t t t", "t", "t t t t", "t t t t", "t t t", "t t t"};
		
		String[] colAfter = {"t t t", "t t", "t t", "t t t", "t", "t t", "t t t", "t t", "t t"};
		
		assertTrue(ps.compareBeforeAndAfter(rowBefore, rowAfter, colBefore, colAfter));
		
	}
	
	/*
	 * Test the compareBeforeAndAfter() method failure used in user story 5
	 * Compare the set of constraint that are satisfied and not satisfied for rows and columns, before and after making some arbitrary change
	 * in the configuration of the grid.
	 * 
	 * To fail the test, some 't' in the 'before' arrays must become 'f' in the 'after'.
	 * Some 'f' in the 'before' array can be either 't' or 'f'
	 */
	@Test
	public void testCompareBeforeAndAfterFail() {
		
		String[] rowBefore = {"t t f f", "t t t", "f t t", "t f f", "f f f", "t", "t f t t", "t t f t", "t f t", "f t t"};
		
		String[] colBefore = {"t t f", "f t", "t f", "t f t", "f", "f t", "t f t", "t t", "t t"};
		
		String[] rowAfter = {"t t f t", "t t t", "t t t", "t t t", "t t t", "t", "t t t t", "t t t t", "t f t", "t f t"};
		
		String[] colAfter = {"t t t", "t t", "t t", "t f t", "t", "t t", "t t t", "t t", "t t"};
		
		assertFalse(ps.compareBeforeAndAfter(rowBefore, rowAfter, colBefore, colAfter));
	}
	
	/*
	 * Test getPriorities() method used for user story 4
	 */
	@Test
	public void testLoadRowPriorities() {
		
		//initialize puzzle for the test case
		PuzzleMaker pm = new PuzzleMaker(10,9);
		
		// # of black tile in each row or column. The higher the number, the higher the priority. 
		int[] rowMarkedTile = {4,4,6,4,4,1,6,4,5,3};
		int[] colMarkedTile = {8,3,5,5,3,2,8,2,5};
		// Expected priorities for the rows for the test case
		int[] rowPriorities = {6,2,8,7,4,3,1,0,9,5};
		
		// Set priorities to our 10x9 puzzle which will be tested.
		pm.setMarkedRowsColsState(rowMarkedTile , colMarkedTile);
		puzzle.setRowPriorities(pm.getRowPriorities());
		
		
		
		assertArrayEquals(Arrays.toString(puzzle.getRowPriorities()), rowPriorities, puzzle.getRowPriorities());
	}
	
	/*
	 * Test getColPriorities() method used for user story 4
	 */
	@Test
	public void testLoadColPriorities() {
		
		//initialize puzzle for the test case
		PuzzleMaker pm = new PuzzleMaker(10,9);
		
		// # of black tiles in each row of column. The higher the number, the higher the priority.
		int[] rowMarkedTile = {4,4,6,4,4,1,6,4,5,3};
		int[] colMarkedTile = {8,3,5,5,3,2,8,2,5};
		// Expected priorities for the rows for the test case
		int[] colPriorities = {6,0,8,3,2,4,1,7,5};
		
		// Set priorities to our 10x9 puzzle which will be tested.
		pm.setMarkedRowsColsState(rowMarkedTile , colMarkedTile);
		puzzle.setColPriorities(pm.getColPriorities());
		
		assertArrayEquals(Arrays.toString(puzzle.getColPriorities()), colPriorities, puzzle.getColPriorities());
		
	}
	
	
	/*
	 * Test the checkPuzzle() method for user story 3
	 * 
	 * Set the configuration of the grid to the solution of the puzzle and the test should pass.
	 */
	@Test
	public void testCheckCurrentGrid() {
		
		Grid solvergrid = ps.getPuzzleBoard().getGrid();
		
		for (int i = 0; i < solution.length; i++) {
			for(int j = 0; j < solution[0].length; j++) {
				if (solution[i][j] == true) {
					solvergrid.setTileSelected(i, j, true);
				}
			}
		}
		
		assertTrue(ps.checkPuzzle());
	}
	
	/*
	 * Test the checkPuzzle() method failure for user story 3
	 * 
	 * Set the configuration of the grid to the solution of the puzzle. 
	 * Change some arbitrary value making the grid not a solution and the test should fail.
	 */
	@Test
	public void testCheckCurrentGridFail() {
		
		Grid solvergrid = ps.getPuzzleBoard().getGrid();
		
		for (int i = 0; i < solution.length; i++) {
			for(int j = 0; j < solution[0].length; j++) {
				if (solution[i][j] == true) {
					solvergrid.setTileSelected(i, j, true);
				}
			}
		}
		solvergrid.setTileSelected(0, 0, false);
		
		assertFalse(ps.checkPuzzle());
	}
	
	/*
	 * Test for user stories 3 and 6.
	 * 
	 * Check whether the state of the rows that are satisfied change in real-time, when some 
	 * arbitrary tile becomes black.
	 */
	@Test
	public void testAutomaticCheckingOfGrid() {
				
		
		boolean[] matchingRows = {false, false, false, false, false, false, false, false , false, false};
		
		Grid solvergrid = ps.getPuzzleBoard().getGrid();
		
		for (int i = 0; i < solution.length; i++) {
			for(int j = 0; j < solution[0].length; j++) {
				if (solution[i][j] == true) {
					solvergrid.setTileSelected(i, j, true);
					ps.setPuzzleState(null);
				}
			}
			matchingRows[i] = true;
			assertArrayEquals(matchingRows, ps.getMatchingRows(ps.getPuzzleState(), headerLeft, 10));
		}		
	}
	
	
	
}
