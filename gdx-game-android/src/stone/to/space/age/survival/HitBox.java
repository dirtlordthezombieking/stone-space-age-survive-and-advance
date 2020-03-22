package stone.to.space.age.survival;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
public class HitBox extends btCollisionObject
{
	public Matrix4 WT_001;
	public HitBox(ModelInstance src)
	{
		super();
		BoundingBox BB_001 = new BoundingBox();
		BB_001=src.calculateBoundingBox(BB_001);
		btCollisionShape CS_001 = new btBoxShape(new Vector3((BB_001.getDimensions(new Vector3()).x/2)-0.01f,(BB_001.getDimensions(new Vector3()).y/2)-0.01f,(BB_001.getDimensions(new Vector3()).z/2)-0.01f));
		setCollisionShape(CS_001);
		Matrix4 M4_001=new Matrix4(src.transform);
		M4_001.setToTranslation(M4_001.getTranslation(new Vector3()).add(BB_001.getCenter(new Vector3())));
		setWorldTransform(M4_001);
		WT_001=M4_001;
	}
	public static boolean checkCollosion(btCollisionObject CO_001,btCollisionObject CO_002,btDispatcher D_001)
	{
		CollisionObjectWrapper COW_001 = new CollisionObjectWrapper(CO_001);
		CollisionObjectWrapper COW_002 = new CollisionObjectWrapper(CO_002);
		btCollisionAlgorithmConstructionInfo CACI_001 = new btCollisionAlgorithmConstructionInfo();
		CACI_001.setDispatcher1(D_001);
		btCollisionAlgorithm CA_001=new btBoxBoxCollisionAlgorithm(null,CACI_001,COW_001.wrapper,COW_002.wrapper);
		btDispatcherInfo DI_001=new btDispatcherInfo();
		btManifoldResult MR_001=new btManifoldResult(COW_001.wrapper,COW_002.wrapper);
		CA_001.processCollision(COW_001.wrapper,COW_002.wrapper,DI_001,MR_001);
		boolean ret=MR_001.getPersistentManifold().getNumContacts()>0;
		MR_001.dispose();
		DI_001.dispose();
		CA_001.dispose();
		CACI_001.dispose();
		COW_001.dispose();
		COW_002.dispose();
		return ret;
	}
	@Override
	public void dispose()
	{
		getCollisionShape().dispose();
		super.dispose();
	} 
}
