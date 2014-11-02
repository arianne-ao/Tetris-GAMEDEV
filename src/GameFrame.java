import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.Timer;


public class GameFrame extends Game {

	Timer timer;
	Boolean over, mainScreen, settingScreen;
	GameFont tetris, main;
	int COL_SIZE = 10,ROW_SIZE = 16, CELL_SIZE = 25;
	BufferedImage block;
	int width = 25;
	Color O = Color.yellow;
	Color T = Color.blue;
	Color currentPieceColor, nextPieceColor;

	int xGhost, yGhost;
	boolean findGhost = false;
	int spaceGhost = 0;
	boolean isBlockBetween = false;
	int specialY;
	int countBlock;
	int option = 1, MAX_OPTION = 3;

	int score, combo = 1;
	int linesCleared;


	int[][] gameField;
	int x_padding = 5, y_padding = 5;
	int gameLevel = 1;

	int level_default = 2, x_coord_default = 6;
	int x_coor = x_coord_default;
	int level = level_default;

	boolean classicMode = false, extremeMode = true;
	String mode = "Extreme";

	boolean temp = false, temp2 = false, pause = false, rotate = false;
	boolean pieceLanded = false;

	int GRID_PADDING_X = (x_padding + COL_SIZE + 1) *CELL_SIZE, GRID_PADDING_Y = (y_padding) *CELL_SIZE;
	int PREVIEW_SIZE = 15, PREVIEW_LENGTH = 5;

	int SLOT_SIZE = 10, SLOT_LENGTH = 5;
	int SLOT_X = (x_padding - 3) * CELL_SIZE;
	int SLOT_Y = y_padding * CELL_SIZE;
	int MAX_STORE = 3;

	ArrayList<Tetromino> storedPiecess;
	Tetromino pChecker; 
	Tetromino ooChecker; 


	int[][] currentPiece, nextPiece;


	Tetromino ii = new t_shape();
	Tetromino current_tetromino, next_tetromino;

	Tetromino storeSlot_1, storeSlot_2, storeSlot_3; 

	ArrayList<PieceFrequency> list;
	int p_freq_pointer;
	int freq_sum;
	int[] pieceWeights;



	public void initResources() {

		pChecker = new p_shape();
		ooChecker = new oo_shape();
		gameField = new int[COL_SIZE][ROW_SIZE];

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

		main = fontManager.getFont(getImages("res/font_ok.png", 10, 10),
				" !\"#$%&'()"+
						"*+,-./0123"+
						"456789:;<=" +
						">?@ABCDEFG" +
						"HIJKLMNOPQ" +
						"RSTUVWXYZ[" +
						"\\]^_`abcde" +
						"fghijklmno" +
						"pqrstuvwxy" +
				"z{|}~     ");


		over = false;
		mainScreen = true;
		settingScreen = false;

		timer = new Timer(300);
		linesCleared = 0;

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

		list = new ArrayList<PieceFrequency>();
		list.add(new PieceFrequency("O", new o_shape(), 3));
		list.add(new PieceFrequency("I", new i_shape(), 4));
		list.add(new PieceFrequency("LL", new l_left_shape(), 3));
		list.add(new PieceFrequency("LR", new l_right_shape(), 3));
		list.add(new PieceFrequency("NL", new n_left_shape(), 3));
		list.add(new PieceFrequency("NR", new n_right_shape(), 3));
		list.add(new PieceFrequency("T", new t_shape(), 3));

		list.add(new PieceFrequency("OO", new oo_shape(), 1));
		list.add(new PieceFrequency("P", new p_shape(), 1));

		p_freq_pointer = 0; freq_sum = 24;
		setFrequency();
	}


	private void startGame() {


		over = false;
		mainScreen = false;
		settingScreen = false;

		timer = new Timer(300);
		setFrequency();

		if(classicMode){
			current_tetromino = newPiece();
			next_tetromino = newPiece();
		}
		else{
			current_tetromino = newPieceExtreme();
			next_tetromino = newPieceExtreme();

		}
	}

	private void clearLine(int lineNum){
		for (int col = lineNum; col > 1; col--) {
			for (int row = 0; row < 10; row++) {
				gameField[row][col] = gameField[row][col-1];
			}
		}

	}

