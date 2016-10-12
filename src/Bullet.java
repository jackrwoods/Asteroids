import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.state.StateBasedGame;

public class Bullet extends GameObject{

	private ID id = ID.Bullet;
	private int x, y;
	private float speed;
	private double dir;
	private Polygon bullet;
	public Bullet(int x, int y, double dir) {
		// TODO Auto-generated constructor stub
		super(x,y,ID.Bullet);
		bullet = new Polygon();
		bullet.addPoint(0,0);
		bullet.addPoint(0, 10);
		bullet.addPoint(10, 10);
		bullet.addPoint(10, 0);
		bullet.setClosed(true);
		this.x = x;
		this.y = y;
		this.dir = dir;
		speed = 20;
	}

	@Override
	public void tick(GameContainer gc, StateBasedGame sbg) {
		// TODO Auto-generated method stub
		x += Math.sin(dir)*speed;
		y += -1 * Math.cos(dir)*speed;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// TODO Auto-generated method stub
		bullet.setX(x);
		bullet.setY(y);
		Transform t = Transform.createRotateTransform((float) dir, bullet.getCenterX(), bullet.getCenterY());
		bullet.transform(t);
		g.draw(bullet);
	}
	public Polygon getShape()
	{
		return bullet;
	}

}
