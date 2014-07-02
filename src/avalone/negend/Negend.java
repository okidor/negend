package avalone.negend;

import java.awt.Canvas;

import avalone.api.lwjgl.AvaloneGLAPI;

import org.gnome.gtk.*;

public class Negend 
{
	public Ore[] ores;
	public Map map;
	public AvaloneGLAPI glapi;
	
	public Negend(int nb)
	{
		int mode = Const.CHOOSE_MODE;
		NegendWindow neg = new NegendWindow(this,nb);
		//Gtk.main();
		//NegendWindow neg = null;
		while(mode != Const.QUIT_MODE)
		{
			//neg = new NegendWindow(this,nb);
			neg.initImages();
			neg.showAll();
			Gtk.main();
			mode = neg.mode;
			if(mode == Const.EDIT_MODE)
			{
				new CharacterEditor();
			}
			else if(mode == Const.PLAY_MODE)
			{
				startGame(nb,neg.fpath.getText(),neg.newGame);
			}
			//System.out.println("mode number is: " + mode);
		}
		//setVisible(true);
		//neg.setVisible(true);
		//startGame(nb,"test",true);
		
		//NegendMainWindow neg = new NegendMainWindow(nb);
	}
	
	public void startGame(int nb,String gameName,boolean newGame)
	{
		glapi = new AvaloneGLAPI(Const.tailleFenX,Const.tailleFenY,"Negend",true);
		glapi.enableTextures();
    	
		ores = new Ore[nb];
		for(int i = 0;i < nb;i++)
		{
			ores[i] = createOre(i);
			System.out.println(ores[i].name);
		}
		Const.debug("(Negend): creating rederer");
		Renderer rend = new Renderer(glapi,ores,Const.blocksPath);
		Const.debug("(Negend): creating map");
		map = new Map(Const.tailleChunkX,Const.tailleChunkY,glapi,rend,ores,gameName,newGame);
		Const.debug("(Negend): entering mainLoop");
		mainLoop();
	}
	
	public void mainLoop()
	{
		while (!glapi.isCloseRequest()) 
    	{
    		glapi.glLoopBegin();
    		
    		map.actions();
    		
    		glapi.drawFPS();
    		glapi.glLoopEnd(60);
    	}
    	glapi.destroyDisplay();
    	System.out.println("attempting to save");
		map.save();
	}
	
	public static void main(String[] args)
    {
		Gtk.init(args);
    	/*RandoMining mine = */new Negend(10);
    }
	
	public Ore createOre(int i)
	{
		Ore ore = new Ore(i,glapi);
		return ore;
	}
}
