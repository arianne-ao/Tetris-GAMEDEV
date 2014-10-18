package tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.Timer;


public class GameFrame extends Game {

	Timer timer;
	Boolean over, mainScreen;
	GameFont tetris;
	int COL_SIZE = 10,ROW_SIZE = 16, CELL_SIZE = 25;
	BufferedImage block;
	int width = 25;
	Color O = Color.yellow;
	Color T = Color.blue;
	Color currentPieceColor, nextPieceColor;
	
	
	int[][] gameField;
	int x_padding = 5, y_padding = 5;
	int level = 0;
	int level_default = 0, x_coord_default = 5;
	int x_coor = x_coord_default;
	
	boolean temp = false, temp2 = false, pause = false;
	boolean pieceLanded = false;

	int GRID_PADDING_X = (x_padding + COL_SIZE + 1) *CELL_SIZE, GRID_PADDING_Y = (y_padding) *CELL_SIZE;
	int PREVIEW_SIZE = 15, PREVIEW_LENGTH = 5;

	
	int[][] currentPiece, nextPiece;
	int[][] o =  
		{{0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}};
	
	
	int[][] t =
		{{0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}};
	
	public void initResources() {
		

		gameField = new int[COL_SIZE][ROW_SIZE];

		gameField[0][10] = 1;

		gameField[0][15] = 1;
		gameField[1][15] = 1;
		gameField[2][15] = 1;

		gameField[3][14] = 1;
		gameField[4][14] = 1;
		gameField[5][14] = 1;
		gameField[6][14] = 1;

		
//		block = getImage("res/blocks.png");
//		Image[] tile = new Image[block.getWidth()/width];
//		for (int i = 0; i < tile.length; i++) {
//			tile[i] = block.getSubimage(i*width, 0, width, width);
//		}
				
		tetris = fontManager.getFont(getImages("res/tetris.png", 9, 7),
                "ABCDEFG" +
        		"HIJKLMNOPQ" +
        		"RSTUVWXYZ01234" +
        		".,\"'!#");

		
		over = false;
		mainScreen = false;
		
		timer = new Timer(1000);

		
		currentPiece = o;
		currentPieceColor = O;
		nextPiece = t;
		nextPieceColor = T;
		
	}
	
	private void addPieceToGameField() {
		System.out.println("add piece");
		for (int i = 0; i < currentPiece.length; i++) {
			for (int j = 0; j < currentPiece[i].length; j++) {
				if(currentPiece[i][j] == 1 || currentPiece[i][j] == 2){
					gameField[x_coor + i - x_padding][level + j - y_padding] = 1;
					System.out.println((x_coor + i - x_padding)+" "+(level + j - y_padding));
				}
			}
		}
		
	}
	
	private void printPieceCoordinate() {
		for (int i = 0; i < currentPiece.length; i++) {
			for (int j = 0; j < currentPiece[i].length; j++) {
				if(currentPiece[i][j] == 1 || currentPiece[i][j] == 2){
					System.out.println((x_coor + i - x_padding)+" "+(level + j - y_padding));
				}
			}
		}		
	}
	
	boolean isInsideGrid(int i, int j){
		System.out.println((j+level+1)+" "+ (y_padding + ROW_SIZE -1));

		
		if((((i+ x_coor) >= x_padding) && ((i+x_coor) <= x_padding + COL_SIZE -1)) &&
		  (((j+ level + 1) >= y_padding) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
		{
//			System.out.println("trueeee  " + (i+x_coor) +" " + (j+level+1));

			return true;
		}
		else {
		
//			System.out.println((i+x_coor) +" " + (j+level+1));
			System.out.println("falseee  "+ (j+level));
			return false;
		}
	}
	
	boolean isInsideGrid(int i, int j, int move){
		if((((i+ x_coor + move) >= x_padding) && ((i + x_coor + move) <= x_padding + COL_SIZE -1)) &&
		  (((j+ level + 1) >= y_padding) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	private void checkPieceCollision(int[][] piece) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if(piece[i][j] == 1){
					//if piece is inside the game field
					if( isInsideGrid(i, j)){
						// System.out.println("inside game field");
						// System.out.println((x_coor + i - 5)+ " " +(level + j - 4));
						if(gameField[x_coor + i - x_padding][level + j + 1 - y_padding] == 1){
							// System.out.println("collied");
							pieceLanded = true;
							// System.out.println((x_coor + i - x_padding)+" "+(level + j + 1 - y_padding));
							//break;
						}
					}
					// if piece will hit bottom wall
					else if(((j + level + 1) > y_padding + ROW_SIZE -1)){
						pieceLanded = true;
						break;

					}
				}
			}
		}

	}
	
	private void movePiece() {

		if(keyPressed(KeyEvent.VK_LEFT)){
			if(!isOutofWall(currentPiece, -1))
				x_coor--;
		}
		else if(keyPressed(KeyEvent.VK_RIGHT)){
			if(!isOutofWall(currentPiece, 1))
				x_coor++;
		}


	}

	private void readInput() {
		if(keyPressed(KeyEvent.VK_SPACE)){
			if(pause){
				System.out.println("resume");
				pause = false;
			}
			else {
				System.out.println("pause");
				printPieceCoordinate();
				pause = true;
			}
		}
	}
	
	
	private void renderPiece(Graphics2D gt, int[][] piece, Color color) {
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){
					gt.setColor(color);
					gt.fillRect((i+x_coor)*width, (j + level)*width, width, width);					
//					System.out.println(((j+level)*width) + "  " + i);
					gt.setColor(Color.black);
					gt.drawRect((i+x_coor)*width, (j + level)*width, width, width);
				}
				else{
					gt.setColor(Color.black);
					gt.drawRect((i+x_coor)*width, (j + level)*width, width, width);
				}
				
			}
		}
	}

	private void renderNextPiece(Graphics2D gt, int[][] piece, Color color) {
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){
					gt.setColor(color);
					gt.fillRect(GRID_PADDING_X + i * PREVIEW_SIZE + PREVIEW_SIZE, GRID_PADDING_Y + j * PREVIEW_SIZE+ PREVIEW_SIZE, PREVIEW_SIZE, PREVIEW_SIZE);				

					gt.setColor(Color.black);
					gt.drawRect(GRID_PADDING_X + i * PREVIEW_SIZE + PREVIEW_SIZE, GRID_PADDING_Y + j * PREVIEW_SIZE+ PREVIEW_SIZE, PREVIEW_SIZE, PREVIEW_SIZE);				
				}
