import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;


//AsteroidLine is the line created when an asteroid is destroyed
public class AsteroidLine extends GameObject{
	
	private int x1,y1,x2,y2; //coordinates for all four points
	private double xVel, yVel; //velocities
	private Line line; //Slick2D line object

	public AsteroidLine(Line line, double xVel, double yVel) {
		
		super (ID.Line);
		this.xVel = xVel;
		this.yVel = yVel;
		this.line = line;
		x1 = (int)line.getX1();
		x2 = (int)line.getX2();
		y1 = (int)line.getY1();
		y2 = (int)line.getY2();
	}

	@Override
	public void tick(GameContainer gc, StateBasedGame sbg) {
	 	//set(float[] start, float[] end)
        //Configure the line
		x1+= xVel;
		y1+= yVel;
		x2+= xVel;
		y2+= yVel;
		float[] point1 = new float[] {x1,y1};
		float[] point2 = new float[] {x2,y2};
		line.set(point1, point2);
		x = x1;
		y = y1;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.draw(line);
	}

	@Override
	public Polygon getShape() {
		return null;
	}

}
