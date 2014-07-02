package avalone.negend;

import avalone.api.lwjgl.AvaloneGLAPI;

public abstract class AliveEntity extends Entity
{
	public Inventory inv;
	public int health; public int maxHealth;
	protected boolean damaged;
	protected int damageCounter;
	protected int knockback;
	protected int antiJumpSpamm;
	protected boolean lastJump;
	
	protected AliveEntity(int posX, int posY, Chunk spawn) 
	{
		super(posX, posY, spawn);
		damaged = false;
		damageCounter = 90;
		knockback = 0;
		antiJumpSpamm = 0;
		inv = new Inventory(this);
		lastJump = true;
	}

	public abstract void movements(AvaloneGLAPI glapi);
	
	public abstract void draw(Renderer rend);
	
	public void takeDamage(AliveEntity ent,int amount,int knockback)
	{
		damaged = true;
		damageCounter = 90;
		health = health - amount;
		this.knockback = knockback;
		Integer i = tagTable.get(ent);
		if(i == null)
		{
			tagTable.put(ent,amount);
		}
		else
		{
			tagTable.put(ent, tagTable.get(ent) + amount);
		}
	}
	
	protected void jump()
	{
		if(nbJump > 0)
    	{
			if(antiJumpSpamm == 0)
			{
				antiJumpSpamm = 10;
				vit = vit + 10;
				nbJump--;
			}
    	}
	}
	
	public void live(AvaloneGLAPI glapi)
	{
		if(antiJumpSpamm > 0)
		{
			antiJumpSpamm--;
		}
		pos.x = pos.x + knockback;
		if(knockback < 0)
		{
			pos.y = pos.y - knockback;
			knockback++;
		}
		else if(knockback > 0)
		{
			pos.y = pos.y + knockback;
			knockback--;
		}
		if(health <= 0)
		{
			death();
		}
		else
		{
			movements(glapi);
		}
		if(damageCounter > 0)
		{
			damageCounter--;
		}
		else if(damageCounter == 0)
		{
			damaged = false;
		}
	}
	
	public void death()
	{
		if(damageCounter > 0 && lastJump)
		{
			for(int i = 0; i < 30;i++)
			{
				WorldItem item = inv.dropItemStack(i);
				if(item != null)
				{
					System.out.println("debug: slot " + i + " has item");
					currentChunk.itemList.add(item);
					System.out.println("chunkcoords: " + currentChunk.pos.x + ", " + currentChunk.pos.y);
				}
				else
				{
					System.out.println("debug: slot " + i + " is empty");
				}
			}
			jump();
			antiJumpSpamm = 0;
			jump();
			antiJumpSpamm = 0;
			jump();
			antiJumpSpamm = 0;
			jump();
			lastJump = false;
			layer = -1;
			if(damageCounter == 0)
			{
				onDeath();
			}
		}
	}
	
	public abstract void onDeath();
}
