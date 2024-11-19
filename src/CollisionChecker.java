import acm.graphics.GObject;
import acm.graphics.GOval;


public class CollisionChecker {

	private Breakout main;
	
	public CollisionChecker(Breakout main)
	{
		this.main = main;
	}
	
	/**
	 * Перевірка на всі можливі колізії м'яча з об'єктами, стінами або якщо м'яч застряг.
	 * 
	 * @param ball М'яч, який перевіряється на колізії
	 * @return Об'єкт, з яким сталося зіткнення, або null, якщо зіткнення немає
	 */
	public GObject check(GOval ball) {
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
		GObject object = main.getElementAt(x + ball.getHeight() / 2, y + ball.getHeight() / 2);
		ball.setLocation(x, y);
    
		if (object != null) {            
			main.speedY = -Math.abs(main.speedY);
			main.speedX = -main.speedX;
			ball.setLocation(x + main.speedX, ball.getHeight() / 2 - 130 - ball.getHeight());
			return true;
		}
    
		return false;
	}

	/**
	 * Перевірка на зіткнення м'яча зі стінами і зміна напрямку руху.
	 * || ball.getY() + 2 * BALL_RADIUS >= HEIGHT - 75
	 * @param ball М'яч, який перевіряється
	 */
	private void checkWallCollision(GOval ball) {
		if (ball.getY() <= 0 )
			main.speedY = -main.speedY;
    
		if (ball.getX() <= 0 || ball.getX() + 2 * ball.getHeight() / 2 >= main.WIDTH)
			main.speedX = -main.speedX;
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
		double diameter = ball.getHeight();
    
		GObject object;

    
		// Перевірка верхньої і нижньої сторони
		for (double i = x; i < x + diameter; i++) {
			object = main.getElementAt(i, y - 1);
			if (object != null) {
				main.speedY = -main.speedY;
				return object;
			}

			object = main.getElementAt(i, y + diameter + 1);
			if (object != null) {
				main.speedY = -main.speedY;
				return object;
			}
		}

		// Перевірка лівої і правої сторони
		for (double i = y; i < y + diameter; i++) {
			object = main.getElementAt(x - 1, i);
			if (object != null) {
				main.speedX = -main.speedX;
				return object;
			}

			object = main.getElementAt(x + diameter + 1, i);
			if (object != null) {
				main.speedX = -main.speedX;
				return object;
			}
		}

		return null;
	}
}
