package stone.to.space.age.survival;
import org.json.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
public class LocalArea
{
	TileSection[][][] TSa_001;
	int AR,SR,AS,SS,AX,AY,AZ,upX,upY,upZ,progress,max;
	FileHandle savedir;
	OpenSimplexNoise OSN_001;
	JSONObject JO_001;
	JSONArray JA_001;
	LocalArea l;
	boolean up,loading,uX,uY,uZ,que=true,load,loaded,loaded2;
	Array<Thread>queue=new Array<Thread>();
	public LocalArea(int radius,int setRadius,int X,int Y,int Z,OpenSimplexNoise OSN_001in,JSONArray JA_001in,JSONObject JO_001in,FileHandle savedirin)
	{
		progress=0;
		loaded=false;
		loaded=false;
		JO_001=JO_001in;
		JA_001=JA_001in;
		OSN_001=OSN_001in;
		l=this;
		AR=radius;
		SR=setRadius;
		AS=(2*AR)+1;
		SS=(2*SR)+1;
		max=AS*AS*AS*AR*AR*AR;
		AX=X;
		AY=Y;
		AZ=Z;
		savedir=savedirin;
		new Thread()
		{
			public void run()
			{
				TSa_001=new TileSection[AS][AS][AS];
				for(int for_001 = 0;for_001<AS;for_001++)
				{
					for(int for_002 = 0;for_002<AS;for_002++)
					{
						for(int for_003 = 0;for_003<AS;for_003++)
						{
							TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
							progress++;
						}
					}
				}
				setrender();
				loaded=true;
			}
		}.start();
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					if(que&&queue.size>0)
					{
						que=false;
						queue.get(0).start();
						queue.removeIndex(0);
					}
				}
			}
		}.start();
	}
	private void setrender()
	{
		int temp_001=AS-1;
		for(int for_001 = 1;for_001<temp_001;for_001++)
		{
			for(int for_002 = 1;for_002<temp_001;for_002++)
			{
				for(int for_003 = 1;for_003<temp_001;for_003++)
				{
					TSa_001[for_001][for_002][for_003].setrender(l);
					progress++;
				}
			}
		}
	}
	public void render(ModelBatch MB_001,Environment E_001,Camera C_001,Array <ModelInstance> MIs_001,JSONObject JO_001)
	throws JSONException
	{
		int temp_001=AS-1;
		for(int for_001 = 1;for_001<temp_001;for_001++)
		{
			for(int for_002 = 1;for_002<temp_001;for_002++)
			{
				for(int for_003 = 1;for_003<temp_001;for_003++)
				{
					try
					{
						TSa_001[for_001][for_002][for_003].render(MB_001,E_001,C_001,MIs_001,JO_001);
					}
					catch(NullPointerException e)
					{}
				}
			}
		}
	}
	public void ShiftUp()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=0;
					int Yhold=1;
					int Zhold=0;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public void ShiftDown()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=0;
					int Yhold=-1;
					int Zhold=0;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public void ShiftFront()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=0;
					int Yhold=0;
					int Zhold=1;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public void Shiftback()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=0;
					int Yhold=0;
					int Zhold=-1;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public void ShiftRight()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=1;
					int Yhold=0;
					int Zhold=0;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public void ShiftLeft()
	{
		queue.add(new Thread()
			{
				public void run()
				{
					int Xhold=-1;
					int Yhold=0;
					int Zhold=0;
					while(!loaded)
					{}
					loading=true;
					AX+=Xhold;
					AY+=Yhold;
					AZ+=Zhold;
					upX=0;
					upY=0;
					upZ=0;
					uX=Xhold!=0;
					uY=Yhold!=0;
					uZ=Zhold!=0;
					TileSection [][][] temp_001=TSa_001;
					if(Xhold==-1||Yhold==-1||Zhold==-1)
					{
						up=false;
						for(int for_001 = AS-1;for_001>-1;for_001--)
						{
							for(int for_002 = AS-1;for_002>-1;for_002--)
							{
								for(int for_003 = AS-1;for_003>-1;for_003--)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					else
					{
						up=true;
						for(int for_001 = 0;for_001<AS;for_001++)
						{
							for(int for_002 = 0;for_002<AS;for_002++)
							{
								for(int for_003 = 0;for_003<AS;for_003++)
								{
									try
									{
										TSa_001[for_001][for_002][for_003]=temp_001[for_001+Xhold][for_002+Yhold][for_003+Zhold];
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
										TSa_001[for_001][for_002][for_003]=new TileSection((AX+for_001)-AR,(AY+for_002)-AR,(AZ+for_003)-AR,OSN_001,JA_001,JO_001,SR,savedir);
									}
									upZ++;
								}
								upY++;
								upZ=0;
							}
							upX++;
							upY=0;
						}
					}
					loading=false;
					setrender();
					que=true;
				}
			});
	}
	public Tile gettile(int X,int Y,int Z)
	{
		double X1=X;
		double X2=Math.round(X1/SS);
		int X3=(int)X2;
		int X4=X-(X3*SS);
		int X5=X3-AX;
		int X6=X4+SR;
		int X7=X5+AR;

		double Y1=Y;
		double Y2=Math.round(Y1/SS);
		int Y3=(int)Y2;
		int Y4=Y-(Y3*SS);
		int Y5=Y3-AY;
		int Y6=Y4+SR;
		int Y7=Y5+AR;

		double Z1=Z;
		double Z2=Math.round(Z1/SS);
		int Z3=(int)Z2;
		int Z4=Z-(Z3*SS);
		int Z5=Z3-AZ;
		int Z6=Z4+SR;
		int Z7=Z5+AR;

		if(loading)
		{
			if(up)
			{
				if(uX&&(upX<X7||(upX>=X7&&upY<Y7)||(upX>=X7&&upY>=Y7&&upZ<Z7)))
				{
					X7++;
				}
				if(uY&&(upX<X7||(upX>=X7&&upY<Y7)||(upX>=X7&&upY>=Y7&&upZ<Z7)))
				{
					Y7++;
				}
				if(uZ&&(upX<X7||(upX>=X7&&upY<Y7)||(upX>=X7&&upY>=Y7&&upZ<Z7)))
				{
					Z7++;
				}
			}
			else
			{
				if(uX&&(upX<(AS-X7)||(upX>=(AS-X7)&&upY<(AS-Y7))||(upX>=(AS-X7)&&upY>=(AS-Y7)&&upZ<(AS-Z7))))
				{
					X7--;
				}
				if(uY&&(upX<(AS-X7)||(upX>=(AS-X7)&&upY<(AS-Y7))||(upX>=(AS-X7)&&upY>=(AS-Y7)&&upZ<(AS-Z7))))
				{
					Y7--;
				}
				if(uZ&&(upX<(AS-X7)||(upX>=(AS-X7)&&upY<(AS-Y7))||(upX>=(AS-X7)&&upY>=(AS-Y7)&&upZ<(AS-Z7))))
				{
					Z7--;
				}
			}
		}

		return TSa_001[X7][Y7][Z7].tiles[X6][Y6][Z6];
	}
	public boolean rendertile(int X,int Y,int Z)
	{
		return gettile(X,Y,Z).render_adj;
	}
}
