package stone.to.space.age.survival;
import org.json.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.physics.bullet.collision.*;
public class Tile
{
	private int X,Y,Z;
	public String id="null";
	boolean rendertile,render_adj,render,collision;
	private JSONObject data;
	public Tile(JSONObject JO_002)
	throws JSONException
	{
		X=JO_002.getInt("X");
		Y=JO_002.getInt("Y");
		Z=JO_002.getInt("Z");
		id=JO_002.getString("id");
		data=JO_002.getJSONObject("data");
		rendertile=JO_002.getBoolean("rendertile");
		render_adj=JO_002.getBoolean("render_adj");
		render=JO_002.getBoolean("render");
		collision=JO_002.getBoolean("collision");
	}
	public Tile(int X,int Y,int Z,int noise,OpenSimplexNoise OSN_001,JSONArray JA_001,JSONObject JO_001)
	throws JSONException
	{
		this.X=X;
		this.Y=Y;
		this.Z=Z;
		data=new JSONObject();
		if(Y>-3&&Y<25)
		{
			int top=(int)Math.round(MyUtils.correct2D(OSN_001.eval(X/3,Z/3),0,2))+(int)Math.round(MyUtils.correct2D(OSN_001.eval(X/5,Z/5),0,2))+noise;//Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(MyUtils.correct2D(OSN_001.eval(X,Z),1,3)*MyUtils.correct2D(OSN_001.eval(X,Z-1),1,3))*MyUtils.correct2D(OSN_001.eval(X,Z+1),1,3))*MyUtils.correct2D(OSN_001.eval(X-1,Z),1,3))*MyUtils.correct2D(OSN_001.eval(X+1,Z),1,3))+noise);
			if(Y<top-2)
			{
				id="stone";
			}
			else if(Y<top)
			{
				id="soil";
			}
			else if(Y==top)
			{
				id="grass";
			}
			else
			{
				id="air";
			}
		}
		else if(Y<-2)
		{
			id="stone";
		}
		else
		{
			id="air";
		}
		if(OSN_001.eval(X/3,Y/3,Z/3)>0.3)
		{
			int temp_001=0;
			if(OSN_001.eval(X/3,Y/3,(Z-1)/3)>0.3)
			{
				temp_001++;
			}
			if(OSN_001.eval(X/3,Y/3,(Z+2)/3)>0.3)
			{
				temp_001++;
			}
			if(OSN_001.eval(X/3,(Y-1)/3,Z/3)>0.3)
			{
				temp_001++;
			}
			if(OSN_001.eval(X/3,(Y+1)/3,Z/3)>0.3)
			{
				temp_001++;
			}
			if(OSN_001.eval((X-1)/3,Y/3,Z/3)>0.3)
			{
				temp_001++;
			}
			if(OSN_001.eval((X+1)/3,Y/3,Z/3)>0.3)
			{
				temp_001++;
			}
			if(temp_001>4)
			{
				id="air";
			}
		}
		data.put("loot_seed",OSN_001.eval(X,Y,Z));
		JSONObject tileinfo=JA_001.getJSONObject(JO_001.getInt(id));
		rendertile=tileinfo.getBoolean("render");
		render_adj=tileinfo.getBoolean("render_adj");
		collision=tileinfo.getBoolean("collision");
	}
	public JSONObject toJSON ()
	throws JSONException
	{
		JSONObject JO_002=new JSONObject();
		JO_002=JO_002.put("X",X);
		JO_002=JO_002.put("Y",Y);
		JO_002=JO_002.put("Z",Z);
		JO_002=JO_002.put("id",id);
		JO_002=JO_002.put("data",data);
		JO_002=JO_002.put("rendertile",rendertile);
		JO_002=JO_002.put("render_adj",render_adj);
		JO_002=JO_002.put("render",render);
		JO_002=JO_002.put("collision",collision);
		return JO_002;
	}
	public void render(ModelBatch MB_002,Environment E_002,Camera C_002,Array <ModelInstance> MIs_001,JSONObject JO_001)
	throws JSONException
	{
		if(rendertile&&render)
		{
			if(isVisible(C_002,X,Y,Z))
			{
				MIs_001.get(JO_001.getInt(id)).transform.setToScaling(1.05f,1.05f,1.05f);
				MIs_001.get(JO_001.getInt(id)).transform.setToTranslation(X,Y,Z);
				MB_002.render(MIs_001.get(JO_001.getInt(id)),E_002);
				MIs_001.get(JO_001.getInt(id)).transform.setToScaling(1.0f,1.0f,1.0f);
			}
		}
	}
	public void setrender(LocalArea LA_001)
	{
		render=false;
		if(rendertile)
		{
			render=LA_001.rendertile(X-1,Y,Z)||LA_001.rendertile(X+1,Y,Z)||LA_001.rendertile(X,Y-1,Z)||LA_001.rendertile(X,Y+1,Z)||LA_001.rendertile(X,Y,Z-1)||LA_001.rendertile(X,Y,Z+1);
		}
	}
	public boolean overlap(HitBox HB_001,Array <ModelInstance> MIs_001,JSONObject JO_001,btDispatcher D_001)
	throws JSONException
	{
		if(collision)
		{
			MIs_001.get(JO_001.getInt(id)).transform.setToTranslation(X,Y,Z);
			HitBox HB_002=new HitBox(MIs_001.get(JO_001.getInt(id)));
			boolean ret=HitBox.checkCollosion(HB_001,HB_002,D_001);
			HB_002.dispose();
			return ret;
		}
		else
		{
			return false;
		}
	}
	protected boolean isVisible(final Camera cam,final float X,final float Y,final float Z)
	{
		return cam.frustum.sphereInFrustum(X,Y,Z,0.707f);
	}
}
