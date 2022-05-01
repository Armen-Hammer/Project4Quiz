import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class CourseEditsOptionScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    public static String LOGGED_IN_TEACHER_OPTIONS = "1.Make a quiz\n2.Edit a quiz\n3.Delete quiz\n4.View quiz submissions\n5.Edit username\n6.Edit password\n7.Logout\n8.Delete Account";

    JLabel prompt = new JLabel("WHAT DO YOU WANT TO DO?");
    JButton makeQuizButton = new JButton("MAKE A QUIZ");
    JButton editQuizButton = new JButton("EDIT A QUIZ");
    JButton deleteQuizButton = new JButton("DELETE A QUIZ");
    JButton submissionsButton = new JButton("VIEW QUIZ SUBMISSIONS");
    JButton logoutButton = new JButton("LOGOUT ");
    JButton resetPassButton = new JButton("RESET PASSWORD");
    JButton resetNameButton = new JButton("RESET USERNAME");
    JButton delAccountButton = new JButton("DELETE ACCOUNT");


    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public CourseEditsOptionScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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
        makeQuizButton.setBounds(25, 220, 150, 30);
        editQuizButton.setBounds(175, 220, 150, 30);
        deleteQuizButton.setBounds(25, 290, 150, 30);
        submissionsButton.setBounds(175, 290, 150, 30);
        resetNameButton.setBounds(25, 360, 150, 30);
        resetPassButton.setBounds(175, 360, 150, 30);
        logoutButton.setBounds(25, 430, 150, 30);
        delAccountButton.setBounds(175, 430, 175, 30);

    }

    public void addComponentsToContainer() {
        container.add(prompt);
        container.add(makeQuizButton);
        container.add(editQuizButton);
        container.add(deleteQuizButton);
        container.add(resetNameButton);
        container.add(resetPassButton);
        container.add(logoutButton);
        container.add(delAccountButton);
        container.add(submissionsButton);
    }

    public void addActionEvent() {
        makeQuizButton.addActionListener(this);
        editQuizButton.addActionListener(this);
        deleteQuizButton.addActionListener(this);
        resetNameButton.addActionListener(this);
        resetPassButton.addActionListener(this);
        logoutButton.addActionListener(this);
        delAccountButton.addActionListener(this);
        submissionsButton.addActionListener(this);
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
            } else if (e.getSource() == makeQuizButton) {
                this.dispose();
                CreateQuizScreen currentScreen = new CreateQuizScreen(socket, pw, oos, ois);
                Utils.makeFrameFromTemplate(currentScreen, "Create Quiz");
                //ChooseQuestionTypeScreen currentScreen = new ChooseQuestionTypeScreen(socket, pw, oos, ois, false);
                //Utils.makeFrameFromTemplate(currentScreen, "Question types");
            } else if (e.getSource() == deleteQuizButton) {
                oos.writeObject(Server.GET_QUIZZES_STR);
                String x = (String) ois.readObject();
                if (x.equals("Success")) {
                    ArrayList < String > quizzes = (ArrayList < String > ) ois.readObject();
                    this.setVisible(false);
                    QuizListScreen currentScreen = new QuizListScreen(socket, pw, oos, ois, quizzes, Server.DELETE_QUIZ, this);
                    Utils.makeFrameFromTemplate(currentScreen, "Delete Quiz");
                } else {
                    JOptionPane.showMessageDialog(this, x);
                }
            } else if (e.getSource() == editQuizButton) {
                oos.writeObject(Server.GET_QUIZZES_STR);
                String x = (String) ois.readObject();
                if (x.equals("Success")) {
                    ArrayList < String > quizzes = (ArrayList < String > ) ois.readObject();
                    this.setVisible(false);
                    QuizListScreen currentScreen = new QuizListScreen(socket, pw, oos, ois, quizzes, Server.EDIT_QUIZ, this);
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