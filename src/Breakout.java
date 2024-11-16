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

import static java.lang.Math.*;

public class Breakout extends GraphicsProgram {
	
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
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
	
	//TODO Зробити головне меню для рестарту гри / Безух

	/**Об'єкт платформи, якою буде керувати ігрок*/
	private GRect platform;

	/**
	 * Основний метод для запуску програми Breakout.
	 */
	public void run() {
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		addMouseListeners();
		startGame();
	}

	/**
	 * Реалізація події mouseMoved для управління платформою.
	 * Платформа переміщується разом із курсором миші.
	 * 
	 * @param e Об'єкт MouseEvent, який містить інформацію про рух миші
	 */
	public void mouseMoved(MouseEvent e) {
		if (platform != null)
			platform.setLocation(e.getX() - 30, platform.getY());
	}

	/**
	 * Метод для запуску гри Breakout.
	 * Ініціалізує об'єкти гри, такі як платформа і м'яч, і починає рух м'яча.
	 */
	public void startGame() {
		createPlatform();
		// TODO написати метод для створення світу / Анкудович
		ballMove();
	}

	/**
	 * Створення платформи для гри. Платформа розташовується по центру внизу екрана.
	 */
	private void createPlatform() {
		platform = new GRect(WIDTH / 2 - 30, HEIGHT - 125, 60, 5);
		platform.setFilled(true);
		add(platform);
	}
	
	/**X складова швидкості*/
	private double speedX;
	/**Y складова швидкості*/
	private double speedY;
	
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
		
		speedX = speed * sin(angle);
		speedY = speed * cos(angle);
    
		while (true) {
			ball.move(speedX, speedY);
			checkCollision(ball);

			// TODO реалізувати ускладнення (прискорення) / ?
			// TODO звуки
			// TODO реалізувати бали (scoreboard) + життя / ?
			// TODO реалізувати програш / Анкудович
			// TODO реалізувати видалення блоку / Анкудович
			// TODO реалізувати перевірку на досягнення кінця гри / Анкудович

			pause(10);
		}
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
	 * Перевірка на всі можливі колізії м'яча з об'єктами, стінами або якщо м'яч застряг.
	 * 
	 * @param ball М'яч, який перевіряється на колізії
	 * @return Об'єкт, з яким сталося зіткнення, або null, якщо зіткнення немає
	 */
	private GObject checkCollision(GOval ball) {
		if (isBallStuck(ball))
			return null;
    
		GObject obj = checkObjectCollision(ball);
		if (obj != null)
			return obj;

		checkWallCollision(ball);
		return null;
	}

	/**
	 * Перевіряє, чи м'яч застряг в об'єкті.
	 * Якщо застряг, змінює напрямок руху і "витягує" м'яч.
	 * 
	 * @param ball М'яч, який перевіряється
	 * @return true, якщо м'яч застряг; false в іншому випадку
	 */
	private boolean isBallStuck(GOval ball) {
		double x = ball.getX(), y = ball.getY();
		ball.setLocation(-100, -100);
		GObject object = this.getElementAt(x + BALL_RADIUS, y + BALL_RADIUS);
		ball.setLocation(x, y);
    
		if (object != null) {            
			speedY = -Math.abs(speedY);
			speedX = -speedX;
			ball.setLocation(x + speedX, HEIGHT - 130 - 2 * BALL_RADIUS);
			return true;
		}
    
		return false;
	}

	/**
	 * Перевірка на зіткнення м'яча зі стінами і зміна напрямку руху.
	 * 
	 * @param ball М'яч, який перевіряється
	 */
	private void checkWallCollision(GOval ball) {
		if (ball.getY() <= 0 || ball.getY() + 2 * BALL_RADIUS >= HEIGHT - 75)
			speedY = -speedY;
    
		if (ball.getX() <= 0 || ball.getX() + 2 * BALL_RADIUS >= WIDTH - 20)
			speedX = -speedX;
	}

	/**
	 * Перевіряє, чи м'яч зіткнувся з об'єктом і змінює напрямок руху.
	 * 
	 * @param ball М'яч, який перевіряється
	 * @return Об'єкт, з яким сталося зіткнення, або null, якщо зіткнення немає
	 */
	private GObject checkObjectCollision(GOval ball) {    
		double x = ball.getX();
		double y = ball.getY();
		double diameter = 2 * BALL_RADIUS;
    
		GObject object;
    
		// Перевірка верхньої і нижньої сторони
		for (double i = x; i < x + diameter; i++) {
			object = getElementAt(i, y - 1);
			if (object != null) {
				speedY = -speedY;
				return object;
			}

			object = getElementAt(i, y + diameter + 1);
			if (object != null) {
				speedY = -speedY;
				return object;
			}
		}

		// Перевірка лівої і правої сторони
		for (double i = y; i < y + diameter; i++) {
			object = getElementAt(x - 1, i);
			if (object != null) {
				speedX = -speedX;
				return object;
			}

			object = getElementAt(x + diameter + 1, i);
			if (object != null) {
				speedX = -speedX;
				return object;
			}
		}

		return null;
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
}
