
import java.util.ArrayList;

import javax.sound.sampled.Line;

import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.state.StateBasedGame;

public class Handler

{
	private int width, height;
	private SpaceShip player;
	ArrayList<GameObject> object; //A list of objects to render
	GameContainer gc;
	Input userInput;
	static int points = 0; //the player's score
	private long startTime, shootTime;
	double blinkPowerUp = 1.0; //Allows the player to teleport when the powerup is full.
	private boolean gameOver, rapidFire, shield, invincibility, spawnPowerUp, fire;
	
	public Handler(int width, int height, GameContainer gc)
	{
		this.gc = gc; //Save the game container
		userInput = gc.getInput(); //mouse and key listener
		this.width = width;
		this.height = height;
		object = new ArrayList<GameObject>(); //initialize arraylist
		player = new SpaceShip (width, height, width/2, height/2, 1.0, 0.0, gc); //create player in middle of the screen
		addObject(player); //add the player to the arraylist, to be rendered
		gameOver = false;
		rapidFire = false;
		shield = false;
		invincibility = false;
		
		//begin game with 10 asteroids
		for (int i = 0; i < 10; i++)
		{
			addObject(new Asteroid(width, height, 3.0)); //create handler for rendering gameplay
		}
	}
	
	public void tick(GameContainer gc, StateBasedGame sbg)
	{
		asteroidSpawn();
		powerUpSpawn();
		
		System.gc(); //remove objects that have been removed from the arraylist from memory
		
		//invincibility powerup uses the same code as shield
		if(invincibility)
		{
			shield = true;
			player.shieldOn();
		}
		
		//refill powerup to a maximum of 2
		if (blinkPowerUp < 2)
		{
			blinkPowerUp += 0.001;
		}
		
		//disable powerup if 4 seconds has elapsed
		if (getTime() - startTime >= 10)
		{
			if (rapidFire)
			{
				player.rapidFireOff();
				rapidFire = false;
			}
			if (invincibility)
			{
				player.invincibilityOff();
				invincibility = false;
			}
		}
		
		//implementing blink functionality
		if (userInput.isMousePressed(Input.MOUSE_RIGHT_BUTTON) && !gameOver && blinkPowerUp >=1) //gameOver is true if the player is dead
		{
			int speed = 10;
			double dir = 0;
			for (int i = 0; i < 3; i++)
			{
				dir += 60;
				object.add(new AsteroidLine(player.destroy().get(i), Math.sin(dir)*speed, Math.cos(dir)*speed));
			}
			blinkPowerUp -= 1;
			player.setX(userInput.getMouseX());
			player.setY(userInput.getMouseY());
			
		}
		
		//implementing shoot functionality
		if (userInput.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !gameOver && !rapidFire) //gameOver is true if the player is dead, rapidfire is true if the rapidfire powerup has been collected
		{
			object.add(new Bullet(player.pointX, player.pointY, player.dir));
		}
		
		//implementing shoot functionality
		if (userInput.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !gameOver && rapidFire) //gameOver is true if the player is dead, rapidfire is true if the rapidfire powerup has been collected
		{
			object.add(new Bullet(player.pointX, player.pointY, player.dir));
		}
		
		// tick all objects in the list
		for (int i = 0; i < object.size(); i++)
		{
			object.get(i).tick(gc, sbg);
		}
		
		//Test for bullet collisions
		for (int i = 0; i < object.size(); i++)
		{
			Bullet current;
			if (object.get(i).getID() == ID.Bullet)
			{
				current = (Bullet) object.get(i);
				
				//destroy bullet if it leaves the screen
				if(current.getX() > width || current.getX() < 0)
				{
					object.remove(i);
				}
				if(current.getY() > width || current.getY() < 0)
				{
					object.remove(i);
				}
				int x = object.get(i).getX();
				int y = object.get(i).getY();
				//if there was a collision, decrement i by appropriate amount
				if(asteroidCollision(current))
				{
					i-=2;
					if(points % 10 == 0) spawnPowerUp = true;
				}
				if(powerUpCollision(current))
				{
					i-=2;
				}
			}
		}
		
		for (int i = 0; i < object.size(); i++)
		{
			if (object.get(i).getID() == ID.Line)
			{
				GameObject current = object.get(i);
				//destroy line if it leaves the screen
				if(current.getX() > width || current.getX() < 0)
				{
					object.remove(i);
				} else if (current.getY() > width || current.getY() < 0)
				{
					object.remove(i);
				}
			}
		}
		
		//test for player collisions
		boolean collision = asteroidCollision(player);
		if(collision && shield)
		{
			shield = false;
			player.shieldOff();
		} else if (collision && !shield){
			gameOver = true;
			int speed = 10;
			double dir = 0;
			for (int i = 0; i < 3; i++)
			{
				dir += 60;
				object.add(new AsteroidLine(player.destroy().get(i), Math.sin(dir)*speed, Math.cos(dir)*speed));
			}
			object.remove(player);;
		}
		
		
		
		//end game
		int asteroidLineCount = 0;
		for (int i = 0; i < object.size(); i++)
		{
			if (object.get(i).getID() == ID.Line) asteroidLineCount++;
		}
		if (gameOver && asteroidLineCount == 0)
		{
			sbg.enterState(2);
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
	{
		//render all objects
		for (int i = 0; i < object.size(); i++)
		{
			object.get(i).render(gc, sbg, g);
		}
		
		//render points and powerup
		g.drawString("Points: "+points, width/2 - 50, height - 40);
		g.drawString(String.format("Blink: %.2f", blinkPowerUp), width/2 - 50, height - 80);
	}
	
	private boolean asteroidCollision(GameObject currentObject)
	{
		//make a list of all asteroid objects
		ArrayList<GameObject> asteroid = new ArrayList<GameObject>();
		for (int i = 0; i < object.size(); i++)
		{
			if (object.get(i).getID() == ID.Asteroid)
			{
				asteroid.add(object.get(i));
			}
		}
		//Iterate through asteroids and test for collisions
		//Polygon currentShape = currentObject.getShape();
		for (int n = 0; n < asteroid.size(); n++)
		{
			Asteroid currentAsteroid = (Asteroid) asteroid.get(n);
			Polygon currentShape = currentObject.getShape();
			Polygon asteroidShape = currentAsteroid.getShape();
			if (asteroidShape.intersects(currentShape) && !gameOver) //if the polygons intersect, there was a collision
			{
				//create and render asteroidLines
				double dir = 0;
				for (int k = 0; k <  currentAsteroid.destroy().size(); k++)
				{
					int speed = 10;
					dir += currentAsteroid.getXPlodeAngle();
					object.add(new AsteroidLine(currentAsteroid.destroy().get(k), (-1 * Math.sin(dir)*speed),  Math.cos(dir)*speed));
				}
				//remove the objects and decrement iteration variables accordingly
				if (currentObject != player){
					object.remove(currentObject);
				}
				object.remove(currentAsteroid);
				asteroid.remove(n);
				n--;
				points += 1; //add 1 point to player's score
				return true;
			}
		}
		return false;
	}
	
	private boolean powerUpCollision(GameObject currentObject)
	{
		//make a list of all asteroid objects
		ArrayList<GameObject> powerUp = new ArrayList<GameObject>();
		for (int i = 0; i < object.size(); i++)
		{
			if (object.get(i).getID() == ID.PowerUp)
			{
				powerUp.add(object.get(i));
			}
		}
		//Iterate through asteroids and test for collisions
		//Polygon currentShape = currentObject.getShape();
		for (int n = 0; n < powerUp.size(); n++)
		{
			PowerUp currentAsteroid = (PowerUp) powerUp.get(n);
			Polygon currentShape = currentObject.getShape();
			Polygon asteroidShape = currentAsteroid.getShape();
			int type = currentAsteroid.getType();
			if (asteroidShape.intersects(currentShape) && !gameOver) //if the polygons intersect, there was a collision
			{
				//create and render asteroidLines
				double dir = 0;
				for (int k = 0; k <  currentAsteroid.destroy().size(); k++)
				{
					int speed = 10;
					dir += currentAsteroid.getXPlodeAngle();
					object.add(new AsteroidLine(currentAsteroid.destroy().get(k), (-1 * Math.sin(dir)*speed),  Math.cos(dir)*speed));
				}
				//remove the objects and decrement iteration variables accordingly
				object.remove(currentObject);
				object.remove(currentAsteroid);
				powerUp.remove(n);
				n--;
				points += 1; //add 1 point to player's score
				
				//set the correct variable for the powerup
				if (type == 0) {
					rapidFire = true;
					player.rapidFireOn();
					setStartTime();
				} else if (type == 1) {
					shield = true;
					player.shieldOn();
					setStartTime();
				} else {
					invincibility = true;
					player.invincibilityOn();
					player.shieldOn();
					setStartTime();
				}
				return true;
			}
		}
		return false;
	}
	
	private void asteroidSpawn()
	{
		double spawnChance = Math.random() / Math.random();
		if (spawnChance > 50) addObject(new Asteroid(width, height, points * .3 + 1));
	}
	
	private void powerUpSpawn()
	{
		if (points % 10 == 0 && points != 0 && spawnPowerUp)
		{
			spawnPowerUp = false;
			double spawnChance = Math.random();
			if (spawnChance > .75) {
				addObject(new PowerUp(width, height, 2));
			} else {
				addObject(new PowerUp(width, height, 1));
			}
		}
	}
	
	public void addObject (GameObject object)
	{
		this.object.add(object);
	}
	
	public GameObject getObject (int i)
	{
		return object.get(i);
	}
	
	public int getSize ()
	{
		return object.size();
	}
	
	public int getPoints ()
	{
		return points;
	}
	
	public void removeObject (GameObject object)
	{
		this.object.remove(object);
	}
	public void setStartTime ()
	{
		startTime = (int) (Sys.getTime() / 1000);
	}
	public int getTime()
	{
		return (int) (Sys.getTime() / 1000);
	}
	
}