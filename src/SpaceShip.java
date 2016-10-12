import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class SpaceShip extends GameObject
{
	
	protected int width, height;
	public int pointX, pointY;
	protected double xVel, yVel, dir, hp, speed; //dir is a direction in degrees
	protected boolean isSpacePressed, renderShield, invincibility;
	protected Input userInput;
	private Polygon ship;
	Color color;
	

	public SpaceShip(int width, int height, int x, int y, double speed, double dir, GameContainer gc) { //MouseInfo.getPointerInfo().getLocation()
		//initialize instance variables
		super(ID.SpaceShip);
		
		//create the space ship's polygon
		ship = new Polygon();
		color = Color.white;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.speed = speed;
		userInput = gc.getInput();
		userInput.enableKeyRepeat();
		this.width = width;
		this.height = height;
		xVel  = 1;
		yVel = 1;
	}
	
	//rotates/orients the space ship
	public void updateDirection(GameContainer gc, StateBasedGame sbg)
	{
		
		float mouseX = userInput.getMouseX();
		float mouseY = userInput.getMouseY();
		
		Vector2f direction = new Vector2f((float) x, (float) y);
		
		float xDis = mouseX - direction.x;
		float yDis = mouseY - direction.y;
		
		dir = Math.atan2(yDis,  xDis) + Math.PI/2; //dir is used to rotate and render the space ship
	}

	@Override
	public void tick(GameContainer gc, StateBasedGame sbg) {
		updateDirection(gc, sbg); //update direction (above)
		if(userInput.isKeyPressed(Input.KEY_SPACE)) //should the ship be accellerating?
		{
			xVel += Math.sin(dir)*speed;
			yVel += Math.cos(dir)*speed;
			isSpacePressed = true; //used to render the rocket booster flame

		} else {
			isSpacePressed = false;
		}
		
		//set the x and y velocities
		x += Math.round(xVel);
		y += -1 * Math.round(yVel);
		
		//setting the speed limits
		if (xVel > 4.0)xVel = 3.5;
		if (yVel > 4.0)yVel = 3.5;
		if (xVel < -4.0)xVel = -3.5;
		if (yVel < -4.0)yVel = -3.5;
		
		
		//allows the space ship to exit the screen and wrap around to the other side
		if (x < 0)
		{
			x = width;
		} else if (x > width)
		{
			x = 0;
		}
		if (y < 0)
		{
			y = height;
		} else if (y > height)
		{
			y = 0;
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		
		//edit the space ship's polygon
		ship = new Polygon();
		int middleX = 15;
		int middleY = 15;
		ship.addPoint(middleX,0);
		ship.addPoint(0, 30);
		ship.addPoint(30, 30);
		ship.setClosed(true);
		//create the rocket booster flame
		Polygon rocket = new Polygon();
		if(isSpacePressed)
		{
			rocket.addPoint(0,31);
			rocket.addPoint(31,31);
			rocket.addPoint(15, (float) (Math.random() * 31 + 45)); //creates random flame effect by altering the height of the triangle
		} else {
			rocket.addPoint(0,31);
			rocket.addPoint(31,31);
			rocket.addPoint(15, 31);
		}
		rocket.setClosed(true);
		
		//set the x and y coordinates of both polygons
		ship.setX((float)x);
		ship.setY((float)y);
		rocket.setX((float)x);
		rocket.setY((float)y);
		
		//the Transform class from Slick2D is used to rotate the polygons. It is applied to both.
		Transform t = Transform.createRotateTransform((float) dir, ship.getCenterX(), ship.getCenterY());
		ship = (Polygon) ship.transform(t); //rotate the ship
		g.setColor(color);
		if(invincibility)
		{
			double random = Math.random(); //the invincibility powerup changes colors
			if (random < .25)
			{
				g.setColor(Color.red);
			} else if (random < .50) {
				g.setColor(Color.green);
			} else if (random < .75) {
				g.setColor(Color.blue);
			} else {
				g.setColor(Color.white);
			}
		}
		g.draw(ship); //render the ship
		g.fill(ship);
		rocket = (Polygon) rocket.transform(t);//rotate the rocket booster
		g.setColor(Color.orange);//change color to orange, because fire is roughly orange
		g.draw(rocket);//render the rocket booster
		g.fill(rocket);//fill it in
		
		//render the shield if the player has a shield
		if (renderShield)
		{
			Circle circle = new Circle(ship.getCenterX(), ship.getCenterY(), ship.getBoundingCircleRadius() + 5);
			g.setColor(Color.green);
			g.draw(circle);
		}
		g.setColor(Color.white);//color should always be white by default
		
		//save the actual x,y coordinates of the ship, for bullet creation
		float[] coordinates = ship.getPoint(0);
		pointX = (int) coordinates[0];
		pointY = (int) coordinates[1];
	}
	
	public void setXVel(double xVel)
	{
		this.xVel = xVel;
	}
	
	public void setYVel(double yVel)
	{
		this.yVel = yVel;
	}
	public Polygon getShape()
	{
		return ship;
	}
	public void shieldOn()
	{
		renderShield = true;
	}
	public void shieldOff()
	{
		renderShield = false;
	}
	public void invincibilityOn()
	{
		invincibility = true;
	}
	public void invincibilityOff()
	{
		invincibility = false;
	}
	public void rapidFireOn()
	{
		color = Color.red;
	}
	public void rapidFireOff()
	{
		color = Color.white;
	}
	
	public ArrayList<Line> destroy()
	{
		ArrayList<Line> line = new ArrayList<Line>();
		for (int i = 0; i < 3; i++)
		{
			float[] point;
			if (i == 0)
			{
				point = ship.getPoint(2);
			} else {
				point = ship.getPoint(i-1);
			}
			float[] point2 = ship.getPoint(i);
			line.add(new Line(point[0], point[1], point2[0], point2[1]));
		}
		return line;
	}

}
