package avalone.negend;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private int x,y;
	private String imageName;
	
	public ImagePanel(int x,int y,String imageName)
	{
		this.x = x;
		this.y = y;
		this.imageName = imageName;
	}
	
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 

        // tu dessines ton image
        //g.drawImage(new ImageIcon(imageName), x, y, this);
    }
}
