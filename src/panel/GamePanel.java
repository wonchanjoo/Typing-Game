package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import game_io.ScoreWriter;
import game_io.TextSource;

public class GamePanel extends JPanel {
    private Container contentPane = null;
    private CardLayout cardLayout = null; // GameFrame에서 화면들을 바꿔주는 배치관리자!

    private boolean status; // true면 게임 진행, false면 게임 종료
    private JLabel text = new JLabel("3초 후에 게임이 시작됩니다.");
    private int level;
    private JLabel levelLabel = new JLabel();

    private int userFinalScore;
    private int userScore;
    private int computerScore;
    private JLabel userScoreLabel;
    private JLabel computerScoreLabel;

    private WordThread wordThread; // 단어의 위치를 계속해서 바꿔주는 스레드
    private ComputerScoreThread computerScoreThread; // 컴퓨터의 점수를 올려주는 스레드
    private JLabel timerBar = new JLabel(); // 타이머 바
    private JLabel timerLabel = new JLabel("30"); // 타이머
    private TimerBarThread timerBarThread = null;
    private TimerLabelThread timerLabelThread = null;

    private final int WORDS = 10; // 단어의 개수
    private Vector<WordLabel> v = new Vector<WordLabel>(WORDS); // 크기가 10인 벡터 생성

    private JTextField input = new JTextField(50);
    private TextSource textSource;
    private ScoreWriter scoreWriter = new ScoreWriter();
    private GameGroundPanel gameGroundPanel;

    public GamePanel(Container contentPane) {
        this.contentPane = contentPane;
        this.cardLayout = (CardLayout)contentPane.getLayout();
        this.setLayout(new BorderLayout());
        // gameGroundPanel을 지금 생성해서 부착하면 안되므로 InputPanel만 생성해서 부착해준다.
        this.add(new InputPanel(), BorderLayout.SOUTH);

    }

    public void prepareGame(String gameMode) { // 게임을 하기 위한 준비
        textSource = new TextSource(gameMode); // 한글 or 영어에 따라 텍스트 파일에서 단어 읽어오기
        gameGroundPanel = new GameGroundPanel(); // gameGroundPanel 생성
        this.add(gameGroundPanel, BorderLayout.CENTER);
        startGame(); // 준비가 끝났으니 게임 시작!!
    }

    public void initGame() {
        level = 1;
        levelLabel.setText(("Level " + Integer.toString(level)));
        userFinalScore = 0;
        userScore = 0;
        userScoreLabel.setText("0");
        computerScore = 0;
        computerScoreLabel.setText("0");
        status = true;
    }
    /* -------------------- 게임 시작 --------------------*/
    public void startGame() {
        initGame();

        timerBarThread = new TimerBarThread();
        timerBarThread.start();

        timerLabel.setText("30");
        timerLabelThread.start();

        wordThread = new WordThread(400); // 스레드 생성하고
        wordThread.start(); // 스레드 시작

        computerScoreThread = new ComputerScoreThread(2000); // 컴퓨터 점수 스레드 생성하고
        computerScoreThread.start(); // 스레드 시작

    }

    public boolean isWin() {
        if(userScore > computerScore)
            return true;
        else
            return false;
    }

    class WordLabel extends JLabel {
        private ImageIcon icon = new ImageIcon("images/cloud.PNG");
        private Image img = icon.getImage();

