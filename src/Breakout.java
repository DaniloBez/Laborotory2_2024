/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.Clip;

import static java.lang.Math.*;

public class Breakout extends GraphicsProgram {
	
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	public static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	/*AudioClip clip = MediaTools.loadAudioClip("sound\\hitting.wav");
	clip.play(); - Непрацює*/
	
	AudioPlayer ballPlayer = new AudioPlayer();

	private UI menu;
	private CollisionChecker collisionChecker;
	
	public boolean isGameStarted = false;
	private GRect platform;
	
	public double speedX;
	public double speedY;

	private double delay = 10;
	
	
	/**
	 * Основний метод для запуску програми Breakout.
	 */
	public void run() {
		ballPlayer.loadAudio("src\\sound\\ball.wav");
		this.setSize(APPLICATION_WIDTH + 20, APPLICATION_HEIGHT);
		addMouseListeners();
		
		menu = new UI(this);
		menu.startMenu();
		
		collisionChecker = new CollisionChecker(this);
		
		while(true) {
			if(isGameStarted)
				startGame();
		}
	}

	/**
	 * Реалізація події mouseMoved для управління платформою.
	 * Платформа переміщується разом із курсором миші.
	 * 
	 * @param e Об'єкт MouseEvent, який містить інформацію про рух миші
	 */
	public void mouseMoved(MouseEvent e) {
		if (isGameStarted && platform != null)
			platform.setLocation(e.getX() - 30, platform.getY());
	}
	
	/**
	 * Реалізація події mouseClicked для початку гри.
	 * Реалізація початку гри винесена в клас UI
	 * 
	 * @param e Об'єкт MouseEvent, який містить інформацію про рух миші
	 */
	public void mouseClicked(MouseEvent e)
	{
		if(menu != null)
			menu.mouseClickedHandler(e);
	}
	
	

	/**
	 * Метод для запуску гри Breakout.
	 * Ініціалізує об'єкти гри, такі як платформа і м'яч, і починає рух м'яча.
	 */
	private void startGame() {
		removeAll();
		
		isGameStarted = true;
		
		setUpWorld();
		ballMove();
	}
	
	/**
	 * Метод для закінчення гри.
	 * @param score Кількість збитих блоків
	 * @param isWin Чи переміг користувач
	 */
	private void stopGame(int score, boolean isWin)
	{
		removeAll();
		isGameStarted = false;
		
		menu.restartMenu(score, isWin);
	}	
	
	
	
	/**
	 * Ініціалізує ігровий світ, створюючи основні об'єкти.
	 * Метод відповідає за створення платформи для гри та стіни цеглин,
	 * Викликається перед початком гри для підготовки середовища.
	 */
	private void setUpWorld() {
		createPlatform();
		createBrickWall();

	}

	/**
	 * Створення платформи для гри. Платформа розташовується по центру внизу екрана.
	 */
	private void createPlatform() {
		platform = new GRect(WIDTH / 2 - 30, HEIGHT - 125, 60, 5);
		platform.setFilled(true);
		add(platform);
	}

