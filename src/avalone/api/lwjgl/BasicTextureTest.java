package avalone.api.lwjgl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

public class BasicTextureTest 
{
    private static final int WIDTH = 800, HEIGHT = 600;
   
    public static void main(String[] args)
    { 
        try
        {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }
      
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1); //2D projection matrix
        glMatrixMode(GL_MODELVIEW);
      
        glClearColor(0, 1, 0, 0); //Green clear color
      
        //Generate a small test image by drawing to a BufferedImage
        //It's of course also possible to just load an image using ImageIO.load()
        BufferedImage test = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = test.createGraphics();

        g2d.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        g2d.fillRect(0, 0, 128, 128); //A transparent white background
      
        g2d.setColor(Color.red);
        g2d.drawRect(0, 0, 127, 127); //A red frame around the image
        g2d.fillRect(10, 10, 10, 10); //A red box 
      
        g2d.setColor(Color.blue);
        g2d.drawString("Test image", 10, 64); //Some blue text
        //BufferedImage test = TexturesLoader.loadImage("textures/test.png");
      
        int textureID = TexturesLoader.loadTexture(test);
      
        glEnable(GL_TEXTURE_2D); //Enable texturing
      
        while(!Display.isCloseRequested())
        {
            glClear(GL_COLOR_BUFFER_BIT);
         
            //Enable blending so the green background can be seen through the texture
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
         
            glPushMatrix();
            glTranslatef(100, 100, 0);
            glBindTexture(GL_TEXTURE_2D, textureID);
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex2f(0, 0);
            
                glTexCoord2f(1, 0);
                glVertex2f(128, 0);
            
                glTexCoord2f(1, 1);
                glVertex2f(128, 128);
            
                glTexCoord2f(0, 1);
                glVertex2f(0, 128);
            }
            glEnd();
            glPopMatrix();
         
            Display.update();
        }
    }
}