        public WordLabel() {
            this.setSize(100, 40);
            this.setForeground(Color.BLUE);
            this.setFont(new Font("나눔고딕 ExtraBold", Font.BOLD, 15));
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,  0, 0, getWidth(), getHeight(), this);
        }
    }


    /* -------------------- GameGroundPanel --------------------*/
    /* ---------- 사용자와 컴퓨터의 점수 라벨이 부착되는 패널 ------------ */
    /* -------------- 타이머와 타이머 바 부착되는 패널 ---------------- */
    class GameGroundPanel extends JPanel {
        public GameGroundPanel() {
            this.setLayout(null);

            // 레벨 라벨 크기와 위치 초기화하고 부착하기
            levelLabel.setSize(100, 50);
            levelLabel.setLocation(0, 0);
            this.add(levelLabel);

            // text 라벨 크기와 위치 초기화하고 부착하기
            text.setLocation(300, 100);
            text.setSize(300, 100);
            text.setVisible(false);
            this.add(text);

            // 사용자의 점수 Label 만들고 부착하기
            ImageIcon userImageIcon = new ImageIcon("images/user.png");
            userScoreLabel = new JLabel(Integer.toString(userScore), userImageIcon, SwingConstants.CENTER);
            userScoreLabel.setSize(110, 60);
            userScoreLabel.setLocation(160, 440);
            this.add(userScoreLabel);

            // 컴퓨터의 점수 Label 만들고 부착하기
            ImageIcon computerImageIcon = new ImageIcon("images/computer.PNG");
            computerScoreLabel = new JLabel(Integer.toString(computerScore), computerImageIcon, SwingConstants.CENTER);
            computerScoreLabel.setSize(100, 60);
            computerScoreLabel.setLocation(500, 440);
            this.add(computerScoreLabel);

            // 벡터에 들어있는 JLabel을 생성하고 크기를 설정해준 후에 벡터에 삽입, 패널에 부착
            for(int i=0; i<WORDS; i++) {
                WordLabel label = new WordLabel();
                v.add(label); // 벡터 v에 label 넣어주기
                this.add(label); // gameGroundPanel에 부착
            }
            initWords();

            timerBar.setBackground(Color.BLACK);
            timerBar.setBounds(740, 60, 30, 435);
            timerBar.setOpaque(true);
            this.add(timerBar); // GameGroundPanel에 부착

            timerBarThread = new TimerBarThread();

            timerLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
            timerLabel.setSize(50, 50);
            timerLabel.setLocation(735, 10);
            this.add(timerLabel); // GameGroundPanel에 부착

            timerLabelThread = new TimerLabelThread();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backGroundIcon = new ImageIcon("images/BackGround.jpeg");
            Image backGround = backGroundIcon.getImage();
            g.drawImage(backGround,  0, 0, getWidth(), getHeight(), this);
        }

        public boolean isOverlapPreviousLabels(int i, int x, int y) {
            for(int j=0; j<i; j++) {
                WordLabel label = v.get(j);
                if(label.getX() == x && label.getY() == y)
                    return true;
            }
            return false;
        }

        public void initWords() { // label의 텍스트와 위치만 초기화
            int x, y;
            for(int i=0; i<v.size(); i++) {
                WordLabel label = v.get(i);
                do {
                    x = ((int)(Math.random() * 7) * 100) + 10;
                    y = (int)(Math.random() * 6) * 50;
                } while(isOverlapPreviousLabels(i, x, y)); // 겹치면 x, y 다시 생성
                label.setText(textSource.get());
                label.setLocation(x, y);
            }
        }

        public void initLabelLocation(JLabel label) {
            int x = ((int)(Math.random() * 7) * 100) + 10;
            int y = (int)(Math.random() * 6) * 50;
            label.setLocation(x, y);
        }

        public void increaseUserScore() { // 사용자 점수 증가
            userScore += 100;
            userScoreLabel.setText(Integer.toString(userScore));
        }
        public void increaseComputerScore() { // 컴퓨터 점수 증가
            computerScore += 100;
            computerScoreLabel.setText(Integer.toString(computerScore));
        }

        public void changeWord(JLabel label) {
            label.setText(textSource.get()); // 텍스트 새로 설정
        }


    } // gameGroundPanel 끝

    /* ------------------- timerBar가 줄어들게 하는 스레드 ------------------ */
    class TimerBarThread extends Thread { // timerBar가 줄어들게하는 스레드
        private void initTimerBar() {
            timerBar.setBackground(Color.BLACK);
            timerBar.setBounds(740, 60, 30, 435);
        }
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    if(status == true) { // next level
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {}
                        initTimerBar();
                    }
                    else // game over
                        break;
                }
                if(timerBar.getHeight() > 0)
                    timerBar.setBounds(timerBar.getX(), timerBar.getY()+15, timerBar.getWidth(), timerBar.getHeight()-15);
                if(Integer.parseInt(timerLabel.getText()) == 5)
                    timerBar.setBackground(Color.RED);
            }
        }
    }
    class TimerLabelThread extends Thread { // timerLabel의 숫자가 줄어들게하는 스레드
        @Override
        public void run() {
            int time = 30;
            while(true) {
                try {
                    Thread.sleep(1000); // 1초 쉬고
                    time = Integer.parseInt(timerLabel.getText()) - 1; // 1초 감소
                } catch (InterruptedException e) {}
                /* ------------------------- 시간 종료 ------------------------- */
                if(time == -1) {
                    userFinalScore += userScore;
                    if(isWin() && level < 4) { // 사용자가 이긴 경우 4레벨 까지는 게임을 계속 진행
                        level++;
                        levelLabel.setText("Level " + Integer.toString(level));
                        userScore = 0;
                        userScoreLabel.setText("0");
                        computerScore = 0;
                        computerScoreLabel.setText("0");
                        wordThread.interrupt(); // 단어 스레드 멈추기
                        timerBarThread.interrupt(); // 타이머 바 스레드 멈추기
                        computerScoreThread.interrupt(); // 컴퓨터 스레드 멈추기
                        try {
                            text.setVisible(true);
                            Thread.sleep(3000); // 3초 기다리고 다시시작
                            text.setVisible(false);
                            time = 30;
                        } catch (InterruptedException e) {}
                    }
                    else { // 컴퓨터가 이겼거나 4레벨을 넘은 경우
                        status = false;
                        wordThread.interrupt(); // 단어 스레드 멈추기
                        timerBarThread.interrupt(); // 타이머 바 스레드 멈추기
                        computerScoreThread.interrupt(); // 컴퓨터 스레드 멈추기
                        String temp = "점수 " + Integer.toString(userFinalScore) + "점, 닉네임을 영어로 입력하세요";
                        String name = JOptionPane.showInputDialog(temp); // 닉네임을 입력받고
                        if(name == null) {}
                        else
                            scoreWriter.scoreWrite(name, userFinalScore); // 점수 파일에 쓰기
                        cardLayout.show(contentPane, "readyPanel");
                        break;
                    }
                }
                timerLabel.setText(Integer.toString(time));
            }
        }
    }

    /* -------------------- 사용자가 단어 입력하는 패널 -------------------- */
    class InputPanel extends JPanel {
        public InputPanel() {
            this.setLayout(new FlowLayout());
            input.addActionListener(new ActionListener() { // textField에 Enter이 눌리면 발생하는 이벤트
                public void actionPerformed(ActionEvent e) {
                    JTextField tf = (JTextField)e.getSource();
                    String inWord = tf.getText(); // 사용자가 입력한 단어
                    for(int i=0; i<v.size(); i++)
                        if(v.get(i).getText().equals(inWord)) { // 맞추기 성공하면
                            gameGroundPanel.increaseUserScore(); // 사용자 점수 증가
                            gameGroundPanel.initLabelLocation(v.get(i)); // 맞춘 라벨의 위치를 초기화
                            gameGroundPanel.changeWord(v.get(i)); // 맞춘 라벨의 텍스트 초기화
                        }
                    input.setText("");
                }
            });
            this.add(input);
        }
    }

    /* -------------------- 단어 스레드 -------------------- */
    class WordThread extends Thread {
        private int delay;
        public WordThread(int delay) {
            this.delay = delay;
        }

        private boolean isOverlapScoreLabels(int x, int y) { // x, y좌표가 모두 겹치면 true
            int userX = userScoreLabel.getX();
            int userY = userScoreLabel.getY();
            if(userX <= x && x <= (userX+userScoreLabel.getWidth()) && userY <= y && y<= (userY+userScoreLabel.getHeight()))
                return true;
            int computerX = computerScoreLabel.getX();
            int computerY = computerScoreLabel.getY();
            if(computerX <= x && x <= (computerX+computerScoreLabel.getWidth()) && computerY <= y && y <= (computerY+computerScoreLabel.getHeight()))
                return true;
            return false;
        }

        private void setWordsInvisible() {
            for(int i=0; i<v.size(); i++)
                v.get(i).setVisible(false);
        }
        private void setWordsVisible() {
            for(int i=0; i<v.size(); i++)
                v.get(i).setVisible(true);
        }
        private void changeLabelsLocation() { // 라벨들의 위치를 바꿔주는 함수
            for(int i=0; i<v.size(); i++) {
                JLabel label = v.get(i);
                int dir = Math.random() > 0.5 ? 1 : -1; // 음수면 왼쪽 양수면 오른쪽
                int x = label.getX();
                int y = (int)(Math.random() * 5) + label.getY();
                if(y > 550 || isOverlapScoreLabels(x, y + label.getHeight())) { // 바닥에 닿거나, score라벨과 닿은 경우
                    gameGroundPanel.initLabelLocation(label); // label의 위치 초기화
                    gameGroundPanel.changeWord(label); // label의 text를 새로운 단어로 초기화
                }
                else
                    label.setLocation(x, y);
            }
        }
        @Override
        public void run() { // 단어의 위치를 계속 바꿔줌
            while(true) {


                try {
                    Thread.sleep(delay);
                    changeLabelsLocation();

                } catch (InterruptedException e) {  // 게임 시간 끝
                    if(status == true) { // 게임을 계속 진행해야되면
                        setWordsInvisible(); // 단어를 다 안보이게 하고
                        gameGroundPanel.initWords(); // 단어의 위치와 텍스트 초기화
                        try {
                            Thread.sleep(3000); // 3초 기다리고
                        } catch (InterruptedException e1) {}
                        setWordsVisible(); // 단어 다 보이게하기
                        delay -= 100; // 딜레이 줄여주고 다시 시작
                    }
                    else
                        break;
                }


            }
        }
    }

    /* -------------------- 일정 시간 마다 컴퓨터의 점수를 올려주고, 단어를 하나씩 바꿔주는 스레드 -------------------- */
    class ComputerScoreThread extends Thread {
        private int delay;
        public ComputerScoreThread(int delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(delay);
                    int r = (int)(Math.random() * WORDS);
                    JLabel label = v.get(r); // 벡터에서 랜덤으로 단어 하나 선택
                    int x = (int)(Math.random() * (gameGroundPanel.getWidth() - label.getWidth()));
                    label.setLocation(x, 0); // 텍스트의 위치 새로 설정
                    label.setText(textSource.get()); // 텍스트 새로 설정
                    gameGroundPanel.increaseComputerScore();
                } catch (InterruptedException e) {
                    if(status == true) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {}
                        delay -= 200;
                    }
                    else break;
                }
            }
        }
    }
}
