package avalone.api.lwjgl;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;

public class api3Dtest 
{
    public float angleX = 0;
    public float angleY = 0;
    public float angleZ = 0;
    public float angleX2 = 0;
    public float angleY2 = 0;
    public float angleZ2 = 0;
    public float move = 0.0f;
    public float move2 = 0.0f;
    
    public void draw()
    {
    	AvaloneGLAPI3D glapi = new AvaloneGLAPI3D(900,600,"API3DTest");
       // APItest test = new APItest();
        FPoint p1 = new FPoint(-1,-1,-1);
        FPoint p2 = new FPoint(1,1,1);
        Point p3 = new Point(-4,-4,-1);
        Point p4 = new Point(-2,-2,1);
 
        // keep looping till the display window is closed the ESC key is down
        
        while (!(glapi.isCloseRequest() || Keyboard.isKeyDown(Keyboard.KEY_Q))) 
        {
        	glapi.defaultMovements();
            
            glapi.glLoopBegin();
            
            angleX = angleX + 0.1f;
            angleY = angleY + 0.2f;
            angleZ = angleZ + 0.3f;
            angleX2 = angleX2 - 0.1f;
            angleY2 = angleY2 - 0.2f;
            angleZ2 = angleZ2 - 0.3f;
            glapi.translation(0.0f, 0.0f, -10.0f);
            p1.movefx(-0.01f);
            p2.movefx(-0.01f);
            glapi.drawCube(p1,p2,"RED");
            glapi.rotation(angleX,angleY,angleZ);
            glapi.drawCube(p3,p4,"RED");
            
            glapi.glLoopEnd();
        }
        glapi.destroyDisplay();
    }
    
    public static void main(String[] argv) 
    {
        api3Dtest test = new api3Dtest();
        test.draw();
    }
}
