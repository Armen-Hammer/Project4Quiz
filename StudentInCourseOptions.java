import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class StudentInCourseOptions extends JFrame implements ActionListener {

    Container container = getContentPane();

    JLabel prompt = new JLabel("WHAT DO YOU WANT TO DO?");
    JButton takeQuizButton = new JButton("TAKE A QUIZ");
    JButton logoutButton = new JButton("LOGOUT ");
    JButton resetPassButton = new JButton("RESET PASSWORD");
    JButton resetNameButton = new JButton("RESET USERNAME");
    JButton delAccountButton = new JButton("DELETE ACCOUNT");


    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public StudentInCourseOptions(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.socket = socket;

        this.pw = pw;
        this.oos = oos;
        this.ois = ois;
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        prompt.setBounds(100, 150, 250, 30);
        takeQuizButton.setBounds(25, 220, 150, 30);
        resetNameButton.setBounds(175, 220, 150, 30);
        resetPassButton.setBounds(25, 290, 150, 30);
        logoutButton.setBounds(175, 290, 150, 30);
        delAccountButton.setBounds(25, 360, 150, 30);

    }

    public void addComponentsToContainer() {
        container.add(prompt);
        container.add(takeQuizButton);
        container.add(resetNameButton);
        container.add(resetPassButton);
        container.add(logoutButton);
        container.add(delAccountButton);
    }

    public void addActionEvent() {
        takeQuizButton.addActionListener(this);
        resetNameButton.addActionListener(this);
        resetPassButton.addActionListener(this);
        logoutButton.addActionListener(this);
        delAccountButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == delAccountButton) {
                oos.writeObject(Server.DELETE_ACCOUNT);
                oos.flush();
                String response = (String) ois.readObject();
                if (response.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    LoginScreen newFrame = new LoginScreen(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Login");
                }
            } else if (e.getSource() == logoutButton) {
                oos.writeObject(Server.LOGOUT);
                JOptionPane.showMessageDialog(this, ois.readObject());
                LoginScreen currentScreen = new LoginScreen(socket, pw, oos, ois);
                Utils.makeFrameFromTemplate(currentScreen, "Login");
            } else if (e.getSource() == resetNameButton) {
                ResetUsernameScreen currentScreen = new ResetUsernameScreen(socket, pw, oos, ois, this);
                Utils.makeFrameFromTemplate(currentScreen, "Edit username");
            } else if (e.getSource() == resetPassButton) {
                ResetPasswordScreen currentScreen = new ResetPasswordScreen(socket, pw, oos, ois, this);
                Utils.makeFrameFromTemplate(currentScreen, "Edit password");
            } else if (e.getSource() == takeQuizButton) {
                oos.writeObject(Server.GET_QUIZZES_STR);
                String x = (String) ois.readObject();
                if (x.equals("Success")) {
                    ArrayList < String > quizzes = (ArrayList < String > ) ois.readObject();
                    this.setVisible(false);
                    QuizListScreen currentScreen = new QuizListScreen(socket, pw, oos, ois, quizzes, Server.TAKE_QUIZ, this);
                    Utils.makeFrameFromTemplate(currentScreen, "Pick Quiz to Edit");
                } else {
                    JOptionPane.showMessageDialog(this, x);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}