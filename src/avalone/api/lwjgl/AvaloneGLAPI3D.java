package avalone.api.lwjgl;

import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import java.lang.Math.*;

public class AvaloneGLAPI3D extends AvaloneGLAPI
{
	FPCameraController camera;
	
	float dx        = 0.0f;
    float dy        = 0.0f;
    float dt        = 0.0f; //length of frame
    float lastTime  = 0.0f; // when the last frame was
    float time      = 0.0f;

    float mouseSensitivity = 0.05f;
    float movementSpeed = 10.0f; //move 10 units per second
    
    private boolean grab = true;
	
    public AvaloneGLAPI3D(int width, int height, String title)
    {
        super(width,height,title);
        camera = new FPCameraController(0, 0, 0);
      //hide the mouse
        Mouse.setGrabbed(true);
    }
    
    public void initOpenGL(int width,int heigth, String title)
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(width, heigth));
            Display.setVSyncEnabled(true);
            Display.setTitle("Cube");
            Display.create();
        }
        catch(Exception e)
        {
            System.out.println("Error setting up display");
            System.exit(0);
        }
        
        glViewport(0,0,width,heigth);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //glOrtho(0, width, 0, heigth, 0.1f, 100f);
        glOrtho(width/2, width/2, heigth/2, heigth/2, 1, -1);
        //glOrtho(0, width, heigth, 0, 1, -1);
        GLU.gluPerspective(45.0f, ((float)width/(float)heigth),0.1f,100.0f);
        glMatrixMode(GL_MODELVIEW);
        //glLoadIdentity();

        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    public void defaultMovements()
    {
    	time = System.nanoTime();
        dt = (time - lastTime)/1000000000.0f;
        lastTime = time;

        //distance in mouse movement from the last getDX() call.
        dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        dy = Mouse.getDY();

        //controll camera yaw from x movement fromt the mouse
        camera.yaw(dx * mouseSensitivity);
        //controll camera pitch from y movement fromt the mouse
        camera.pitch(dy * mouseSensitivity);

        //when passing in the distance to move
        //we times the movementSpeed with dt this is a time scale
        //so if its a slow frame u move more then a fast frame
        //so on a slow computer you move just as fast as on a fast computer
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))//move forward
        {
        	camera.walkForward(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))//move backwards
        {
        	camera.walkBackwards(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))//strafe left
        {
        	camera.strafeLeft(movementSpeed*dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))//strafe right
        {
        	camera.strafeRight(movementSpeed*dt);
        }
        
        while (Keyboard.next()) 
        {
        	if (Keyboard.getEventKeyState()) 
        	{
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) 
                {
                	grab = !grab;
                	Mouse.setGrabbed(grab);
                }
            }
        }
    }
    
    public void glLoopBegin()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        camera.lookThrough();
    }
    
    public void drawCube(Point p1,Point p2,String c)
    {
        drawBaseCube(p1,p2,c,GL_QUADS);
    }
    
    public void translation(float axeX,float axeY,float axeZ)
    {
        GL11.glTranslatef(axeX, axeY, axeZ);
    }
    
    public void rotation(float angleX,float angleY,float angleZ)
    {
        glRotatef(angleX, angleY, angleZ, 10.0f);
    }
    
    public void drawBaseCube(Point p1,Point p2,String c,int GL_SET)
    {
        float[] colortaker = AvColor.getColorByRGB(c);
	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
	glBegin(GL_SET);
        // Front Face
        glColor3f(1.0f, 1.0f, 1.0f);
        //glNormal3f(0.0f, 0.0f, 1.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p1.x, p1.y, p2.z);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p2.x, p1.y, p2.z);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p2.x, p2.y, p2.z);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p1.x, p2.y, p2.z);
        // Back Face
        glColor3f(1.0f, 0.1f, 0.1f);
        //glNormal3f(0.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p1.x, p1.y, p1.z);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p1.x, p2.y, p1.z);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p2.x, p2.y, p1.z);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p2.x, p1.y, p1.z);
        // Top Face
        glColor3f(0.0f, 1.0f, 0.1f);
        //glNormal3f(0.0f, 1.0f, 0.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p1.x, p2.y, p1.z);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p1.x, p2.y, p2.z);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p2.x, p2.y, p2.z);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p2.x, p2.y, p1.z);
        // Bottom Face
        glColor3f(0.2f, 0.1f, 9.1f);
        //glNormal3f(0.0f, -1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p1.x, p1.y, p1.z);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p2.x, p1.y, p1.z);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p2.x, p1.y, p2.z);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p1.x, p1.y, p2.z);
        // Right face
        glColor3f(1.0f, 0.1f, 9.1f);
        //glNormal3f(1.0f, .0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p2.x, p1.y, p1.z);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p2.x, p2.y, p1.z);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p2.x, p2.y, p2.z);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p2.x, p1.y, p2.z);
        // Left Face
        glColor3f(1.0f, 1.0f, 0.0f);
        //glNormal3f(-1.0f, 0.0f, 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3i(p1.x, p1.y, p1.z);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3i(p1.x, p1.y, p2.z);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3i(p1.x, p2.y, p2.z);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3i(p1.x, p2.y, p1.z);
        glEnd();	
    }

}
