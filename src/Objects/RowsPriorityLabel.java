package Objects;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * priority labels for each row
 * that appears at the right side of the frame
 * 
 * @author team t
 *
 */
public class RowsPriorityLabel extends JPanel  {

	
	private PriorityLabel[] rowPriorityLabel;	//GUI of the priority label
	private int[] rowPriority;	// priorities DATA, in an array


	/**
	 * constructor
	 * @param rows
	 */
	public RowsPriorityLabel(int rows) {
		
		// init. and setup label array
		this.setLayout(new GridLayout(rows, 1));
		
		rowPriority = new int[rows];	
		rowPriorityLabel = new PriorityLabel[rows];	
		
		for (int i=0; i < rows; i++) {
			//create, initialize, and add to the array
			rowPriorityLabel[i] = new PriorityLabel(" P ");
			this.add(rowPriorityLabel[i]);
		}	
	}
	
	
	/**
	 * constructor that takes an int array
	 * and loads it into a new priority label array
	 * 
	 */
	public RowsPriorityLabel(int[] rowP) {
		
		int rows = rowP.length;
		
		this.setLayout(new GridLayout(rows, 1));
		
		rowPriority = new int[rows];	
		rowPriorityLabel = new PriorityLabel[rows];	
		
		for (int i=0; i < rows; i++) {
			//create, initialize, and add to the array
			//rowPriorityLabel[i] = new PriorityLabel("P#" + rowP[i] );
			rowPriorityLabel[i] = new PriorityLabel("");
			this.add(rowPriorityLabel[i]);	
			rowPriority[i] = rowP[i];
		}	
		
	}
	
	
	
	/**
	 * set selected text of rowPriority
	 * 
	 * @param row
	 * @param t
	 */
	public void setRowPriorityText(int row, int p) {
		
		rowPriority[row] = p;
		rowPriorityLabel[row].setText(" P#" + (p+1) + "  ");
	}
	
	/**
	 * @param row
	 * @return get the priority of this row
	 */
	public int getRowPriority(int row) {
		return rowPriority[row];
	}
	
	
	/**
	 * @return the rowPriority
	 */
	public int[] getRowPriority() {
		return rowPriority;
	}
	
	
	/**
	 * set currently visible priority rows
	 * 
	 * @param row
	 * @param b
	 */
	public void setVisible(int row, boolean b) {
		
		//hide all other labels
		for (int i=0; i < this.rowPriorityLabel.length; i++)
			this.rowPriorityLabel[i].setVisible(false);

		//show next
			this.rowPriorityLabel[row].setText("<html>&#8592;<html>");
			this.rowPriorityLabel[row].setVisible(b);
			this.rowPriorityLabel[row].setForeground(Color.GREEN);
	}
	
}
