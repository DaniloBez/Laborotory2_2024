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

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		
		addMouseListeners();

		
		SetUpPlatform();
		
		ballMove();
	}
	
	// TODO написати метод для створення світу / Анкудович
	
	// TODO Додавати javadoc
	// TODO поменьше використовувати глобалні змінні, краще передавати значення у методах.
	// Єдині глобальні змінні: платформа?
	private GRect platform; 
	
	
	public void mouseMoved(MouseEvent e)
	{
		platform.setLocation(e.getX() - 30, platform.getY());
	}
	
	private void SetUpPlatform()
	{
		platform = new GRect(WIDTH / 2 - 30, HEIGHT - 125, 60, 5);
		platform.setFilled(true);
		
		add(platform);
	}
	
    private double speedX;
    private double speedY;
	
	private void ballMove()
	{
		double x = WIDTH / 2, y = HEIGHT / 2 - BALL_RADIUS;
		float speed = 5f;
		
		GOval ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		ball.setFillColor(Color.black);
		add(ball);
		
		double angle = getRandomAngle();		
	    
		speedX = speed * sin(angle);
		speedY = speed * cos(angle);
	    
	    while(true) {
	        ball.move(speedX, speedY);
	        
	        checkCollision(ball);
	        
	        //TODO реалізувати ускладнення (прискорення) / ?
	        //TODO звуки
	        
	        //TODO реалізувати бали (scoreboard) + життя / ?
	        
	        //TODO реалізувати програш / Анкудович
	        //TODO реалізувати видалення блоку / Анкудович
	        //TODO реалізувати перевірку на досягнення кінця гри (є змінна кількості блоків, при видаленні блоку --) / Анкудович
	        
	        pause(10);
	    }    	
	}
	
	private void checkCollision(GOval ball)//TODO випривати колізію / Безух
	{
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= WIDTH - 20)
        	speedX = -speedX;
        

        if (ball.getY() <= 0 || ball.getY() + ball.getHeight() >= HEIGHT - 70) 
        	speedY = -speedY;
        
        if(ball.getBounds().intersects(platform.getBounds()))
        	speedY = -speedY;
	}

	private double getRandomAngle()
	{
		RandomGenerator random = RandomGenerator.getInstance();
	    
		double angle;
	    if(random.nextBoolean())
	        angle = 2 + random.nextInt(45);
	    else 
	        angle = -2 - random.nextInt(45);
	    
	    return toRadians(angle);
	}
}
