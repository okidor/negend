package avalone.negend;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Mouse;

import avalone.api.lwjgl.AvaloneGLAPI;
import avalone.api.lwjgl.Degrade;
import avalone.api.lwjgl.Point;
import avalone.api.lwjgl.TexturesLoader;

public class CharacterEditor 
{
	private AvaloneGLAPI glapi;
	private BufferedImage img0;private int loadedImg0;
	private BufferedImage img1;private int loadedImg1;
	private BufferedImage img2;private int loadedImg2;
	Point p0l;			Point p0r;
	Point p1l;			Point p1r;
	Point p2l;			Point p2r;
	private Point pColor;
	private int selectedColor;
	private Degrade deg1;		private int loadedDeg1;
	private Degrade deg2;		private int loadedDeg2;
	private Degrade deg3;		private int loadedDeg3;
	private Degrade deg4;		private int loadedDeg4;
	private byte click;
	private boolean close;
	
	public CharacterEditor()
	{
		img0 = TexturesLoader.loadImage("player0.png");
		img1 = TexturesLoader.loadImage("player1.png");
		img2 = TexturesLoader.loadImage("player2.png");
		p0l = new Point(5,10);			p0r = new Point(165,330);
		p1l = new Point(170,10);			p1r = new Point(330,330);
		p2l = new Point(335,10);			p2r = new Point(495,330);
		pColor = new Point(505,20);
		glapi = new AvaloneGLAPI(1040,600,"character editor",true);
		glapi.enableTextures();
		loadedImg0 = TexturesLoader.loadTexture(img0);
		loadedImg1 = TexturesLoader.loadTexture(img1);
		loadedImg2 = TexturesLoader.loadTexture(img2);
		deg1 = new Degrade(Color.white,Color.cyan,Color.yellow,Color.magenta);
		deg2 = new Degrade(Color.black,Color.blue,Color.green,Color.red);
		deg3 = new Degrade(Color.black,Color.red,Color.yellow,Color.blue);
		deg4 = new Degrade(Color.white,Color.magenta,Color.green,Color.cyan);
		loadedDeg1 = TexturesLoader.loadTexture(deg1.getImg());
		loadedDeg2 = TexturesLoader.loadTexture(deg2.getImg());
		loadedDeg3 = TexturesLoader.loadTexture(deg3.getImg());
		loadedDeg4 = TexturesLoader.loadTexture(deg4.getImg());
		selectedColor = 0;
		click = 0;
		close = false;
		mainLoop();
	}
	
	public void mainLoop()
	{
		while (!glapi.isCloseRequest() && !close) 
    	{
    		glapi.glLoopBegin();
    		
    		textureUpdate();
    		colorDisplay();
    		button();
    		mouseActions();
    		glapi.unbindTexture();
    		glapi.drawRect(new Point(500,0), new Point (501,600),"red");
    		glapi.drawFPS();
    		glapi.glLoopEnd(1000);
    	}
		glapi.destroyDisplay();
	}
	
	public void button()
	{
		glapi.unbindTexture();
		Point p1 = new Point(600,570);
		Point p2 = new Point(700,590);
		Point p3 = p2.clone(20,0);
		glapi.drawRect(p1,p3,"red");
		glapi.drawText(p1,p2,"enregistrer","black");
	}
	
	public void textureUpdate()
	{
		TexturesLoader.reloadTexture(img0,loadedImg0);
		TexturesLoader.reloadTexture(img1,loadedImg1);
		TexturesLoader.reloadTexture(img2,loadedImg2);
		glapi.drawTexturedRect(p0l,p0r,loadedImg0);
		glapi.drawTexturedRect(p1l,p1r,loadedImg1);
		glapi.drawTexturedRect(p2l,p2r,loadedImg2);
	}
	
	public String realPath(String path)
	{
		return System.getProperty("user.dir") + File.separator + "textures" + File.separator + path;
	}
	
