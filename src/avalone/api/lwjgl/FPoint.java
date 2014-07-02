package avalone.api.lwjgl;

import java.io.Serializable;

public class FPoint extends Point implements Serializable
{
	public float fx;
    public float fy;
    public float fz;
    
    public FPoint()
    {
    	super();
    	setCoords(0.0f,0.0f,0.0f);
    }
    
    public FPoint(float fx, float fy)
    {
    	super((int)fx,(int)fy);
    	setCoords(fx,fy,0.0f);
    }
    
    public FPoint(float fx,float fy,float fz)
    {
    	super((int)fx,(int)fy,(int)fz);
    	setCoords(fx,fy,fz);
    }
    
    public FPoint clone(int ad)
    {
    	return new FPoint(fx+ad,fy+ad,fz+ad);
    }
    
    public FPoint clone(int adX,int adY)
    {
    	return new FPoint(fx+adX,fy+adY,fz);
    }
    
    public void setCoords(float newfx,float newfy,float newfz)
    {
    	super.setCoords((int)newfx, (int)newfy, (int)newfz);
    	fx = newfx;
    	fy = newfy;
    	fz = newfz;
    }
    
    public void setcoords(float newfx,float newfy)
    {
    	super.setCoords((int)newfx, (int)newfy);
    	fx = newfx;
    	fy = newfy;
    }
    
    public void movefx(float dx)
    {
    	super.movex((int)dx);
    	fx = fx + dx;
    }
    
    public void movefy(float dy)
    {
    	super.movey((int)dy);
    	fy = fy + dy;
    }
    
    public void movefz(float dz)
    {
    	super.movez((int)dz);
    	fz = fz + dz;
    }
    
    public void movecoords(float dx,float dy,float dz)
    {
    	super.moveCoords((int)dx,(int)dy,(int)dz);
    	setCoords(fx+dx,fy+dy,fz+dz);
    }
    
    public void movecoords(float dx,float dy)
    {
    	super.moveCoords((int)dx,(int)dy);
    	setcoords(fx+dx,fy+dy);
    }
}
