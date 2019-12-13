package stone.to.space.age.survival;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.shaders.*;
import com.badlogic.gdx.backends.android.*;

public class MyGdxGame implements ApplicationListener, InputProcessor
{
	AndroidApplication AA_001;
	Array <AnimationController> ACs_001;
	Array <AnimationController.AnimationDesc> AC_ADs_001;
	Array <Model> Ms_001;
	Array <ModelInstance> MIs_001;
	Camera C_001;
	Environment E_001;
	ModelBatch MB_001;
	ShaderProvider SP_001;
	SpriteBatch SB_001;
	public MyGdxGame (AndroidApplication AA_001)
	{
		this.AA_001 = AA_001;
	}
	@Override
	public void create()
	{
		E_001 = new Environment();
		E_001.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		C_001 = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		C_001.position.set(0f, 0f, 5f);
		C_001.lookAt(0f, 0f, 0f);
		C_001.near = 0.5f;
		C_001.far = 300f;
		C_001.update();
		Gdx.input.setInputProcessor(this);
		SP_001 = new DefaultShaderProvider(new DefaultShader.Config(DefaultShader.getDefaultVertexShader(), DefaultShader.getDefaultFragmentShader()));
		MB_001 = new ModelBatch(SP_001);
		SB_001 = new SpriteBatch();
	}
	@Override
	public void render()
	{
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		MB_001.begin(C_001);
		MB_001.end();
		SB_001.begin();
		SB_001.end();
	}
	@Override
	public void dispose()
	{
	}
	@Override
	public void resize(int width, int height)
	{
	}
	@Override
	public void pause()
	{
	}
	@Override
	public void resume()
	{
	}
	//app response
	public boolean onBackPressed()
	{
		return true;
	}
	//input
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}
	//eventual pc port (if it happens)
	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}
    @Override
    public boolean keyTyped(char character)
	{
        return false;
    }
	@Override
	public boolean keyUp(int keycode)
	{
        return false;
	}
    @Override
    public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}
    @Override
    public boolean scrolled(int amount)
	{
        return false;
    }
}
