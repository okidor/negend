package avalone.api.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.image.BufferedImage;

import avalone.api.lwjgl.FPoint;
import avalone.api.lwjgl.Point;
import avalone.api.lwjgl.TexturesLoader;

public class StringBuilder 
{
	private int id;
	private Letter[] letters;
	
	public StringBuilder(/*AvaloneGLAPI glapi*/)
	{
		BufferedImage buf = TexturesLoader.loadImage("charsimg/chars.png");
		id = TexturesLoader.loadTexture(buf);
		//this.glapi = glapi;
	}
	
	public int getTextureID()
	{
		return id;
	}
	
	//public void

	public void buildFromImage(Point p1,Point p2,String word)
	{
		//System.out.println(p1 + " " + p2 + " " + word);
		glBindTexture(GL_TEXTURE_2D, id);
		int ind = (p2.x - p1.x)/word.length();
		Point[] p = new Point[word.length()*2];
		letters = new Letter[word.length()];
		for(int i = 0; i < word.length();i++)
		{
			p[i*2] = new Point(p1.x + i*ind,p1.y);
			p[i*2+1] = new Point(p1.x + (i+1)*ind,p2.y);
			buildFromImage(p[i*2],p[i*2+1],word.charAt(i),i);
		}
		adjust();
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void buildFromImage(Point p1,Point p2,char c,int i)
	{
		//glBindTexture(GL_TEXTURE_2D, id);
		float imglen = 256.0f;
		float length = 1.0f;
		float height = 18/imglen;
		float offsetX = 0.0f;
		float offsetY = 3/imglen;
		float rapportX = 1.0f;
		float rapportY = 1.0f;
		boolean decal = true;
		switch(c)
		{
			case 'a':
				length = 9/imglen;
				break;
			case 'b':
				offsetX = 11/imglen;
				length = 10/imglen;
				break;
			case 'c':
				offsetX = 21/imglen;
				length = 9/imglen;
				break;
			case 'd':
				offsetX = 30/imglen;
				length = 10/imglen;
				break;
			case 'e':
				offsetX = 41/imglen;
				length = 11/imglen;
				break;
			case 'f':
				offsetX = 52/imglen;
				length = 7/imglen;
				break;
			case 'g':
				offsetX = 59/imglen;
				length = 10/imglen;
				break;
			case 'h':
				offsetX = 70/imglen;
				length = 9/imglen;
				break;
			case 'i':
				offsetX = 81/imglen;
				length = 4/imglen;
				break;
			case 'j':
				offsetX = 84/imglen;
				length = 5/imglen;
				break;
			case 'k':
				offsetX = 90/imglen;
				length = 10/imglen;
				break;
			case 'l':
				offsetX = 101/imglen;
				length = 4/imglen;
				break;
			case 'm':
				offsetX = 106/imglen;
				length = 15/imglen;
				break;
			case 'n':
				offsetX = 123/imglen;
				length = 10/imglen;
				break;
			case 'o':
				offsetX = 133/imglen;
				length = 11/imglen;
				break;
			case 'p':
				offsetX = 145/imglen;
				length = 10/imglen;
				break;
			case 'q':
				offsetX = 155/imglen;
				length = 10/imglen;
				break;
			case 'r':
				offsetX = 167/imglen;
				length = 7/imglen;
				break;
			case 's':
				offsetX = 174/imglen;
				length = 8/imglen;
				break;
			case 't':
				offsetX = 182/imglen;
				length = 8/imglen;
				break;
			case 'u':
				offsetX = 190/imglen;
				length = 10/imglen;
				break;
			case 'v':
				offsetX = 200/imglen;
				length = 12/imglen;
				break;
			case 'w':
				offsetX = 212/imglen;
				length = 15/imglen;
				break;
			case 'x':
				offsetX = 227/imglen;
				length = 12/imglen;
				break;
			case 'y':
				offsetX = 238/imglen;
				length = 12/imglen;
				break;
			case 'z':
				offsetY = 25/imglen;
				length = 9/imglen;
				break;
			case '1':
				offsetY = 91/imglen;
				offsetX = 1/imglen;
				length = 9/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '2':
				offsetY = 91/imglen;
				offsetX = 11/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '3':
				offsetY = 91/imglen;
				offsetX = 22/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '4':
				offsetY = 91/imglen;
				offsetX = 33/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '5':
				offsetY = 91/imglen;
				offsetX = 45/imglen;
				length = 9/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '6':
				offsetY = 91/imglen;
				offsetX = 55/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '7':
				offsetY = 91/imglen;
				offsetX = 66/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '8':
				offsetY = 91/imglen;
				offsetX = 77/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '9':
				offsetY = 91/imglen;
				offsetX = 88/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case '0':
				offsetY = 91/imglen;
				offsetX = 99/imglen;
				length = 10/imglen;
				//height = 13/imglen;
				decal = false;
				break;
			case ' ':
				//letters[i] = new Letter(p1,p2,' ',height,length,new FPoint(0.0f,0.0f),new FPoint(1.0f,1.0f));
				return;
			case '-':
				offsetY = 114/imglen;
				offsetX = 15/imglen;
				length = 7/imglen;
				height = 15/imglen;
				break;
			default:
				System.out.println("unknown char '" + c + "'");
				letters[i] = new Letter(p1,p2,c,height,length,new FPoint(0.0f,0.0f),new FPoint(1.0f,1.0f));
				//drawCoordRect(p1,p2,new FPoint(0.0f, 0.0f), new FPoint(1.0f,1.0f));
				return;
		}
		rapportX = length/height;
		rapportY = height/length;
		letters[i] = new Letter(p1,p2,c,height,length,new FPoint(offsetX,offsetY),new FPoint(rapportX,rapportY));
		letters[i].adjustY();
		/*if(decal)
			aligner(letters[i].p1,letters[i].p2);*/
		//glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private void aligner(Point p1,Point p2)
	{
		float decalage = (4.0f/18.0f) * (p2.y - p1.y);
		int decal = (int)(decalage);
		p2.moveCoords(0,-decal);
		p1.moveCoords(0,-decal);
	}
	
	private void adjust()
	{
		int larger = 0;int ind = 0;int height = 0;
		float length = 1.0f;float lengthSup = 1.0f;float lengthMid = 0.5f;float avgLength = 0.0f;
		for(int i = 0; i < letters.length; i++)
		{
			if(letters[i] != null)
			{
				avgLength = avgLength + letters[i].length;
			}
		}
		avgLength = avgLength / (float)letters.length;
		for(int i = 0; i < letters.length; i++)
		{
			if(letters[i] != null)
			{
				if(letters[i].length > avgLength)
				{
					if(letters[i].length - avgLength < length)
					{
						length = letters[i].length;
						ind = i;
					}
				}
				else if(letters[i].length < avgLength)
				{
					if(avgLength - letters[i].length < length)
					{
						length = letters[i].length;
						ind = i;
					}
				}
			}
		}
		//System.out.println(letters[ind].c);
		//adjustY(p[ind],p[ind+1],rapportsY[ind]);
		for(int i = 0; i < letters.length; i++)
		{
			if(letters[i] != null)
			{
				letters[i].p2.y = letters[ind].p2.y;
				if(i > 0)
				{
					if(letters[i-1] != null)
					{
						letters[i].p1.x = letters[i-1].p2.x;
					}
					else
					{
						
					}
				}
				letters[i].adjustX();
				letters[i].draw();
			}
		}
	}
	
	public int adjustX(Point p1,Point p2,float rapport,int compteur)
	{
		float length = (float)(p2.x - p1.x);
		float height = (float)(p2.y - p1.y);
		if(length/height == rapport)
		{
			return p2.x;
		}
		else if(compteur > 99)
		{
			return p2.x;
		}
		else if(length/height > rapport)
		{
			return adjustX(new Point(p1.x - 1,p1.y),new Point(p2.x - 1,p2.y),rapport,compteur+1);
		}
		else if(length/height < rapport)
		{
			return adjustX(new Point(p1.x + 1,p1.y),new Point(p2.x + 1,p2.y),rapport,compteur+1);
		}
		else
		{
			System.out.println("warning, problem while adjusting letter");
			return 0;
		}
	}
	
}
