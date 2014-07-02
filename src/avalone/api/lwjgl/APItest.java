package avalone.api.lwjgl;

import java.io.IOException;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
 
public class APItest
{
    public static Point p1;
    public static Point p2;
    public static Point p3;
    public static Point p4;

    public APItest()
    {
    	p1 = new Point();
    	p2 = new Point();
		p3 = new Point();
		p4 = new Point();
    }

    public static void main(String[] argv) throws IOException 
    {
    	AvaloneGLAPI glapi = new AvaloneGLAPI(1200,600,"APITest");
        /*APItest test =*/ new APItest();
        
        glEnable(GL_TEXTURE_2D); //Enable texturing
        int z = 0;
        boolean forward = true; 
        while (!Display.isCloseRequested()) 
        {
        	glapi.glLoopBegin();
        	if(z < 100 && forward)
        	{
        		z++;
        		glViewport(z,0,Display.getDisplayMode().getWidth(),Display.getDisplayMode().getHeight());
        	}
        	else if(z == 100 && forward)
        	{
        		forward = false;
        	}
        	else if(z > -100 && !forward)
        	{
        		z--;
        		glViewport(z,0,Display.getDisplayMode().getWidth(),Display.getDisplayMode().getHeight());
        	}
        	else if(z == -100 && !forward)
        	{
        		forward = true;
        	}
        	//insertion du code ici
        	
        	p1.x = -10; p2.x = 10;
        	p1.y = -10; p2.y = 10;
        	glapi.drawRect(p1,p2,"GREEN");
        	glBegin(GL_QUADS);
        	glVertex2i(-50, -50);
        	glVertex2i(50, -50);
        	glVertex2i(50, 50);
        	glVertex2i(-50, 50);
        	glEnd();
        	
        	p1.x = 20; p2.x = 40;
        	p1.y = 20; p2.y = 40;
        	glapi.drawRect(p1,p2,"RED");
        	glapi.drawEmptyRect(p1,p2,"WHITE");
            
        	p1.x = 100;
        	p2.x = 300;
        	glapi.drawCircle(p2,20,"BLUE");
            
        	p2.x = 120;
        	p1.y = 200; p2.y = 180;
        	glapi.drawEllipse(p1,p2,50,"YELLOW");
            
        	p3.y = 150;
        	p1.x = 600; p2.x = 800; p3.x = 700;
        	glapi.drawTriangle(p1,p2,p3,"GREEN");
        	glapi.drawEmptyTriangle(p1,p2,p3,"WHITE");
            
        	p1.x = 1000;
        	glapi.drawStar(p1,30,"DARK_BLUE");
	    	glapi.drawEmptyStar(p1,30,"WHITE");
	    	
	    	p1.y = 500;
	    	glapi.drawThunder(p1,30,"WHITE");
            
            p1.x = 400; p2.x = 450;
            p1.y = 150; p2.y = 200;
            glapi.drawTexturedRect(p1,p2,"test.png"); //x et y normaux
            
            p1.y = 100;p2.y = 50;
            glapi.drawTexturedRect(p1,p2,"test.png"); //y inversé
            
            p1.x = 550;p2.x = 500;
            glapi.drawTexturedRect(p1,p2,"test.png"); //x et y inversés
            
            p1.y = 150;p2.y = 200;
            glapi.drawTexturedRect(p1,p2,"test.png"); //x inversé
            glapi.unbindTexture();
            
            p1.x = 1050;p2.x = 1100;
            p1.y = 250;p2.y = 300;
            glapi.drawAlphaRect(p1,p2,"RED",0.3f);
            
            p1.y = 400;p2.y = 450;
            p1.x = 750;p2.x = 800;
            glapi.drawText(p1,p2,"test","BLUE");
            
            p1.y = 300;
            p2.y = 330;
            glapi.drawText(p1,p2,"abcde","BLUE");
            
            p1.x = 100;p2.x = 355;
            p1.y = 515;p2.y = 560;
            glapi.drawText(p1,p2,"dégradé lwjgl","BLUE");
            p1.y = 250;p2.y = 505;
            
            glBegin(GL_QUADS);
            glColor3f(0,0,0);
            glVertex2i(p1.x,p1.y);
            glColor3f(0,1,0);
            glVertex2i(p2.x,p1.y);
            glColor3f(1,0,0);
            glVertex2i(p2.x,p2.y);
            glColor3f(0,0,1);
            glVertex2i(p1.x,p2.y);
            glEnd();
            
            p1.x = 400;p2.x = 600;
            p1.y = 515;p2.y = 560;
            glapi.drawText(p1,p2,"dégradé okidor","BLUE");
            p1.y = 250;p2.y = 505;
            
            glBegin(GL_POINTS);
            for(int i = 0;i < 256;i++)
            {
                for(int j = 0; j < 256;j++)
                {
                    int red = (i*j)/255;
                    int green = i-red;
                    int blue = j-red;
                    glColor3f(red/255.0f,green/255.0f,blue/255.0f);
                    glVertex2i(p1.x+i,p1.y+j);
                }
            }
            glEnd();
            glapi.glLoopEnd();

	    Display.update();
	}
	Display.destroy();
    }

}
