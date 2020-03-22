package stone.to.space.age.survival;
public class Pointer
{
	int X,Y;
	boolean active,dragged;
	public Pointer(int X,int Y)
	{
		this.X=X;
		this.Y=Y;
		active=true;
		dragged=false;
	}
	public void deactivate()
	{
		active=false;
	}
	public void activate()
	{
		active=true;
		dragged=false;
	}
}
