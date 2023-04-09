package game_io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
    private Vector<String> words = new Vector<String>(1500);

    public TextSource(String gameMode) { // 파일에서 단어 입력받기
        if(gameMode.equals("kor")) { // 한글 모드인 경우
            try {
                File file = new File("korean.txt");
                Scanner scanner = null;
                try {
                    scanner = new Scanner(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                while(scanner.hasNext()) {
                    String word = scanner.nextLine();
                    words.add(word.trim());
                }
                scanner.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else { // 영어 모드인 경우
            try {
                Scanner fscanner = new Scanner(new FileReader("english.txt"));
                while(fscanner.hasNext()) {
                    String word = fscanner.nextLine();
                    words.add(word.trim());
                }
                fscanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String get() { // 벡터에 있는 단어들 중 하나를 선택해서 반환
        int index = (int)(Math.random() * words.size());
        return words.get(index);
    }
}
