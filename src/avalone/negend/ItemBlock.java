package avalone.negend;

public class ItemBlock extends Item
{
	public ItemBlock(int id, int subID) 
	{
		super(id, subID, EnumItem.standardBlock, 0, 0,Block.getBlock(id).getTexture(subID));
	}
	
	public void setType()
	{
		type = 1;
	}
}
