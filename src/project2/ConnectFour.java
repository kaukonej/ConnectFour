package project2;

import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/**********************************************************************
 * 
 * Starts a game of Connect Four. Prompts the user for type of game
 * to play upon startup, as well as the size if they choose a 2D
 * game.
 * @author Justin Kaukonen and Adrian Harrell
 * 
 *********************************************************************/
public class ConnectFour {
	//-----------------------------------------------------------------
	//  Creates and displays the main program frame.
	//-----------------------------------------------------------------
	public static void main (String[] args) {
	    JMenuBar menus;
	    JMenu fileMenu;
	    JMenuItem quitItem;
	    JMenuItem gameItem;   

		JFrame frame = new JFrame ("Connect Four");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("quit");
        gameItem = new JMenuItem("new game");
        
        fileMenu.add(gameItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        frame.setJMenuBar(menus);
        menus.add(fileMenu);
        String gameString = "";
        while (gameString.length() != 1 || !Pattern.matches("[0,1,2]", 
        		gameString)) {
        	gameString = JOptionPane.showInputDialog(null, 
    				"2-Player 2D Game: 0, 1-Player 2D Game: 1, 2-Player"
    				+ " 3D Game: 2");
            if (gameString.length() != 1) {
            	JOptionPane.showMessageDialog(frame, "Input must be"
						+ " a single digit long.");
            } else if (!Pattern.matches("[0-2]+", gameString)) {
            	JOptionPane.showMessageDialog(frame, "Input must be"
						+ " a positive integer, using only digits"
						+ " 0, 1, or 2.");
            }
        }
        
        if (gameString.equals("0")) {
        	ConnectFourPanel panel = new ConnectFourPanel(quitItem,
        			gameItem);
        	frame.getContentPane().add(panel);
        } else if (gameString.equalsIgnoreCase("1")) {
        	ConnectFourPanel panel = new ConnectFourPanel(quitItem,
        			gameItem);
        	panel.setAI(true);
        	frame.getContentPane().add(panel);
        } else if (gameString.equals("2")) {
        	ConnectFourPanel3D panel = new ConnectFourPanel3D(quitItem,
        			gameItem);
        	frame.getContentPane().add(panel);
        }
        
		frame.pack();
		frame.setVisible(true);
	} 
}