	public void mouseActions()
	{
		if(Mouse.isButtonDown(0))
		{
			if(click < 3)
			{
				click++;
			}
			Point p = glapi.getMouse();
			if(p.x > 505 && p.x < 505+256)
			{
				if(p.y > 20 && p.y < 276)
				{
					selectedColor = deg1.getImg().getRGB(p.x-505, 256-(p.y-20));
				}
				else if(p.y > 280 && p.y < 280+256)
				{
					selectedColor = deg2.getImg().getRGB(p.x-505, 256-(p.y-280));
				}
				else if(click == 1)
				{
					if(p.x > 600 && p.x < 720)
					{
						if(p.y > 570 && p.y < 590)
						{
							File play0 = new File(realPath("player0.png"));
							File play1 = new File(realPath("player1.png"));
							File play2 = new File(realPath("player2.png"));
							try 
							{
								ImageIO.write(img0, "png", play0);
								ImageIO.write(img1, "png", play1);
								ImageIO.write(img2, "png", play2);
							} 
							catch (IOException e)
							{
								e.printStackTrace();
							}
							finally
							{
								close = true;
							}
						}
					}
				}
			}
			else if(p.x > 505+260 && p.x < 505+260+256)
			{
				if(p.y > 20 && p.y < 276)
				{
					selectedColor = deg4.getImg().getRGB(p.x-505-260, 256-(p.y-20));
				}
				else if(p.y > 280 && p.y < 280+256)
				{
					selectedColor = deg3.getImg().getRGB(p.x-505-260, 256-(p.y-280));
				}
			}
			else if(p.y > 10 && p.y < 330)
			{
				int y = 31-(p.y-10)/10;
				//System.out.println("y = " + y);
				if(p.x >= 5 && p.x < 165)
				{
					int x = (p.x-10)/10;
					//System.out.println("x = " + x);
					img0.setRGB((p.x-10)/10, y, selectedColor);
				}
				else if(p.x > 170 && p.x < 330)
				{
					int x = (p.x-170)/10;
					//System.out.println("x = " + x);
					img1.setRGB((p.x-170)/10, y, selectedColor);
				}
				else if(p.x > 335 && p.x < 495)
				{
					int x = (p.x-335)/10;
					//System.out.println("x = " + x);
					img2.setRGB((p.x-335)/10, y, selectedColor);
				}
			}
		}
		else
		{
			click = 0;
		}
		if(Mouse.isButtonDown(1))
		{
			Point p = glapi.getMouse();
			if(p.y > 10 && p.y < 330)
			{
				int y = 31-(p.y-10)/10;
				//System.out.println("y = " + y);
				if(p.x >= 5 && p.x < 165)
				{
					int x = (p.x-10)/10;
					//System.out.println("x = " + x);
					selectedColor = img0.getRGB((p.x-10)/10, y);
				}
				else if(p.x > 170 && p.x < 330)
				{
					int x = (p.x-170)/10;
					//System.out.println("x = " + x);
					selectedColor = img1.getRGB((p.x-170)/10, y);
				}
				else if(p.x > 335 && p.x < 495)
				{
					int x = (p.x-335)/10;
					//System.out.println("x = " + x);
					selectedColor = img2.getRGB((p.x-335)/10, y);
				}
			}
		}
	}
	
	public void colorDisplay()
	{
		glapi.drawTexturedRect(pColor, pColor.clone(256), loadedDeg1);
		pColor.moveCoords(0,260);
		glapi.drawTexturedRect(pColor, pColor.clone(256), loadedDeg2);
		pColor.moveCoords(260,0);
		glapi.drawTexturedRect(pColor, pColor.clone(256), loadedDeg3);
		pColor.moveCoords(0,-260);
		glapi.drawTexturedRect(pColor, pColor.clone(256), loadedDeg4);
		pColor.moveCoords(-260,0);
	}
}
