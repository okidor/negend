package avalone.api.lwjgl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.GL_QUADS;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import avalone.api.util.StringBuilder;

public class APItest2 
{
	Point p1;
	public StringBuilder stp;
	
	public APItest2()
	{
		AvaloneGLAPI glapi = new AvaloneGLAPI(1200,600,"APITest2");
		//stp = new StringBuilder(glapi);
		glEnable(GL_TEXTURE_2D);
		glLoadIdentity();
		glOrtho(-50, Display.getDisplayMode().getWidth()-50, 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
		while (!Display.isCloseRequested()) 
        {
			glapi.glLoopBegin();
			render(glapi);
			glapi.glLoopEnd();

		    Display.update();
		}
		Display.destroy();
	}
	
	public void render(AvaloneGLAPI glapi)
	{
		//glBindTexture(GL_TEXTURE_2D, stp.getTextureID());
		//glapi.drawRect(new Point(150,150),new Point(406,406),"red");
		Point p1 = new Point(100,100);
		Point p2 = p1.clone(500);
		glapi.unbindTexture();
		Point p3 = new Point(700,150);
		Point p4 = p3.clone(90,130);
		//glapi.drawText(p3,p4,"abcg","red");
		glapi.drawEmptyRect(p3.clone(-1),p4.clone(1),"white");
		glapi.drawText(p1,p2,"abcdefghijklmnopqrstuvwxyz123456789","red");
		glapi.drawEmptyRect(p1,p2,"white");
	}
	
	public static void main(String[] argv)
    {
    	new APItest2();
    }
}
