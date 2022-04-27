import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ResetPasswordScreen extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField passwordTextField = new JTextField();
    JButton submitButton = new JButton("SUBMIT");
    JButton cancelButton = new JButton("CANCEL");


    Socket socket;
    PrintWriter pw;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    JFrame calledFrom;

    public ResetPasswordScreen(Socket socket, PrintWriter pw, ObjectOutputStream oos, ObjectInputStream ois, JFrame calledFrom) {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        this.socket = socket;
        this.calledFrom = calledFrom;

        this.pw = pw;
        this.oos = oos;
        this.ois = ois;

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        passwordLabel.setBounds(50, 150, 100, 30);
        passwordTextField.setBounds(150, 150, 150, 30);
        submitButton.setBounds(50, 300, 100, 30);
        cancelButton.setBounds(200, 300, 100, 30);
    }

    public void addComponentsToContainer() {
        container.add(passwordLabel);
        container.add(passwordTextField);
        container.add(submitButton);
        container.add(cancelButton);
    }

    public void addActionEvent() {
        submitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == submitButton) {
            try {
                oos.writeObject(Server.EDIT_USERNAME);
                oos.writeObject(passwordTextField.getText());
                oos.flush();
                String response = (String) ois.readObject();
                if(response.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Success");
                    this.dispose();
                    calledFrom.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }



    }

}

