import java.awt.Color;


public class Tetromino {

	Color color;
	int initialPos;
	int[][] piecePosition;
	int color_index;

	public Tetromino(Color color, int initialPos, int[][] piecePosition, int color_index){
		this.color = color;
		this.initialPos = initialPos;
		this.piecePosition = piecePosition;
		this.color_index = color_index;
	}

		public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getInitialPos() {
		return initialPos;
	}
	public void setInitialPos(int initialPos) {
		this.initialPos = initialPos;
	}
	public int[][] getPiecePosition() {
		return piecePosition;
	}
	public void setPiecePosition(int[][] piecePosition) {
		this.piecePosition = piecePosition;
	}

	public int getColor_index() {
		return color_index;
	}

	public void setColor_index(int color_index) {
		this.color_index = color_index;
	}
		


}	
class o_shape extends Tetromino{

	static int[][] o =  
		{{0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}};
		
	public o_shape(){
		super(Color.yellow, 5, o, 0);		
	}
	
}

class i_shape extends Tetromino{

	static int[][] i =   
		   {{0, 0, 1, 0, 0},
		    {0, 0, 1, 0, 0},
		    {0, 0, 2, 0, 0},
		    {0, 0, 1, 0, 0},
		    {0, 0, 0, 0, 0}};
		
	public i_shape(){
		super(Color.cyan, 5, i, 1);		
	}
	
}

class l_left_shape extends Tetromino{

	static int[][] l_left =  {
		{0, 0, 0, 0, 0},
		{0, 1, 1, 0, 0},
		{0, 0, 2, 0, 0},
		{0, 0, 1, 0, 0},
		{0, 0, 0, 0, 0}};

	public l_left_shape(){
		super(Color.orange, 5, l_left, 2);		
	}
	
}

class l_right_shape extends Tetromino{

	static int[][] l_right = {
		{0, 0, 0, 0, 0},
		{0, 0, 1, 1, 0},
		{0, 0, 2, 0, 0},
		{0, 0, 1, 0, 0},
		{0, 0, 0, 0, 0}};

	public l_right_shape(){
		super(Color.blue, 5, l_right, 3);		
	}
	
}

class n_left_shape extends Tetromino{

	static int[][] n_left = {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}};

	public n_left_shape(){
		super(new Color(220, 20, 60), 5, n_left, 4);		
	}
	
}

class n_right_shape extends Tetromino{

	static	int[][] n_right = {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 0, 0, 0}};

	public n_right_shape(){
		super(Color.green, 5, n_right, 5);		
	}
	
}

class t_shape extends Tetromino{

	static	int[][] t =
		{{0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}};
	
	public t_shape(){
		super(new Color(158, 0, 240), 5, t, 6);		
	}
	
}

class oo_shape extends Tetromino{

	static	int[][] oo =
		{{0, 0, 0, 0, 0},
	    {0, 1, 1, 1, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}};
	
	public oo_shape(){
		super(Color.gray, 5, oo, 7);		
	}
	
}

class p_shape extends Tetromino{

	static	int[][] p =
		{{0, 0, 1, 1, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}};
	
	public p_shape(){
		super(Color.pink, 5, p, 8);		
	}
	
}
	