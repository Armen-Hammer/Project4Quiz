import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class QuestionListScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("What question do you want to choose");


    JComboBox < String > jComboBox;

    JButton enterQuestionButton = new JButton("Select");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int action;
    JFrame calledFrom;

    public QuestionListScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, ArrayList < String > questions, int action, JFrame calledFrom) {
        jComboBox = new JComboBox < > (questions.toArray(new String[questions.size()]));

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        this.calledFrom = calledFrom;
        this.socket = socket;
        this.action = action;
        this.pw = pw;
        this.oos = oos;
        this.ois = ois;
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {

        jComboBox.setBounds(50, 140, 200, 30);

        questionLabel.setBounds(50, 70, 350, 70);

        enterQuestionButton.setBounds(50, 240, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(jComboBox);
        container.add(enterQuestionButton);
        container.add(questionLabel);
    }

    public void addActionEvent() {
        enterQuestionButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == enterQuestionButton) {
                //get course edit options
                switch (action) {
                    case Server.DELETE_QUESTION:
                        oos.writeObject(action);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        this.dispose();
                        TeacherBaseView currentSreen = new TeacherBaseView(socket, pw, oos, ois);
                        Utils.makeFrameFromTemplate(currentSreen, "Home");
                        //calledFrom.setVisible(true);
                        break;
                    case Server.EDIT_QUIZ:
                        oos.writeObject(Server.SET_CURRENT_QUIZ);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        this.dispose();
                        QuizEditOptions currentScreen = new QuizEditOptions(socket, pw, oos, ois, false);
                        Utils.makeFrameFromTemplate(currentScreen, "Quiz edit options");
                        break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}