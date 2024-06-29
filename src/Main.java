import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true); // видимость экрана
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // расположение относ нуля
        frame.setResizable(false); // пост размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // закрытие при нажатии на крестик

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}