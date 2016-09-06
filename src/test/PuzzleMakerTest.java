/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import Objects.Board;
import components.PuzzleMaker;

/**
 * 
 * unit test for PuzzleMaker
 * 
 * @author team t
 *
 */
public class PuzzleMakerTest {
	
	//this test expects an exception if rows passed is 0
	
	@Test(expected = IllegalArgumentException.class)
	public void testRowsNotZero() {
		
		PuzzleMaker puzzlemaker = new PuzzleMaker(0,10);	//0 is invalid # of row
	}
	
	
	
	//this test expects an exception if column passed is 0
	
	@Test(expected = IllegalArgumentException.class)
	public void testColsNotZero() {
		
		PuzzleMaker puzzlemaker = new PuzzleMaker(10,0);		//0 is invalid # of columns
	}
	
	//this test expects an exception if both are 0
	
	@Test(expected = IllegalArgumentException.class)
	public void testRowsAndColsNotZero() {
		
		PuzzleMaker puzzlemaker = new PuzzleMaker(0,0);		// both 0 rows and columns invalid
	}

	
	//this test expects an exception if both are 0
	
	@Test(expected = IllegalArgumentException.class)
	public void testRowsAndColsMax() {
		
		PuzzleMaker puzzlemaker = new PuzzleMaker(26,26);		// both 0 rows and columns invalid
	}
	
	
	
	
	
	//##TEST CASE 2.1
	//create board with 4x4, then confirm # of rows and columns
	
	@Test
	public void testCreateBoard() {
		
		//create new PuzzleMaker
		PuzzleMaker puzzlemaker = new PuzzleMaker(4,4);
		
		//assert that rows is 4 and cols is 4
		
		int rows = puzzlemaker.getPuzzleBoard().getNumRows();
		int cols = puzzlemaker.getPuzzleBoard().getNumCols();
		
		assertEquals(4, rows);
		assertEquals(4,cols);
	}
	
	
	
	// test puzzle marks
	//## TEST CASE 2.3
	
	@Test
	public void testPuzzleMarks(){
		
		//create new PuzzleMaker
		PuzzleMaker puzzlemaker = new PuzzleMaker(4,4);
		
		/*    0 1 2 3
		 * 0  X _ X _
		 * 1  _ _ X X
		 * 2  X X _ _
		 * 3  X X X X
		 */
		
		boolean[][] markedTiles = {{true, false, true, false},
									{false, false, true, true},
									{true, true, false, false},
									{false, false, false, false}
									};
		
		//assign marked tiles manually using created array

		for (int i=0; i < markedTiles.length; i++) {
			for (int j=0; j < markedTiles[0].length; j++) {
				
				puzzlemaker.getPuzzleBoard().getGrid().setTileMarked(i, j, markedTiles[i][j]);
			}
		}
		//assert that priorities matched with marked rows, cols
		assertArrayEquals(markedTiles, puzzlemaker.getPuzzleBoard().getMarkedTiles());
	}
	
	
	
	// test puzzle hints
	//## TEST CASE 2.4
	
	@Test
	public void testPuzzleHints(){
		
		//create new PuzzleMaker
		PuzzleMaker puzzlemaker = new PuzzleMaker(4,4);
		
		/*    0 1 2 3
		 * 0  X _ X _
		 * 1  _ _ X X
		 * 2  X X _ _
		 * 3  X X X X
		 */
		
		boolean[][] hintedTiles = {{true, false, true, false},
									{false, false, true, true},
									{true, true, false, false},
									{false, false, false, false}
									};
		
		//assign marked tiles manually using created array

		for (int i=0; i < hintedTiles.length; i++) {
			for (int j=0; j < hintedTiles[0].length; j++) {
				
				puzzlemaker.getPuzzleBoard().getGrid().setTileHint(i, j, hintedTiles[i][j]);
			}
		}
		//assert that priorities matched with marked rows, cols
		assertArrayEquals(hintedTiles, puzzlemaker.getPuzzleBoard().getHintedTiles());
	}
	
	
	
	// test setting of headers
	//## TEST CASE 2.6
	
	@Test
	public void testHeaders() {
		
		//create new PuzzleMaker
		PuzzleMaker puzzlemaker = new PuzzleMaker(4,4);	
		
		/*		1   2 1
		 * 		2 2 1 1
		 *            
		 * 1 1  X _ X _
		 * 2    _ _ X X
		 * 2    X X _ _
		 * 4    X X X X
		 */
		
		boolean[][] markedTiles = {{true, false, true, false},
									{false, false, true, true},
									{true, true, false, false},
									{true, true, true, true}
									};
		
		// set headers manually based on marked tiles
		
		String[] leftHeaders = {"1 1","2","2","4"};
		String[] topHeaders = {"1 2", "2", "2 1", "1 1"};
		
		
		//assign marked tiles manually using created array
		for (int i=0; i < markedTiles.length; i++) {
			for (int j=0; j < markedTiles[0].length; j++) {
				
				puzzlemaker.getPuzzleBoard().getGrid().setTileMarked(i, j, markedTiles[i][j]);
				
				puzzlemaker.getPuzzleBoard().updateHeaders(i, j);
				
			}
		}
		
		//assert that headers matched with determined headers from marked tiles
		assertArrayEquals(leftHeaders, puzzlemaker.getPuzzleBoard().getHeaderLeft().getStringArr());
		assertArrayEquals(topHeaders, puzzlemaker.getPuzzleBoard().getHeaderTop().getStringArr());
	}
	
	
	
