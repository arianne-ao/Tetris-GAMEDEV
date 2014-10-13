import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.golden.gamedev.Game;


public class GameFrame extends Game{

	int [][] line = {{1,1,1,1},{0,0,0,0},{0,0,0,0},{0,0,0,0}}; 
	private int [][] grid;
	private final int SPACE = 24;
	private final int OFFSET = 10;
	private final int BUTTOM = 610;
	//private tetrisPiece temp;
	
	@Override
	public void initResources() {
		grid = new int[20][25];
		int[][] arr;
		//temp = new tPiece();
		/*arr = temp.nextRotation(1);
		for(int a = 0; a< arr.length;a++){
			for(int b = 0; b<arr[a].length;b++){
				System.out.print(arr[a][b]);
			}
			System.out.println();
		}*/
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight() );
		drawGrid(g);
		tetrisPiece temp = new tPiece();
		
		
		//g.setColor(Color.BLUE);
		//g.drawLine(0,BUTTOM, 100, BUTTOM);
		//g.fillRect(2*SPACE+10, 5*SPACE+10, SPACE, SPACE);
		
		//temp.render(g, temp.topleftX, temp.topleftY);
		drawPiece(g, temp.normalShape);

	}

	@Override
	public void update(long l) {
		
	}

	public void drawPiece(Graphics2D g, int[][] t){
		g.setColor(Color.RED);
		for(int e=0; e< t.length; e++){
			for(int d=0; d< t[e].length; d++){
				if(t[e][d] == 1){
					g.fillRect(d*SPACE+10, e*SPACE+10, SPACE, SPACE);
					//temp.render(g, d*SPACE+10, e*SPACE+10);
				}
			}
		}
	}

	public void readInput()
	{

		if (keyPressed(KeyEvent.VK_UP)){

		}
		if (keyPressed(KeyEvent.VK_RIGHT)){


		}
		if (keyPressed(KeyEvent.VK_DOWN)){

		}
		if (keyPressed(KeyEvent.VK_LEFT)){


		}

	}

	public void drawGrid(Graphics2D g){
		g.setColor(Color.BLACK);
		for(int r=0; r < grid.length; r++){
			for(int e = 0; e < grid[r].length; e++){
				g.drawRect(r*SPACE + OFFSET, e*SPACE + OFFSET, SPACE, SPACE);
			}

		}
	}

}
