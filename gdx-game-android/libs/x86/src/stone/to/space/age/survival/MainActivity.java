package stone.to.space.age.survival;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
public class MainActivity extends AndroidApplication
{
	MyGdxGame MGG_001;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration AAC_001 = new AndroidApplicationConfiguration();
		MGG_001 = new MyGdxGame(this);
		initialize(MGG_001, AAC_001);
	}
	@Override
	public void onBackPressed()
	{
		if(MGG_001.onBackPressed())
			super.onBackPressed();
	}
}
