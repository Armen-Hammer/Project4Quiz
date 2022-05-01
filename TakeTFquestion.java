import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TakeTFquestion extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("Question");
    JLabel answerLabel = new JLabel("Answer");

    JLabel questionField;

    String[] optionsToChoose = {
        "TRUE",
        "FALSE"
    };

    JComboBox < String > jComboBox = new JComboBox < > (optionsToChoose);

    JButton answerQuestionButton = new JButton("Answer question");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    QuizSubmission submission;
    Question question;


    public TakeTFquestion(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, TFquestion question, QuizSubmission submission) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        this.socket = socket;
        this.question = question;
        questionField = new JLabel(question.getQuestion());
        this.submission = submission;
        this.pw = pw;
        this.oos = oos;
        this.ois = ois;
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        answerLabel.setBounds(50, 200, 100, 30);
        jComboBox.setBounds(150, 200, 150, 30);

        questionLabel.setBounds(50, 70, 100, 30);
        questionField.setBounds(150, 70, 100, 30);


        answerQuestionButton.setBounds(150, 300, 150, 30);

    }

    public void addComponentsToContainer() {

        container.add(jComboBox);
        container.add(answerLabel);

        container.add(answerQuestionButton);
        container.add(questionField);
        container.add(questionLabel);
    }

    public void addActionEvent() {
        answerQuestionButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == answerQuestionButton) {
            boolean answer = false;
            if (jComboBox.getSelectedIndex() == 0) {
                answer = true;
            }


        }
    }
}