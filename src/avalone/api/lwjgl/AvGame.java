package avalone.api.lwjgl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

public abstract class AvGame 
{
	ArrayList<Object> al;
	public void init()
	{
		AvaloneGLAPI glapi = new AvaloneGLAPI(1200,600,"APITest2");
		al = new ArrayList<Object>();
		glEnable(GL_TEXTURE_2D);
		while (!Display.isCloseRequested()) 
        {
			glapi.glLoopBegin();
			render(glapi);
			glapi.glLoopEnd();

		    Display.update();
		}
		Display.destroy();
	}
	
	abstract void initVars();
	
	abstract void render(AvaloneGLAPI glapi);
}
