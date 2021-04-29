import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;

	private final Point[][][] Tetraminos = {
			// I-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O-Piece
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S-Piece
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			// T-Piece
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z-Piece
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};
	
	private final Color[] tetraminoColors = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
	};
	
	private Point pieceOrigin;
	private boolean gameOver = false;
	private boolean gamePaused = false;
	private int currentPiece;
	private int nextPiece;
	private int rotation;
	private int level = 0;
	private int difficulty = 5;
	private int heldPiece = -1;
    private ArrayList<Integer> pieces = new ArrayList<Integer>(){{
        add(0);
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
    }};
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

	private int linesCleared;
	private Color[][] well;
	
	// Creates a border around the well and initializes the dropping piece
	private void init() {
		well = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || i == 11 || j == 22) {
					well[i][j] = Color.GRAY;
				} else {
					well[i][j] = Color.BLACK;
				}
			}
		}
		newPiece();
	}
	
	// Put a new, random piece into the dropping position
	public void newPiece() {
		pieceOrigin = new Point(4, 0);
		rotation = 0;
		if (nextPieces.size() < 6) {
            Collections.shuffle(pieces);
			Collections.addAll(nextPieces, pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3), pieces.get(4));
		}
		currentPiece = nextPiece;
		nextPiece = nextPieces.get(0);
		nextPieces.remove(0);
		
		
