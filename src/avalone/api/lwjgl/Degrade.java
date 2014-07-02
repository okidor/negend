package avalone.api.lwjgl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Degrade 
{
	BufferedImage img;
	
	public Degrade(Color color1,Color color2,Color color3,Color color4)
	{
		img = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		int r1 = color1.getRed();	int r2 = color2.getRed();	int r3 = color3.getRed();	int r4 = color4.getRed();
		int g1 = color1.getGreen();	int g2 = color2.getGreen();	int g3 = color3.getGreen();	int g4 = color4.getGreen();
		int b1 = color1.getBlue();	int b2 = color2.getBlue();	int b3 = color3.getBlue();	int b4 = color4.getBlue();
		
		for(int i = 0;i < 256;i++)
		{
			for(int j = 0;j < 256;j++)
			{
				int r5 = interpolationLin2D(r1,r2,r3,r4,i/255.0f,j/255.0f);
				int g5 = interpolationLin2D(g1,g2,g3,g4,i/255.0f,j/255.0f);
				int b5 = interpolationLin2D(b1,b2,b3,b4,i/255.0f,j/255.0f);
				Color col = new Color(r5,g5,b5);
				img.setRGB(j, i,col.getRGB());
			}
		}
	}
	
	public BufferedImage getImg()
	{
		return img;
	}
	
	public int interpolationLin(int a,int b,float x)
	{
	    return (int) (a*(1-x)+b*x);
	}

	public int interpolationCos(int a,int b,float x)
	{
		double pi = 3.14259265;
	    int k = (int) ((1-Math.cos(x*pi))/2);
	    return interpolationLin(a,b,k);
	}

	public int interpolationCos2D(int a,int b,int c,int d,float x,float y)
	{
	    int x1 = interpolationCos(a,b,x);
	    int x2 = interpolationCos(c,d,x);
	    return interpolationCos(x1,x2,y);
	}

	public int interpolationLin2D(int a,int b,int c,int d,float x,float y)
	{
	    int x1 = interpolationLin(a,b,x);
	    int x2 = interpolationLin(c,d,x);
	    return interpolationLin(x1,x2,y);
	}
}
