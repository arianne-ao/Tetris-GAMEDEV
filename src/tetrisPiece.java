import com.golden.gamedev.object.Sprite;


public class tetrisPiece {
	public int[][] normalShape;
	/*public int[][] secondShape;
	public int[][] thirdShape;
	public int[][] forthShape;
	public int rotation = 1;

	public int topleftX;
	public int topleftY;*/
	
	/*public tetrisPiece(){
		
	}*/
	
	public tetrisPiece(int[][] shape){
		
		normalShape = shape;
	}

	




	public int[][] Rotation(int[][] currentRotation){
		/* Reference 
		http://gamedev.stackexchange.com/questions/54299/tetris-rotations-using-linear-algebra-rotation-matrices
		 */	
		int [][] nextShape = new int [4][4];
		int N = currentRotation.length;
		for(int x = 0; x < N; x++){
			for(int y = 0; y < N; y++ ){
				nextShape[y][N-x-1] = currentRotation[x][y];

			}

		}
		return nextShape;
		

	}
	
	//public abstract int[][] nextRotation(int currentRotation);
}
