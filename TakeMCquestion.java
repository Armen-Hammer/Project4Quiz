import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TakeMCquestion extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("Question");
    JLabel choiceAlabel = new JLabel("Choice A");
    JLabel choiceBlabel = new JLabel("Choice B");
    JLabel choiceClabel = new JLabel("Choice C");
    JLabel choiceDlabel = new JLabel("Choice D");
    JLabel answerLabel = new JLabel("Answer");


    JLabel choiceAfield;
    JLabel choiceBfield;
    JLabel choiceCfield;
    JLabel choiceDfield;

    JTextField questionField = new JTextField();

    String[] optionsToChoose = {
        "1",
        "2",
        "3",
        "4"
    };

    JComboBox < String > jComboBox = new JComboBox < > (optionsToChoose);

    JButton answerQuestionButton = new JButton("Answer question");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public TakeMCquestion(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, MCquestion question) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        this.socket = socket;

        this.pw = pw;
        this.oos = oos;
        this.ois = ois;

        this.choiceAfield = new JLabel(question.getChoiceA());
        this.choiceBfield = new JLabel(question.getChoiceB());
        this.choiceCfield = new JLabel(question.getChoiceC());
        this.choiceDfield = new JLabel(question.getChoiceD());

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        choiceAlabel.setBounds(50, 150, 100, 30);
        choiceBlabel.setBounds(50, 220, 100, 30);
        choiceAfield.setBounds(150, 150, 150, 30);
        choiceBfield.setBounds(150, 220, 150, 30);

        choiceClabel.setBounds(50, 290, 100, 30);
        choiceDlabel.setBounds(50, 360, 100, 30);
        choiceCfield.setBounds(150, 290, 150, 30);
        choiceDfield.setBounds(150, 360, 150, 30);

        answerLabel.setBounds(50, 410, 100, 30);
        jComboBox.setBounds(150, 410, 150, 30);

        questionLabel.setBounds(50, 70, 100, 30);
        questionField.setBounds(150, 70, 100, 30);


        answerQuestionButton.setBounds(150, 460, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(choiceAfield);
        container.add(choiceBfield);
        container.add(choiceCfield);
        container.add(choiceDfield);

        container.add(choiceAlabel);
        container.add(choiceBlabel);
        container.add(choiceClabel);
        container.add(choiceDlabel);

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
        //do something to connect to control flow
        if (e.getSource() == answerQuestionButton) {
            MCquestion q = new MCquestion(questionField.getText(), choiceAfield.getText(), choiceBfield.getText(), choiceCfield.getText(), choiceDfield.getText(), jComboBox.getSelectedIndex() + 1);
            try {
                oos.writeObject(Server.CREATE_MC_QUESTION);
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