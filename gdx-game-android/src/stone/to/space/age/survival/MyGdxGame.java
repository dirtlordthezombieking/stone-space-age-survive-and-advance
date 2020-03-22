package stone.to.space.age.survival;
import android.widget.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.android.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
import com.badlogic.gdx.graphics.g3d.loader.*;
import com.badlogic.gdx.graphics.g3d.shaders.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.*;
import java.io.*;
import java.util.*;
import org.json.*;
public class MyGdxGame implements ApplicationListener,InputProcessor
{
	Model test_012;
	ModelInstance test_001;
	boolean test_002=false,test_006=false,test_008=false,test_009=false,test_011=false;
	String test_003="tree",test_007="grasp_left",test_010="";
	AnimationController test_004;
	AnimationController.AnimationDesc test_005;

	AndroidApplication AA_001;
	Array <AnimationController> ACs_001=new Array <AnimationController>();
	Array <AnimationController.AnimationDesc> AC_ADs_001=new Array <AnimationController.AnimationDesc>();
	Array <Model> Ms_001=new Array <Model>();
	Array <ModelInstance> MIs_001=new Array <ModelInstance>();
	Array <Pointer> Ps_001 = new  Array <Pointer>();
	BitmapFont BF_001;
	btCollisionConfiguration CC_002;
	btDispatcher D_001;
	boolean game=false,jump=false,onGround=false,menu=true,newgame=false,sellect=false;
	Camera C_001;
	CameraInputController CIC_001;
	Environment E_001;
	FileHandle maindir,save;
	float ddims,jdims,jx,jy,dby1,dby2,jby1,jby2,jbx2,XRT,ZRT,BYS,BXS,B1Y,B2Y,BX,BBX2,B1BY1,B1BY2,B2BY1,B2BY2;
	G3dModelLoader GML_001;
	int PX=0,PY=0,PZ=0,TX=0,TY=3,TZ=0,CX=0,CY=0,CZ=0,XO=0,YO=0,ZO=0,jumptime=0,PYROT=0,frames,FPS,AR,AS,SR,SS;
	listHandler LH_001;
	LocalArea LA_001;
	Model player_m;
	ModelBatch MB_001;
	ModelInstance player;
	OpenSimplexNoise OSN_001;
	ShaderProvider SP_001;
	SpriteBatch SB_001;
	String filename;
	Texture dpad,jump_button,button;
	JSONArray JA_001;
	JSONObject JO_001;
	String[]saves;
	public MyGdxGame(AndroidApplication AA_001)
	{
		this.AA_001=AA_001;
	}
	@Override
	public void create()
	{
		try
		{
			Bullet.init();
//			Vector3 o=new Vector3();
//			o.x=5;
//			Vector3 m=o;
//			o.x=7;7.0
//			Gdx.app.getClipboard().setContents(""+m.x);
			Gdx.graphics.setContinuousRendering(false);
			maindir=Gdx.files.external("games/stone to space age");
			if(!maindir.exists())
				maindir.mkdirs();
			//Gdx.input.setOnscreenKeyboardVisible(true);
			AR=2;
			SR=3;
			AS=(2*AR)+1;
			SS=(2*SR)+1;
			BF_001=new BitmapFont(Gdx.files.internal("font2.fnt"),new TextureRegion(new Texture(Gdx.files.internal("font.png"))));
			BF_001.setColor(Color.GOLD);
			BF_001.getData().setScale(2);
			E_001=new Environment();
			E_001.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f,0.4f,0.4f,1f));
			E_001.add(new PointLight().set(1.0f,1.0f,1.0f,20f,20f,20f,1000));
			C_001=new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			C_001.position.set(0f,0f,5f);
			C_001.lookAt(0f,0f,0f);
			C_001.near=0.01f;
			C_001.far=300f;
			C_001.update();
			CC_002=new btDefaultCollisionConfiguration();
			D_001=new btCollisionDispatcher(CC_002);
			if(test_008)
			{
				CIC_001=new CameraInputController(C_001);
				Gdx.input.setInputProcessor(CIC_001);
			}
			else
			{
				Gdx.input.setInputProcessor(this);
			}
			SP_001=new DefaultShaderProvider(new DefaultShader.Config(DefaultShader.getDefaultVertexShader(),DefaultShader.getDefaultFragmentShader()));
			MB_001=new ModelBatch(SP_001);
			SB_001=new SpriteBatch();
			GML_001=new G3dModelLoader(new JsonReader());
			//OSN_001=new OpenSimplexNoise(Long.MAX_VALUE/42);
			JA_001=new JSONArray(Gdx.files.internal("tiles.json").readString());
			JO_001=new JSONObject();
			if(test_002)
			{
				test_012=GML_001.loadModel(Gdx.files.internal("models/"+test_003+".g3dj"));
				test_001=new ModelInstance(test_012);
				if(test_006)
				{
					test_004=new AnimationController(test_001);
					test_005=test_004.setAnimation(test_007);
					test_005.speed=0.1f;
					test_004.inAction=true;
					test_005.loopCount=1000;
					test_001.getNode("left_upper_arm_joint").rotation.setEulerAngles(0,-90,0);
				}
			}
			else
			{
				dpad=new Texture(Gdx.files.internal("dpad.png"));
				jump_button=new Texture(Gdx.files.internal("jump.png"));
				button=new Texture(Gdx.files.internal("button.png"));
				ddims=Gdx.graphics.getHeight()/3;
				jdims=Gdx.graphics.getHeight()/6;
				jy=Gdx.graphics.getHeight()/12;
				jx=Gdx.graphics.getWidth()-(jdims+jy);
				dby1=Gdx.graphics.getHeight()-ddims;
				dby2=Gdx.graphics.getHeight();
				jby1=Gdx.graphics.getHeight()-(jdims+jy);
				jby2=Gdx.graphics.getHeight()-jy;
				jbx2=Gdx.graphics.getWidth()-jy;
				BYS=Gdx.graphics.getHeight()/5;
				BXS=BYS*4;
				BX=(Gdx.graphics.getWidth()-BXS)/2;
				BBX2=Gdx.graphics.getWidth()-BX;
				B1Y=Gdx.graphics.getHeight()/15;
				B2Y=(B1Y*3)+BYS;
				B2BY1=B2Y;
				B2BY2=B2BY1+BYS;
				B1BY1=(B1Y*2)+B2BY2;
				B1BY2=B1BY1+BYS;
				player_m=GML_001.loadModel(Gdx.files.internal("models/player.g3dj"));
				player=new ModelInstance(player_m);
			}
			new Thread()
			{
				public void run()
				{
					while(true)
					{
						FPS=frames;
						frames=0;
						try
						{
							Thread.sleep(1000);
						}
						catch(InterruptedException e)
						{}
					}
				}
			}.start();
			new Thread()
			{
				public void run()
				{
					while(true)
					{
						Gdx.graphics.requestRendering();
						try
						{
							Thread.sleep(67);
						}
						catch(InterruptedException e)
						{}
					}
				}
			}.start();
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	@Override
	public void render()
	{
		try
		{
			boolean loaded=false;
			int progress=0;
			if(game)
			{
				loaded=LA_001.loaded;
				progress=(LA_001.progress*100)/LA_001.max;
			}
			frames++;
			if((!test_002)&&game&&loaded)
			{
				Iterator I_001=Ps_001.iterator();
				while(I_001.hasNext())
				{
					Pointer P_001=(Pointer)I_001.next();
					if(P_001.active)
					{
						int screenX=P_001.X,screenY=P_001.Y;
						if(screenX<ddims&&screenY>dby1&&screenX>0&&screenY<dby2)
						{
							float dx=((((float)screenX)/ddims)*2)-1;
							float dy=(((((float)screenY)-dby1)/ddims)*-2)+1;
							dpad(dx,dy);
						}
					}
				}
				PX=(CX*SS)+TX;
				PY=(CY*SS)+TY;
				PZ=(CZ*SS)+TZ;
				if(XRT>=1)
				{
					XO+=1;
					XRT-=1;
					checkPlayerCollision(1,0,0,true);
					if(XO>+5)
					{
						shiftplayer(1,0,0);
					}
				}
				else if(XRT<=-1)
				{
					XO-=1;
					XRT+=1;
					checkPlayerCollision(-1,0,0,true);
					if(XO<-5)
					{
						shiftplayer(-1,0,0);
					}
				}
				if(ZRT>=1)
				{
					ZO+=1;
					ZRT-=1;
					checkPlayerCollision(0,0,1,true);
					if(ZO>+5)
					{
						shiftplayer(0,0,1);
					}
				}
				else if(ZRT<=-1)
				{
					ZO-=1;
					ZRT+=1;
					checkPlayerCollision(0,0,-1,true);
					if(ZO<-5)
					{
						shiftplayer(0,0,-1);
					}
				}
				if(!jump)
				{
					YO-=1;
					if(!checkPlayerCollision(0,-1,0,true))
					{
						onGround=true;
					}
					if(YO<-5)
					{
						shiftplayer(0,-1,0);
					}
				}
				else
				{
					YO+=2;
					if(checkPlayerCollision(0,1,0,true))
					{
						jumptime--;
						if(jumptime==0)
						{
							jump=false;
						}
					}
					else
					{
						jumptime=0;
						jump=false;
						YO-=1;
					}
					if(YO>5)
					{
						shiftplayer(0,1,0);
					}
				}
				if(!checkPlayerCollision(0,0,0,true))
				{
					YO+=2;
					if(YO>5)
					{
						shiftplayer(0,1,0);
					}
				}
				PX=(CX*SS)+TX;
				PY=(CY*SS)+TY;
				PZ=(CZ*SS)+TZ;
			}
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glClearColor(0,0.5f,1,1);
			Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
			MB_001.flush();
			if((!test_002)&&game&&loaded)
			{
				player.transform.set(new Vector3(PX+((((float)XO)+XRT)/10),(PY+(((float)YO)/10))+0.5f,PZ+(((float)ZO)+ZRT)/10),new Quaternion().setEulerAngles(PYROT,0,0));
			}
			if((!test_002)&&game&&(!test_011)&&loaded)
			{
				Vector3 V3_001=new Vector3(player.transform.getTranslation(new Vector3())).add(0,0.73f,0);
				C_001.position.set(V3_001);
				double lookX=Math.sin(Math.toRadians(PYROT));
				double lookZ=Math.cos(Math.toRadians(PYROT));
				Vector3 V3_002=new Vector3(V3_001).add((float)lookX,0,(float)lookZ);
				C_001.lookAt(V3_002);
				C_001.normalizeUp();
				C_001.update();
			}
			MB_001.begin(C_001);
			if(test_002)
			{
				if(test_006)
				{
					test_004.update(0.0002f);
				}
				MB_001.render(test_001,E_001);
			}
			if((!test_002)&&game&&loaded)
			{
				LA_001.render(MB_001,E_001,C_001,MIs_001,JO_001);
				MB_001.render(player,E_001);
			}
			MB_001.end();
			SB_001.begin();
			BF_001.getData().setScale(2);
			if(test_009)
			{
				BF_001.draw(SB_001,test_010,0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Align.left,true);
			}
			else
			{
				if(game&&(!loaded)&&!test_002)
				{
					BF_001.draw(SB_001,""+FPS+" X:"+PX+" Z:"+PY+" Z:"+PZ+"    "+progress+"%",0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Align.left,true);
				}
				else
				{
					BF_001.draw(SB_001,""+FPS+" X:"+PX+" Z:"+PY+" Z:"+PZ,0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Align.left,true);
				}
			}
			if(game&&loaded&&!test_002)
			{
				SB_001.draw(dpad,0,0,ddims,ddims);
				SB_001.draw(jump_button,jx,jy,jdims,jdims);
			}
			else if(menu)
			{
				SB_001.draw(button,BX/2,B1Y,BXS+BX,BYS);
				SB_001.draw(button,BX/2,B2Y,BXS+BX,BYS);
				BF_001.getData().setScale((BYS/30));
				BF_001.draw(SB_001,"LOAD GAME",BX,B2Y+(BYS*0.75f),BXS,Align.center,false);
				BF_001.draw(SB_001,"NEW GAME",BX,B1Y+(BYS*0.75f),BXS,Align.center,false);
			}
			else if(newgame)
			{
				BF_001.getData().setScale((BYS/30));
				BF_001.draw(SB_001,filename,0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Align.center,false);
			}
			else if(sellect)
			{
				LH_001.draw(BF_001,SB_001,0);
			}
			SB_001.end();
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	@Override
	public void dispose()
	{
		try
		{
			MB_001.dispose();
			Model[] Ma_001=Ms_001.toArray();
			D_001.dispose();
			CC_002.dispose();
			if(test_002)
			{
				test_012.dispose();
			}
			else
			{
				player_m.dispose();
			}
			for(Model M_001:Ma_001)
				(M_001).dispose();
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	@Override
	public void resize(int width,int height)
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	@Override
	public void pause()
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	@Override
	public void resume()
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
	}
	//app response
	public boolean onBackPressed()
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return true;
	}
	//input
    @Override
    public boolean touchDown(int screenX,int screenY,int pointer,int button)
	{
		try
		{
			test_010=""+pointer+","+Ps_001.size;
			if(Ps_001.size<=pointer)
			{
				Ps_001.add(new Pointer(screenX,screenY));
			}
			else
			{
				Ps_001.get(pointer).activate();
				Ps_001.get(pointer).X=screenX;
				Ps_001.get(pointer).Y=screenY;
			}
			if(game&&screenX>jx&&screenX<jbx2&&screenY>jby1&&screenY<jby2&&onGround)
			{
				jump=true;
				onGround=false;
				jumptime=7;
			}
			else if(menu)
			{
				if(screenX>BX&&screenX<BBX2)
				{
					if(screenY>B1BY1&&screenY<B1BY2)
					{
						filename="";
						menu=false;
						newgame=true;
						Gdx.input.setOnscreenKeyboardVisible(true);
					}
					if(screenY>B2BY1&&screenY<B2BY2)
					{
						FileHandle[] savefiles=maindir.child("saves").list();
						if(savefiles.length>0)
						{
							int temp_001=savefiles.length;
							saves=new String[temp_001];
							for(int for_001=0;for_001<temp_001;for_001++)
							{
								saves[for_001]=savefiles[for_001].name();
							}
							LH_001=new listHandler(saves,5);
							Ps_001.get(pointer).dragged=true;
							menu=false;
							sellect=true;
						}
						else
						{
							AA_001.runOnUiThread(
								new Runnable()
								{
									public void run()
									{
										Toast T_002=Toast.makeText(AA_001,"No saves",100);
										T_002.show();
									}
								});
						}
					}
				}
				else if(newgame)
				{
					Gdx.input.setOnscreenKeyboardVisible(true);
				}
			}
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return false;
	}
    @Override
    public boolean touchDragged(int screenX,int screenY,int pointer)
	{
		try
		{
			if(sellect)
			{
				LH_001.scroll(-(screenY-Ps_001.get(pointer).Y));
			}
			Ps_001.get(pointer).X=screenX;
			Ps_001.get(pointer).Y=screenY;
			Ps_001.get(pointer).dragged=true;
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return false;
	}
    @Override
    public boolean touchUp(int screenX,int screenY,int pointer,int button)
	{
		try
		{
			Ps_001.get(pointer).deactivate();
			if(sellect&&!Ps_001.get(pointer).dragged)
			{
				try
				{
					save=maindir.child("saves").child(saves[LH_001.click(screenX,Gdx.graphics.getHeight()-screenY)]);
					sellect=false;
					JSONObject JO_002=new JSONObject(save.child("data").readString());
					start(JO_002.getLong("seed"),save,JO_002.getInt("X"),JO_002.getInt("Y"),JO_002.getInt("Z"),JO_002.getInt("XP"),JO_002.getInt("YP"),JO_002.getInt("ZP"));
					AA_001.runOnUiThread(
						new Runnable()
						{
							public void run()
							{
								Toast T_002=Toast.makeText(AA_001,save.name(),100);
								T_002.show();
							}
						});
				}
				catch(ArrayIndexOutOfBoundsException e)
				{}
			}
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return false;
	}
    @Override
    public boolean keyTyped(char character)
	{
		try
		{
			filename+=character;
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
        return false;
    }
	@Override
	public boolean keyUp(int keycode)
	{
		try
		{
			if(newgame&&keycode==Input.Keys.BACKSPACE)
			{
				filename=filename.substring(0,filename.length()-1);
			}
			else if(newgame&&keycode==Input.Keys.ENTER)
			{
				if(!filename.equals(""))
				{
					save=maindir.child("saves").child(filename);
					if(save.exists())
					{
						AA_001.runOnUiThread(
							new Runnable()
							{
								public void run()
								{
									Toast T_002=Toast.makeText(AA_001,"A save by this name already exists",100);
									T_002.show();
								}
							});
					}
					else
					{
						try
						{
							save.mkdirs();
							Gdx.input.setOnscreenKeyboardVisible(false);
							newgame=false;
							long seed=MathUtils.random(Long.MIN_VALUE,Long.MAX_VALUE);
							save.child("data").writeString(new JSONObject().put("seed",seed).put("X",0).put("Y",0).put("Z",0).put("rot",0).put("XP",0).put("YP",3).put("ZP",0).toString(),false);
							start(seed,save,0,0,0,0,3,0);
						}
						catch(Throwable e)
						{
							AA_001.runOnUiThread(
								new Runnable()
								{
									public void run()
									{
										Toast T_002=Toast.makeText(AA_001,"Invalid filename name",100);
										T_002.show();
									}
								});
						}
					}
				}
			}
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
        return false;
	}
	@Override
	public boolean keyDown(int keycode)
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return false;
	}
    @Override
    public boolean mouseMoved(int screenX,int screenY)
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
		return false;
	}
    @Override
    public boolean scrolled(int amount)
	{
		try
		{
		}
		catch(Throwable T_001)
		{
			handle(T_001);
		}
        return false;
    }
	//utils
	public void dpad(float X,float Y)
	throws JSONException
	{
		float X2=X;
		float Y2=Y;
		if(X2<0)
		{
			X2*=-1;
		}
		if(Y2<0)
		{
			Y2*=-1;
		}
		if(X2>Y2)
		{
			if(X>0)
			{
				PYROT-=3;
			}
			else
			{
				PYROT+=3;
			}
		}
		else
		{
			double lookX=Math.sin(Math.toRadians(PYROT));
			double lookZ=Math.cos(Math.toRadians(PYROT));
			if(Y>0)
			{
				XRT+=lookX;
				ZRT+=lookZ;
				if(!checkPlayerCollision(0,0,0,false))
				{
					XRT-=lookX;
					ZRT-=lookZ;
				}
			}
			else
			{
				XRT-=lookX/2;
				ZRT-=lookZ/2;
				if(!checkPlayerCollision(0,0,0,false))
				{
					XRT+=lookX/2;
					ZRT+=lookZ/2;
				}
			}
		}
	}
	public boolean checkPlayerCollision(int X,int Y,int Z,boolean correct)
	throws JSONException
	{
		player.transform.set(new Vector3(PX+((((float)XO)+XRT)/10),(PY+(((float)YO)/10))+0.5f,PZ+(((float)ZO)+ZRT)/10),new Quaternion().setEulerAngles(PYROT,0,0));
		HitBox HB_001=new HitBox(player);
		boolean down = true;
		int[]start={-1,-1,1};
		int[]end={0,2,2};
		//-1,0,1_(-1,0),(-1,2),(1,2)
		int X1=start[X+1];
		int X2=end[X+1];
		int Y1=start[Y+1];
		int Y2=end[Y+1];
		int Z1=start[Z+1];
		int Z2=end[Z+1];
		for(int for_001 = X1;for_001<X2;for_001++)
		{
			for(int for_002 = Y1;for_002<Y2;for_002++)
			{
				for(int for_003 = Z1;for_003<Z2;for_003++)
				{
					Tile T_001;
					if(!LA_001.loaded)
					{
						if(correct)
						{
							XO-=X;
							YO-=Y;
							ZO-=Z;
							if(X==0&&Y==0&&Z==0)
							{
								Y-=2;
							}
						}
						return false;
					}
					T_001=LA_001.gettile(PX+for_001,PY+for_002,PZ+for_003);

					if(T_001.overlap(HB_001,MIs_001,JO_001,D_001))
					{
						down=false;
						if(correct)
						{
							XO-=X;
							YO-=Y;//devnote:when working out collision I initialy forgot this step and spent several hours over two days trying to figure out why the player character was falling through the ground
							ZO-=Z;
						}
						break;
					}
				}
				if(!down)
				{
					break;
				}
			}
			if(!down)
			{
				break;
			}
		}
		HB_001.dispose();
		return down;
	}
	public void shiftplayer(int X,int Y,int Z)
	{
		XO-=10*X;
		TX+=X;
		YO-=10*Y;
		TY+=Y;
		ZO-=10*Z;
		TZ+=Z;
		int e=SR*2;
		if(TX<-SR)
		{
			TX-=e*X;
			CX+=X;
			PX=(CX*SS)+TX;
			LA_001.ShiftLeft();
		}
		if(TX>SR)
		{
			TX-=e*X;
			CX+=X;
			PX=(CX*SS)+TX;
			LA_001.ShiftRight();
		}
		if(TY<-SR)
		{
			TY-=e*Y;
			CY+=Y;
			PY=(CY*SS)+TY;
			LA_001.ShiftDown();
		}
		if(TY>SR)
		{
			TY-=e*Y;
			CY+=Y;
			PY=(CY*SS)+TY;
			LA_001.ShiftUp();
		}
		if(TZ<-SR)
		{
			TZ-=e*Z;
			CZ+=Z;
			PZ=(CZ*SS)+TZ;
			LA_001.Shiftback();
		}
		if(TZ>SR)
		{
			TZ-=e*Z;
			CZ+=Z;
			PZ=(CZ*SS)+TZ;
			LA_001.ShiftFront();
		}
	}
	public void start(long seed,FileHandle savedir,int X,int Y,int Z,int XP,int YP,int ZP)
	throws JSONException
	{
		if(!test_002)
		{
			PX=XP;
			PY=YP;
			PZ=ZP;
			OSN_001=new OpenSimplexNoise(seed);
			int int_005=JA_001.length();
			for(int for_001=0;for_001<int_005;for_001++)
			{
				JSONObject JO_002=JA_001.getJSONObject(for_001);
				JO_001.put(JO_002.getString("id"),for_001);
				FileHandle FH_001=Gdx.files.internal("models/"+JO_002.getString("model")+".g3dj");
				Ms_001.add(GML_001.loadModel(FH_001));
				MIs_001.add(new ModelInstance(Ms_001.get(for_001)));
			}
			LA_001=new LocalArea(AR,SR,X,Y,Z,OSN_001,JA_001,JO_001,savedir);
			game=true;
			new Thread()
			{
				public void run()
				{
					while(game)
					{
						try
						{
							Thread.sleep(10000);
						}
						catch(InterruptedException e)
						{}
						try
						{
							if(game)
							{
								save.child("data").writeString((new JSONObject(save.child("data").readString()).put("X",LA_001.AX).put("Y",LA_001.AY).put("Z",LA_001.AZ).put("XP",PX).put("YP",PY).put("ZP",PZ).put("rot",PYROT)).toString(),false);
							}
						}
						catch(Throwable e)
						{
							handle(e);
						}
					}
				}
			}.start();
		}
	}
	//error handling
	public void handle(Throwable T_001)
	{
		AA_001.runOnUiThread(
			new Runnable()
			{
				public void run()
				{
					Toast T_002=Toast.makeText(AA_001,"error detected",100);
					T_002.show();
				}
			});
		StackTraceElement[] STEa_001=T_001.getStackTrace();
		String S_002=T_001+"\n";
		for(StackTraceElement STE_001:STEa_001)
		{
			S_002+=STE_001.toString()+"\n";
		}
		FileHandle err=Gdx.files.external("games/stone to space age/error logs");
		if(!err.exists())
			err.mkdirs();
		err.child(((new Date(TimeUtils.millis())).toString()+".txt").replace(":"," ")).writeString(S_002,false);
	}
	public static void silentHandle(Throwable T_001)
	{
		StackTraceElement[] STEa_001=T_001.getStackTrace();
		String S_002=T_001+"\n";
		for(StackTraceElement STE_001:STEa_001)
		{
			S_002+=STE_001.toString()+"\n";
		}
		FileHandle err=Gdx.files.external("games/stone to space age/error logs");
		if(!err.exists())
			err.mkdirs();
		err.child(((new Date(TimeUtils.millis())).toString()+".txt").replace(":"," ")).writeString(S_002,false);
	}
}