//				else{
//					gt.setColor(Color.black);
//					gt.drawRect(GRID_PADDING_X + i * PREVIEW_SIZE + PREVIEW_SIZE, GRID_PADDING_Y + j * PREVIEW_SIZE+ PREVIEW_SIZE, PREVIEW_SIZE, PREVIEW_SIZE);				
//				}

			}
		}
	}

	private void printMainScreen(Graphics2D gt) {
		tetris.drawString(gt, "TETRIS", 20, 440);



	}

	public void render(Graphics2D gt) {
		if(mainScreen)
			printMainScreen(gt);

		gt.setColor(Color.white);
		gt.fillRect(0, 0, getWidth(), getHeight());

		gt.setColor(Color.black);
		
		//Draw Grid
		for (int i = 0; i < gameField.length; i++) {
			for (int j = 0; j < gameField[i].length; j++) {
				if(gameField[i][j] == 1){
					gt.setColor(Color.red);
//					gt.fillRect(i*CELL_SIZE + x, j*CELL_SIZE + y, CELL_SIZE, CELL_SIZE);				
					gt.fillRect((i + x_padding) *CELL_SIZE, (j + y_padding)*CELL_SIZE, CELL_SIZE, CELL_SIZE);				
				}
				gt.setColor(Color.black);
//				gt.drawRect(i*CELL_SIZE + x, j*CELL_SIZE + y, CELL_SIZE, CELL_SIZE);				
				gt.drawRect((i + x_padding) *CELL_SIZE, (j + y_padding)*CELL_SIZE, CELL_SIZE, CELL_SIZE);				
			}
		}
		
		// Display next piece

		gt.drawRect(GRID_PADDING_X + PREVIEW_SIZE, GRID_PADDING_Y + PREVIEW_SIZE, PREVIEW_SIZE * PREVIEW_LENGTH, PREVIEW_SIZE * PREVIEW_LENGTH);				
		
//		for (int i = 0; i < PREVIEW_LENGTH; i++) {
//			for (int j = 0; j < PREVIEW_LENGTH; j++) {
//				gt.drawRect(GRID_PADDING_X + i * PREVIEW_SIZE, GRID_PADDING_Y + j * PREVIEW_SIZE, PREVIEW_SIZE, PREVIEW_SIZE);				
//				
//			}
//		}

		renderNextPiece(gt, nextPiece, nextPieceColor);
		
		
//		renderPiece(gt, o, O);
		renderPiece(gt, currentPiece, currentPieceColor);


		//		gt.drawRect(x, y, COL_SIZE * CELL_SIZE, ROW_SIZE*CELL_SIZE);


	}
	
	
	// checks if the piece is still inside the game field
	private boolean isOutofWall(int[][] piece, int move) {
		System.out.println("out of wall func");
		boolean wall = false;
	
		// loop though the piece array
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
		
				// if array item contains a piece
				if(piece[i][j] == 1 || piece[i][j] == 2){
					
					System.out.println(i+ x_coor + move);
					// if piece is inside field
					if(isInsideGrid(i, j, move)){
						// if check will hit an existing piece
						if(gameField[x_coor + i + move - x_padding][level + j + 1 - y_padding] == 1){
							// then the move is not allowed
							System.out.println("out of bounds");
							wall = true; 
						}
					}
					else{ 	// if piece is not inside the game field (will hit a wall)
						// then the move is also not allowed
						System.out.println("out of bounds");
						wall = true; 
						break;
						
					}
				}
			}
		}
		return wall;

	}

	private void generateNewPiece() {
		//generate new piece
		currentPieceColor = nextPieceColor;
		currentPiece = nextPiece;

		nextPieceColor = T;
		nextPiece = t;
		
		// reinitialize values
		level = level_default;
		x_coor = x_coord_default;
		pieceLanded = false;

	}
	
	public void update(long l) {
		
		if(!pause){
		
			
			if(timer.action(l)){
			
				
				checkPieceCollision(currentPiece);
				if(pieceLanded){
					addPieceToGameField();
					generateNewPiece();
					
							
				}
				else{
//				if(!temp)
					level++;
				}
		}
		
		}
		
		movePiece();
		readInput();
	}
	
	
	

}
