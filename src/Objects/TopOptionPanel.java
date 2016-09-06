package Objects;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * a panel that holds the TOP components
 * including welcome text, priority checkbox
 * and some other options (to be implemented later on)
 * 
 * @author team t
 *
 */
public class TopOptionPanel extends JPanel {

	public TopOptionPanel () {

		super();
		
		//sets a border from the top and bottom components
		this.setBorder(new EmptyBorder(10,10,0,10));
	}
}
