package avalone.negend;

import avalone.api.lwjgl.Point;

public class SpawnChunk extends SurfaceChunk
{
	public SpawnChunk(Map map,Point pos,boolean setLight,Renderer rend,boolean init)
	{
		super(map,pos,setLight,rend,init);
	}
	
	public void setSurfaceModifier()
	{
		surfaceModifier = 10;
	}
	
	protected int chooseBiome()
	{
		return Const.plainsBiome;
	}
}
