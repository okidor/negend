package avalone.api.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import avalone.api.lwjgl.FPoint;
import avalone.api.lwjgl.Point;

public class Letter 
{
	Point p1;
	Point p2;
	char c;
	float height;
	float length;
	FPoint offset;
	FPoint rapport;
	
	public Letter(Point p1,Point p2,char c,float height,float length,FPoint offset,FPoint rapport)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
		this.height = height;
		this.length = length;
		this.offset = offset;
		this.rapport = rapport;
	}
	
	public void adjustY()
	{
		float renderLength = (float)(p2.x - p1.x);
		int newHeight = (int)(renderLength * rapport.fy);
		p2.y = p1.y + newHeight;
	}
	
	public void adjustX()
	{
		float renderHeight = (float)(p2.y - p1.y);
		int newLength = (int)(renderHeight * rapport.fx);
		p2.x = p1.x + newLength;
	}
	
	public void draw()
	{
		drawCoordRect(p1, p2, new FPoint(offset.fx,1.0f - (offset.fy+height)), new FPoint(offset.fx+length,1.0f - offset.fy));
	}
	
	private void drawCoordRect(Point p1,Point p2,FPoint beginIndex,FPoint endIndex)
	{
		//System.out.println("xmin = " + p1.x + " et xmax = " + p2.x + ", ymin = " + p1.y + " et ymax = " + p2.y);
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
    	//System.out.println("fxmin = " + beginIndex.fx + " et fxmax = " + endIndex.fx + ", fymin = " + beginIndex.fy + " et fymax = " + endIndex.fy);
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
}

