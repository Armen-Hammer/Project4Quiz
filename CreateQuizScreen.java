import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CreateQuizScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Quiz Name");
    JTextField quizNameField = new JTextField();
    JButton makeCourseButton = new JButton("Create Quiz");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public CreateQuizScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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
        nameLabel.setBounds(50, 150, 100, 30);
        quizNameField.setBounds(150, 150, 150, 30);
        makeCourseButton.setBounds(150, 290, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(quizNameField);
        container.add(makeCourseButton);
    }

    public void addActionEvent() {
        makeCourseButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) { //fixme
        if (e.getSource() == makeCourseButton) {

            try {
                String quizName = quizNameField.getText();
                if (quizName.length() == 0) {
                    JOptionPane.showMessageDialog(this, "The course name must be longer than 1 character");
                    return;
                }
                Quiz c = new Quiz(quizName);
                oos.writeObject(Server.CREATE_QUIZ);
                oos.writeObject(c);
                String response = (String) ois.readObject();
                JOptionPane.showMessageDialog(this, response);
                this.dispose();
                //TeacherBaseView newFrame = new TeacherBaseView(socket, pw, oos, ois);
                //Utils.makeFrameFromTemplate(newFrame, "Home");
                ChooseQuestionTypeScreen currentScreen = new ChooseQuestionTypeScreen(socket, pw, oos, ois, false);
                Utils.makeFrameFromTemplate(currentScreen, "Question types");

            } catch (Exception exc) { //fixme make the error specific
                System.out.println(exc);
                return;
            }

            //do something to connect to control flow

        }
    }

}