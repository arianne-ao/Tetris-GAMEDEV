import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.golden.gamedev.Game;


public class GameFrame extends Game{

	int [][] iPiece = {{1,1,1,1},{0,0,0,0},{0,0,0,0},{0,0,0,0}}; 
	int [][] tPiece = {{0,1,0,0},{1,1,1,0},{0,0,0,0},{0,0,0,0}};
	int [][] oPiece = {{1,1,0,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}};
	int [][] sPiece = {{0,1,1,0},{1,1,0,0},{0,0,0,0},{0,0,0,0}};
	int [][] zPiece = {{1,1,0,0},{0,1,1,0},{0,0,0,0},{0,0,0,0}};
	int [][] lPiece = {{1,0,0,0},{1,0,0,0},{1,1,0,0},{0,0,0,0}};
	int [][] jPiece = {{0,1,0,0},{0,1,0,0},{1,1,0,0},{0,0,0,0}};
	
	private int [][] grid;
	private final int SPACE = 24;
	private final int OFFSET = 10;
	private final int BUTTOM = 610;
	private tetrisPiece temp;
	
	@Override
	public void initResources() {
		grid = new int[20][25];
		int[][] arr;
		temp = new tetrisPiece(iPiece);
		arr = temp.Rotation(lPiece);
		for(int a = 0; a< arr.length;a++){
			for(int b = 0; b<arr[a].length;b++){
				System.out.print(arr[a][b]);
			}
			System.out.println();
		}
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight() );
		drawGrid(g);
		

	}

	@Override
	public void update(long l) {
		
	}

	public void drawPiece(Graphics2D g, int[][] t){
		g.setColor(Color.RED);
		for(int e=0; e< t.length; e++){
			for(int d=0; d< t[e].length; d++){
				if(t[e][d] == 1){
					g.fillRect(d*SPACE + OFFSET, e*SPACE + OFFSET, SPACE, SPACE);
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
