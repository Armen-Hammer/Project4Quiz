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

public class QuizListScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("What quiz do you want to choose");


    JComboBox < String > jComboBox;

    JButton enterQuizButton = new JButton("Select");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int action;
    JFrame calledFrom;

    public QuizListScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, ArrayList < String > quizes, int action, JFrame calledFrom) {
        jComboBox = new JComboBox < > (quizes.toArray(new String[quizes.size()]));

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

        enterQuizButton.setBounds(50, 240, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(jComboBox);
        container.add(enterQuizButton);
        container.add(questionLabel);
    }

    public void addActionEvent() {
        enterQuizButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == enterQuizButton) {
                //get course edit options
                switch (action) {
                    case Server.DELETE_QUIZ:
                        oos.writeObject(action);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        JOptionPane.showMessageDialog(this, ois.readObject());
                        this.dispose();
                        calledFrom.setVisible(true);
                        break;
                    case Server.EDIT_QUIZ:
                        oos.writeObject(Server.SET_CURRENT_QUIZ);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        this.dispose();
                        QuizEditOptions currentScreen = new QuizEditOptions(socket, pw, oos, ois, false);
                        Utils.makeFrameFromTemplate(currentScreen, "Course edit options");
                        break;
                    case Server.TAKE_QUIZ:
                        Quiz quiz = (Quiz) ois.readObject();
                        ArrayList < Question > questions = quiz.getQuestions();
                        break;

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}