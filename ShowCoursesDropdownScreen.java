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

public class ShowCoursesDropdownScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("What course do you want to choose");


    JComboBox < String > jComboBox;

    JButton enterCourseButton = new JButton("Select");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int action;
    JFrame calledFrom;
    public ShowCoursesDropdownScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, ArrayList < String > courses, int action, JFrame calledFrom) {
        jComboBox = new JComboBox < > (courses.toArray(new String[courses.size()]));

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

        enterCourseButton.setBounds(50, 240, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(jComboBox);
        container.add(enterCourseButton);
        container.add(questionLabel);
    }

    public void addActionEvent() {
        enterCourseButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == enterCourseButton) {
                //get course edit options
                switch (action) {
                    case Server.DELETE_COURSE:
                        System.out.println("We here");
                        oos.writeObject(action);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        JOptionPane.showMessageDialog(this, ois.readObject());
                        this.dispose();
                        calledFrom.setVisible(true);
                        break;
                    case Server.EDIT_COURSE:
                        this.dispose();
                        oos.writeObject(Server.SET_CURRENT_COURSE);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        CourseEditsOptionScreen currentScreen = new CourseEditsOptionScreen(socket, pw, oos, ois);
                        Utils.makeFrameFromTemplate(currentScreen, "Course edit options");
                        break;
                    case Server.ENTER_COURSE:
                        this.dispose();
                        oos.writeObject(Server.SET_CURRENT_COURSE);
                        oos.writeObject(jComboBox.getSelectedIndex());
                        StudentInCourseOptions currScreen = new StudentInCourseOptions(socket, pw, oos, ois);
                        Utils.makeFrameFromTemplate(currScreen, "Course action options");
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