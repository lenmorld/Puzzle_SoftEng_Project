package Objects;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * priority label of columns which appears
 * at the bottom of the frame
 * 
 * @author team t
 *
 */
public class ColumnsPriorityLabel extends JPanel  {
	
	private PriorityLabel[] colPriorityLabel; 	//GUI of the priority label
	private int[] colPriority;					//data (actual numbers) of the priority label

	/**
	 * constructor
	 * 
	 * @param cols number of columns
	 */
	public ColumnsPriorityLabel(int cols) {

		// init. and setup label array
		this.setLayout(new GridLayout(1, cols));
		
		colPriority = new int[cols];					//init. columns priority array	
		colPriorityLabel = new PriorityLabel[cols];	
		
		// initialize all priority label objects
		for (int i=0; i < cols; i++) {
			colPriorityLabel[i] = new PriorityLabel(" P ");
			this.add(colPriorityLabel[i]);			// add to label array
		}	
	}
	
	
	/**
	 * sets the text inside priority label columns
	 * 
	 * @param colP int array of column priorities
	 */
	public ColumnsPriorityLabel(int[] colP) {
		
		int cols = colP.length;
		
		this.setLayout(new GridLayout(1, cols));
		
		colPriority = new int[cols];					// init. columns priority array
		colPriorityLabel = new PriorityLabel[cols];	
		
		//init all column priority labels
		for (int i=0; i < cols; i++) {
			//colPriorityLabel[i] = new PriorityLabel("P#" + colP[i]);
			colPriorityLabel[i] = new PriorityLabel("");
			this.add(colPriorityLabel[i]);
			colPriority[i] = colP[i];
		}	
		
	}
	
	
	/**
	 * set selected text of columnPriority
	 * 
	 * @param col
	 * @param t
	 */
	public void setColPriorityText(int col, int p) {

		colPriority[col] = p;
		colPriorityLabel[col].setText("   P#" + (p+1));
	}
	
	
	/** 
	 * @param col which priority#
	 * @return index of that column
	 */
	public int getColPriority(int col) {
		return colPriority[col];
	}


	/**
	 * @return the colPriority
	 */
	public int[] getColPriority() {
		return colPriority;
	}
	
	
	/**
	 * set currently visible priority column
	 * 
	 * @param col
	 * @param b
	 */
	public void setVisible(int col, boolean b) {
		
		//hide all other labels
		for (int i=0; i < this.colPriorityLabel.length; i++)
			this.colPriorityLabel[i].setVisible(false);

		//show next
			this.colPriorityLabel[col].setText("<html>&#8593;<html>");
			this.colPriorityLabel[col].setVisible(b);
			this.colPriorityLabel[col].setForeground(Color.GREEN);
	}

	
}
