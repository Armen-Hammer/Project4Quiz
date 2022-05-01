import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CreateCourseScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel nameLabel = new JLabel("Course Name");
    JLabel numberLabel = new JLabel("Course Number");
    JTextField courseNameField = new JTextField();
    JTextField numberField = new JTextField();
    JButton makeCourseButton = new JButton("Create Course");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public CreateCourseScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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
        numberLabel.setBounds(50, 220, 100, 30);
        courseNameField.setBounds(150, 150, 150, 30);
        numberField.setBounds(150, 220, 150, 30);
        makeCourseButton.setBounds(150, 290, 150, 30);
    }

    public void addComponentsToContainer() {
        container.add(nameLabel);
        container.add(numberLabel);
        container.add(courseNameField);
        container.add(numberField);
        container.add(makeCourseButton);
    }

    public void addActionEvent() {
        makeCourseButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == makeCourseButton) {

            try {
                int courseNumber = Integer.parseInt(numberField.getText());
                String courseName = courseNameField.getText();
                if (courseName.length() == 0) {
                    JOptionPane.showMessageDialog(this, "The course name must be longer than 1 character");
                    return;
                }
                Course c = new Course(courseName, courseNumber);
                oos.writeObject(Server.MAKE_COURSE);
                oos.writeObject(c);
                String response = (String) ois.readObject();
                JOptionPane.showMessageDialog(this, response);
                this.dispose();
                TeacherBaseView newFrame = new TeacherBaseView(socket, pw, oos, ois);
                Utils.makeFrameFromTemplate(newFrame, "Home");

            } catch (Exception exc) { //fixme make the error specific
                System.out.println(exc);
                JOptionPane.showMessageDialog(this, "The course number must be an integer"); //fixme
                return;
            }

            //do something to connect to control flow

        }
    }

}