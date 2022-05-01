import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Utils {

    public static void makeFrameFromTemplate(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public static void reWriterUserFile(ArrayList < User > users, String fileName) { //do this when they delete their account, change username, password, or any other user attribute that is stored
        File f = new File(fileName);
        String descriptor;
        FileWriter fwr = null;
        try {
            fwr = new FileWriter(f, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (var v: users) {
            if (v instanceof Teacher) {
                descriptor = "Teacher";
            } else {
                descriptor = "Student";
            }
            try {
                fwr.append(String.format("%s,%s,%s\n", v.getUsername(), v.getPassword(), descriptor));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fwr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}