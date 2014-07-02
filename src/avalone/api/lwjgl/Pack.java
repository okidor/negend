package avalone.api.lwjgl;

public class Pack<S, T, U, V>
{
	public S o1;
	public T o2;
	public U o3;
	public V o4;
	
	public Pack(S o1,T o2)
	{
		this.o1 = o1;
		this.o2 = o2;
	}
	
	public Pack(S o1,T o2,U o3)
	{
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
	}
	
	public Pack(S o1,T o2,U o3,V o4)
	{
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
		this.o4 = o4;
	}
}
