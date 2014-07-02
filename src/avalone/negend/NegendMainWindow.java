package avalone.negend;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class NegendMainWindow extends JFrame implements ActionListener 
{
	private JPanel panel;
	private JPanel upSubPanel;
	private JPanel middleSubPanel;
	private JPanel downSubPanel;
	public int mode;
	public boolean newGame;
	private int nb;
	
	public NegendMainWindow(int nb) 
	{
		super("Negend");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pack();
		this.nb = nb;
		setSize(500,400);
		panel = new JPanel(new BorderLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		getContentPane().add(panel);
		upSubPanel = new JPanel(new BorderLayout());
		upSubPanel.setLayout(new BoxLayout(upSubPanel, BoxLayout.X_AXIS));
		middleSubPanel = new JPanel(new BorderLayout());
		middleSubPanel.setLayout(new BoxLayout(middleSubPanel, BoxLayout.X_AXIS));
		downSubPanel = new JPanel(new BorderLayout());
		downSubPanel.setLayout(new BoxLayout(downSubPanel, BoxLayout.X_AXIS));
		initUI();
		//panel.add('widget', BorderLayout.CENTER);
		//setContentPane(panel);
		//pack();
		panel.add(upSubPanel);
		panel.add(Box.createHorizontalGlue());
		panel.add(middleSubPanel);
		panel.add(Box.createHorizontalGlue());
		panel.add(downSubPanel);
		setVisible(true);
	}
	
	public void initUI()
	{
		initUpPanel();
		initMidPanel();
		initDownPanel();
	}
	
	public void initUpPanel()
	{
		JLabel avatar = new JLabel("Avatar :");
		upSubPanel.add(avatar);
		initImages();
		JButton edit = new JButton("edit");
		edit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	mode = Const.EDIT_MODE;
            }
        });
		edit.setSize(60,20);
		upSubPanel.add(edit);
	}
	
	public void initMidPanel()
	{
		JLabel shadows = new JLabel("render shadows :");
		JLabel game = new JLabel("game name :");
		
		middleSubPanel.add(shadows);
		middleSubPanel.add(Box.createHorizontalGlue());
		middleSubPanel.add(game);
		GameFile gamef = new GameFile(Const.configPath);
		String s[] = gamef.al.get(0);
		boolean shadow = Boolean.valueOf(s[2]);
		JCheckBox box = new JCheckBox();
		box.setEnabled(shadow);
		
		middleSubPanel.add(box);
		
		JTextField fpath = new JTextField();
	}
	
	public void initDownPanel()
	{
		upSubPanel.add(Box.createHorizontalGlue());
		JButton game = new JButton("start game");
		
		game.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	mode = Const.PLAY_MODE;
            }
        });
		
		game.setSize(150,20);
		
		game.setAlignmentX(Component.CENTER_ALIGNMENT);
		downSubPanel.add(game);
	}
	
	public void initImages()
	{
		JLabel lab1 = new JLabel(new ImageIcon("player0.png"));
		JLabel lab2 = new JLabel(new ImageIcon("player1.png"));
		JLabel lab3 = new JLabel(new ImageIcon("player2.png"));
		
		JPanel subPanel = new JPanel();
		
		subPanel.add(lab1);
		subPanel.add(lab2);
		subPanel.add(lab3);
		upSubPanel.add(subPanel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	} 
}