package Objects;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * left headers (constraints)
 * that appears at the left side of the board
 * 
 * @author team t
 *
 */
public class HeaderLeft extends JPanel {

	private LabelLine[] labelArrayLeft;

	/**
	 * constructor for left headers
	 * used in creating new puzzles
	 */
	public HeaderLeft(int rows) {
		
		// init. and setup header array
		this.setLayout(new GridLayout(rows, 1));
		labelArrayLeft = new LabelLine[rows];	
		
		for (int i=0; i < rows; i++) {
			labelArrayLeft[i] = new LabelLine("", "");
			this.add(labelArrayLeft[i]);
	
		}
	}

	/**
	 * copy constructor
	 * used for loading headers from a saved puzzle
	 * 
	 * since we already have the saved headers from the puzzle made by p. maker
	 * 
	 * 
	 * @param headerLeftPuzzle
	 */
	public HeaderLeft(String[] savedHeaderLeft) {
		
		this.setLayout(new GridLayout(savedHeaderLeft.length, 1));
		
		labelArrayLeft = new LabelLine[savedHeaderLeft.length];	
		
		for (int i=0; i < savedHeaderLeft.length; i++) {
			
			// create current label, passing the orig. text from the saved label
			labelArrayLeft[i] = new LabelLine(savedHeaderLeft[i], savedHeaderLeft[i] );
			
			//add to left header
			this.add(labelArrayLeft[i]);
		}	
	}
	
	
	/**
	 * @return string stored in a constraint header
	 */
	public String[] getStringArr() {
		
		String temp[] = new String[labelArrayLeft.length];
		
		for (int i=0; i < labelArrayLeft.length; i++)
		{
			temp[i] = labelArrayLeft[i].getPlainText();
		}
	
		return temp;
	}
	
	
	/**
	 * @param row desired left label
	 * @param s text
	 */
	public void setText(int row, String s) {
		labelArrayLeft[row].setHtmlText(s);
		labelArrayLeft[row].setPlainText(s);
	}
	
	
	/**
	 * @param row desired 
	 * @return constraint saved in the specified row
	 */
	public String getText(int row) {
		return labelArrayLeft[row].getText();
	}
	
	/**
	 * @return get number of rows/length of label array
	 */
	public int getNumberOfLabel() {
		return labelArrayLeft.length;
	}
	
	
}
