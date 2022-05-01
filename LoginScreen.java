import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class LoginScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JButton signUpButton = new JButton("SIGN UP (TEACHER)");
    JButton signUpAsStudent = new JButton("SIGN UP (STUDENT)");


    JCheckBox showPassword = new JCheckBox("Show Password");

    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public LoginScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois) {
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
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
        signUpButton.setBounds(100, 350, 175, 30);
        signUpAsStudent.setBounds(100, 400, 175, 30);
    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
        container.add(signUpButton);
        container.add(signUpAsStudent);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        signUpButton.addActionListener(this);
        signUpAsStudent.addActionListener(this);
        showPassword.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        try {
            if (e.getSource() == loginButton) {
                User user = new User(userTextField.getText(), passwordField.getText());
                oos.writeObject(Server.SIGN_IN);
                oos.writeObject(user);
                oos.flush();
                String response = (String) ois.readObject();
                if (response.equals("T")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    TeacherBaseView newFrame = new TeacherBaseView(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Home");
                } else if (response.equals("S")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    StudentBaseView newFrame = new StudentBaseView(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Home");
                } else {
                    JOptionPane.showMessageDialog(this, response);
                }
            }
            //Coding Part of RESET button
            if (e.getSource() == resetButton) {
                userTextField.setText("");
                passwordField.setText("");
            }
            //Coding Part of showPassword JCheckBox
            if (e.getSource() == showPassword) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            } else if (e.getSource() == signUpAsStudent) { //make a student
                User user = new User(userTextField.getText(), passwordField.getText());
                oos.writeObject(Server.CREATE_STUDENT);
                oos.writeObject(user);
                oos.flush();
                String response = (String) ois.readObject();
                if (response.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    StudentBaseView newFrame = new StudentBaseView(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Home");
                } else {
                    JOptionPane.showMessageDialog(this, response);
                }

            } else if (e.getSource() == signUpButton) { //make a teacher
                User user = new User(userTextField.getText(), passwordField.getText());
                oos.writeObject(Server.CREATE_TEACHER);
                oos.writeObject(user);
                oos.flush();
                String response = (String) ois.readObject();
                if (response.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    TeacherBaseView newFrame = new TeacherBaseView(socket, pw, oos, ois);
                    Utils.makeFrameFromTemplate(newFrame, "Home");
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