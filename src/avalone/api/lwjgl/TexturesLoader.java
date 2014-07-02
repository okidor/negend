package avalone.api.lwjgl;

import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;


import static org.lwjgl.opengl.GL11.*;

public class TexturesLoader 
{
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    
    public static int loadTexture(BufferedImage image)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = image.getHeight()-1; y >= 0; y--) //modifie par rapport au code d'origine car je veux le point d'origine (0,0) en bas a gauche
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }
        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

            // You now have a ByteBuffer filled with the color data of each pixel.
            // Now just create a texture ID and bind it. Then you can load it using 
            // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

            //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            //Return the texture ID so we can bind it later again
        return textureID;
    }
    
    public static void reloadTexture(BufferedImage image,int textureID)
    {
    	int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = image.getHeight()-1; y >= 0; y--) //modifie par rapport au code d'origine car je veux le point d'origine (0,0) en bas a gauche
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }
        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

            // You now have a ByteBuffer filled with the color data of each pixel.
            // Now just create a texture ID and bind it. Then you can load it using 
            // whatever OpenGL method you want, for example:

        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

            //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    public static BufferedImage loadImage(String path)
    {
    	String absolutePath = System.getProperty("user.dir") + File.separator + "textures" + File.separator + path;
    	try
    	{
    		File f = new File(absolutePath);
    		BufferedImage img = ImageIO.read(f);
    		/*BufferedImage img = ImageIO.read(TexturesLoader.class.getClassLoader().getResource("/textures" + File.separator + path)); 
    		System.out.println(img.toString());*/
    		return img;
    		 /*When running code using java -jar app.jar, java uses ONLY the class path defined in the manifest of the JAR file (i.e. Class-Path attribute). If the class is in app.jar, or the class is in the class path set in the Class-Path attribute of the JAR's manifest, you can load that class using the following code snippet, where the className is the fully-qualified class name.

    		 final String classAsPath = className.replace('.', '/') + ".class";
    		 final InputStream input = ClassLoader.getSystemResourceAsStream( path/to/class );

    		 Now if the class is not part of the JAR, and it isn't in the manifest's Class-Path, then the class loader won't find it. Instead, you can use the URLClassLoader, with some care to deal with differences between windows and Unix/Linux/MacOSX.

    		 // the class to load
    		 final String classAsPath = className.replace('.', '/') + ".class";

    		 // the URL to the `app.jar` file (Windows and Unix/Linux/MacOSX below)
    		 final URL url = new URL( "file", null, "///C:/Users/diffusive/app.jar" );
    		 //final URL url = new URL( "file", null, "/Users/diffusive/app.jar" );

    		 // create the class loader with the JAR file
    		 final URLClassLoader urlClassLoader = new URLClassLoader( new URL[] { url } );

    		 // grab the resource, through, this time from the `URLClassLoader` object
    		 // rather than from the `ClassLoader` class
    		 final InputStream input = urlClassLoader.getResourceAsStream( classAsPath );

    		 In both examples you'll need to deal with the exceptions, and the fact that the input stream is null if the resource can't be found. Also, if you need to get the InputStream into a byte[], you can use Apache's commons IOUtils.toByteArray(...). And, if you then want a Class, you can use the class loader's defineClass(...) method, which accepts the byte[].

    		 You can find this code in a ClassLoaderUtils class in the Diffusive source code, which you can find on SourceForge at github.com/robphilipp/diffusive

    		 And a method to create URL for Windows and Unix/Linux/MacOSX from relative and absolute paths in RestfulDiffuserManagerResource.createJarClassPath(...)*/

    	}
    	catch(IOException e)
    	{
    		System.out.println("image not found");
    		System.out.println("path = " + absolutePath);
    	}
    	return null;
    }
    
    public static File checkExists(String path,String filetype)
    {
    	File f = new File(path);
    	if(!f.exists())
    	{
    		System.out.println(filetype + " " + path + " does not exist.");
    		return new File("error");
    	}
    	return f;
    }
}