package stone.to.space.age.survival;
import org.json.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.*;
import java.util.*;
import java.util.logging.*;
import com.badlogic.gdx.math.*;
public class Entity
{
	Array<Runnable> behaviours=new Array<Runnable>();
	boolean combat, hit=false,hit2=false,delete=false;
	Entity tis;
	int X,Y,Z,id;
	int[] health={0,0};
	JSONObject extra;
	MyGdxGame core;
	public Entity(JSONObject data,int X,int Y,int Z,JSONObject extra,MyGdxGame core)
	throws JSONException
	{
		this.X=X;
		this.Y=Y;
		this.Z=Z;
		this.core=core;
		tis=this;
		id=data.get("id");
		this.extra=extra;
		if (id==1)
		{
			health[1]=extra.getInt("time");
		}
		combat=data.has("health");
		if(combat&&!extra.has("loot"))
			extra.put("loot",data.getJSONObject("loot"));
		if(combat)
		{
			int temp_001=data.getInt("health");
			health = new int[] {temp_001,temp_001};
		}
		JSONObject temp_002=data.getJSONObject("behaviours");
		if (temp_002.has("support"))
		{
			behaviours.add(new Runnable()
			{
				public void run()
				{
					if (!tis.core.LA_001.gettile(tis.X,tis.Y-1,tis.Z).collision)
					{
						health[0]=0;
					}
				}
			});
		}
		extra.put("rotate",0.0);
		if (temp_002.has("rotate"))
		{
			behaviours.add(new Runnable()
				{
					public void run()
					{
						try
						{
							tis.extra.put("rotate",tis.extra.getDouble("rotate")+Math.random());
						}
						catch (Throwable T_001)
						{
							tis.core.handle(T_001);
						}
					}
				});
		}
	}
	public void render(Array<Model> entityModels,ModelBatch batch,Array<ModelInstance> tileModels,JSONObject tileids)
	throws JSONException
	{
		Model self = entityModels.get(id);
		if (id==0)
		{
			if(self.getMaterial("default").has(TextureAttribute.Diffuse))
				self.getMaterial("default").remove(TextureAttribute.Diffuse);
			//self.getNode("n_001").parts.get(0).material=self.getMaterial(extra.getString("item"));
			self.getMaterial("default").set(FloatAttribute.createAlphaTest(0.1f));
			self.getMaterial("default").set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
			self.getMaterial("default").set(self.getMaterial(extra.getString("item")).get(TextureAttribute.Diffuse));
		}
		ModelInstance self2=new ModelInstance(self);
		Quaternion q=new Quaternion().setEulerAngles((float)extra.getDouble("rotate"),0,0);
		self2.transform.set(X,Y,Z,q.x,q.y,q.z,q.w,1,1,1);
		hit2=false;
		if(id==1)
		{
			self2=tileModels.get(tileids.getInt(extra.getString("tileid")));
			float scale=((float)extra.getInt("time"))/((float)health[1]);
			self2.transform.set(X,Y,Z,0,0,0,1,scale,scale,scale);
		}
		if (hit)
		{
			self2.transform.set(X,Y,Z,q.x,q.y,q.z,q.w,0.9f,0.9f,0.9f);
			hit2=true;
		}
		batch.render(self2);
		self2.transform.set(X,Y,Z,0,0,0,1,1,1,1);
		hit=false;
		Iterator<Runnable> it=behaviours.iterator();
		while(it.hasNext())
		{
			it.next().run();
		}
	}
	public JSONObject toJSON()
	throws JSONException
	{
		return new JSONObject().put("data",extra).put("health",new JSONArray().put(health[0]).put(health[1])).put("X",X).put("Y",Y).put("Z",Z).put("id",id).put("combat",combat);
	}
	public Entity(JSONArray ents,JSONObject info,MyGdxGame core)
	throws JSONException
	{
		this.X=info.getInt("X");
		this.Y=info.getInt("Y");
		this.Z=info.getInt("Z");
		this.core=core;
		tis=this;
		id=info.getInt("id");
		this.extra=info.getJSONObject("data");
		if(!extra.has("rotate"))
			extra.put("rotate",0.0);
		combat=info.getBoolean("combat");
		JSONArray temp_001=info.getJSONArray("health");
		health = new int[] {temp_001.getInt(0),temp_001.getInt(1)};
		JSONObject data=ents.getJSONObject(id);
		if(combat&&!extra.has("loot"))
			extra.put("loot",data.getJSONObject("loot"));
		JSONObject temp_002=data.getJSONObject("behaviours");
		if (temp_002.has("support"))
		{
			behaviours.add(new Runnable()
				{
					public void run()
					{
						if (!tis.core.LA_001.gettile(tis.X,tis.Y-1,tis.Z).collision)
						{
							health[0]=0;
						}
					}
				});
		}
		if (temp_002.has("rotate"))
		{
			behaviours.add(new Runnable()
				{
					public void run()
					{
						try
						{
							tis.extra.put("rotate",tis.extra.getDouble("rotate")+Math.random());
						}
						catch (Throwable T_001)
						{
							tis.core.handle(T_001);
						}
					}
				});
		}
	}
}
