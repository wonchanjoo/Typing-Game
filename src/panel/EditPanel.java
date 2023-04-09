package panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game_io.TextWriter;

public class EditPanel extends JPanel {
    private Container contentPane = null;
    private CardLayout cardLayout = null;
    private TextWriter textWriter = null;

    public EditPanel(Container contentPane) {
        this.contentPane = contentPane;
        this.cardLayout = (CardLayout)contentPane.getLayout();
        textWriter = new TextWriter();
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


        JLabel koreanLabel = new JLabel("한글 단어 추가하기");
        koreanLabel.setFont(new Font("굴림", Font.BOLD, 20));
        koreanLabel.setForeground(Color.WHITE);
        koreanLabel.setSize(300, 50);
        koreanLabel.setLocation(300, 100);
        this.add(koreanLabel);

        JTextField koreanInput = new JTextField();
        koreanInput.setSize(150, 20);
        koreanInput.setLocation(270, 170);
        this.add(koreanInput);

        JButton koreanBtn = new JButton("추가");
        koreanBtn.setSize(100, 40);
        koreanBtn.setLocation(430, 160);
        koreanBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = koreanInput.getText().trim();
                textWriter.textWrite("korean.txt", text);
                koreanInput.setText("");
            }
        });
        this.add(koreanBtn);


        JLabel englishLabel = new JLabel("영어 단어 추가하기");
        englishLabel.setFont(new Font("굴림", Font.BOLD, 20));
        englishLabel.setForeground(Color.WHITE);
        englishLabel.setSize(300, 50);
        englishLabel.setLocation(300, 300);
        this.add(englishLabel);

        JTextField englishInput = new JTextField();
        englishInput.setSize(150, 20);
        englishInput.setLocation(270, 370);
        this.add(englishInput);

        JButton englishBtn = new JButton("추가");
        englishBtn.setSize(100, 40);
        englishBtn.setLocation(430, 360);
        englishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = englishInput.getText().trim();
                textWriter.textWrite("english.txt", text);
                englishInput.setText("");
            }
        });
        this.add(englishBtn);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
    }
}
