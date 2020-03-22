package stone.to.space.age.survival;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.*;
public class listHandler
{
	String[]list;
	int scroll=0;
	int size;
	public listHandler(String[]list,int size)
	{
		this.list=list;
		this.size=size;
	}
	public void draw(BitmapFont font,SpriteBatch batch,int X)
	{
		font.getData().setScale(size);
		int length=list.length;
		for (int for_001=0;for_001<length;for_001++)
		{
			font.draw(batch,list[for_001],X,Gdx.graphics.getHeight()+scroll-((size*2)+(for_001*(size*25))));
		}
	}
	public void scroll(int ammount)
	{
		scroll+=ammount;
		float max=(list.length*(size*25))-Gdx.graphics.getHeight();
		if (scroll>max)
		{
			scroll=(int)max;
		}
		if (scroll<0)
		{
			scroll=0;
		}
	}
	public int click(int X,int Y)
	{
		//    size:6    ( y,scroll,pos) (scroll+size)-y
		//****####$#****( 1,     4,  9)
		//****##$###****( 3,     4,  7)
		//******$#####**( 5,     6,  7)
		//******##$###**( 3,     6,  9)
		return (int)((((float)scroll)+Gdx.graphics.getHeight())-((float)Y))/(size*25);
		//return((int)Math.ceil((((double)scroll)+((double)Y)-(size*2.5))/(size*21)))-2;
	}
}
