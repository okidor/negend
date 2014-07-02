package avalone.api.lwjgl;

import java.util.ArrayList;

public class CustomIndex2DList<T>
{
	private ArrayList<ArrayList<T>> upLeft;
	private ArrayList<ArrayList<T>> upRight;
	private ArrayList<ArrayList<T>> downLeft;
	private ArrayList<ArrayList<T>> downRight;
	
	public CustomIndex2DList()
	{
		upLeft = new ArrayList<ArrayList<T>>();
		upRight = new ArrayList<ArrayList<T>>();
		downLeft = new ArrayList<ArrayList<T>>();
		downRight = new ArrayList<ArrayList<T>>();
	}
	
	public int abs(int x)
	{
		if(x >= 0)
		{
			return x;
		}
		return -x;
	}
	
	public int[] size()
	{
		int[] sizes = new int[4];
		sizes[0] = upLeft.size();
		sizes[1] = upRight.size();
		sizes[2] = downLeft.size();
		sizes[3] = downRight.size();
		return sizes;
	}
	
	public int[] totalSize()
	{
		int[] sizes = new int[4];
		sizes[0] = subSize(upLeft);
		sizes[1] = subSize(upRight);
		sizes[2] = subSize(downLeft);
		sizes[3] = subSize(downRight);
		return sizes;
	}
	
	private int subSize(ArrayList<ArrayList<T>> al)
	{
		int size = 0;
		for(int i = 0;i < al.size();i++)
		{
			size = size + al.get(i).size();
		}
		return size;
	}
	
	public void add(int indexX,int indexY,T element)
	{
		ArrayList<ArrayList<T>> chosenList = chooseList(indexX,indexY);
		fillGap(chosenList,indexX,indexY);
		chosenList.get(abs(indexX)).add(abs(indexY),element);
	}
	
	public boolean contains(Object o)
	{
		return subContains(upLeft,o) || subContains(upRight,o) || subContains(downLeft,o) || subContains(downRight,o);
	}
	
	private boolean subContains(ArrayList<ArrayList<T>> al,Object o)
	{
		for(int i = 0;i < al.size();i++)
		{
			if(al.get(i).contains(o))
			{
				return true;
			}
		}
		return false;
	}
	
	public void clear()
	{
		upLeft.clear();
		upRight.clear();
		downLeft.clear();
		downRight.clear();
	}
	
	public T remove(int indexX,int indexY)
	{
		T toReturn = chooseList(indexX,indexY).get(abs(indexX)).remove(abs(indexY));
		return toReturn;
	}
	
	public boolean isEmpty()
	{
		return upLeft.isEmpty() && upRight.isEmpty() && downLeft.isEmpty() && downRight.isEmpty();
	}
	
	public T get(int indexX,int indexY)
	{
		ArrayList<ArrayList<T>> chosenList = chooseList(indexX,indexY);
		if(chosenList.size() > abs(indexX))
		{
			if(chosenList.get(abs(indexX)).size() > abs(indexY))
			{
				T toReturn = chosenList.get(abs(indexX)).get(abs(indexY));
				return toReturn;
			}
			else
			{
				/*System.out.println("warning out of range");
				System.out.println("asked for coordY: " + abs(indexY) + " but sizeY is " + chosenList.get(abs(indexX)).size());*/
				return null;
			}
		}
		else
		{
			/*System.out.println("warning out of range");
			System.out.println("asked for coordX: " + abs(indexX) + " but sizeX is " + chosenList.size());*/
			return null;
		}
	}
	
	public void fillGap(ArrayList<ArrayList<T>> al,int indexX,int indexY)
	{
		while(abs(indexX) >= al.size())
		{
			al.add(new ArrayList<T>());
		}
		ArrayList<T> subAl = al.get(abs(indexX));
		while(abs(indexY) >= subAl.size())
		{
			subAl.add(null);
		}
	}
	
	public ArrayList<ArrayList<T>> chooseList(int indexX,int indexY)
	{
		if(indexX >= 0)
		{
			if(indexY >= 0)
			{
				return upRight;
			}
			else
			{
				return downRight;
			}
		}
		else
		{
			if(indexY >= 0)
			{
				return upLeft;
			}
			else
			{
				return downLeft;
			}
		}
	}
	
	public String toString()
	{
		return upLeft.toString() + upRight.toString() + downLeft.toString() + downRight.toString();
	}
}
