package panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ReadyPanel extends JPanel {
    private Container contentPane;
    private CardLayout cardLayout;
    private GamePanel gamePanel;
    private JTextField nameInput = new JTextField();
    private JButton korStart;
    private JButton engStart;

    private ImageIcon backgroundIcon = new ImageIcon("images/readyBackground.jpg");
    private Image background = backgroundIcon.getImage();

    public ReadyPanel(Container contentPane, GamePanel gamePanel) {
        this.contentPane = contentPane;
        this.cardLayout = (CardLayout)contentPane.getLayout();
        this.gamePanel = gamePanel;
        this.setLayout(null);

        JLabel text = new JLabel("버튼을 누르면 게임이 시작됩니다.");
        text.setForeground(Color.WHITE);
        text.setFont(new Font("gothic", Font.BOLD, 20));
        text.setSize(500, 50);
        text.setLocation(240, 250);
        this.add(text);

        // 한글 모드로 시작하기 버튼
        korStart = new JButton("한글모드");
        korStart.setSize(100, 50);
        korStart.setLocation(240, 320);
        korStart.addActionListener(new ActionListener() { // 버튼이 눌리면
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "gamePanel"); // 화면을 gamePanel로 바꿔주고
                gamePanel.prepareGame("kor"); // gamePanel에게 한글 모드로 준비하라고 알려줌
            }
        });
        this.add(korStart); // 한글 버튼 부착

        // 영어 모드로 시작하기 버튼
        engStart = new JButton("영어모드");
        engStart.setSize(100, 50);
        engStart.setLocation(440, 320);
        engStart.addActionListener(new ActionListener() { // 버튼이 눌리면
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "gamePanel"); // 화면을 gamePanel로 바꿔주고
                gamePanel.prepareGame("eng"); // gamePanel에게 영어 모드로 준비하라고 알려줌
            }
        });
        this.add(engStart);	// 영어 버튼 부착
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background,  0, 0, getWidth(), getHeight(), this);
    }
}
