import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Asteroid extends GameObject{
	// Initialize instance variables
	private int x, y, width, height, points, xPlodeSpeed;
	private double speed, xVel, yVel, dir;
	Polygon asteroid;
	public boolean size;
	
	public Asteroid(int width, int height, double speed) {
		super(ID.Asteroid); //Sets the object's enumeration ID for reference in Handler class iteration
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
		
		this.speed = (float) (speed + Math.random() * 5); //generates a random speed
		points = (int) (Math.random() * 10) + 5; //number of verticies in the asteroid (ex: a five point figure is a pentagon
		
		asteroid = new Polygon(); //initializes the Slick2D polygon object
		
		int asteroidW = 10 + (int) (Math.random() * 100); //The asteroid is generated from a circle, so a radius is calculated
		
		if (asteroidW > 50) //size determines whether or not this asteroid will break up into smaller asteroids
		{
			size = true;
		} else {
			size = false;
		}
		
		
		/*
		 * This for loop iterates from 0 - 2PI radians (360 degrees), in increments of: 360 degrees / the number of points.
		 * 
		 * Each point is assigned a random radius. The coordinates are then converted to coordinates on a Cartesian plane
		 * using basic trigonometric conversions.
		 */
		double angle = 0; //the angle starts at 0 radians
		for (int i = 0; i < points; i++)
		{
			angle += Math.toRadians(360.0 / (double) points);
			int distance = 10 + (int) (Math.random() * asteroidW);
			asteroid.addPoint((float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)));
		}
		
		xPlodeSpeed = 10; //sets the speed at which lines will "explode" from the center of the asteroid, when the asteroid
						  // is hit with a bullet
		asteroid.setClosed(true); //The asteroid is a closed polygon.
		
		dir = Math.random() * Math.PI * 2; //Assigns the asteroid a random direction to travel in.
		
		//the direction is constant, so the xVel and yVel are constant (and assigned in the constructor only).
		xVel += Math.sin(dir)*speed;
		yVel += Math.cos(dir)*speed;
	}
	
	public float[] getCenter() //retrieves the coordinates of the center point of the polygon, and returns an array.
	{
		return asteroid.getCenter();
	}
	
	
	@Override
	public void tick(GameContainer gc, StateBasedGame sbg) {
		
		//update the asteroid's location
		x += Math.round(xVel);
		y += -1 * Math.round(yVel);
		
		//The if statements allow an asteroid to leave the screen and "wrap around" to the other side.
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
		
		//render the polygon
		asteroid.setX((float)x);
		asteroid.setY((float)y);
		g.draw(asteroid);
	}
	
	//returns the asteroid's Polygon object
	public Polygon getShape()
	{
		return asteroid;
	}
	
	/*
	 * When the asteroid is destroyed, a series of Slick2D line objects are created.
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
				point = asteroid.getPoint(points - 1);
			} else {
				point = asteroid.getPoint(i-1);
			}
			float[] point2 = asteroid.getPoint(i);
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
}
