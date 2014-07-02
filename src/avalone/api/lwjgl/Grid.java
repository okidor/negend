package avalone.api.lwjgl;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;

public class Grid 
{
	AvaloneGLAPI3D glapi;
	public Cube[] cubes;
	
	public Grid()
	{
		AvaloneGLAPI3D glapi = new AvaloneGLAPI3D(900,600,"API3DTest");
		draw();
	}
	
	public void draw()
	{
		while (!(glapi.isCloseRequest() || Keyboard.isKeyDown(Keyboard.KEY_Q))) 
        {
        	glapi.defaultMovements();
            
            glapi.glLoopBegin();
            
            glapi.glLoopEnd();
        }
        glapi.destroyDisplay();
	}
	
	public static void main(String[] argv) 
	{
	    Grid grid = new Grid();
	}
}
