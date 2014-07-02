package avalone.negend;

import avalone.api.lwjgl.Point;

public class SpawnChunk extends SurfaceChunk
{
	public SpawnChunk(Point pos,boolean setLight,Renderer rend)
	{
		super(pos,null,setLight,rend);
	}
	
	public void setSurfaceModifier()
	{
		surfaceModifier = 3;
	}
}
