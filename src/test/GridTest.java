package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.*;
import Objects.*;

/**
 * unit test for Grid
 * 
 * @author team t
 *
 */
public class GridTest {

	//create a grid 
	Grid grid = new Grid(4, 4);
	
	//simulate a selected array
	boolean[][] selected = new boolean[4][4];

	
	//initialize the grid and array values
	
	@Before
	public void setup() {
		
		grid.initPuzzleMakerGrid();		// init. Puzzle Maker  
		
		//go through entire grid and array and set tile as selected manually
		for (int i = 0; i < 4; i++) {			
			grid.setTileSelected(i, i, true);
			selected[i][i] = true;
		}
	}
	
	
	// compare grid selected tiles and simulated selected array
	// assert that they are equal
	
	@Test
	public void testGetSelectedTiles() {
		assertArrayEquals(grid.getSelectedTiles(), selected);
	}
	
	
	// create a new similar grid
	// compare orig. grid and new grid selected tiles
	// assert that they are equal
	
	@Test
	public void testInitPuzzleSolverGrid() {
		Grid grid2 = new Grid(4, 4);
		grid2.initPuzzleSolverGrid(selected);
		assertArrayEquals(grid.getSelectedTiles(), grid2.getSelectedTiles());
	}
	
	
	// create a new similar grid
	// compare orig. grid and new grid selected tiles
	// assert that they are not equal
	
	@Test
	public void testInitPuzzleSolverGridFailTest() {
		selected[1][1] = false;
		Grid grid2 = new Grid(4, 4);
		grid2.initPuzzleSolverGrid(selected);
		assertFalse(Arrays.equals(grid.getSelectedTiles(), grid2.getSelectedTiles()));
		
	}
}
