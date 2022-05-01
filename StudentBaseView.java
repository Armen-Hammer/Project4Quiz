import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class StudentBaseView extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel prompt = new JLabel("WHAT DO YOU WANT TO DO?");
    JButton enterCourseButton = new JButton("ENTER A COURSE");
    JButton resetPassButton = new JButton("RESET PASSWORD");
    JButton resetNameButton = new JButton("RESET USERNAME");
    JButton logoutButton = new JButton("LOGOUT");
    JButton delAccountButton = new JButton("DELETE ACCOUNT");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public StudentBaseView(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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
        prompt.setBounds(100, 150, 250, 30);
        enterCourseButton.setBounds(25, 220, 150, 30);
        resetNameButton.setBounds(175, 220, 150, 30);
        resetPassButton.setBounds(25, 290, 150, 30);
        logoutButton.setBounds(175, 290, 150, 30);
        delAccountButton.setBounds(25, 360, 175, 30);
    }

    public void addComponentsToContainer() {
        container.add(prompt);
        container.add(enterCourseButton);
        container.add(resetNameButton);
        container.add(resetPassButton);
        container.add(logoutButton);
        container.add(delAccountButton);
    }

    public void addActionEvent() {
        enterCourseButton.addActionListener(this);
        resetNameButton.addActionListener(this);
        resetPassButton.addActionListener(this);
        logoutButton.addActionListener(this);
        delAccountButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == delAccountButton) {
                oos.writeObject(Server.DELETE_ACCOUNT);
                oos.flush();
                String response = (String) ois.readObject();
                if (response.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    LoginScreen newFrame = new LoginScreen(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Login");
                }
            } else if (e.getSource() == resetNameButton) {
                ResetUsernameScreen currentScreen = new ResetUsernameScreen(socket, pw, oos, ois, this);
                Utils.makeFrameFromTemplate(currentScreen, "Edit username");
            } else if (e.getSource() == resetPassButton) {
                ResetPasswordScreen currentScreen = new ResetPasswordScreen(socket, pw, oos, ois, this);
                Utils.makeFrameFromTemplate(currentScreen, "Edit password");
            } else if (e.getSource() == logoutButton) {
                oos.writeObject(Server.LOGOUT);
                JOptionPane.showMessageDialog(this, ois.readObject());
                this.dispose();
                LoginScreen currentScreen = new LoginScreen(socket, pw, oos, ois);
                Utils.makeFrameFromTemplate(currentScreen, "Login");
            } else if (e.getSource() == enterCourseButton) {
                oos.writeObject(Server.GET_COURSES_STR);
                String response = (String) ois.readObject();
                if (response.equals("Success")) {
                    ArrayList < String > arr = (ArrayList < String > ) ois.readObject();
                    this.dispose();
                    ShowCoursesDropdownScreen currentScreen = new ShowCoursesDropdownScreen(socket, pw, oos, ois, arr, Server.ENTER_COURSE, this);
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