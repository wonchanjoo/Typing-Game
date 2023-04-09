package game_io;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class TextWriter {
    private PrintWriter pw = null;
    private FileOutputStream output = null;
    private OutputStreamWriter writer = null;
    private BufferedWriter out = null;

    public void textWrite(String fileName, String text) {
        try {
            output = new FileOutputStream(fileName, true);
            try {
                writer = new OutputStreamWriter(output, "UTF-8");
                out = new BufferedWriter(writer);
                try {
                    out.write(text);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {}
            } catch (UnsupportedEncodingException e) {}
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            out.close();
        } catch (IOException e) {}
    }
}
