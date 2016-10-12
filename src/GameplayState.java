import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//in-game gamestate
public class GameplayState extends BasicGameState{

	static Handler handler;
	private int width, height;
	
	public GameplayState(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		handler = new Handler(width, height, gc);//Initialize
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		handler.render(gc, sbg, g);	
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		handler.tick(gc, sbg);
		Input userInput = gc.getInput();
		if (userInput.isKeyPressed(Input.KEY_ESCAPE))
		{
			System.exit(0); //Exit if escape key is pressed
		}
		
	}

	@Override
	public int getID() {
		return 1;
	}

}