package game;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import panel.*;
public class GameFrame extends JFrame {
    CardLayout cardLayout;

    private Container contentPane;
    private ReadyPanel readyPanel;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private EditPanel editPanel;

    private File file;
    private AudioInputStream audioStream;
    private Clip clip;

    public GameFrame() {
        setTitle("Typing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 게임 준비 화면과 게임 실행 화면이 번갈아가면서 나타나야 되기 때문에 cardLayout 배치관리자 사용
        contentPane = this.getContentPane();
        cardLayout = new CardLayout();
        contentPane.setLayout(cardLayout);

        // Panel들 생성
        gamePanel = new GamePanel(contentPane);
        readyPanel = new ReadyPanel(contentPane, gamePanel);
        scorePanel = new ScorePanel(contentPane);
        editPanel = new EditPanel(contentPane);

        // contentPane에 부착
        contentPane.add(readyPanel, "readyPanel");
        contentPane.add(gamePanel,"gamePanel");
        contentPane.add(scorePanel, "scorePanel");
        contentPane.add(editPanel, "editPanel");

        // 메뉴바 만들기
        createMenu();

        // 음악 파일 불러오기
        file = new File("way_back_then.wav");
        try {
            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch(Exception e) {}
        playMusic(); // 음악 재생

        setSize(800, 600);
        setResizable(false);
        setVisible(true);
        setLocation(500, 150);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu scoreMenu = new JMenu("Score");
        JMenu editMenu = new JMenu("Edit");
        JMenu soundMenu = new JMenu("Sound");

        JMenuItem home = new JMenuItem("Home");
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "readyPanel");
            }
        });
        gameMenu.add(home);
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gameMenu.add(exit);


        JMenuItem scoreShow = new JMenuItem("Show");
        scoreShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scorePanel.updateRank(); // 랭킹 업데이트 해주고
                cardLayout.show(contentPane, "scorePanel"); // scorePanel로 바꿔주기
            }
        });
        scoreMenu.add(scoreShow);

        JMenuItem add = new JMenuItem("Add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "editPanel");
            }
        });
        editMenu.add(add);

        JMenuItem soundOn = new JMenuItem("On");
        soundOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playMusic();
            }
        });
        JMenuItem soundOff = new JMenuItem("Off");
        soundOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopMusic();
            }
        });
        soundMenu.add(soundOn);
        soundMenu.add(soundOff);

        menuBar.add(gameMenu);
        menuBar.add(scoreMenu);
        menuBar.add(editMenu);
        menuBar.add(soundMenu);

        this.setJMenuBar(menuBar);
    }

    private void playMusic() {
        clip.start();
    }
    private void stopMusic() {
        clip.stop();
    }
    class SoundListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