//		if (well[6][4] != tetraminoColors[currentPiece]) {
//			this.gameOver = true;
//		}
	 }
	
	// Collision test for the dropping piece
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	
	// Rotate the piece clockwise or counterclockwise
	public void rotate(int i) {
	    if (!this.gamePaused && !this.gameOver) {
    		int newRotation = (rotation + i) % 4;
    		if (newRotation < 0) {
    			newRotation = 3;
    		}
    		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
    			rotation = newRotation;
    		}
    		repaint();
	    }
	}
	
	// Move the piece left or right
	public void move(int i) {
        if (!this.gamePaused && !this.gameOver) {
    		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
    			pieceOrigin.x += i;	
    		}
    		repaint();
        }
	}
	
	// Drops the piece one line or fixes it to the well if it can't drop
	public void dropDown() {
		this.level = this.linesCleared / this.difficulty;
		if (!this.gamePaused && !this.gameOver) {
			if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
				pieceOrigin.y += 1;
			} else {
				fixToWell();
			}	
		}
		repaint();
	}
	
	// Make the dropping piece part of the well, so it is available for
	// collision detection.
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}
	
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				well[i][j+1] = well[i][j];
			}
		}
	}
	
	// Clear completed rows from the field and award score according to
	// the number of simultaneously cleared rows.
	public void clearRows() {
		boolean gap;
		int numClears = 0;
		
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (well[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				numClears += 1;
			}
		}
		
		linesCleared += numClears;
	}
	
	//checks for the theoretical y position of the gray Tetramino
	public int checkTheoreticalPos(){
            ArrayList<Integer> theor = new ArrayList<>();
            for (Point p : Tetraminos[currentPiece][rotation]) {
                int theorVal = 0;
                for(int j = p.y + pieceOrigin.y + 1; j < 22; j++){
                    if(well[p.x + pieceOrigin.x][j]==Color.BLACK){
                        theorVal++;
                    }else break;
                }
                theor.add(theorVal);
            }
            return Collections.min(theor) + pieceOrigin.y;
        }

	// Draw the falling piece
    private void drawPiece(Graphics g) {
    	
		//paints the theoretical gray Tetramino
        g.setColor(Color.GRAY);
		for (Point p : Tetraminos[currentPiece][rotation]) {
                        g.fillRect((p.x + pieceOrigin.x) * 26, 
					   (p.y + checkTheoreticalPos()) * 26, 
					   25, 25);
		}
        
		// Paints the current tetramino
        g.setColor(tetraminoColors[ currentPiece]);
        for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26, 
					   (p.y + pieceOrigin.y) * 26, 
					   25, 25);
		}
        
        // Paints the next tetramino
        g.setColor(tetraminoColors[this.nextPiece]);
        for (Point p : Tetraminos[this.nextPiece][0]) {
        	g.fillRect((p.x + 3) * 13, (p.y + 1) * 13, 13, 13);
        }
        
        // Paints the held tetramino
        if (this.heldPiece != -1) {
            g.setColor(tetraminoColors[this.heldPiece]);
            for (Point p : Tetraminos[this.heldPiece][0]) {
                g.fillRect((p.x + 3) * 13, (p.y + 3) * 13, 13, 13);
            }
        }

	}	
    
    // Drop the piece to the bottom
    private void dropPiece() {
        if (!this.gamePaused && !this.gameOver) {
            if (!collidesAt(pieceOrigin.x, checkTheoreticalPos(), rotation)) {
                pieceOrigin.y = checkTheoreticalPos();
                fixToWell();
                repaint();
            }
        }        
    }
    
    // Replace current piece with held piece if one exists
    private void holdPiece() {
        if (this.heldPiece == -1) {
            this.heldPiece = this.currentPiece;
            newPiece();
        } else {
            this.nextPieces.set(0,  nextPiece);
            this.nextPiece = this.heldPiece;
            this.heldPiece = this.currentPiece;
            newPiece();
        }
    }
    
	@Override 
	public void paintComponent(Graphics g)
	{
		// Paint the well
		g.fillRect(0, 0, 26*12, 26*23);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(well[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		
		// Display the score
		g.setColor(Color.WHITE);
		g.drawString("Lines Cleared: " + linesCleared, 15*12, 25);
		if (this.gamePaused) {
			g.drawString("PAUSED", 8*12, 25);
		}
		
		// Say Game Over
		if (this.gameOver) {
			g.clearRect(26*4,  26*6, 26*4-1,  26*2-1);
			g.fillRect(26*4,  26*6, 26*4-1,  26*2-1);
			g.setColor(Color.BLACK);
			g.drawString("Game Over", 26*4+20, 26*6+30);
		}
		
		// Draw the currently falling piece
		drawPiece(g);
	}

	public static void main(String[] args) {	
		final Tetris game = new Tetris();
		game.init();	
		
		/*
		Scanner console = new Scanner(System.in);
		System.out.println("TETRIS");
		System.out.println("Controls:");
		System.out.println("Use the arrow keys to control movement, \nuse the space key to make it fall faster,\nhit Escape to pause or unpause,\nand hit Q to terminate the game!");
		System.out.println("How many lines to clear a level? \n(Defaults to 10, the higher the easier, \nor use \"cancel\" to cancel the operation!");
		while(true) {
			String consoleInput = console.nextLine();
			if (consoleInput.equals("")) {
				game.difficulty = 10;
				System.out.println("Continuing.");
				break;
			} else if (consoleInput.equalsIgnoreCase("cancel")) { 
				console.close();
				java.lang.System.exit(0);				
			}else {
				int diff;
				try {
					diff = Integer.parseInt(consoleInput);
				} catch(NumberFormatException e) {
					System.out.println("Invalid number: " + consoleInput);
					continue;
				}
				game.difficulty = diff;
				System.out.println("Continuing.");
				break;
			}
		}	
		console.close();
		*/
		
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(12*26+10, 26*23+25);
		f.setVisible(true);
		f.add(game);

		
		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					game.rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.dropDown();
					break;
				case KeyEvent.VK_ESCAPE:
				    game.gamePaused = !game.gamePaused;
				    break;
				case KeyEvent.VK_Q:
					java.lang.System.exit(0);
					break;
				case KeyEvent.VK_C:
				    game.dropPiece();
				    break;
				case KeyEvent.VK_S:
				    game.holdPiece();
				    break;
				} 
				
				
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// Make the falling piece drop every second
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
	                        Thread.sleep(1000/(1 + game.level));
	                        game.dropDown();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}