	private void lineFull(){
		//int index = -1;
		boolean fullLine = true;
		for(int col = 0; col < 16;col++){
			for(int row = 0; row<10 && fullLine; row++){
				//assume line is full
				if(gameField[row][col] == 0){
					fullLine = false;
				}

			}//y
			if(fullLine == true){
				clearLine(col);
				linesCleared++;
				
			}

			fullLine = true;
		}//x
		if(linesCleared == 0){
			combo = 1;
		}else if(linesCleared ==1){
			score += 100*gameLevel*combo;
			combo++;
		}else if(linesCleared==2){
			score += 200*gameLevel*combo;
			combo++;
		}else if(linesCleared == 3){
			score += 300*gameLevel*combo;
			combo++;
		}else {
			score += 500*gameLevel*combo;
			combo++;
		}
		//System.out.println("Score: "+ score);
		//System.out.println("Combo: " + combo);

		//return index;
	}

	private void addPieceToGameField(int[][] currentPiece, int color_index) {
		
		int newX = 0, newY = 0;

		for (int i = 0; i < currentPiece.length; i++) {
			for (int j = 0; j < currentPiece[i].length; j++) {
				if(currentPiece[i][j] == 1 || currentPiece[i][j] == 2){
					newX = x_coor + i - x_padding;
					newY = level + j - y_padding;
					if((newX < COL_SIZE && newX >= 0) && (newY < ROW_SIZE && newY >= 0)){
						gameField[x_coor + i - x_padding][level + j - y_padding] = color_index + 2;
						
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



		for (int i = 0; i < ROW_SIZE; i++) {
			for (int j = 0; j < COL_SIZE; j++) { 
						
			}
			System.out.println();
		}
	}


	private void printPieceCoordinate() {
		for (int i = 0; i < currentPiece.length; i++) {
			for (int j = 0; j < currentPiece[i].length; j++) {
				if(currentPiece[i][j] == 1 || currentPiece[i][j] == 2){
					
				}
			}
		}		
	}

	boolean isInsideGrid(int i, int j){
		


		if((((i+ x_coor) >= x_padding) && ((i+x_coor) <= x_padding + COL_SIZE -1)) &&
				(((j+ level + 1) >= y_padding + 1) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
		{
			

			return true;
		}
		else {

		
			return false;
		}
	}

	boolean isInsideGrid(int i, int j, int move){
		if(rotate){
			//System.out.println(" "+(i+ x_coor + move) + " " + x_padding +" "+((i+ x_coor + move) >= x_padding));
		}

		if((((i+ x_coor + move) >= x_padding) && ((i + x_coor + move) <= x_padding + COL_SIZE -1)) &&
				(((j+ level + 1) >= y_padding + 1) && ((j+level+1) <= y_padding + ROW_SIZE -1)))
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
						//						 System.out.println((x_coor + i - 5)+ " " +(level + j - 4));
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


	
	private void movePiece(int move) {
		if(!isOutofWall(current_tetromino.getPiecePosition(), move))				
			x_coor += move;
	}

	private void readInput() {

		if(mainScreen && !settingScreen){
			if(keyPressed(KeyEvent.VK_UP)){
				option--;
				if(option < 1)
					option = 1;
			}
			else if(keyPressed(KeyEvent.VK_DOWN)){
				option++;
				if(option > MAX_OPTION)
					option = MAX_OPTION;
			}
			else if(keyPressed(KeyEvent.VK_ENTER)){
				if(option == 1){
					//start game
					startGame();
				}
				else if (option == 2){
					//mode
					toogleMode();
				}
			
				else if(option == 3){
					settingScreen = true;
				}
			}

		}
		else if(settingScreen){
			if(keyPressed(KeyEvent.VK_UP)){
				p_freq_pointer--;
				if(p_freq_pointer < 0)
					p_freq_pointer = 0;
			}
			else if(keyPressed(KeyEvent.VK_DOWN)){
				int maxPiece = 9;
				p_freq_pointer++;
				if(p_freq_pointer > maxPiece - 1)
					p_freq_pointer = maxPiece - 1;
			}
			else if(keyPressed(KeyEvent.VK_RIGHT)){
				changeFrequency(1);
			}
			else if(keyPressed(KeyEvent.VK_LEFT)){
				changeFrequency(-1);
			}
			else if(keyPressed(KeyEvent.VK_BACK_SPACE)){
				settingScreen = false;				
			}

		}
		else {
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
			else if (keyPressed(KeyEvent.VK_SPACE)){
				level = yGhost + 1;
			}
		
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
				
				y -= (SLOT_LENGTH + 2) * SLOT_SIZE;
			}
		}
	}

	private void printMainScreen(Graphics2D gt) {
		clearScreen(gt);


		tetris.drawString(gt, "TETRIS", 80, 100);

		main.drawString(gt, "START", 200, 300);		
		
		main.drawString(gt, mode, 165, 380);

		main.drawString(gt, "Settings", 150, 460);

		int[][] xPlay = {{50, 70, 50},{305, 330, 355}};
		int[][] xLevel = {{50, 70, 50},{385, 410, 435}};
		int[][] xSettings = {{50, 70, 50},{465, 490, 515}};

		//+105 
		gt.setColor(Color.blue);
		if(option == 1)
			gt.fillPolygon(xPlay[0], xPlay[1], 3);
		else if(option == 2)
			gt.fillPolygon(xLevel[0], xLevel[1], 3);
		else if(option == 3)
			gt.fillPolygon(xSettings[0], xSettings[1], 3);



	}

	private void toogleMode() {
		if(classicMode){
			extremeMode = true;
			classicMode = false;		
			mode = "Extreme";
		}
		else{
			extremeMode = false;
			classicMode = true;			
			mode = "Clasic";

		}
	}

	private void printSelectPieceFrequency(Graphics2D gt) {	
		clearScreen(gt);

		main.drawString(gt,"*Extreme Only", 10, 10);	
		int x = 170, y = 40;
		int x_temp = x;


		for (PieceFrequency p: list){
			main.drawString(gt, p.name, 50, y + 20);	
			for (int i = 0; i < p.freq; i++) {
				drawPieceForFreqSetting(gt, p.t.getPiecePosition(), p.t.getColor(), x_temp, y);
				x_temp += 80;
			}
			x_temp = x;
			y += 65;
		}

		movePFArrow(gt);

	}

	private void movePFArrow(Graphics2D gt) {
		int pad = 65;
		int[][] arrow = {{10, 30, 10},{65, 80, 95}};
		int[] temp = arrow[1];

		for (int i = 0; i < 3; i++) {
			temp[i] += p_freq_pointer * pad;
		}

		gt.setColor(Color.blue);
		gt.fillPolygon(arrow[0], temp, 3);

	}

	private void changeFrequency(int add) {
		int freq = list.get(p_freq_pointer).freq;
		if(freq + add > 5)
			freq = 5;
		else if (freq + add < 0)
			freq = 0;
		else{
			freq += add;
			freq_sum += add;
		}

		list.get(p_freq_pointer).freq = freq;
	}

	private void setFrequency() {
		pieceWeights = new int[freq_sum];
		int t_index = 0, rand_index = 0; 
		for (PieceFrequency p : list) {
			for (int i = 0; i < p.freq; i++) {
				pieceWeights[rand_index] = t_index;
				rand_index++;
			}
			t_index++;			
		}
	}



	private void drawPieceForFreqSetting(Graphics2D gt, int piece[][], Color color, int x, int y) {
		int PIECE_SETTING_SIZE= 15;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){
					gt.setColor(color);
					gt.fillRect(x + i *PIECE_SETTING_SIZE, y + j * PIECE_SETTING_SIZE, PIECE_SETTING_SIZE, PIECE_SETTING_SIZE);				

					gt.setColor(Color.black);
					gt.drawRect(x + i *PIECE_SETTING_SIZE, y + j * PIECE_SETTING_SIZE, PIECE_SETTING_SIZE, PIECE_SETTING_SIZE);				
				}
				//				else{
				//					gt.setColor(Color.black);
				//					gt.drawRect(x + i *PIECE_SETTING_SIZE, y + j * PIECE_SETTING_SIZE, PIECE_SETTING_SIZE, PIECE_SETTING_SIZE);				
				//				}

			}
		}

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
		case 9: c = Color.gray;break;
		case 10: c = Color.pink;break;

		default: c = Color.darkGray;
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

	private void clearScreen(Graphics2D gt) {
		gt.setColor(Color.white);
		gt.fillRect(0, 0, getWidth(), getHeight());
	}

	public void render(Graphics2D gt) {

		if(mainScreen){
			printMainScreen(gt);
			if (settingScreen) {
				printSelectPieceFrequency(gt);
			}
		}
		else{
			clearScreen(gt);

			gt.setColor(Color.black);

			
			renderGameField(gt);

			// Display next piece

			gt.drawRect(GRID_PADDING_X + PREVIEW_SIZE, GRID_PADDING_Y + PREVIEW_SIZE, PREVIEW_SIZE * PREVIEW_LENGTH, PREVIEW_SIZE * PREVIEW_LENGTH);				


			renderGhostPiece(gt, current_tetromino.getPiecePosition());

			renderNextPiece(gt, next_tetromino.getPiecePosition(), next_tetromino.getColor());
			renderPiece(gt, current_tetromino.getPiecePosition(), current_tetromino.getColor());


			//		gt.drawRect(x, y, COL_SIZE * CELL_SIZE, ROW_SIZE*CELL_SIZE);

			main.drawString(gt, score+"", 430, 250);

			if(extremeMode){
				drawStoreSlots(gt);
				renderStoredPieces(gt);
			}
			
		}
	}
	private int NumberOfBlocks(int[][] piece){
		int num=0;
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {			
				if(piece[i][j] == 1 || piece[i][j] == 2){
					num++;
				}
			}		
		}
		return num;

	}
	private void findGhostPosition(Graphics2D gt, int[][] piece) {

		int temp=0, xtemp = 0, ytemp = 0;
		
		ArrayList<Point> list;
		//int numberOfBlocks
		list = new ArrayList<>();
		
		
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				if(piece[i][j] == 1 || piece[i][j] == 2){

					xtemp  = xGhost + i - x_padding;
					ytemp = yGhost + j + 1 - y_padding;
					System.out.println(ytemp);
					
					if( isInsideGrid(i, j) && xtemp >= 0 && ytemp >= 0){
						//System.out.println("Ghost Field: " + xtemp + " " + ytemp + " Ghost: " + xGhost + " " + yGhost);
						//spaceBetween( piece);
						//blockBetween(piece);
						/*while(ytemp < 0)
							ytemp++;*/
							
						if(gameField[xtemp][ytemp] == 0){
							temp++;
						//	gt.fillRect((xtemp+x_padding)*width, (ytemp+1+y_padding)*width, width, width);
							list.add(new Point(xtemp+x_padding, ytemp+1+y_padding));
						}

					}
				}
			}
		}

		if(temp == NumberOfBlocks(piece)){
			findGhost = true;
			for (int a = 0; a < list.size(); a++) {
				gt.fillRect((list.get(a).x)*width, (list.get(a).y)*width, width, width);
				
			}
		}
		
		if(ytemp < 15){
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5 ; j++) {
					if(piece[i][j] == 1 || piece[i][j] == 2 && isInsideGrid(i, j) &&  xGhost + i - x_padding >= 0 && yGhost + j + 1 - y_padding >= 0){
						if(gameField[xtemp][ytemp++] > 0){
							//isBlockBetween = true;
							//if(specialY == ytemp){
								System.out.println("space");
							//}
						}
					}

				}
			}
		
	
		}

	}
	

	private void spaceBetween(int[][] piece){
		int ytemp = 0;
		int yLowest = 0;
		spaceGhost = 0;
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				if(piece[a][b] == 1 || piece[a][b] == 2){
					//	if(gameField[xGhost + a - x_padding][yGhost + b + 1 - y_padding] == 0){
					//int xtemp  = xGhost + i - x_padding;
					ytemp = yGhost + b + 1 - y_padding;
					if(ytemp > yLowest)
						yLowest = ytemp;
					//	System.out.println("yTemp: " + ytemp + " yLowest: " + yLowest);

					//	}

				}
			}
		}
		spaceGhost = 15 - yLowest;
		specialY = yLowest ;
		for(int y = yLowest; y<15 && !isBlockBetween; y++){
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5 ; j++) {
					if(piece[i][j] == 1 || piece[i][j] == 2){
						if(gameField[xGhost + i - x_padding][y] > 0){
							isBlockBetween = true;
							if(specialY == y){
								countBlock++;
								specialY++;
							}
						}
					}

				}
			}
			//System.out.println("y: " + y);
			/*if(isBlockBetween)
				spaceGhost = (15 - y );
*/
		}

	/*	if(spaceGhost == 0 && isBlockBetween){
			 spaceGhost = 1;
		}
		if(spaceGhost == 1 && isBlockBetween){
			
		}*/
		/*if(spaceGhost < 4 && spaceGhost > 2 && isBlockBetween){
			spaceGhost--;
		}*/

		//System.out.println(spaceGhost);

	}
	
	private void renderGhostPiece(Graphics2D gt,  int[][] piece){

		for (int j = 0; j < 15 && !findGhost; j++) {

			//if(){
			xGhost = x_coor;
			yGhost = j;
			//System.out.println("finding");
			findGhostPosition(gt,piece);
			//System.out.println("find");
			//}


		}

		/*for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {

			//	gt.setColor(Color.RED);
			//	gt.drawRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
			//if(isInsideGrid(a, b)){
					if(piece[a][b] == 1 || piece[a][b] == 2){
						//gt.setColor(Color.RED);
						//gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);

						gt.setColor(Color.LIGHT_GRAY);

						//if(spaceGhost == 0 && isBlockBetween){
							gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);

						}else if(spaceGhost == 1 && isBlockBetween){
							if(countBlock == 2){
								gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
							
							}else if (countBlock == 1){
								gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
								
							}
								
							

						}else if(spaceGhost == 1 && !isBlockBetween){
							gt.fillRect((a+xGhost)*width, (b + yGhost+1+spaceGhost)*width, width, width);
						
						}else if(spaceGhost == 2 && isBlockBetween) {{
							if(countBlock == 2){
								gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
							}else if(countBlock == 1){
								gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
							}
						}
							
							
						}else if(spaceGhost == 2 && !isBlockBetween) {
							gt.fillRect((a+xGhost)*width, (b + yGhost+1+spaceGhost)*width, width, width);
							
						}else{
							gt.fillRect((a+xGhost)*width, (b + yGhost+1)*width, width, width);
						}


					}
					gt.setColor(Color.BLACK);
					gt.fillRect((xGhost)*width, ( yGhost+1)*width, width, width);					
			//	}
			}
		}*/

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

					
					// if piece is inside field
					if(isInsideGrid(i, j, move)){
						// if check will hit an existing piece
						if(gameField[x_coor + i + move - x_padding][level + j + 1 - y_padding] >= 1){
							// then the move is not allowed
					
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


	private Tetromino newPieceExtreme() {
		Tetromino piece = new o_shape();

		int i = ((int)(Math.random() * pieceWeights.length)) ;
		int rand = pieceWeights[i];
		switch (rand) {
		case 0: piece = new o_shape(); break;
		case 1: piece = new i_shape(); break;
		case 2: piece = new l_left_shape();break;
		case 3: piece = new l_right_shape(); break;
		case 4: piece = new n_left_shape(); break;
		case 5: piece = new n_right_shape(); break;
		case 6: piece = new t_shape(); break;
		case 7: piece = new oo_shape(); break;
		case 8: piece = new p_shape(); break;
		}

		return piece;	
	}


	private void resetValuesForNewPiece() {
		// reinitialize values
		level = level_default;
		x_coor = x_coord_default;
		pieceLanded = false;
		findGhost = false;
		linesCleared = 0;
		isBlockBetween = false;

	}

	private void generateNewPiece() {
		current_tetromino = next_tetromino;
		if(classicMode)
			next_tetromino = newPiece(); 
		else //extreme mode
			next_tetromino = newPieceExtreme();

		resetValuesForNewPiece();
	}

	
	public void update(long l) {

		
		readInput();
		findGhost = false;
		linesCleared = 0;
		isBlockBetween = false;
		countBlock=0;
		specialY = 0;
		
		if(!pause && !over && !mainScreen){
			if(timer.action(l)){
			
				checkPieceCollision(current_tetromino.getPiecePosition());
				if(pieceLanded){

							
					// using tetromino class
					addPieceToGameField(current_tetromino.getPiecePosition(), current_tetromino.getColor_index());
					lineFull();
					generateNewPiece();
					

				}
				else{
					level++;
				}
			}

		}
	}

}
