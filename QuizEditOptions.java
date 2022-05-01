import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class QuizEditOptions extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel prompt = new JLabel("WHAT TYPE OF QUESTION DO YOU WANT TO MAKE?");
    JButton addQuestionButton = new JButton("ADD QUESTION");
    JButton editQuestionButton = new JButton("EDIT QUESTION");
    JButton deleteQuestionButton = new JButton("DELETE QUESTION");
    JButton finishButton = new JButton("FINISH");


    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    private boolean showFinishButton;

    public QuizEditOptions(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, boolean showFinishButton) {
        this.showFinishButton = showFinishButton;

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
        prompt.setBounds(50, 150, 500, 30);
        addQuestionButton.setBounds(100, 220, 150, 30);
        editQuestionButton.setBounds(100, 290, 150, 30);
        deleteQuestionButton.setBounds(100, 360, 150, 30);

        if (showFinishButton) {
            finishButton.setBounds(100, 420, 150, 30);
        }
    }

    public void addComponentsToContainer() {
        container.add(prompt);
        container.add(addQuestionButton);
        container.add(deleteQuestionButton);
        //container.add(editQuestionButton);

        if (showFinishButton) {
            container.add(finishButton);
        }

    }

    public void addActionEvent() {
        addQuestionButton.addActionListener(this);
        deleteQuestionButton.addActionListener(this);
        editQuestionButton.addActionListener(this);
        finishButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addQuestionButton) {
                this.dispose();
                ChooseQuestionTypeScreen currentScreen = new ChooseQuestionTypeScreen(socket, pw, oos, ois, false);
                Utils.makeFrameFromTemplate(currentScreen, "Add Questions");
            } else if (e.getSource() == editQuestionButton) {

            } else if (e.getSource() == deleteQuestionButton) {
                oos.writeObject(Server.GET_QUESTIONS_STR);
                String res = (String) ois.readObject();
                if (res.equals("Success")) {
                    ArrayList < String > questions = (ArrayList < String > ) ois.readObject();
                    this.setVisible(false);
                    QuestionListScreen currentScreen = new QuestionListScreen(socket, pw, oos, ois, questions, Server.DELETE_QUESTION, this);
                    Utils.makeFrameFromTemplate(currentScreen, "Delete Question");
                } else {
                    JOptionPane.showMessageDialog(this, res);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
}