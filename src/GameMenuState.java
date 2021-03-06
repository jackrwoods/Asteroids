import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameMenuState extends BasicGameState{

	static Handler handler;
	private int width, height;
	
	public GameMenuState(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//Initializes a handler to render objects - currently unused
		handler = new Handler(width, height, gc);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("By Jack Woods", width/2 - 50, height - 40);
		g.drawString("Use the mouse to rotate your ship.", width/2 - 150, height - 80);
		g.drawString("Use the left mouse button to shoot.", width/2 - 150, height - 120);
		g.drawString("Use the right mouse button to blink.", width/2 - 150, height - 160);
		g.drawString("Use the spacebar to accellerate.", width/2 - 150, height - 200);
		g.drawString("Press Spacebar to Play", width/2 - 100, 170);
		Image image = new Image("assets/images/logo.png"); //Image taken from http://www.drakkashi.com/portfolio.php
		g.drawImage(image, width/2 - 210, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		Input userInput = gc.getInput(); //listens for keyboard and mouse input
		if (userInput.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(1); //start the game if the spacebar is pressed
		}
		if (userInput.isKeyPressed(Input.KEY_ESCAPE))
		{
			System.exit(0); //exit the game if the escape key is pressed
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}