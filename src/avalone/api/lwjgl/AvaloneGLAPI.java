package avalone.api.lwjgl; 

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;

import avalone.api.util.StringBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AvaloneGLAPI
{
	protected Point scroll;
	protected Point modif;
	protected Point scale;
	protected int[] texturesID;
	protected String[] texturesName;
	protected BufferedImage[] bufImg;
	protected HashMap<String,Integer> hm;
	protected HashMap<String,BufferedImage> hmb;
	protected StringBuilder stp;
	protected long lastFPS;
	protected int fps;
	protected long lastFrame;
	protected int drawFPS;
	protected boolean windowCloseRequest;
	
	protected JFrame jf;
	protected Canvas can;
	
    public AvaloneGLAPI()
    {
        this(500,500,"Powered By AvaloneGLAPI");
    }
    
    public AvaloneGLAPI(int width, int height, String title)
    {
    	this(width, height,title,false);
    }

    public AvaloneGLAPI(int width, int height, String title,boolean windowed)
    {
    	scroll = new Point();
    	modif = new Point();
    	scale = new Point(1,1);
    	windowCloseRequest = false;
    	loadNatives();
    	initOpenGL(width, height, title, windowed);
        initTextures();
        stp = new StringBuilder();
        lastFPS = getTime();
    }
    
    private void setWindow(int width, int height, String title)
    {
    	jf = new JFrame(title);
    	jf.setSize(width+2, height+29);
    	jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	jf.addWindowListener(new WindowAdapter() 
    	{
            public void windowClosing(WindowEvent e) 
            {
            	jf.setVisible(false);
            	jf.dispose();
            	windowCloseRequest = true;
            }
        });
		can = new Canvas();
		can.setSize(width,height);
		can.setFocusable(true);
		can.requestFocus();
		can.setIgnoreRepaint(true);
		JPanel container = new JPanel(new BorderLayout());
		container.add(can, BorderLayout.CENTER);
		jf.add(container);
		jf.setVisible(true);
    }
    
    public JFrame getJFrame()
    {
    	return jf;
    }
    
    public long getTime() 
    {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
    
    public void initTextures()
    {
    	File dir = new File("textures");
    	File[] files = dir.listFiles();
    	hm = new HashMap<String,Integer>();
    	hmb = new HashMap<String,BufferedImage>();
    	loadFolderTextures(files);
    	unbindTexture();
    }
    
    public void loadFolderTextures(File[] files)
    {
    	for(int i = 0; i < files.length;i++)
    	{
    		String textureName = files[i].getName();
    		if(textureName.endsWith(".jpg") || textureName.endsWith(".png"))
    		{
    			
    			System.out.println("loaded texture " + textureName);
    			BufferedImage bufImg = TexturesLoader.loadImage(textureName);
    			Integer textureID = TexturesLoader.loadTexture(bufImg);
    			System.out.println("id attributed: " + textureID);
    			hm.put(textureName,textureID);
    			hmb.put(textureName, bufImg);
    		}
    	}
    }

    public void initOpenGL(int width,int heigth, String title,boolean windowed)
    {
    	try
    	{
    		setDisplayMode(width, heigth);
    		if(windowed)
    		{
    			setWindow(width,heigth,title);
    			Display.setParent(can);
    		}
    		else
    		{
    			Display.setTitle(title);
    		}
       		Display.create();
    	}
    	catch (LWJGLException e) 
    	{
    		String path = System.getProperty("user.dir") + File.separator + "lwjgl";
    		System.out.println("problem with lwjgl, path should be : " + path + "\nprinting stackstrace...");
    		e.printStackTrace();
    		System.exit(0);
    	}
    	glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
    	glViewport(0,0,Display.getDisplayMode().getWidth(),Display.getDisplayMode().getHeight());
    	glEnable(GL_BLEND);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    
    private void setDisplayMode(int width,int heigth)
    {
    	try
    	{
    		Display.setDisplayMode(new DisplayMode(width, heigth));
    	}
    	catch(LWJGLException e) 
    	{
    		try
    		{
    			Display.destroy();
    			Display.setDisplayMode(new DisplayMode(width, heigth));
    		}
    		catch(LWJGLException e1) 
        	{
    			e1.printStackTrace();
        	}
    	}
    }

    public void mustsleep(int p)
    {
    	try
    	{
    		Thread.sleep(p);
    	}
    	catch(InterruptedException ie)
    	{
    		ie.printStackTrace();
    		System.exit(0);
    	}
    }
    
    private void loadNatives()
    {
        System.out.println("[AvaloneGLAPI] Loading LWJGL native files...");
        
        try
        {
        	//System.out.println(System.getProperty("user.dir") + File.separator + "lwjgl" + File.separator + "natives");
            System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + File.separator + "lwjgl" + File.separator + "natives");
            System.out.println("[AvaloneGLAPI] Native files loaded!\n");
        }
        catch(Exception e)
        {
            System.err.println("[AvaloneGLAPI] Failed to load natives\nYour application will shut down...");
            System.exit(0);
        }
    }
    
    public void enableTextures()
    {
    	glEnable(GL_TEXTURE_2D);
    }
    
    public void setVerticalOffset(int dy)
    {
    	scroll.y = scroll.y + dy;
    }
    
    public Point getMouse()
    {
    	Point p = new Point(Mouse.getX(),Mouse.getY());
    	p.moveCoords(scroll.x,scroll.y);
    	return p;
    }
    
    public String checkMouseWheel()
    {
    	int dWheel = Mouse.getDWheel();
        if (dWheel < 0) 
        {
            return "DOWN";
        } 
        else if (dWheel > 0)
        {
            return "UP";
        }
        else
        {
        	return "EQUAL";
        }
    }
    
    public int changeValueWithWheel(int val,int mul)
    {
    	if(mul == 0)
    	{
    		System.out.println("Warning, value linked to wheel won't change.");
    	}
    	String s = checkMouseWheel();
    	if(s.equals("DOWN"))
    	{
    		val = val - (1 * mul);
    	}
    	else if(s.equals("UP"))
    	{
    		val = val + (1 * mul);
    	}
    	return val;
    }
    
    public void scroll(int dx,int dy)
    {
    	scroll.moveCoords(dx, dy);
    	glLoadIdentity();
    	glOrtho(scroll.x, Display.getDisplayMode().getWidth()+scroll.x, scroll.y, Display.getDisplayMode().getHeight()+scroll.y, -1.0, 1.0);
    }
    
    public void setView(int x,int y)
    {
    	scroll.setCoords(x, y);
    	glLoadIdentity();
    	glOrtho(scroll.x, Display.getDisplayMode().getWidth()+scroll.x, scroll.y, Display.getDisplayMode().getHeight()+scroll.y, -1.0, 1.0);
    }
    
    public void zoom(int z)
    {
    	if(z % 2 != 0)
    	{
    		z++;
    	}
    	int totalWidth = Display.getDisplayMode().getWidth();
    	int totalHeight = Display.getDisplayMode().getHeight();
    	modif.x = -((z-1)*totalWidth)/2;
    	modif.y = -((z-1)*totalHeight)/2;
    	scale.x = z;
    	scale.y = z;
    	glViewport(scroll.x+modif.x,scroll.y+modif.y,totalWidth*z,totalHeight*z);
    }
    
    public void unzoom(int z)
    {
    	if(z % 2 != 0)
    	{
    		z--;
    	}
    	int totalWidth = Display.getDisplayMode().getWidth();
    	int totalHeight = Display.getDisplayMode().getHeight();
    	modif.x = ((z-1)*totalWidth)/(2*z);
    	modif.y = ((z-1)*totalHeight)/(2*z);
    	glViewport(scroll.x+modif.x,scroll.y+modif.y,totalWidth/z,totalHeight/z);
    }
    
    public void clearZoom()
    {
    	modif.x = 0;
    	modif.y = 0;
    	scale.x = 1;
    	scale.y = 1;
    	glViewport(scroll.x,scroll.y,Display.getDisplayMode().getWidth(),Display.getDisplayMode().getHeight());
    }
    
    public boolean isCloseRequest()
    {
    	return Display.isCloseRequested() || windowCloseRequest;
    }

    public void glLoopBegin()
    {
    	glClear(GL_COLOR_BUFFER_BIT);
    }

    public void glLoopEnd(int i)
    {
    	if(!isCloseRequest())
    	{
    		Display.update();
    	}
    	Display.sync(i);
    	
    }
    
    public void glLoopEnd()
    {
    	glLoopEnd(80);
    }

    public void destroyDisplay()
    {
    	if(!isCloseRequest())
		{
    		Display.destroy();
    		jf.setVisible(false);
        	jf.dispose();
		}
    }
    
    public double distance(Point p1,Point p2)
    {
	double distance = Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
	return distance;
    }

    public Point wait_clic()
    {
	Point p1 = new Point();
	boolean flag = true;
	int i = 0;
	while(flag)
	{
	    if(Mouse.isButtonDown(0))
	    {
		flag = false;
		p1.x = Mouse.getX();
		p1.y = Mouse.getY();
		System.out.println("clicked");
	    }
	    mustsleep(100);
	    i++;
	    System.out.println("waiting " + i + " times");
	}
	return p1;
    }

    public int abs(int nb)
    {
        if(nb < 0)
        {
            return -nb;
        }
        return nb;
    }
    
    public static int maxi(int n1,int n2)
    {
        if(n1 > n2)
        {
            return n1;
        }
        return n2;
    }
    
    public static int mini(int n1,int n2)
    {
        if(n1 < n2)
        {
            return n1;
        }
        return n2;
    }
    
    public void drawCoordRect(Point p1,Point p2,FPoint beginIndex,FPoint endIndex)
	{
    	if(beginIndex.fx > endIndex.fx && beginIndex.fy > endIndex.fy)
    	{
    		System.out.println("Warning, wrong indexes. Doing changes...");
    		FPoint tmp = beginIndex.clone(0);
    		beginIndex = endIndex.clone(0);
    		endIndex = tmp.clone(0);
    	}
    	if(beginIndex.fx < 0)
    	{
    		System.out.println("debug1");
    		beginIndex.fx = 0;
    	}
    	if(endIndex.fx > 1)
    	{
    		System.out.println("debug2");
    		endIndex.fx = 1;
    	}
    	if(beginIndex.fy < 0)
    	{
    		System.out.println("debug3");
    		beginIndex.fy = 0;
    	}
    	if(endIndex.fy > 1)
    	{
    		System.out.println("debug4");
    		endIndex.fy = 1;
    	}
		glBegin(GL_QUADS);
    	glTexCoord2f(beginIndex.fx, beginIndex.fy);
    	glVertex2i(p1.x, p1.y);
    	glTexCoord2f(beginIndex.fx, endIndex.fy);
    	glVertex2i(p1.x, p2.y);
    	glTexCoord2f(endIndex.fx, endIndex.fy);
    	glVertex2i(p2.x, p2.y);
    	glTexCoord2f(endIndex.fx, beginIndex.fy);
    	glVertex2i(p2.x, p1.y);
    	glEnd();
	}
    
    public void drawTexturedCoordRect(Point p1,Point p2,FPoint beginIndex,FPoint endIndex,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
		drawCoordRect(p1,p2,beginIndex,endIndex);
    }
    
    private void drawColoredRect(Point p1,Point p2,String c,int GL_SET,float alpha)
    {
		float[] colortaker = AvColor.getColorByRGB(c);
		glColor4f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f,alpha);
    	drawBaseRect(p1,p2,GL_SET);
    }
    
    public void drawColoredRect(Point p1,Point p2,int r,int g,int b,int a)
    {
    	glColor4f(r/255.0f,g/255.0f,b/255.0f,a/255.0f);
    	drawBaseRect(p1,p2,GL_QUADS);
    }
    
    private void drawColoredMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String c,int GL_SET,float alpha)
    {
    	float[] colortaker = AvColor.getColorByRGB(c);
		glColor4f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f,alpha);
    	for(int i = 0;i < p1s.size();i++)
        {
        	drawBaseRect(p1s.get(i),p2s.get(i),GL_SET);
        }
    }

    private void drawBaseRect(Point p1,Point p2,int GL_SET)
    {
    	glBegin(GL_SET);
    	glTexCoord2f(0.0f, 0.0f);
    	glVertex2i(p1.x, p1.y);
    	glTexCoord2f(0.0f, 1.0f);
    	glVertex2i(p1.x, p2.y);
    	glTexCoord2f(1.0f, 1.0f);
    	glVertex2i(p2.x, p2.y);
    	glTexCoord2f(1.0f, 0.0f);
    	glVertex2i(p2.x, p1.y);
    	glEnd();
    }
    
    public void clearFilter()
    {
    	glColor4f(1.0f,1.0f,1.0f,1.0f);
    }
    
    public void drawAlphaRect(Point p1,Point p2,String c,float alpha)
    {
    	drawColoredRect(p1,p2,c,GL_QUADS,alpha);
    }
    
    public void drawMultiAlphaRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String c,float alpha)
    {
    	drawColoredMultiRect(p1s,p2s,c,GL_QUADS,alpha);
    }
    
    public void setFilter(String c)
    {
    	float[] colortaker = AvColor.getColorByRGB(c);
		glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    }
    
    public void setFilter(float[] color)
    {
		glColor4f(color[0],color[1],color[2],color[3]);
    }
    
    public float[] getFilter(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	Color col = new Color(img.getRGB(i, j));
    	float[] colorComp = new float[4];
    	colorComp[0] = col.getRed()/255.0f;
    	colorComp[1] = col.getGreen()/255.0f;
    	colorComp[2] = col.getBlue()/255.0f;
    	colorComp[3] = 0.4f;
    	return colorComp;
    }
    
    public void setRandomFilter(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	Color col = new Color(img.getRGB(i, j));
    	glColor4f(col.getRed()/255.0f,col.getGreen()/255.0f,col.getBlue()/255.0f,0.4f);
    }
    
    public void randTexture(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	BufferedImage img2 = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    	img2.setRGB(0, 0, img.getRGB(i, j));
    }
    
    public void drawTexturedRect(Point p1,Point p2,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
    	drawRect(p1,p2);
    }
    
    public void drawMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s)
    {
    	for(int i = 0;i < p1s.size();i++)
        {
        	drawRect(p1s.get(i),p2s.get(i));
        }
    }
    
    public void drawTexturedMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
        drawMultiRect(p1s,p2s);
    }
    
    public void drawTexturedRect(Point p1,Point p2,int id)
    {
    	glBindTexture(GL_TEXTURE_2D, id);
    	drawRect(p1,p2);
    }
    
    public void unbindTexture()
    {
    	glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public int getIDFromName(String path)
    {
    	Integer i = hm.get(path);
    	if(i != null)
    	{
    		return i.intValue();
    	}
    	else
    	{
    		return hm.get("default.png");
    	}
    }
    
    public BufferedImage getImgFromName(String path)
    {
    	BufferedImage img = hmb.get(path);
    	if(img != null)
    	{
    		return img;
    	}
    	else
    	{
    		return hmb.get("default.png");
    	}
    }
    
    public void drawFPS()
    {
    	int h = Display.getHeight();
    	Point p1 = new Point(10+scroll.x,h-30+scroll.y);
    	Point p2 = new Point(40+scroll.x,h-10+scroll.y);
    	if (getTime() - lastFPS > 1000) 
		{
    		drawFPS = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
		drawText(p1,p2,drawFPS,"red");
    }
    
    public void drawText(Point p1,Point p2,String text,String c)
    {
    	setFilter(c);
		stp.buildFromImage(p1,p2,text);
		clearFilter();
    }
    
    public void drawText(Point p1,Point p2,int number,String c)
    {
    	String s = Integer.toString(number);
    	drawText(p1,p2,s,c);
    }

    public void drawRect(Point p1,Point p2,String c)
    {
    	drawColoredRect(p1,p2,c,GL_QUADS,1.0f);
    }
    
    public void drawRect(Point p1,Point p2)
    {
    	drawBaseRect(p1,p2,GL_QUADS);
    }

    public void drawEmptyRect(Point p1,Point p2,String c)
    {
    	drawColoredRect(p1,p2,c,GL_LINE_LOOP,1.0f);
    	Point p3 = new Point(p1.x-1,p1.y-1);
    	drawPixel(p3,c);
    }

    public void drawCenterRect(Point p1,int tailleX,int tailleY,String c)
    {
    	Point p2 = new Point(p1.x-tailleX/2,p1.y-tailleY/2);
    	Point p3 = new Point(p1.x+tailleX/2,p1.y+tailleY/2);
    	drawRect(p2,p3,c);
    }

    public void drawBaseQuad(Point p1,Point p2,Point p3,Point p4,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p1.x, p1.y);
		glVertex2i(p2.x, p2.y);
    	if(p2.y>p1.y)
    	{
    		if(p3.y<p4.y)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.y<p1.y)
    	{
    		if(p3.y>p4.y)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.x<p1.x)
    	{
    		if(p3.x>p4.x)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.x>p1.x)
    	{
    		if(p3.x<p4.x)
    		{

    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	else
    	{
    		glVertex2i(p3.x, p3.y);
	   		glVertex2i(p4.x, p4.y);
    	}
    	glEnd();
    }

    public void drawQuad(Point p1,Point p2,Point p3,Point p4,String c)
    {
    	drawBaseQuad(p1,p2,p3,p4,c,GL_QUADS);
    }

    public void drawEmptyQuad(Point p1,Point p2,Point p3,Point p4,String c)
    {
    	drawBaseQuad(p1,p2,p3,p4,c,GL_LINE_LOOP);
    }

    public void drawCircle(Point centre,int radius,String c)
    {
    	//float colortaker[] = AvColor.getColorByRGB(c);
    	//glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_POINTS);
    	Point p1 = new Point();
		for(p1.x = centre.x-radius/2;p1.x <= centre.x+radius/2;p1.x++)
		{
			for(p1.y = centre.y-radius/2;p1.y <= centre.y+radius/2;p1.y++)
			{
				double dist = distance(centre,p1);
				if(dist <= radius/2)
				{
					drawRawPixel(p1,c);
				}
			}
		}
		glEnd();
    }

    public void drawEllipse(Point f1,Point f2,int dist,String c)
    {
    	//float colortaker[] = AvColor.getColorByRGB(c);
    	//glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	Point p1 = new Point();
    	double i,j;
    	glBegin(GL_POINTS);
    	for (p1.y=f1.y-dist;p1.y<f2.y+dist;p1.y++)
    	{
    		for (p1.x=f1.x-dist;p1.x<f2.x+dist;p1.x++)
    		{
    			i = distance(p1,f1);
    			j = distance(p1,f2);
    			if ((i+j)<=dist)
    			{
    				drawRawPixel(p1,c);
    			}
    		}
    	}
    	glEnd();
    }

    public void drawBaseThunder(Point p,int taille,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	float longu = taille/2 + taille/10;
    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taille);
    	glVertex2f(p.x+longu, p.y+taille-longu);
    	glVertex2d(p.x+taille-longu, p.y+longu-(taille/5)*Math.sqrt(2));

    	glVertex2i(p.x+taille, p.y);
    	glVertex2f(p.x+taille-longu, p.y+longu);
		glVertex2d(p.x+longu, p.y+taille-longu+(taille/5)*Math.sqrt(2));
		glEnd();
    }

    public void drawThunder(Point p,int taille,String c)
    {
    	drawBaseThunder(p,taille,c,GL_TRIANGLES);
    }

    public void drawEmptyThunder(Point p,int taille,String c)
    {
    	drawBaseThunder(p,taille,c,GL_LINE_LOOP);
    }

    public void drawBaseStar(Point p,int taillebranche,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taillebranche*3);
    	glVertex2i(p.x+taillebranche*4, p.y+taillebranche*3);
    	glVertex2i(p.x+taillebranche*2, p.y-1);
    	glEnd();

    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taillebranche);
    	glVertex2i(p.x+taillebranche*4, p.y+taillebranche);
    	glVertex2i(p.x+taillebranche*2, p.y+taillebranche*4+1);
		glEnd();
    }

    public void drawStar(Point p,int taillebranche,String c)
    {
    	drawBaseStar(p,taillebranche,c,GL_TRIANGLES);
    }

    public void drawEmptyStar(Point p,int taillebranche,String c)
    {
    	drawBaseStar(p,taillebranche,c,GL_LINE_LOOP);
    }

    public void drawCenterStar(Point p,int taillebranche,String c)
    {
    	Point r = new Point(p.x-taillebranche*2,p.y-taillebranche*2);
    	drawStar(r,taillebranche,c);
    }

    public void drawPixel(Point p1, String c)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_POINTS);
    	glVertex2i(p1.x,p1.y);
    	glEnd();
    }
    
    protected void drawRawPixel(Point p1, String c)		//glBegin avant, glEnd apres la méthode
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glVertex2i(p1.x,p1.y);
    }

    public void drawBaseTriangle(Point p1,Point p2,Point p3,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p1.x, p1.y);
    	glVertex2i(p2.x, p2.y);
    	glVertex2i(p3.x, p3.y);
    	glEnd();
    }

    public void drawTriangle(Point p1,Point p2,Point p3,String c)
    {
    	drawBaseTriangle(p1,p2,p3,c,GL_TRIANGLES);
    }

    public void drawEmptyTriangle(Point p1,Point p2,Point p3,String c)
    {
    	drawBaseTriangle(p1,p2,p3,c,GL_LINE_LOOP);
    }
	
    public void drawLine(Point p1,Point p2,String c)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
        glBegin(GL_LINES);
        glVertex2i(p1.x, p1.y);
        glVertex2i(p2.x, p2.y);
        glEnd();
    }
}
