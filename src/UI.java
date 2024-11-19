import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GRect;
import acm.graphics.GCompound;

public class UI {
	private Breakout main;
	private double width, height;
	private GRect startButton;
	
	public UI(Breakout main) {
		this.main = main;
		width = main.getWidth();
		height = main.getHeight();
	}
	
	/**
	 * Реалізація початку гри.
	 * @param e Об'єкт MouseEvent, який містить інформацію про рух миші
	 */
	public void mouseClickedHandler(MouseEvent e)
	{
		if(!main.isGameStarted && startButton != null) {
			main.isGameStarted = true;
		}
	}
	
	/**
	 * Метод для малювання початкового екрану
	 */
    public void startMenu() {
    	GCompound menu = new GCompound();

        // Створення тексту заголовка
        GLabel title = new GLabel("Лабораторна робота №2, гра Breakout");
        title.setFont("SansSerif-bold-18");
        double titleX = (width - title.getWidth()) / 2;
        double titleY = height / 4;
        title.setLocation(titleX, titleY);
        menu.add(title);

        // Створення напису для кнопки
        GLabel startLabel = new GLabel("Розпочати гру");
        startLabel.setFont("SansSerif-16");
        
        double buttonX = (width - 150) / 2;
        double buttonY = height / 2;
        
    	startButton = new GRect(150, 50);
        startButton.setFilled(true);
        startButton.setFillColor(Color.LIGHT_GRAY);        
        startButton.setLocation(buttonX, buttonY);
        menu.add(startButton);
        
        double labelX = buttonX + (150 - startLabel.getWidth()) / 2;
        double labelY = buttonY + (50 + startLabel.getAscent()) / 2;       
        
        startLabel.setLocation(labelX, labelY);
        menu.add(startLabel);

        // Додавання авторства внизу праворуч
        GLabel authors = new GLabel("Гру створили\nБезух Данило та Анкудович Григорій");
        authors.setFont("SansSerif-12");
        double authorsX = width - authors.getWidth() - 10;
        double authorsY = height - 20;
        authors.setLocation(authorsX, authorsY);
        menu.add(authors);

        main.add(menu);
    }

    /**
     * Метод для малювання екрану після закінчення гри
     * @param score Кількість збитих блоків
     * @param isWin Чи переміг гравець
     */
    public void restartMenu(int score, boolean isWin) {

    	GCompound menu = new GCompound();

        // Створення заголовка залежно від результату гри
        String resultText = isWin ? "Ви виграли!" : "Ви програли!";
        GLabel resultLabel = new GLabel(resultText + " Ваші бали: " + score);
        resultLabel.setFont("SansSerif-bold-18");
        double resultX = (width - resultLabel.getWidth()) / 2;
        double resultY = height / 4;
        resultLabel.setLocation(resultX, resultY);
        menu.add(resultLabel);


        // Напис для кнопки перезапуску
        GLabel startLabel = new GLabel("Грати ще раз");
        startLabel.setFont("SansSerif-16");
        
        double buttonX = (width - 150) / 2;
        double buttonY = height / 2;
        
    	startButton = new GRect(150, 50);
        startButton.setFilled(true);
        startButton.setFillColor(Color.LIGHT_GRAY);        
        startButton.setLocation(buttonX, buttonY);
        menu.add(startButton);
        
        double labelX = buttonX + (150 - startLabel.getWidth()) / 2;
        double labelY = buttonY + (50 + startLabel.getAscent()) / 2;   
       
        startLabel.setLocation(labelX, labelY);
        menu.add(startLabel);

        main.add(menu);
    }
    

	/**
	 * Оновлює відображення рахунку на екрані. Видаляє попередній напис рахунку, якщо він існує.
	 *
	 * @param currentScore   Поточний рахунок, який потрібно відобразити.
	 * @param previousScore  Об'єкт GLabel, що представляє попередній рахунок, або null, якщо його немає.
	 * @return Новий об'єкт GLabel, який представляє оновлений рахунок.
	 */
	public GLabel setScoreBoard(int currentScore, GLabel previousScore) {
	    if (previousScore != null) {
	        main.remove(previousScore);
	    }
	    GLabel score = new GLabel("Рахунок: " + currentScore);
	    score.setFont("Arial-bold-18");
	    double scoreX = (width - score.getWidth()) / 2;
	    double scoreY = score.getAscent();
	    score.setLocation(scoreX, scoreY);

	    main.add(score);
	    double lineY = scoreY + 5; // Відстань від тексту до лінії
	    GLine line = new GLine(0, lineY, width, lineY);
	    main.add(line);
	    return score;
	}

	/**
	 * Оновлює відображення кількості життів. Видаляє попередній напис про кількість життів, якщо він існує.
	 *
	 * @param lifesLeft      Кількість життів, що залишились.
	 * @param previousLifes  Об'єкт GLabel, що представляє попередню кількість життів, або null, якщо його немає.
	 * @return Новий об'єкт GLabel, який представляє оновлену кількість життів.
	 */
	public GLabel setLifeCounter(int lifesLeft, GLabel previousLifes) {
	    if (previousLifes != null) {
	        main.remove(previousLifes);
	    }
	    GLabel lifes = new GLabel("Життя: " + lifesLeft);
	    lifes.setFont("Arialc-bold-16");
	    double lifeX = lifes.getX() + 5;
	    double lifeY = lifes.getAscent() + 2;
	    lifes.setLocation(lifeX, lifeY);
	    main.add(lifes);
	    return lifes;
	}
}

