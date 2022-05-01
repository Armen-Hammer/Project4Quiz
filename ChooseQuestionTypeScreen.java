import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChooseQuestionTypeScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel prompt = new JLabel("WHAT TYPE OF QUESTION DO YOU WANT TO MAKE?");
    JButton multChoiceButton = new JButton("MULTIPLE CHOICE");
    JButton trueFalseButton = new JButton("TRUE/FALSE");
    JButton fillBlankButton = new JButton("FILL IN THE BLANK");
    JButton finishButton = new JButton("FINISH");


    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    private boolean showFinishButton;

    public ChooseQuestionTypeScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, boolean showFinishButton) {
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
        multChoiceButton.setBounds(100, 220, 150, 30);
        trueFalseButton.setBounds(100, 290, 150, 30);
        fillBlankButton.setBounds(100, 360, 150, 30);

        if (showFinishButton) {
            finishButton.setBounds(100, 420, 150, 30);
        }
    }

    public void addComponentsToContainer() {
        container.add(prompt);
        container.add(multChoiceButton);
        container.add(fillBlankButton);
        container.add(trueFalseButton);

        if (showFinishButton) {
            container.add(finishButton);
        }

    }

    public void addActionEvent() {
        multChoiceButton.addActionListener(this);
        fillBlankButton.addActionListener(this);
        trueFalseButton.addActionListener(this);
        finishButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int x = 0;
        if (e.getSource() == multChoiceButton) {
            this.dispose();
            CreateMCquestionScreen currentScreen = new CreateMCquestionScreen(socket, pw, oos, ois);
            Utils.makeFrameFromTemplate(currentScreen, "Create Multiple Choice Question");
        } else if (e.getSource() == trueFalseButton) {
            this.dispose();
            CreateTFquestionScreen currentScreen = new CreateTFquestionScreen(socket, pw, oos, ois);
            Utils.makeFrameFromTemplate(currentScreen, "Create True/False Question");
        } else if (e.getSource() == fillBlankButton) {
            this.dispose();
            CreateFBquestionScreen currentScreen = new CreateFBquestionScreen(socket, pw, oos, ois);
            Utils.makeFrameFromTemplate(currentScreen, "Create a fill in the blank Question");
        } else if (e.getSource() == finishButton) {
            this.dispose();
            TeacherBaseView currentScreen = new TeacherBaseView(socket, pw, oos, ois);
            Utils.makeFrameFromTemplate(currentScreen, "Home");
        }
    }

}