	/**
	 * Створює стіну з цеглин для ігрового процесу.
	 * Метод генерує ряди цеглин із заданими розмірами, кольорами та позиціями.
	 * Цеглини розташовуються у вигляді прямокутної сітки з відповідними відступами
	 * та кольорами для кожної пари рядів. Перший рядок враховує спеціальний відступ.
	 */
	private void createBrickWall() {
		for(int i = 0;i< NBRICK_ROWS; ++i) {
			for(int j =0;j<NBRICKS_PER_ROW;++j) {
				GRect brick = new GRect(BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				
				brick.setColor(getBrickColor(j));	
				
				//дадаємо bricks. Перший brick кожного ряду робимо з відступом BRICK_SEP/2
				if(i == 0)
					add(brick, BRICK_SEP/2+BRICK_WIDTH*i,BRICK_Y_OFFSET+(j+1)*BRICK_SEP+BRICK_HEIGHT*j);
				else
					add(brick, BRICK_SEP/2+i*BRICK_SEP+BRICK_WIDTH*i,BRICK_Y_OFFSET+(j+1)*BRICK_SEP+BRICK_HEIGHT*j);
			}
		}
		
	}
	
	/**
	 * Повертає колів в залежності від індексу
	 * @param index Номер рядка
	 * @return Колір
	 */
	private Color getBrickColor(int index)
	{
		Color color = null;
		
		switch(index) {
			case 0: case 1: 
				color = Color.decode("#ee6700"); 
				break;
			case 2: case 3: 
				color = Color.decode("#f6f60f");
				break;
			case 4: case 5: 
				color = Color.decode("#70c413");
				break;
			case 6: case 7: 
				color = Color.decode("#2b9ad6");
				break;
			case 8:case 9: 
				color = Color.decode("#ab3fd4");
				break;	
		}
		
		return color;
	}

	/**
	 * Створює м'яч для гри з початковим розташуванням по центру екрана.
	 * 
	 * @return Об'єкт GOval, який представляє м'яч
	 */
	private GOval createBall() {
		double x = WIDTH / 2, y = HEIGHT / 2 - BALL_RADIUS;
		GOval ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		ball.setFillColor(Color.black);
		add(ball);
		return ball;
	}

	

	/**
	 * Знаходить випадковий кут для початкового руху м'яча.
	 * Кут визначає напрямок руху м'яча при старті гри.
	 * 
	 * @return Кут у радіанах
	 */
	private double getRandomAngle() {
		RandomGenerator random = RandomGenerator.getInstance();
		double angle;
		if (random.nextBoolean())
			angle = 2 + random.nextInt(45);
		else 
			angle = -2 - random.nextInt(45);
    
		return toRadians(angle);
	}
	

	
	/**
	 * Основний метод для керування рухом м'яча.
	 * М'яч рухається постійно, змінюючи напрямок при зіткненні зі стінами або іншими об'єктами.
	 * 
	 * Кожна пауза в циклі відповідає одному кадру анімації.
	 */
	private void ballMove() {
		GOval ball = createBall();
	    float speed = 5f;
	    double angle = getRandomAngle();
	    
	    GLabel previousScore = menu.setScoreBoard(0, null);
	    GLabel previousLifes = menu.setLifeCounter(3, null);

	    int currentScore = 0;
	    int maxScore = NBRICK_ROWS * NBRICKS_PER_ROW;
	    int totalLifes = NTURNS;

	    speedX = speed * sin(angle);
	    speedY = speed * cos(angle);
	    
		pause(800);
	    while (true) {
	    	
	        // Перевірка умов завершення гри
	        if (totalLifes == 0 || currentScore == maxScore) {
	            removeAll();
	            if (totalLifes == 0) {
	            	stopGame(currentScore, false);
	            }

	            else if (currentScore == maxScore) {
	            	stopGame(currentScore, true);
	            }
            
	            break;
	        }
	        
	     // Перевірка падіння м'яча за нижній край
	        if (ball.getY() + BALL_RADIUS >= HEIGHT) {
	        	ballPlayer.playAudio();
	        	delay = 10;
	            totalLifes--;
	            previousLifes = menu.setLifeCounter(totalLifes, previousLifes);
	            if (totalLifes > 0) {
	                angle = getRandomAngle();
	                remove(ball);
	                pause(200);
	                ball = createBall();
	                pause(800);
	                speedX = speed * sin(angle);
	                speedY = speed * cos(angle);
	            }
	        }
	  
	        // Перевірка зіткнення з цеглою
	        GObject getBrick = collisionChecker.check(ball);
	        if (getBrick != null && getBrick != platform && !(getBrick instanceof GLine)) {
	        	remove(getBrick);
	            currentScore++;
	            previousScore = menu.setScoreBoard(currentScore, previousScore);
	            pause(delay);
	            delay -= 0.02;
	        }
	        
	        ball.move(speedX, speedY);
	        pause(delay);
	    }
	}

}