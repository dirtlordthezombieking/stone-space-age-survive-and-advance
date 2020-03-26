package stone.to.space.age.survival;
import org.json.*;

public class LootHandler
{
	public static JSONArray handle(JSONObject pool)
	throws JSONException
	{
		JSONArray ret=new JSONArray();
		int l=pool.length();
		int prev=10000;
		JSONArray names=pool.names();
		for(int i=0;i<l;i++)
		{
			JSONArray item=pool.getJSONArray(names.getString(i));
			if(((int)(Math.random()*100))<item.getInt(0))
			{
				int min=item.getInt(1);
				int max=item.getInt(2);
				if(min==-1)
					min=prev;
				if(max==-1)
					max=prev;
				int dif=(max-min)+1;
				prev=((int)(Math.random()*dif))+min;
				ret.put(new JSONArray().put(names.getString(i).replace("_","")).put(prev));
			}
		}
		return ret;
	}
}
