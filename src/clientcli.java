import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class clientcli extends JFrame implements ActionListener {
    Socket s;
    ObjectInputStream ip;
    ObjectOutputStream op;
    String cname;
    Container c;
    LayoutManager tablelay;
    JMenuBar menu;
    JMenu property,message,exit;
    JMenuItem showall,showsel,chatbox,logout;
    JLabel l1,l2;
    JTextField t1;
    JTextArea ta1;
    JButton b1,b2;
    JScrollPane sp;
    DefaultTableModel model;
    JTable table;
    ArrayList<prop> arrprop;
    ArrayList<Object> msg;

    clientcli(Socket s, ObjectOutputStream op, ObjectInputStream ip, String cname) throws Exception {
        this.s = s;
        this.op = op;
        this.ip = ip;
        this.cname = cname;

        setTitle("Client");
        setSize(900,600);
        setLocation(700,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        c =  getContentPane();
        tablelay = c.getLayout();
        c.setLayout(null);

        menu = new JMenuBar();
        property = new JMenu("Properties");
        showall = new JMenuItem("Show all properties");
        showsel = new JMenuItem("Search");
        property.add(showall);
        property.add(showsel);
        showall.addActionListener(this);
        showsel.addActionListener(this);

        message = new JMenu("Messages");
        chatbox = new JMenuItem("Chatbox");
        message.add(chatbox);
        chatbox.addActionListener(this);

        exit = new JMenu("Exit");
        logout = new JMenuItem("Log out");
        exit.add(logout);
        logout.addActionListener(this);

        menu.add(property);
        menu.add(message);
        menu.add(exit);
        setJMenuBar(menu);

        setVisible(true);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        c.removeAll();
        c.repaint();
        if(e.getSource()==showall) {
            try {
                op.reset();
                op.writeObject(2);
                arrprop = (ArrayList) ip.readObject();
                Object[][] data = {};
                String[] columnNames = {"ID","Owner ID","Type","Sqrfeet","Price","Age","Address","Status"};
                model = new DefaultTableModel(data,columnNames);
                table =new JTable(model);
                for(prop i : arrprop){
                    int id = i.id;
                    int owid = i.ownerId;
                    String type = i.type;
                    Long sqrfeet= i.sqrfeet;
                    Long price = i.price;
                    int age = i.age;
                    String addr = i.address;
                    String sta = i.status;
                    Object[] newRow = {id,owid,type,sqrfeet,price,age,addr,sta};
                    model.addRow(newRow);
                }
                c.setLayout(tablelay);
                c.add(new JScrollPane(table));
                validate();
                c.setLayout(null);
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }
        else if (e.getSource()==showsel) {
            l1 = new JLabel("Location to be searched ");
            l1.setBounds(200,50,200,30);
            t1 = new JTextField();
            t1.setBounds(400,50,150,30);
            b1 = new JButton("Search");
            b1.setBounds(380,100,120,20);
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        op.reset();
                        op.writeObject(1);
                        op.writeObject(t1.getText());
                        arrprop = (ArrayList) ip.readObject();
                        Object[][] data = {};
                        String[] columnNames = {"ID","Owner ID","Type","Sqrfeet","Price","Age","Address","Status"};
                        model = new DefaultTableModel(data,columnNames);
                        table =new JTable(model);
                        table.setBounds(0,150,900,250);
                        for(prop i : arrprop){
                            int id = i.id;
                            int owid = i.ownerId;
                            String type = i.type;
                            Long sqrfeet= i.sqrfeet;
                            Long price = i.price;
                            int age = i.age;
                            String addr = i.address;
                            String sta = i.status;
                            Object[] newRow = {id,owid,type,sqrfeet,price,age,addr,sta};
                            model.addRow(newRow);
                        }
                        Container tc = new Container();
                        tc.setBounds(0,0,900,600);
                        tc.setLayout(tablelay);
                        sp = new JScrollPane(table);
                        sp.setBounds(0,150,900,250);
                        tc.add(sp);
                        c.add(tc);
                        c.setLayout(null);
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            c.add(l1);c.add(t1);c.add(b1);
        }
        else if (e.getSource()==chatbox) {

            ta1 = new JTextArea();
            ta1.setEditable(false);
            ta1.setBounds(300,50,300,300);

            try {
                op.reset();
                op.writeObject(3);
                op.writeObject(cname);
                msg = (ArrayList) ip.readObject();
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < msg.size(); i += 2) {
                    if ((int) msg.get(i) == 0) {
                        s.append("-> ").append(msg.get(i + 1)).append("\n");
                    } else {
                        s.append("<- ").append(msg.get(i + 1)).append("\n");
                    }
                }
                ta1.setText(String.valueOf(s));
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }

            l2 = new JLabel("Type Message ");
            l2.setBounds(250,400,150,20);
            t1 = new JTextField();
            t1.setBounds(420,400,150,30);
            b1 = new JButton("Send");
            b1.setBounds(350,450,100,20);
            b2 = new JButton("Refresh");
            b2.setBounds(470,450,100,20);
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        op.reset();
                        op.writeObject(4);
                        op.writeObject(t1.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            b2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        op.reset();
                        op.writeObject(3);
                        op.writeObject(cname);
                        msg = (ArrayList) ip.readObject();
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < msg.size(); i += 2) {
                            if ((int) msg.get(i) == 0) {
                                s.append("-> ").append(msg.get(i + 1)).append("\n");
                            } else {
                                s.append("<- ").append(msg.get(i + 1)).append("\n");
                            }
                        }
                        ta1.setText(String.valueOf(s));
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            c.add(ta1);c.add(l2);c.add(t1);c.add(b1);c.add(b2);
        }
    }
}