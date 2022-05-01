import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CreateFBquestionScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("Question");
    JLabel answerLabel = new JLabel("Answer");
    JTextField answerField = new JTextField();

    JTextField questionField = new JTextField();


    JButton makeQuestionButton = new JButton("Create FB question");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public CreateFBquestionScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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

        answerLabel.setBounds(50, 200, 100, 30);
        answerField.setBounds(150, 200, 150, 30);

        questionLabel.setBounds(50, 70, 100, 30);
        questionField.setBounds(150, 70, 100, 30);


        makeQuestionButton.setBounds(150, 300, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(answerField);

        container.add(answerLabel);

        container.add(makeQuestionButton);
        container.add(questionField);
        container.add(questionLabel);
    }

    public void addActionEvent() {
        makeQuestionButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == makeQuestionButton) {
            FillBlankQuestion q = new FillBlankQuestion(questionField.getText(), answerField.getText());
            try {
                oos.writeObject(Server.CREATE_FB_QUESTION);
                oos.writeObject(q);
                this.dispose();
                ChooseQuestionTypeScreen currentScreen = new ChooseQuestionTypeScreen(socket, pw, oos, ois, true);
                Utils.makeFrameFromTemplate(currentScreen, "Quiz Maker");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}