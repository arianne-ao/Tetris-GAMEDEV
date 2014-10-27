import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

	int Score, combo = 1;

	int[][] gameField;
	int x_padding = 5, y_padding = 5;
	int gameLevel = 1;

	int level_default = 2, x_coord_default = 6;
	int x_coor = x_coord_default;
	int level = level_default;
	int xGhost, yGhost;

	boolean extremeMode = true;
	boolean findGhost = false;

	boolean temp = false, temp2 = false, pause = false, rotate = false;
	boolean pieceLanded = false;

	int GRID_PADDING_X = (x_padding + COL_SIZE + 1) *CELL_SIZE, GRID_PADDING_Y = (y_padding) *CELL_SIZE;
	int PREVIEW_SIZE = 15, PREVIEW_LENGTH = 5;

	int SLOT_SIZE = 10, SLOT_LENGTH = 5;
	int SLOT_X = (x_padding - 3) * CELL_SIZE;
	int SLOT_Y = y_padding * CELL_SIZE;
	int MAX_STORE = 3;

	//	ArrayList<int[][]> storedPieces;
	ArrayList<Tetromino> storedPiecess;


	int[][] currentPiece, nextPiece;

	/*	int[][] o =  
		{{0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}};

	int[][] i =   
		   {{0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0},
		    {0, 1, 2, 1, 1},
		    {0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0}};

	int[][] l_left =  
		{{0, 0, 0, 0, 0},
		{0, 0, 1, 0, 0},
		{0, 0, 2, 0, 0},
		{0, 0, 1, 1, 0},
		{0, 0, 0, 0, 0}};

	int[][] l_right = 
		{{0, 0, 0, 0, 0},
		{0, 0, 1, 0, 0},
		{0, 0, 2, 0, 0},
		{0, 1, 1, 0, 0},
		{0, 0, 0, 0, 0}};



	int[][] n_left = {
		    {0, 0, 0, 0, 0},
		    {0, 0, 0, 1, 0},
		    {0, 0, 2, 1, 0},
		    {0, 0, 1, 0, 0},
		    {0, 0, 0, 0, 0}
		    };

	int[][] n_right = {
		    {0, 0, 0, 0, 0},
		    {0, 0, 1, 0, 0},
		    {0, 0, 2, 1, 0},
		    {0, 0, 0, 1, 0},
		    {0, 0, 0, 0, 0}
		    };

	int[][] t =
		{{0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}};
	 */	


	Tetromino ii = new t_shape();
	Tetromino current_tetromino, next_tetromino;

	Tetromino storeSlot_1, storeSlot_2, storeSlot_3; 


	public void initResources() {


		gameField = new int[COL_SIZE][ROW_SIZE];

	/*	gameField[0][10] = 1;

		gameField[0][15] = 1;
		gameField[1][15] = 1;
		gameField[2][15] = 1;

		gameField[3][14] = 1;
		gameField[4][14] = 1;
		gameField[5][14] = 1;
		gameField[6][14] = 1;
*/

		block = getImage("res/blocks.png");
		Image[] tile = new Image[block.getWidth()/width];
		for (int i = 0; i < tile.length; i++) {
			tile[i] = block.getSubimage(i*width, 0, width, width);
		}

		tetris = fontManager.getFont(getImages("res/tetris.png", 9, 7),
				"ABCDEFG" +
						"HIJKLMNOPQ" +
						"RSTUVWXYZ01234" +
				".,\"'!#");


		over = false;
		mainScreen = false;

		timer = new Timer(300);


		/*		currentPiece = o;
		currentPieceColor = O;
		nextPiece = t;
		nextPieceColor = T;
		 */


		//		storedPieces = new ArrayList<int[][]>();
		//		storedPieces.add(o);
		//		storedPieces.add(t);
		//		storedPieces.add(i);

		storedPiecess = new ArrayList<Tetromino>();
		//		ii.setPiecePosition(t);
		storedPiecess.add(storeSlot_1);
		storedPiecess.add(storeSlot_2);
		storedPiecess.add(storeSlot_3);

		current_tetromino = newPiece();
		next_tetromino = newPiece();

	}

	private void clearLine(int lineNum){
		for (int col = lineNum; col > 1; col--) {
			for (int row = 0; row < 10; row++) {
				gameField[row][col] = gameField[row][col-1];
			}
		}

		/*for (int x = lineNum; x < 1; x--) {
				for(int y=0; y<gameField[x].length;y++){
					gameField[x][y] = 0;
				}
				//gameField[x] = //gameField[x-1];
			}*/

	}

	private void lineFull(){
		//int index = -1;
		int linesCleared = 0;
		boolean fullLine = true;
		for(int col = 0; col < 16;col++){
			for(int row = 0; row<10 && fullLine; row++){
				//assume line is full
				if(gameField[row][col] == 0){
					//System.out.println("line not full");
					fullLine = false;
				}

			}//y
			if(fullLine == true){
				//System.out.println("Line full " + "col: " + col   );
				clearLine(col);
				linesCleared++;
				//	System.out.println("Lines Cleared: " + linesCleared);
			}

			fullLine = true;
		}//x
		if(linesCleared == 0){
			combo = 1;
		}else if(linesCleared ==1){
			Score += 100*gameLevel*combo;
			combo++;
		}else if(linesCleared==2){
			Score += 200*gameLevel*combo;
			combo++;
		}else if(linesCleared == 3){
			Score += 300*gameLevel*combo;
			combo++;
		}else {
			Score += 500*gameLevel*combo;
			combo++;
		}
		//	System.out.println("Score: "+ Score);
		//System.out.println("Combo: " + combo);

		//return index;
	}

	private void addPieceToGameField(int[][] currentPiece, int color_index) {
		System.out.println("add piece");
		int newX = 0, newY = 0;

		for (int i = 0; i < currentPiece.length; i++) {
			for (int j = 0; j < currentPiece[i].length; j++) {
				if(currentPiece[i][j] == 1 || currentPiece[i][j] == 2){
					newX = x_coor + i - x_padding;
					newY = level + j - y_padding;
					if((newX < COL_SIZE && newX >= 0) && (newY < ROW_SIZE && newY >= 0)){
						//						gameField[x_coor + i - x_padding][level + j - y_padding] = 1;
						gameField[x_coor + i - x_padding][level + j - y_padding] = color_index + 2;
						System.out.println((x_coor + i - x_padding)+" "+(level + j - y_padding));
					}
					else{
						System.out.println("Game Over");
						gameOver();
					}					
				}
			}
		}

	}


	private void printGameField() {



		for (int i = 0; i < ROW_SIZE-1; i++) {
			for (int j = 0; j < COL_SIZE-1; j++) { 
				System.out.print(gameField[j][i] + " ");				
			}
			System.out.println();
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
		//		System.out.println((j+level+1)+" "+ (y_padding + ROW_SIZE -1));


		if((((i+ x_coor) >= x_padding) && ((i+x_coor) <= x_padding + COL_SIZE -1)) &&
				(((j+ level + 1) >= y_padding) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
		{
			//			System.out.println("trueeee  " + (i+x_coor) +" " + (j+level+1));

			return true;
		}
		else {

			//			System.out.println((i+x_coor) +" " + (j+level+1));
			//			System.out.println("falseee  "+ (j+level));
			return false;
		}
	}

	boolean isInsideGrid(int i, int j, int move){
		if(rotate){
			System.out.println(" "+(i+ x_coor + move) + " " + x_padding +" "+((i+ x_coor + move) >= x_padding));
		}

		if((((i+ x_coor + move) >= x_padding) && ((i + x_coor + move) <= x_padding + COL_SIZE -1)) &&
				(((j+ level + 1) >= y_padding) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
		{
			//			System.out.println((i+ x_coor + move) + " " + x_padding +" "+((i+ x_coor + move) >= x_padding));
			return true;
		}
		else {
			return false;
		}		
	}

	private void checkPieceCollision(int[][] piece) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){
					//if piece is inside the game field
					if( isInsideGrid(i, j)){
						// System.out.println("inside game field");
						//System.out.println( + (x_coor + i - 5)+ " " +(level + j - 4));
						if(gameField[x_coor + i - x_padding][level + j + 1 - y_padding] > 0){
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

	public int[][] rotation(int[][] currentRotation){
		/* Reference 
		http://gamedev.stackexchange.com/questions/54299/tetris-rotations-using-linear-algebra-rotation-matrices
		 */	
		int [][] nextShape = new int [5][5];
		int N = currentRotation.length;
		for(int x = 0; x < N; x++){
			for(int y = 0; y < N; y++ ){
				nextShape[y][N-x-1] = currentRotation[x][y];
			}
		}
		return nextShape;
	}


	//	private void movePiece() {
	//
	//		if(keyPressed(KeyEvent.VK_LEFT)){
	////			if(!isOutofWall(currentPiece, -1))
	//			if(!isOutofWall(current_tetromino.getPiecePosition(), -1))				
	//				x_coor--;
	//		}
	//		else if(keyPressed(KeyEvent.VK_RIGHT)){
	////			if(!isOutofWall(currentPiece, 1))
	//			if(!isOutofWall(current_tetromino.getPiecePosition(), 1))
	//				x_coor++;
	//		}
	//		else if(keyPressed(KeyEvent.VK_UP)){
	//			rotate = true;
	///*			if(!isOutofWall(rotation(currentPiece), 0))
	//				currentPiece = rotation(currentPiece);
	//*/
	//			if(!isOutofWall(rotation(current_tetromino.getPiecePosition()), 0))
	//				current_tetromino.setPiecePosition(rotation(current_tetromino.getPiecePosition()));
	//
	//
	//			System.out.println();
	//			rotate = false;
	//		}
	//
	//
	//	}


	private void movePiece(int move) {
		if(!isOutofWall(current_tetromino.getPiecePosition(), move))				
			x_coor += move;
	}

	private void readInput() {
		if(keyPressed(KeyEvent.VK_ENTER)){
			if(pause){
				System.out.println("resume");
				pause = false;
			}
			else {
				System.out.println("pause");
				//				printPieceCoordinate();
				pause = true;
			}
		}		
		else if(keyPressed(KeyEvent.VK_LEFT)){
			movePiece(-1);
		}
		else if(keyPressed(KeyEvent.VK_RIGHT)){
			movePiece(1);
		}
		else if(keyPressed(KeyEvent.VK_UP)){
			rotate = true;
			if(!isOutofWall(rotation(current_tetromino.getPiecePosition()), 0))
				current_tetromino.setPiecePosition(rotation(current_tetromino.getPiecePosition()));

			System.out.println();
			rotate = false;
		}
		/*		
		//fix
		else if(keyPressed(KeyEvent.VK_SPACE)){
			if(!isOutofWall(rotation(current_tetromino.getPiecePosition()), 0))
				level++;

		}
		 */
		else if(keyPressed(KeyEvent.VK_CONTROL)){
			storeSlot_1 = storePiece(storeSlot_1);
			updateStorePieceList();
		}
		else if(keyPressed(KeyEvent.VK_SHIFT)){
			storeSlot_2 = storePiece(storeSlot_2);				
			updateStorePieceList();
		}
		else if(keyPressed(KeyEvent.VK_CAPS_LOCK)){
			storeSlot_3 = storePiece(storeSlot_3);
			updateStorePieceList();			
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
				//				else{
				//					gt.setColor(Color.black);
				//					gt.drawRect((i+x_coor)*width, (j + level)*width, width, width);
				//				}

			}
		}
	}
	private void findGhostPosition(int[][] piece) {
		int temp=0;
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){

					//if( isInsideGrid(i, j) && xGhost+ i - x_padding >= 0 && yGhost + j + 1 - y_padding >= 0){
						int xtemp  = xGhost + i - x_padding;
						int ytemp = yGhost + j + 1 - y_padding;
						System.out.println("Ghost Field: " + xtemp + " " + ytemp + " Ghost: " + xGhost + " " + yGhost);
						
						if(gameField[xGhost + i - x_padding][yGhost + j + 1 - y_padding] == 0){
							temp++;



						//}

					}

				}
			}
		}
		if(temp == 4){
			findGhost = true;
		}



	}

	private void renderGhostPiece(Graphics2D gt,  int[][] piece){

		//for (int i = x_coor; i < 10; i++) {
			for (int j = 15; j >= 0; j--) {
				
					if(!findGhost){
						xGhost = x_coor;
						yGhost = j;
						//System.out.println("finding");
						findGhostPosition(piece);
						//System.out.println("find");
					}
					

			//}
		}
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				if(piece[a][b] == 1 || piece[a][b] == 2){

					gt.setColor(Color.LIGHT_GRAY);
					gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);					

					gt.setColor(Color.BLACK);
					gt.drawRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
					//System.out.println(xGhost +" "  + yGhost);

					
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

	private void drawStoreSlots(Graphics2D gt) {

		int y = SLOT_Y;

		for (int n = 0; n < 3; n++) {
			gt.drawRect(SLOT_X, y, SLOT_SIZE * SLOT_LENGTH, SLOT_SIZE * SLOT_LENGTH);							

			//			for (int i = 0; i < SLOT_LENGTH; i++) {
			//				for (int j = 0; j < SLOT_LENGTH; j++) {
			//					gt.drawRect(SLOT_X + i * SLOT_SIZE, y + j * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);							
			//				}
			//			}

			y += (SLOT_LENGTH + 2) * SLOT_SIZE;
		}

	}


	private void renderStoredPieces(Graphics2D gt) {
		int y = SLOT_Y;
		y = SLOT_Y + ((SLOT_LENGTH + 2) * SLOT_SIZE) * 2 ;

		for (Tetromino p: storedPiecess) {
			if(p != null){
				int[][] piece = p.getPiecePosition();
				for (int i = 0; i < piece.length; i++) {
					for (int j = 0; j < piece[i].length; j++) {
						if(piece[i][j] == 1 || piece[i][j] == 2){
							gt.setColor(p.getColor());
							gt.fillRect(SLOT_X + i * SLOT_SIZE, y + j * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);		
							gt.setColor(Color.black);						
							gt.drawRect(SLOT_X + i * SLOT_SIZE, y + j * SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);		
						}
					}
				}
				//			y += (SLOT_LENGTH + 2) * SLOT_SIZE;
				y -= (SLOT_LENGTH + 2) * SLOT_SIZE;
			}
		}
	}

	private void printMainScreen(Graphics2D gt) {
		tetris.drawString(gt, "TETRIS", 20, 440);
	}

	private void printGameOverScreen(Graphics2D gt) {
		tetris.drawString(gt, "GAME OVER", 10, 440);

	}

	private Tetromino storePiece(Tetromino storeSlot) {
		Tetromino store;
		if(storeSlot != null){
			store = current_tetromino;
			current_tetromino = storeSlot;
		}
		else{
			store = current_tetromino;
			generateNewPiece();
		}

		return store;
	}

	private void updateStorePieceList() {
		storedPiecess = new ArrayList<Tetromino>();
		storedPiecess.add(storeSlot_1);
		storedPiecess.add(storeSlot_2);
		storedPiecess.add(storeSlot_3);
	}

	private Color setBlockColor(int color) {
		Color c;

		switch (color) {
		case 2: c = Color.yellow; break;
		case 3: c = Color.cyan; break;
		case 4: c = Color.orange; break;
		case 5: c = Color.blue; break;
		case 6: c = new Color(220, 20, 60); break;
		case 7: c = Color.green; break;
		case 8: c = new Color(158, 0, 240); break;
		default: c = Color.gray;
		}

		return c;
	}


	private void renderGameField(Graphics2D gt) {
		//Draw Grid		

		for (int i = 0; i < gameField.length; i++) {
			for (int j = 0; j < gameField[i].length; j++) {
				if(gameField[i][j] > 0){
					//					gt.setColor(Color.red);
					gt.setColor(setBlockColor(gameField[i][j]));
					gt.fillRect((i + x_padding) *CELL_SIZE, (j + y_padding)*CELL_SIZE, CELL_SIZE, CELL_SIZE);				
					gt.setColor(Color.black);
					gt.drawRect((i + x_padding) *CELL_SIZE, (j + y_padding)*CELL_SIZE, CELL_SIZE, CELL_SIZE);				
				}
				gt.setColor(Color.black);
				// 			gt.drawRect(i*CELL_SIZE + x, j*CELL_SIZE + y, CELL_SIZE, CELL_SIZE);				
				gt.drawRect((i + x_padding) *CELL_SIZE, (j + y_padding)*CELL_SIZE, CELL_SIZE, CELL_SIZE);				
			}
		}

	}

	public void render(Graphics2D gt) {
		if(mainScreen)
			printMainScreen(gt);

		gt.setColor(Color.white);
		gt.fillRect(0, 0, getWidth(), getHeight());

		gt.setColor(Color.black);

		//Draw outer wall
		//		gt.drawRect(x_padding * CELL_SIZE, y_padding * CELL_SIZE, COL_SIZE * CELL_SIZE, ROW_SIZE * CELL_SIZE);

		renderGameField(gt);

		// Display next piece

		gt.drawRect(GRID_PADDING_X + PREVIEW_SIZE, GRID_PADDING_Y + PREVIEW_SIZE, PREVIEW_SIZE * PREVIEW_LENGTH, PREVIEW_SIZE * PREVIEW_LENGTH);				

		//		for (int i = 0; i < PREVIEW_LENGTH; i++) {
		//			for (int j = 0; j < PREVIEW_LENGTH; j++) {
		//				gt.drawRect(GRID_PADDING_X + i * PREVIEW_SIZE, GRID_PADDING_Y + j * PREVIEW_SIZE, PREVIEW_SIZE, PREVIEW_SIZE);				
		//			}
		//		}



		/*		renderNextPiece(gt, nextPiece, nextPieceColor);
		renderPiece(gt, currentPiece, currentPieceColor);
		 */
		renderNextPiece(gt, next_tetromino.getPiecePosition(), next_tetromino.getColor());
		renderPiece(gt, current_tetromino.getPiecePosition(), current_tetromino.getColor());
		renderGhostPiece(gt, current_tetromino.getPiecePosition());

		//		gt.drawRect(x, y, COL_SIZE * CELL_SIZE, ROW_SIZE*CELL_SIZE);

		if(extremeMode){
			drawStoreSlots(gt);
			renderStoredPieces(gt);
		}
		//		if(over)
		//			printGameOverScreen(gt);


	}




	// checks if the piece is still inside the game field
	private boolean isOutofWall(int[][] piece, int move) {
		//		System.out.println("out of wall func");
		//		boolean wall = false;

		// loop though the piece array
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {

				// if array item contains a piece
				if(piece[i][j] == 1 || piece[i][j] == 2){

					//					System.out.println(i+ x_coor + move);
					// if piece is inside field
					if(isInsideGrid(i, j, move)){
						// if check will hit an existing piece
						if(gameField[x_coor + i + move - x_padding][level + j + 1 - y_padding] >= 1){
							// then the move is not allowed
							//							System.out.println("out of bounds");
							//							wall = true; 
							return true;
						}
					}
					else{ 	// if piece is not inside the game field (will hit a wall)
						// then the move is also not allowed
						//						System.out.println("out of bounds");
						//						wall = true; 
						//						break;
						return true;
					}
				}
			}
		}
		return false;

	}

	private void gameOver() {
		over = true;
		System.out.println("Game over");
	}


	private Tetromino newPiece() {
		Tetromino piece = new o_shape();

		int rand = ((int)(Math.random() * 7)) ;
		switch (rand) {
		case 0: piece = new o_shape(); break;
		case 1: piece = new i_shape(); break;
		case 2: piece = new l_left_shape();break;
		case 3: piece = new l_right_shape(); break;
		case 4: piece = new n_left_shape(); break;
		case 5: piece = new n_right_shape(); break;
		case 6: piece = new t_shape(); break;
		}

		return piece;	
	}

	private void resetValuesForNewPiece() {
		// reinitialize values
		level = level_default;
		x_coor = x_coord_default;
		pieceLanded = false;
		findGhost = false;
	}

	private void generateNewPiece() {
		current_tetromino = next_tetromino;
		next_tetromino = newPiece(); 
		resetValuesForNewPiece();
	}

	/*	private void generateNewPiece() {

		//generate new piece
		currentPieceColor = nextPieceColor;
		currentPiece = nextPiece;

		int rand = ((int)(Math.random() * 7)) ;
		switch (rand) {
		case 0: nextPiece = o; break;
		case 1: nextPiece = i; break;
		case 2: nextPiece = l_left; break;
		case 3: nextPiece = l_right; break;
		case 4: nextPiece = n_left; break;
		case 5: nextPiece = n_right; break;
		case 6: nextPiece = t; break;
		}

//		nextPieceColor = T;
//		nextPiece = t;

		// reinitialize values
		level = level_default;
		x_coor = x_coord_default;
		pieceLanded = false;

	}

	 */	
	public void update(long l) {

		//		movePiece();
		readInput();
		findGhost = false;
		//lineFull();
		if(!pause && !over){
			if(timer.action(l)){
				//				checkPieceCollision(currentPiece);
				checkPieceCollision(current_tetromino.getPiecePosition());
				if(pieceLanded){

					/*					addPieceToGameField();
					generateNewPiece();
					 */					
					// using tetromino class
					addPieceToGameField(current_tetromino.getPiecePosition(), current_tetromino.getColor_index());
					lineFull();
					generateNewPiece();
					//					printGameField();

				}
				else{
					//if(!temp)
					level++;
				}
			}

		}


	}




}
