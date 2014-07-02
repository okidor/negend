package avalone.api.lwjgl;

import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import java.lang.Math.*;

public class Cube 
{
    public float wid,hei,len;
    public int x, y, z;
    public int pitch = 0;
    /** width of window */
    private int viewportWidth = 800;
    public float angleX;
    public float angleY;
    public float angleZ;
    public float distance;
    public boolean zoom = true;
 
    /** height of window */
    private int viewportHeight = 600;
    
    public Cube()
    {
        angleX = 0.0f;
        angleY = 0.0f;
        angleZ = 0.0f;
        distance = 45.0f;
        init();
        x = y = z = 0;
        gameLoop();
    }
    
    private void init()
    {
        int w=800;
        int h=600;
        try
        {
            Display.setDisplayMode(new DisplayMode(w, h));
            Display.setVSyncEnabled(true);
            Display.setTitle("Cube");
            Display.create();
        }
        catch(Exception e)
        {
            System.out.println("Error setting up display");
            System.exit(0);
        }
        
        GL11.glViewport(0,0,w,h);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, ((float)w/(float)h),0.1f,100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }
    
    private void gameLoop()
    {
	while (!Display.isCloseRequested())
	{
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            render(); 
 
            Display.update();
            Display.sync(60);
        }
 
        Display.destroy();
    }
    
    public void render()
    {
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
        GL11.glRotatef(angleX, angleY, angleZ, 10.0f);
        angleX = angleX + 0.1f;
        angleY = angleY + 0.2f;
        angleZ = angleZ + 0.3f;
        int x1 = -1;
        int y1 = -1;
        int z1 = -1;
        int x2 = 1;
        int y2 = 1;
        int z2 = 1;
        GL11.glBegin(GL11.GL_QUADS);
        // Front Face
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x1, y1, z2);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x2, y1, z2);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x2, y2, z2);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x1, y2, z2);
        // Back Face
        GL11.glColor3f(1.0f, 0.1f, 0.1f);
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x1, y1, z1);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x1, y2, z1);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x2, y2, z1);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x2, y1, z1);
        // Top Face
        GL11.glColor3f(0.0f, 1.0f, 0.1f);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x1, y2, z1);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x1, y2, z2);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x2, y2, z2);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x2, y2, z1);
        // Bottom Face
        GL11.glColor3f(0.2f, 0.1f, 9.1f);
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x1, y1, z1);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x2, y1, z1);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x2, y1, z2);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x1, y1, z2);
        // Right face
        GL11.glColor3f(1.0f, 0.1f, 9.1f);
        GL11.glNormal3f(1.0f, .0f, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x2, y1, z1);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x2, y2, z1);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x2, y2, z2);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x2, y1, z2);
        // Left Face
        GL11.glColor3f(1.0f, 1.0f, 0.0f);
        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
        GL11.glVertex3i(x1, y1, z1);
        GL11.glTexCoord2f(1.0f, 0.0f);
        //GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
        GL11.glVertex3i(x1, y1, z2);
        GL11.glTexCoord2f(1.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
        GL11.glVertex3i(x1, y2, z2);
        GL11.glTexCoord2f(0.0f, 1.0f);
        //GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
        GL11.glVertex3i(x1, y2, z1);
        GL11.glEnd();		
    }
    
    public static void main(String[] argv) 
    {
	Cube cube = new Cube();
    } 
}
