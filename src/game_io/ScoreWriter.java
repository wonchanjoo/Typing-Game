package game_io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ScoreWriter {
    private PrintWriter printWriter = null;

    public void scoreWrite(String name, int score) {
        String text = name + " " + Integer.toString(score);
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter("score.txt", true)));
            printWriter.println(text);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            return;
        }
    }
}
