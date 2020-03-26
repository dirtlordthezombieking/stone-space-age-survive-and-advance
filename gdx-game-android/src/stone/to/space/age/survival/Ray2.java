package stone.to.space.age.survival;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.collision.*;
import com.badlogic.gdx.math.*;
//I could not get intersecter.intersectraybounds to cooperate so I made this then ended up figuring it out
public class Ray2
{
	Vector3 start,end;
	double distance;
	public Ray2(Camera camera,float X,float Y)
	{
		Ray temp_001=camera.getPickRay(X,Y);
		start=temp_001.origin;
		end=temp_001.direction;
		end.set((end.x*100)+start.x,(end.y*100)+start.y,(end.z*100)+start.z);
		distance=Math.sqrt((double)(((start.x-end.x)*(start.x-end.x))+((start.y-end.y)*(start.y-end.y))+((start.z-end.z)*(start.z-end.z))));
	}
	public boolean pointInRange(Vector3 point,double range)
	{
		double near=Math.sqrt((double)(((start.x-point.x)*(start.x-point.x))+((start.y-point.y)*(start.y-point.y))+((start.z-point.z)*(start.z-point.z))));
		double far=Math.sqrt((double)(((point.x-end.x)*(point.x-end.x))+((point.y-end.y)*(point.y-end.y))+((point.z-end.z)*(point.z-end.z))));
		double theta=Math.acos(((near*near)+(distance*distance)-(far*far))/2*near*distance);
		double closest=near*Math.sin(theta);
		return range>closest;
	}
}
