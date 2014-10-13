import java.awt.image.BufferedImage;


public class tPiece extends tetrisPiece{
/*	public int[][] normalShape;
	public int[][] secondShape;
	public int[][] thirdShape;
	public int[][] forthShape;
	public int rotation = 1;
	*/
	
	
	public tPiece(){
		normalShape = new int [][] {{0,1,0,0},
									{1,1,1,0},
									{0,0,0,0},
									{0,0,0,0}};
		
		secondShape = new int [][] {{1,0,0,0},
									{1,1,0,0},
									{1,0,0,0},
									{0,0,0,0}};
		
		thirdShape = new int [][]  {{1,1,1,0},
									{0,1,0,0},
									{0,0,0,0},
									{0,0,0,0}};
		
		forthShape = new int [][]  {{0,1,0,0},
									{1,1,0,0},
									{0,1,0,0},
									{0,0,0,0}};
		rotation = 1;
		topleftX = 10;
		topleftY = 10;
		
	}
	
	@Override
	public int[][] nextRotation(int currentRotation) {
		int [][] nextShape = null;
		
		if(currentRotation == 1){
			nextShape = secondShape;
			rotation = 2;
		} else if(currentRotation == 2){
			nextShape = thirdShape;
			rotation = 3;
		}else if(currentRotation == 3){
			nextShape = forthShape;
			rotation = 4;
		}else if(currentRotation == 4){
			nextShape = normalShape;
			rotation = 1;
		}
		return nextShape;
		
	}

}
