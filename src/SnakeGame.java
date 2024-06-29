import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        private int x;
        private int y;
        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    //Snake
    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;

    //Food
    private Tile food;
    private Random random;

    //game logic
    private Timer gameLoop;
    private int velocityX;
    private int velocityY;
    private boolean gameOver = false;

    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int TILE_SIZE = 25;


    public SnakeGame(int boardWidth, int boardHeight) {
        this.BOARD_WIDTH = boardWidth;
        this.BOARD_HEIGHT = boardHeight;
        setPreferredSize(new Dimension(this.BOARD_WIDTH, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
        //Gird
        for (int i = 0; i < BOARD_WIDTH /TILE_SIZE; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, BOARD_HEIGHT);
            g.drawLine(0, i * TILE_SIZE, BOARD_WIDTH, i * TILE_SIZE);
        }

        //Food
        g.setColor(Color.blue);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        //Snake Head
        g.setColor(Color.red);
        g.fillRect(snakeHead.x * TILE_SIZE, snakeHead.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        //Snake Body
        for (int i = 0; i <snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.white);
            g.drawString("Game over: " + String.valueOf(snakeBody.size()), TILE_SIZE - 16, TILE_SIZE);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), TILE_SIZE - 16, TILE_SIZE);
        }
    }
    private void placeFood() {
        food.x = random.nextInt(BOARD_WIDTH /TILE_SIZE); //600/25 = 24
        food.y = random.nextInt(BOARD_HEIGHT /TILE_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver)
            gameLoop.stop();
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return  tile1.x == tile2.x && tile1.y == tile2.y;
    }
    private void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart))
                gameOver = true;
        }

        if (snakeHead.x * TILE_SIZE < 0 || snakeHead.x * TILE_SIZE > BOARD_WIDTH || snakeHead.y * TILE_SIZE < 0 || snakeHead.y * TILE_SIZE > BOARD_HEIGHT)
            gameOver = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
