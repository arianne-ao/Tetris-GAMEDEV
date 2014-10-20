
import java.awt.Dimension;

import com.golden.gamedev.GameLoader;

public class TetrisGame {
	
	public static void main(String[] args) {
		GameFrame gt = new GameFrame();
		GameLoader game = new GameLoader();
		game.setup(gt, new Dimension(640, 640), false); // false - window format , true - full screen
		game.start();		
	}

}
