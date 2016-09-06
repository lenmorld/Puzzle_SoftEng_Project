package Objects;
import javax.swing.JLabel;

/**
 * this is a single label from which the headers are composed of
 * 
 * this has two members
 * 1. HTML text (VIEW or GUI), mostly needed for top headers (HTML formatting)
 * 		where we need a line break inside the label
 * 		for it to display    1
 * 							 1    instead of 1 1
 * 
 * 2. plain text (DATA constraints) , needed for encoding headers and calculating
 * 		the current grid against the constraints 
 * 
 * @author team t
 *
 */
public class LabelLine extends JLabel {

	private String htmlText;
	private String plainText;

	/**
	 * constructor, init. size and position
	 * 
	 * @param label
	 */
	LabelLine(String plainText, String htmlText) {
		
		super(htmlText);			//display the htmlText in the GUI Label
		
		this.setHtmlText(htmlText);
		this.plainText = plainText;			//also set the plain text as data
		
		//set apperance
		//this.setSize(75, 10);		// let it scale with the text
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
	}


	/**
	 * @return the htmlText
	 */
	public String getHtmlText() {
		return htmlText;
	}

	/**
	 * @param htmlText the htmlText to set
	 */
	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;	
		this.setText(htmlText);
	}

	/**
	 * @return the plainText
	 */
	public String getPlainText() {
		return plainText;
	}

	/**
	 * @param plainText the plainText to set
	 */
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	
	

}
