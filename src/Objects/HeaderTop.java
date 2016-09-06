package Objects;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * top headers (constraints)
 * that appear at the top of the frame
 * 
 * @author team t
 *
 */
public class HeaderTop extends JPanel {
	
	private LabelLine[] labelArrayTop;
	
	/**
	 * constructor for top headers
	 * used in creating new puzzles
	 * 
	 */
	public HeaderTop(int cols) {
			
		// init. and setup header array
		this.setLayout(new GridLayout(1, cols));
		labelArrayTop = new LabelLine[cols];	
		
		for (int i=0; i < cols; i++) {
			labelArrayTop[i] = new LabelLine("","");
			this.add(labelArrayTop[i]);
		}
	}
	
	
	/**
	 * copy constructor
	 * used for loading headers from a saved puzzle
	 * 
	 * since we already have the saved headers from the puzzle made by p. maker
	 * 
	 * @param headerTopPuzzle
	 */
	public HeaderTop(String[] savedHeaderTop) {
		//init array
		this.setLayout(new GridLayout(1, savedHeaderTop.length));
		labelArrayTop = new LabelLine[savedHeaderTop.length];
		
		for (int i=0; i < savedHeaderTop.length; i++) {
			
			System.out.println(savedHeaderTop[i]);
			
			String plainText = savedHeaderTop[i];
			
			//some exceptions might occur such as if it's empty
			try {	
				String htmlText = plainText.replaceAll(" ", "<br>");		// add new line to each space to make them appear stacked
				htmlText = "<html>" + htmlText + "</html>";
				// create current label, passing the orig. text from the saved label
				labelArrayTop[i] = new LabelLine(plainText, htmlText);
				
				//add to top header
				this.add(labelArrayTop[i]);
			}
			catch (Exception e) {		//if empty, just let exception go
			}
		}
		
	}

	
	/**
	 * @return string stored in a constraint header
	 */
	public String[] getStringArr() {
		
		String temp[] = new String[labelArrayTop.length];
		
		for (int i=0; i < labelArrayTop.length; i++)
		{
			temp[i] = labelArrayTop[i].getPlainText();
		}
	
		return temp;
	}
	

	/**
	 * @param col desired column
	 * @return the constraint saved in specified column
	 */
	public String getText(int col) {
		return labelArrayTop[col].getPlainText();
	}
	
	/**
	 * @return num.of columns/top label array length
	 */
	public int getNumberOfLabel() {
		return labelArrayTop.length;
	}

	/**
	 * set constraint of given column
	 * should set the plainText (DATA)
	 * and the htmlText (formatted text for the label)
	 * 
	 * @param col column
	 * @param origText constraint
	 */
	public void setText(int col, String origText) {
		
		String s = origText.replaceAll(" ", "<br>");		// add new line to each space to make them appear stacked
		
		//labelArrayTop[col].setText("<html>" + s + "</html>");
		
		labelArrayTop[col].setHtmlText("<html>" + s + "</html>");   
		labelArrayTop[col].setPlainText(origText);
		
		//System.out.println(s);
		
	}
	
}
