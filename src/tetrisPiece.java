import com.golden.gamedev.object.Sprite;


public abstract class tetrisPiece {
	public int[][] normalShape;
	public int[][] secondShape;
	public int[][] thirdShape;
	public int[][] forthShape;
	public int rotation = 1;
	
	public int topleftX;
	public int topleftY;
	
	public abstract int[][] nextRotation(int currentRotation);
	
	
}
