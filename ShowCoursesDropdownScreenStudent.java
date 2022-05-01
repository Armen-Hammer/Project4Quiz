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

public class ShowCoursesDropdownScreenStudent extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel questionLabel = new JLabel("What course do you want to enter");


    JComboBox < String > jComboBox;

    JButton enterCourseButton = new JButton("Select");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int action;
    JFrame calledFrom;

    public ShowCoursesDropdownScreenStudent(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, ArrayList < String > courses, int action, JFrame calledFrom) {
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
                oos.writeObject(Server.GET_COURSES_STR);
                String response = null;
                response = (String) ois.readObject();
                if (response.equals("Success")) {
                    ArrayList < String > arr = (ArrayList < String > ) ois.readObject();
                    this.dispose();
                    ShowCoursesDropdownScreen currentScreen = new ShowCoursesDropdownScreen(socket, pw, oos, ois, arr, Server.EDIT_COURSE, this);
                    Utils.makeFrameFromTemplate(currentScreen, "Courses");
                } else {
                    JOptionPane.showMessageDialog(this, response);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}