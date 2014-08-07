package avalone.negend;

import avalone.api.lwjgl.Point;

public class MineralItem extends Item
{
	public float[] color;
	
	MineralItem(int subID,float[] color)
	{
		super(Const.unplacableOffset,subID,EnumItem.mineralBlock,0,0,Block.getBlock(Const.unplacableOffset).getTexture(subID));
		this.color = color;
	}
	
	public void draw(Renderer rend,Point p)
	{
		p.moveCoords(7, 7);
		rend.drawMineral(p,p.clone(16),color);
	}
	
	public void setType()
	{
		type = 2;
	}
}