	//test priorities
	//## (TEST CASE 4.1 - POSITIVE TEST)
	//## Unit Test UT-04.1 getPriorityArray() test
	@Test
	public void testPriorities(){

		//assign num of marked tiles per row and col manually
		int[] numTilesMarkedEachRow = {2,3,1,4,2};
		int[] tilesMarkedEachCol = {2,4,2,2,2};
		
		
		//create a copy 
		int[] copynumTilesMarkedEachRow = {2,3,1,4,2};
		int[] copyTilesMarkedEachCol = {2,4,2,2,2};
		
		
		//create PuzzleMaker with marked tiles
		PuzzleMaker puzzlemaker = new PuzzleMaker(numTilesMarkedEachRow.length,tilesMarkedEachCol.length);
		

		//assign expected priorities per row and column
		int[] expectedRowPriorities = getPriorityArray(copynumTilesMarkedEachRow);
		int[] expectedColPriorities = getPriorityArray(copyTilesMarkedEachCol);

		System.out.println(Arrays.toString(expectedRowPriorities));
		System.out.println(Arrays.toString(expectedColPriorities));
		
		puzzlemaker.setMarkedRowsColsState(numTilesMarkedEachRow, tilesMarkedEachCol);
		
		
		//assert that priorities matched with marked rows, cols
		assertArrayEquals(expectedRowPriorities, puzzlemaker.getRowPriorities());
		assertArrayEquals(expectedColPriorities, puzzlemaker.getColPriorities());
	}
	
	
	//test priorities
	//## TEST CASE 4.2g - NEGATIVE TEST
	//## Unit Test UT-04.1 getPriorityArray() test
	@Test
	public void testPrioritiesN(){

		//assign num of marked tiles per row and col manually
		int[] numTilesMarkedEachRow = {2,3,1,4,2};
		int[] tilesMarkedEachCol = {2,4,2,2,2};
		
		
		//create a copy 
		int[] copynumTilesMarkedEachRow = {2,3,1,4,2};
		int[] copyTilesMarkedEachCol = {2,4,2,2,2};
		
		
		//create PuzzleMaker with marked tiles
		PuzzleMaker puzzlemaker = new PuzzleMaker(numTilesMarkedEachRow.length,tilesMarkedEachCol.length);
		

		//assign expected priorities per row and column
		int[] expectedRowPriorities = getPriorityArray(copynumTilesMarkedEachRow);
		int[] expectedColPriorities = getPriorityArray(copyTilesMarkedEachCol);
		
		//PURPOSELY MODIFY expectedRowPriorities
		//to show that this shouldn't match
		expectedRowPriorities[0] = 0;
		expectedRowPriorities[1] = 0;
		expectedRowPriorities[2] = 0;
				
		expectedColPriorities[0] = 0;
		expectedColPriorities[1] = 0;
		expectedColPriorities[2] = 0;
		
		System.out.println(Arrays.toString(expectedRowPriorities));
		System.out.println(Arrays.toString(expectedColPriorities));
		
		puzzlemaker.setMarkedRowsColsState(numTilesMarkedEachRow, tilesMarkedEachCol);
		
		
		
		//assert that priorities DID NOT match with marked rows, cols
		assertFalse(Arrays.equals(expectedRowPriorities, puzzlemaker.getRowPriorities()) );
		assertFalse(Arrays.equals(expectedColPriorities, puzzlemaker.getColPriorities()) );
	}
		
	
	
	
	
	
	
	// TEST-helper method
	// get priority array from artificially created Puzzle row/col
	
	private int[] getPriorityArray(int[] tilesMarked) {
			
			int[] priorityArrayRows = new int[tilesMarked.length];
			
			for (int k=0; k <  tilesMarked.length; k++) {
				
				int maxIndexRows  = getIndexOfMax(tilesMarked);
				
				priorityArrayRows[k] = maxIndexRows;
				
				tilesMarked[maxIndexRows] = -1;		//eliminate this option, for we recorded its index already in priority
			}
			return priorityArrayRows;
		}
	
	
	// TEST-helper method
	//get max index in rows and cols from aritificially created Puzzle
	
	private int getIndexOfMax(int[] numArray) {
	
		int maxIndex = 0;
		
		for (int k=0; k < numArray.length; k++) {
			if (numArray[k] >= numArray[maxIndex])
				maxIndex = k;
		}
	
		//System.out.println("MAX INDEX: " + maxIndex);
		return maxIndex;
		
	}

}
