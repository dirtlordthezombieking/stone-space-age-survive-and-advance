package stone.to.space.age.survival;
import org.json.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.files.*;
import java.util.*;
public class TileSection
{
	public Tile[][][] tiles;
	Array<Entity> ents=new Array<Entity>();
	Array<Entity> pents=new Array<Entity>();
	private int X,Y,Z,rad,size;
	boolean render,checkrender;
	FileHandle savedir;
	boolean loaded,loaded2;
	public TileSection(int Xin,int Yin,int Zin,OpenSimplexNoise OSN_001,JSONArray JA_001,JSONObject JO_001,int radin,FileHandle savedirin,JSONArray enitities, MyGdxGame core)
	{
		loaded=false;
		loaded2=false;
		try
		{
			savedir=savedirin.child("set_"+Xin+"_"+Yin+"_"+Zin);
			if(savedir.exists())
			{
				JSONObject JO_002=new JSONObject(savedir.readString());
				X=Xin;
				Y=Yin;
				Z=Zin;
				if(JO_002.has("pents"))
				{
					JSONArray persistablEntities=JO_002.getJSONArray("pents");
					int l=persistablEntities.length();
					for(int i=0;i<l;i++)
					{
						pents.add(new Entity(enitities,persistablEntities.getJSONObject(i),core));
					}
				}
				render=JO_002.getBoolean("render");
				checkrender=JO_002.getBoolean("checkrender");
				rad=JO_002.getInt("rad");
				size=JO_002.getInt("size");
				tiles=new Tile[size][size][size];
				for(int for_001 = 0;for_001<size;for_001++)
				{
					for(int for_002 = 0;for_002<size;for_002++)
					{
						for(int for_003 = 0;for_003<size;for_003++)
						{
							tiles[for_001][for_002][for_003]=new Tile(JO_002.getJSONObject("tile_"+for_001+"_"+for_002+"_"+for_003));
						}
					}
				}
				loaded=true;
			}
			else
			{
				rad=radin;
				size=(2*rad)+1;
				tiles=new Tile[size][size][size];
				X=Xin;
				Y=Yin;
				Z=Zin;
				checkrender=true;
				render=false;
				int noise=(int)Math.round(MyUtils.correct2D(OSN_001.eval(X,Z),0,2))+(int)Math.round(MyUtils.correct2D(OSN_001.eval(X/3,Z/3),0,2))+(int)Math.round(MyUtils.correct2D(OSN_001.eval(X/5,Z/5),0,2));//Math.sqrt(MyUtils.correct2D(OSN_001.eval(X,Z),1,3)*MyUtils.correct2D(OSN_001.eval(Z,X),1,3))+Math.sqrt(MyUtils.correct2D(OSN_001.eval(X/15,Z/15),1,3)*MyUtils.correct2D(OSN_001.eval(Z/15,X/15),1,3));//0;//(int)Math.round(MyUtils.correct2D(OSN_001.eval(X,Z), 0, 2))+(int)Math.round(MyUtils.correct2D(OSN_001.eval(X/20,Z/20), 0, 2));
				JSONObject JO_002=new JSONObject();
				JO_002=JO_002.put("render",render);
				JO_002=JO_002.put("checkrender",checkrender);
				JO_002=JO_002.put("rad",rad);
				JO_002=JO_002.put("size",size);
				for(int for_001 = 0;for_001<size;for_001++)
				{
					for(int for_002 = 0;for_002<size;for_002++)
					{
						for(int for_003 = 0;for_003<size;for_003++)
						{
							tiles[for_001][for_002][for_003]=new Tile(((X*size)-rad)+for_001,((Y*size)-rad)+for_002,((Z*size)-rad)+for_003,noise,OSN_001,JA_001,JO_001);
							JO_002=JO_002.put("tile_"+for_001+"_"+for_002+"_"+for_003,tiles[for_001][for_002][for_003].toJSON());
						}
					}
				}
				savedir.writeString(JO_002.toString(),false);
			}
			loaded=true;
		}
		catch(Throwable e)
		{
		}
	}
	public void save()
	throws JSONException
	{
		JSONObject JO_002=new JSONObject();
		JO_002=JO_002.put("render",render);
		JO_002=JO_002.put("checkrender",checkrender);
		JO_002=JO_002.put("rad",rad);
		JO_002=JO_002.put("size",size);
		for(int for_001 = 0;for_001<size;for_001++)
		{
			for(int for_002 = 0;for_002<size;for_002++)
			{
				for(int for_003 = 0;for_003<size;for_003++)
				{
					JO_002=JO_002.put("tile_"+for_001+"_"+for_002+"_"+for_003,tiles[for_001][for_002][for_003].toJSON());
				}
			}
		}
		JSONArray entities=new JSONArray();
		Iterator<Entity> es=pents.iterator();
		while(es.hasNext())
		{
			entities.put(es.next().toJSON());
		}
		JO_002.put("pents",entities);
		savedir.writeString(JO_002.toString(),false);
	}
	public void render(ModelBatch MB_002,Environment E_002,Camera C_002,Array <ModelInstance> MIs_001,JSONObject JO_001,JSONArray entites,LocalArea LA_001,MyGdxGame core,Array<Model> entmods)
	throws JSONException
	{
		if(loaded&&loaded2)
		{
			tick(entites,LA_001,core);
			if(render)
			{
				for(int for_001 = 0;for_001<size;for_001++)
				{
					for(int for_002 = 0;for_002<size;for_002++)
					{
						for(int for_003 = 0;for_003<size;for_003++)
						{
							tiles[for_001][for_002][for_003].render(MB_002,E_002,C_002,MIs_001,JO_001);
						}
					}
				}
			}
			Iterator<Entity> it=ents.iterator();
			int ind=0;
			while(it.hasNext())
			{
				Entity en=it.next();
				en.render(entmods,MB_002,MIs_001,JO_001);
				if(en.combat)
				if(en.health[0]<1)
				{
					JSONArray loot=LootHandler.handle(en.extra.getJSONObject("loot"));
					int l=loot.length();
					for(int i=0;i<l;i++)
					{
						pents.add(new Entity(entites.getJSONObject(0),en.X,en.Y,en.Z,new JSONObject().put("item",loot.getJSONArray(i).getString(0)).put("count",loot.getJSONArray(i).getInt(1)),core));
					}
					ents.removeIndex(ind);
				}
			if(en.delete)
				ents.removeIndex(ind);
				ind++;
			}
			it=pents.iterator();
			ind=0;
			while(it.hasNext())
			{
				Entity en=it.next();
				en.render(entmods,MB_002,MIs_001,JO_001);
				if(en.combat)
					if(en.health[0]<1)
					{
						JSONArray loot=LootHandler.handle(en.extra.getJSONObject("loot"));
						int l=loot.length();
						for(int i=0;i<l;i++)
						{
							pents.add(new Entity(entites.getJSONObject(0),en.X,en.Y,en.Z,new JSONObject().put("item",loot.getJSONArray(i).getString(0)).put("count",loot.getJSONArray(i).getInt(1)),core));
						}
						pents.removeIndex(ind);
					}
				if(en.delete)
					pents.removeIndex(ind);
				ind++;
			}
		}
	}
	public void setrender(LocalArea LA_001)
	{
		if(checkrender)
		{
			try
			{
				render=false;
				JSONObject JO_002=new JSONObject();
				for(int for_001 = 0;for_001<size;for_001++)
				{
					for(int for_002 = 0;for_002<size;for_002++)
					{
						for(int for_003 = 0;for_003<size;for_003++)
						{
							while(true)
							{
								try
								{
									tiles[for_001][for_002][for_003].setrender(LA_001);
									JO_002.put("tile_"+for_001+"_"+for_002+"_"+for_003,tiles[for_001][for_002][for_003].toJSON());
									if(tiles[for_001][for_002][for_003].render)
									{
										render=true;
									}
									break;
								}
								catch(Throwable e)
								{
									MyGdxGame.silentHandle(e);
								}
							}
						}
					}
				}
				JO_002=JO_002.put("checkrender",false);
				JO_002=JO_002.put("rad",rad);
				JO_002=JO_002.put("size",size);
				JO_002=JO_002.put("render",render);
				savedir.writeString(JO_002.toString(),false);
				checkrender=false;
			}
			catch(Throwable e)
			{
			}
		}
		loaded2=true;
	}
	public void tick(JSONArray entites,LocalArea LA_001,MyGdxGame core)
	throws JSONException
	{
		int
		RX= (int)(Math.random()*size),
		RY= (int)(Math.random()*size),
		RZ= (int)(Math.random()*size);
		if(LA_001.gettile(RX+((X*size)-rad),RY+((Y*size)-rad),RZ+((Z*size)-rad)).id.equals("grass")&&Math.random()<0.01&&ents.size<5)
		{
			ents.add(new Entity(entites.getJSONObject(2),RX+((X*size)-rad),RY+((Y*size)-rad),RZ+((Z*size)-rad),new JSONObject("{}"),core));
		}
	}
}
