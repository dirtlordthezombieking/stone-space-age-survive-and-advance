package stone.to.space.age.survival;
public class MyUtils
{
	public static double correct2D(double min,double max,double noise)
	{
		return (((noise*(0.5/0.707))+0.5)*(max-min))+min;
	}
	public static double correct3D(double min,double max,double noise)
	{
		return (((noise*(0.5/0.866))+0.5)*(max-min))+min;
	}
	public static double correct4D(double min,double max,double noise)
	{
		return (((noise*(0.5))+0.5)*(max-min))+min;
	}
}
