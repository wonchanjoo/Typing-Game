package game_io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

import game.User;

public class ScoreReader {
    public Vector<User> users = new Vector<User>(100);

    public ScoreReader() {
        // score 텍스트 파일에서 이름와 점수를 읽어와서 벡터에 저장
        try {
            FileReader fileReader = new FileReader("score.txt");
            BufferedReader br = new BufferedReader(fileReader);
            String str = null;
            try {
                while((str = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(str);
                    String name = st.nextToken();
                    String score = st.nextToken();
                    User user = new User(name, Integer.parseInt(score));
                    users.add(user);
                }
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {}

    }

    public void sortUsers() {
        Collections.sort(users, new UserComparator());
    }

    public User[] getTop10() {
        User[] top10 = new User[10];
        for(int i=0; i<top10.length; i++) {
            if(i == users.size()) break;
            top10[i] = users.get(i);
        }
        return top10;
    }

    public void updateScore() {
        users.removeAllElements();
        try {
            FileReader fileReader = new FileReader("score.txt");
            BufferedReader br = new BufferedReader(fileReader);
            String str = null;
            try {
                while((str = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(str);
                    String name = st.nextToken();
                    String score = st.nextToken();
                    User user = new User(name, Integer.parseInt(score));
                    users.add(user);
                }
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {}
        sortUsers();
    }
}

class UserComparator implements Comparator<User> {
    @Override
    public int compare(User o0, User o1) {
        return  o0.getScore() > o1.getScore() ? -1 : (o0.getScore() == o1.getScore() ? 0 : 1);
    }
}
