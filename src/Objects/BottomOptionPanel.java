package Objects;

import components.*;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * a panel that holds the SAVE/CHECK button
 * and some other options (to be implemented later on)
 * 
 * @author team t
 *
 */
public class BottomOptionPanel extends JPanel {

	public BottomOptionPanel () {
			super();
			
			this.setLayout(new BorderLayout());
			
			//sets a border from the top and bottom components
			this.setBorder(new EmptyBorder(10,0,10,0));
	}
	
}
