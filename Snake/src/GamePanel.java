import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, MouseListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int peachesEaten;
	int peachX;
	int peachY;
	char direction = 'R';
	boolean running = false;
	boolean gameOver = false;
	Timer timer;
	Random random;
	Button startButton;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.white);
		this.setBackground(new Color(200, 200, 200));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.setLayout(null);
		startButton = new Button("Start Game");
		startButton.setBounds((SCREEN_WIDTH - 100)/2, (SCREEN_HEIGHT -100)/2, 100, 50);
		startButton.addMouseListener(this);
		this.add(startButton);
	}
	
	public void startGame() {
		newApple();
		remove(startButton);
		repaint();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			//Draw the white dividing lines
			g.setColor(Color.black);
			for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			//draws the peach
			Image img = new ImageIcon(this.getClass().getResource("/peach.png")).getImage();
			g.drawImage(img, peachX, peachY, UNIT_SIZE, UNIT_SIZE, null, null);
		
			
			//draws the snake
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45, 180, 0)); //light green
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} 
			}
			
			//draws the score in the lower right
			g.setColor(new Color(0, 149, 213));//light blue
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("" + peachesEaten, (SCREEN_WIDTH - metrics.stringWidth(""+ peachesEaten)), SCREEN_HEIGHT);
		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		peachX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		peachY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;	
	}
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;	
		}
	}
	
	public void checkApple() {
		if((x[0] ==peachX) && (y[0] ==peachY)) {
			bodyParts++;
			peachesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
				gameOver = true;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
			gameOver = true;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
			gameOver = true;
		}
		//Check if head touches top border
		if(y[0] < 0) {
			running = false;
			gameOver = true;
		}
		//Check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
			gameOver = true;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		if(gameOver) {
		//draw game over
		g.setColor(new Color(0, 149, 213));//light blue
		g.setFont(new Font("Helvetica", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//draw score
		g.setFont(new Font("Helvetica", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + peachesEaten, SCREEN_WIDTH/2 - metrics2.stringWidth("Score:" + peachesEaten)/2, 
				g.getFont().getSize() + (int)(SCREEN_HEIGHT*0.05));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction !='R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		startGame();
	}
}
