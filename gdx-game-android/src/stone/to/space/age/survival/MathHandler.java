package stone.to.space.age.survival;
import com.badlogic.gdx.files.*;
import org.json.*;
import java.util.regex.*;
import com.badlogic.gdx.*;
public class MathHandler
{
	//illegal characters/strings("&",":-:","=","!")
	private FileHandle formulas;
	private String[] names;
	public MathHandler(FileHandle formulas)
	{
		String [] temp_001=formulas.readString().split("\n"+"&"+"\n");
		int temp_002=temp_001.length;
		names=new String[temp_002];
		for(int temp_003=0;temp_003<temp_002;temp_003++)
		{
			names[temp_003]=temp_001[temp_003].split("\n")[0];
		}
		this.formulas=formulas;
	}
	public double calculate(String formula,double[] vars)
	throws MathHandlerException
	{
		int var=0;
		int linenum=1;
		String [] lines;
		try
		{
			lines=formulas.readString().split("\n"+"&"+"\n")[find(names,formula)].split("\n");
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new MathHandlerException("formula \""+formula+"\" not found");
		}
		boolean Rule_int=false;
		while(true)
		{
			String line;
			try
			{
				line=lines[linenum];
				linenum++;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				throw new MathHandlerException("start calculation data with \"[\"");
			}
			if(line.equals("["))
			{
				break;
			}
			if(line.split(":")[0].equals("MODIFIER"))
			{
				String rule=line.split(":")[1];
				if(rule.equals("INT"))
				{
					Rule_int=true;
				}
			}
		}
		JSONObject data=new JSONObject();
		while(true)
		{
			String line;
			try
			{
				line=lines[linenum];
				linenum++;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				throw new MathHandlerException("end calculation data with \"]\"");
			}
			boolean B=true;
			if(line.length()>1)
			{
				B=!line.substring(0,2).equals("//");
			}
			if(B)
			{
				if(line.equals("]"))
				{
					break;
				}
				String [] parts=line.split(":-:");
				if(parts.length!=2)
				{
					throw new MathHandlerException("line "+(linenum)+" in \""+formula+"\" invalid");
				}
				else if(parts[0].equals("?"))
				{
					String name=parts[1];
					if(vars.length<=var)
					{
						throw new MathHandlerException("not enough variables in \""+formula);
					}
					double val=vars[var];
					if(Rule_int)
					{
						int I=(int)val;
						val=I;
					}
					try
					{
						data.put(parts[1],val);
					}
					catch(JSONException r)
					{
						throw new MathHandlerException("invalid ID in \""+formula+"\" on line "+(linenum));
					}
					var++;
				}
				else if(parts[0].substring(0,1).equals("!"))
				{
					String name=parts[1];
					if(vars.length<=var)
					{
						throw new MathHandlerException("not enough variables in \""+formula);
					}
					double val=vars[var];
					String[] vals=parts[0].substring(1).split(Pattern.quote("|"));
					boolean valid=false;
					for(String S:vals)
					{
						if(val==Double.parseDouble(S))
						{
							valid=true;
							break;
						}
					}
					if(!valid)
					{
						throw new MathHandlerException("variable "+val+" does not match fixednum in \""+formula+"\" on line "+(linenum));
					}
					if(Rule_int)
					{
						int I=(int)val;
						val=I;
					}
					try
					{
						data.put(name,val);
					}
					catch(JSONException r)
					{
						throw new MathHandlerException("invalid Variable name in \""+formula+"\" on line "+(linenum));
					}
					var++;
				}
				else
				{
					String[] S=line.split("=")[0].split(" ");
					if(S.length!=3)
					{
						throw new MathHandlerException("invalid calculation in \""+formula+"\" on line "+(linenum));
					}
					double D1;
					try
					{
						D1=Double.parseDouble(S[0]);
					}
					catch(NumberFormatException e)
					{
						try
						{
							D1=data.getDouble(S[0]);
						}
						catch(JSONException e2)
						{
							throw new MathHandlerException("variable "+S[0]+"not declared in \""+formula+"\" before line "+(linenum));
						}
					}
					double D2;
					try
					{
						D2=Double.parseDouble(S[2]);
					}
					catch(NumberFormatException e)
					{
						try
						{
							D2=data.getDouble(S[2]);
						}
						catch(JSONException e2)
						{
							throw new MathHandlerException("variable "+S[2]+"not declared in \""+formula+"\" before line "+(linenum));
						}
					}
					String [] signs={"+","-","*","/"};
					double val;
					switch (find(signs,S[1]))
					{
						case 0:
							val=D1+D2;
							break;
						case 1:
							val=D1-D2;
							break;
						case 2:
							val=D1*D2;
							break;
						case 3:
							val=D1/D2;
							break;
						default:
							throw new MathHandlerException("invalid operator in \""+formula+"\" on line "+(linenum));
					}
					if(Rule_int)
					{
						int I=(int)val;
						val=I;
					}
					try
					{
						data.put(parts[1],val);
					}
					catch(JSONException r)
					{
						throw new MathHandlerException("invalid Variable name in \""+formula+"\" on line "+(linenum));
					}
				}
			}
		}
		while(true)
		{
			String line;
			try
			{
				line=lines[linenum];
				linenum++;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				throw new MathHandlerException("no output value in \""+formula+"\" \"OUT:X\"");
			}
			if(line.split(":")[0].equals("OUT"))
			{
				try
				{
					return data.getDouble(line.split(":")[1]);
				}
				catch(JSONException e2)
				{
					throw new MathHandlerException("variable "+line.split(":")[1]+"not declared before line "+(linenum));
				}
			}
		}
	}
	private int find(String[] Ss_001,String S_001)
	{
		int temp_001=Ss_001.length;
		for(int temp_002=0;temp_002<temp_001;temp_002++)
		{
			if(Ss_001[temp_002].equals(S_001))
			{
				return temp_002;
			}
		}
		return-1;
	}
}
