import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;

//Abstract game object
public abstract class GameObject
{
	protected int x, y;
	protected ID id;
	
	public abstract void tick(GameContainer gc, StateBasedGame sbg);
	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics g);
	public abstract Polygon getShape();
	
	public GameObject(ID id)
	{
		
		setID(id);
		x = 0;
		y = 0;
	}
	
	public GameObject(int x, int y, ID id)
	{
		
		setID(id);
		this.x = x;
		this.y = y;
	}
	
	
	public ID getID()
	{
		return id;
	}
	
	public void setID(ID id)
	{
		this.id = id;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
}
