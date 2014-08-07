package avalone.negend;

public class Const 
{
	public static final boolean debug = true;
	
	public static final int tailleChunkX = 75;
	public static final int tailleChunkY = 38;
	public static final int tailleCase = 16;
	public static final int tailleItem = 10;
	public static final int tailleFenX = 1200;
	public static final int tailleFenY = 608;
	public static final int totalJump = 2;
	public static final int unplacableOffset = 1073741823;
	public static final int modOffset = 1024;
	public static final int depl = 5;
	
	public static final String blocksPath = "mod/blocks.txt";
	public static final String configPath = "mod/conf.txt";
	public static final String savePath = "save/gamelist.txt";
	
	public static final int maxLight = 6;
	public static final int gravity = -1;
	public static final int maxLevel = 1000;
	public static final int startGenHeight = 3;
	
	public static final int EDIT_MODE = 0;
	public static final int PLAY_MODE = 1;
	public static final int QUIT_MODE = 2;
	public static final int CHOOSE_MODE = 3;
	
	public static final int noMetaData = 0;
	public static final int defaultSubID = 0;
	public static final int upSubID = 1;
	public static final int leftSubID = 2;
	public static final int rightSubID = 3;
	public static final int mineralID = Const.unplacableOffset;
	
	public static final int hillsBiome = 0;
	public static final int plainsBiome = 1;
	public static final int mountainsBiome = 2;
	public static final int oceanBiome = 3;
	public static final int beachBiome = 4;
	public static final int deepOceanBiome = 5;
	public static final int skyMountainsBiome = 6;
	public static final int nbBiomes = 7;
	
	public static int max(int a,int b)
	{
		if(a > b)
		{
			return a;
		}
		return b;
	}
	
	public static int max(int a,int b,int c)
	{
		return max(max(a,b),c);
	}
	
	public static int max(int a,int b,int c,int d)
	{
		return max(max(a,b),max(c,d));
	}
	
	public static void debug(String words)
	{
		if(debug)
		{
			System.out.println("[debug] " + words);
		}
	}
}
