import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class loginFrame extends JFrame {
    loginFrame(){
        setTitle("Login Form");
        setSize(700,450);
        setLocation(700,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container c =  getContentPane();
        c.setLayout(null);
        JLabel label1 = new JLabel("Username ");
        label1.setBounds(200,100,200,20);
        JLabel label2 = new JLabel("Password ");
        label2.setBounds(200,150,200,20);

        JTextField user = new JTextField();
        user.setBounds(400,100,200,20);
        JPasswordField pass = new JPasswordField();
        pass.setBounds(400,150,200,20);

        JRadioButton r1 = new JRadioButton("Admin");
        r1.setBounds(350,230,80,20);
        JRadioButton r2 = new JRadioButton("Client");
        r2.setBounds(450,230,80,20);
        ButtonGroup b = new ButtonGroup();
        b.add(r1);
        b.add(r2);

        JButton login = new JButton("Login");
        login.setBounds(350,300,80,20);
        JButton exit = new JButton("Exit");
        exit.setBounds(450,300,80,20);

        ImageIcon ic = new ImageIcon(new ImageIcon("./Loginimage.png").getImage().getScaledInstance(200,200,Image.SCALE_DEFAULT));
        JLabel label3 = new JLabel();
        label3.setIcon(ic);
        label3.setBounds(0,50,200,200);
        JLabel label4 = new JLabel();
        label4.setBounds(380,180,200,200);
        Font f = new Font("Arial",Font.BOLD,12);
        label4.setFont(f);
        label4.setForeground(Color.RED);

        JProgressBar pb = new JProgressBar(0,20);
        pb.setBounds(365,330,150,20);
        pb.setValue(0);
        pb.setForeground(Color.GREEN);
        pb.setBorderPainted(true);

        c.add(label1);
        c.add(label2);
        c.add(label3);
        c.add(label4);
        c.add(user);
        c.add(pass);
        c.add(r1);
        c.add(r2);
        c.add(login);
        c.add(exit);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                c.remove(pb);
                label4.setText("");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(r1.isSelected()){
                    if(user.getText().equals("admin") && pass.getText().equals("1234")) {
                        c.add(pb);
                        Timer t = new Timer(50, new ActionListener() {
                            int i=0;
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                if(i==20){
                                    Socket s = null;
                                    try {
                                        s = new Socket("localhost",1234);
                                        ObjectOutputStream op = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ip = new ObjectInputStream(s.getInputStream());
                                        new admincli(s,op,ip);
                                        dispose();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                i++;
                                pb.setValue(i);
                            }
                        });
                        t.start();
                    }
                    else{
                        label4.setText("Invalid Credentials");
                    }
                }
                else {
                    c.add(pb);
                    Timer t = new Timer(50, new ActionListener() {
                        int i=0;
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            if(i==20){
                                Socket s = null;
                                try {
                                    s = new Socket("localhost",1234);
                                    ObjectOutputStream op = new ObjectOutputStream(s.getOutputStream());
                                    ObjectInputStream ip = new ObjectInputStream(s.getInputStream());
                                    op.reset();
                                    op.writeObject(1);
                                    op.writeObject(user.getText());
                                    String usrstat = (String) ip.readObject();
                                    if(usrstat.equals("OK")) {
                                        new clientcli(s,op,ip,user.getText().toString());
                                        dispose();
                                    }
                                    else {
                                        s.close();
                                        ip.close();
                                        op.close();
                                        label4.setText("Invalid Credentials");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            i++;
                            pb.setValue(i);
                        }
                    });
                    t.start();
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(1);
            }
        });

        setVisible(true);
        setResizable(false);
    }
}

public class user {
    public static void main(String[] args) throws Exception{
        loginFrame loginFrame = new loginFrame();
    }
}