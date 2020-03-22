package stone.to.space.age.survival;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import java.io.*;
import android.content.*;
public class MainActivity extends AndroidApplication
{
	MyGdxGame MGG_001;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration AAC_001 = new AndroidApplicationConfiguration();
		
		AAC_001.useImmersiveMode=true;
		MGG_001 = new MyGdxGame(this);
		initialize(MGG_001, AAC_001);
	}
	@Override
	public void onBackPressed()
	{
		if(MGG_001.onBackPressed())
			super.onBackPressed();
	}
	@Override
	protected void onDestroy()
	{
		deleteDirectory(this.getCacheDir());
		super.onDestroy();
	}
	public boolean deleteDirectory(File file)
	{
		if(file!=null&&file.isDirectory())
		{
			String[]files=file.list();
			for(int for_1=0;for_1<files.length;for_1++)
			{
				boolean deleated=deleteDirectory(new File(file,files[for_1]));
				if(!deleated)
				{
					return false;
				}
			}
			return file.delete();
		}
		else if(file!=null&&file.isFile())
		{
			return file.delete();
		}
		else
		{
			return false;
		}
	}
}
