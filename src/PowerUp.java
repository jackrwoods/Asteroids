import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;

public class PowerUp extends GameObject{

	// Initialize instance variables
	private int x, y, width, height, points, xPlodeSpeed, type;  //0 - rapid fire, 1 - shield, 2 - invincibility
	private double speed, xVel, yVel, dir;
	private Polygon powerUp;
	
	public PowerUp(int width, int height, int type) {
		super(ID.PowerUp);
		this.type = type;
		powerUp = new Polygon();
		this.width = width;
		this.height = height;
		
		//if statement randomly chooses a spawn location for the asteroid
		// it is a 25% chance that the asteroid will spawn on a side of the screen
		double random = Math.random();
		if (random < .25) //top
		{
			x = (int) (Math.random() * width) + 5;
			y = 5;
		} else if (random < .50) {//left
			x = 5;
			y = (int) (Math.random() * height) + 5;
		} else if (random < .75) {//bottom
			x = (int) (Math.random() * width) + 5;
			y = height - 5;
		} else { //right
			x = width - 5;
			y = (int) (Math.random() * height) + 5;
		}
		
		double angle = Math.toRadians(360.0 / 12.0); //the angle starts at 0 radians
		int distance;
		for (int i = 1; i < 13; i++)
		{
			angle += Math.toRadians(360.0 / 12.0);
			if (i % 2 == 0)
			{
				distance = 20;
			} else {
				distance = 10;
			}
			powerUp.addPoint((float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)));
		}

		powerUp.setClosed(true);
		
		double dir = Math.random() * Math.PI * 2; //Assigns the asteroid a random direction to travel in.
		int speed = 3;
		//the direction is constant, so the xVel and yVel are constant (and assigned in the constructor only).
		xVel += Math.sin(dir)*speed;
		yVel += Math.cos(dir)*speed;
	}

	@Override
	public void tick(GameContainer gc, StateBasedGame sbg) {
		
		//update the asteroid's location
		x += Math.round(xVel);
		y += -1 * Math.round(yVel);
		
		//MOVE THIS TO HANDLER CLASS TO DESTROY OBJECT
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
		//render the polygon w/ designated color
		if (type == 0)
		{
			g.setColor(Color.red);
		} else if (type == 1) {
			g.setColor(Color.green);
		} else {
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
		powerUp.setX((float)x);
		powerUp.setY((float)y);
		g.draw(powerUp);
		g.setColor(Color.white);
	}

	@Override
	public Polygon getShape() {
		// TODO Auto-generated method stub
		return powerUp;
	}
	
	/*
	 * When the powerUp is destroyed, a series of Slick2D line objects are created.
	 * This method returns an ArrayList of objects that can be added to the ArrayList in the handler class.
	 */
	public ArrayList<Line> destroy()
	{
		ArrayList<Line> line = new ArrayList<Line>();
		for (int i = 0; i < points; i++)
		{
			float[] point;
			if (i == 0)
			{
				point = powerUp.getPoint(points - 1);
			} else {
				point = powerUp.getPoint(i-1);
			}
			float[] point2 = powerUp.getPoint(i);
			line.add(new Line(point[0], point[1], point2[0], point2[1]));
		}
		return line;
	}
	
	
	//Getter methods.
	public double getXPlodeSpeed()
	{
		return xPlodeSpeed;
	}
	public double getXPlodeAngle()
	{
		return Math.toRadians(360.0 / (double) points);
	}
	public int getType()
	{
		return type;
	}

}
