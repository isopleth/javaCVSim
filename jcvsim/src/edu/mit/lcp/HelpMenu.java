package edu.mit.lcp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;


public class HelpMenu extends JMenu {

    JFrame aboutFrame;

    public HelpMenu() {
	
	super("Help");
	this.add(new DisplayAboutFrameAction());
	
	createAboutFrame();
    }
    
    private void createAboutFrame() {
	JTextPane textPane = new JTextPane();
	textPane.setEditable(false);
	textPane.setText(new JcvsimVersion().toString() + 
                new ExtraVersion().toString() + "\n\n"
                +
                "A pure Java version of CVSim Version " 
                + CVSim.versionNumber.getMajorVerNum() 
			 + "." + CVSim.versionNumber.getMinorVerNum() + "\n"+
                "jCVSim is released under the GNU General Public License. " +
			 "See http://www.gnu.org for more information.\n\n" +
			 "CVSim is a hemodynamic simulation developed by the Massachusetts " + 
			 "Institute of Technology Laboratory for Computational Physiology.\n\n" +
			 "CVSim is released under the GNU General Public License. " +
			 "See http://www.gnu.org for more information.");
	JScrollPane scrollPane = new JScrollPane(textPane);
	
	aboutFrame = new JFrame("About jCVSim");
	aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	
	JPanel contentPane = new JPanel(new BorderLayout());
	contentPane.add(scrollPane, BorderLayout.CENTER);
	contentPane.setOpaque(true);
	aboutFrame.setContentPane(contentPane);

	int width = 300;
	int height = 300;
	aboutFrame.setSize(width, height);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	aboutFrame.setLocation(screenSize.width/2 - width/2,
			       screenSize.height/2 - height/2);
    }



    private class DisplayAboutFrameAction extends AbstractAction {
	public DisplayAboutFrameAction() {
	    putValue(Action.NAME, "About jCVSim");
	}
        @Override
	public void actionPerformed(ActionEvent e) {
	    if ( !(aboutFrame.isVisible()) )
		aboutFrame.setVisible(true);
	    if ( aboutFrame.getExtendedState() == JFrame.ICONIFIED )
		aboutFrame.setExtendedState(JFrame.NORMAL);
	}
    }

}
