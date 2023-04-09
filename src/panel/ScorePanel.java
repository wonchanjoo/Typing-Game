package panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import game.User;
import game_io.ScoreReader;

public class ScorePanel extends JPanel {
    private Container contentPane = null;
    private CardLayout cardLayout = null;
    private ScoreReader scoreReader = null;

    private JLabel ranking = new JLabel("Ranking");
    private User[] top10 = new User[10];
    private JLabel[] top10Label = new JLabel[10];

    public ScorePanel(Container contentPane) {
        this.contentPane = contentPane;
        this.cardLayout = (CardLayout)contentPane.getLayout();
        this.setLayout(null);

        JLabel back = new JLabel("←");
        back.setSize(50, 50);
        back.setFont(new Font("gothic", Font.BOLD, 20));
        back.setLocation(10, 0);
        back.setForeground(Color.WHITE);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPane, "readyPanel");
            }
        });
        this.add(back);

        ranking.setSize(200, 40);
        ranking.setForeground(Color.RED);
        ranking.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 30));
        ranking.setLocation(320, 10);
        this.add(ranking);

        for(int i=0; i<top10Label.length; i++) {
            int x, y;
            if(i < 5) {
                x = 150;
                y = (i + 1) * 80;
            }
            else {
                x = 450;
                y = (i + 1) * 80 - 400;
            }
            top10Label[i] = new JLabel();
            top10Label[i].setForeground(Color.WHITE);
            top10Label[i].setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 20));
            top10Label[i].setSize(300, 30);
            top10Label[i].setLocation(x, y);
            this.add(top10Label[i]);
        }

        scoreReader = new ScoreReader();
        scoreReader.sortUsers(); // 벡터 정렬해주기
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
    }

    public void updateRank() {
        scoreReader.updateScore();
        top10 = scoreReader.getTop10();
        for(int i=0; i<top10.length; i++) {
            if(i == scoreReader.users.size()) break;
            String name = top10[i].getName();
            int score = top10[i].getScore();
            String text = (i+1) + ".  " + name + "  " + score;
            top10Label[i].setText(text);
        }
    }
}
