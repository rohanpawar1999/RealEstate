import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class admincli extends JFrame implements ActionListener {
    Socket s;
    ObjectInputStream ip;
    ObjectOutputStream op;
    Container c;
    JFrame f = this;
    LayoutManager tablelay;
    JMenuBar menu;
    JMenu client,owner,property,message,exit;
    JMenuItem addcli,addow,addprop,logout,edit,chatbox;
    JMenuItem showcli,showow,showprop;
    JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10;
    JTextField t1,t2,t3,t4;
    JTextArea ta1;
    JButton b1,b2;
    JRadioButton r1,r2,r3,r4,r5,r6;
    JComboBox cb1,cb2;
    ButtonGroup bg1,bg2;
    DefaultTableModel model;
    JTable table;
    ArrayList<propclientOwner> arrclientowner;
    ArrayList<prop> arrprop;
    ArrayList<Object> msg;
    prop po;
    Font font = new Font("Arial",Font.BOLD,12);

    admincli(Socket s, ObjectOutputStream op, ObjectInputStream ip) throws Exception {

        this.s = s;
        this.op = op;
        this.ip = ip;
        op.reset();
        op.writeObject(0);

        setTitle("Admin");
        setSize(900,600);
        setLocation(700,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        c =  getContentPane();
        tablelay = c.getLayout();
        c.setLayout(null);

        menu = new JMenuBar();
        client = new JMenu("Client");
        addcli = new JMenuItem("Add Client");
        showcli = new JMenuItem("Show Clients");
        client.add(addcli);
        client.add(showcli);
        addcli.addActionListener(this);
        showcli.addActionListener(this);

        owner = new JMenu("Owner");
        addow = new JMenuItem("Add Owner");
        showow = new JMenuItem("Show Owners");
        owner.add(addow);
        owner.add(showow);
        addow.addActionListener(this);
        showow.addActionListener(this);

        property = new JMenu("Property");
        addprop = new JMenuItem("Add property");
        showprop = new JMenuItem("Show property");
        edit = new JMenuItem("Edit property");
        property.add(addprop);
        property.add(showprop);
        property.add(edit);
        addprop.addActionListener(this);
        showprop.addActionListener(this);
        edit.addActionListener(this);

        message = new JMenu("Messages");
        chatbox = new JMenuItem("Chatbox");
        message.add(chatbox);
        chatbox.addActionListener(this);

        exit = new JMenu("Exit");
        logout = new JMenuItem("Log out");
        exit.add(logout);
        logout.addActionListener(this);

        menu.add(client);
        menu.add(owner);
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
        if(e.getSource()==addcli){
            l1 = new JLabel("Name ");
            l1.setBounds(250,50,150,20);
            t1 = new JTextField();
            t1.setBounds(350,50,150,20);
            l2 = new JLabel("Mob. No. ");
            l2.setBounds(250,100,100,20);
            t2 = new JTextField();
            t2.setBounds(350,100,150,20);
            l3 = new JLabel("Address ");
            l3.setBounds(250,150,100,20);
            ta1 = new JTextArea();
            ta1.setBounds(350,150,400,200);
            b1 = new JButton("Add");
            b1.setBounds(400,400,100,20);
            l4 = new JLabel("");
            l4.setFont(font);
            l4.setForeground(Color.RED);
            l4.setBounds(350,450,300,30);


            c.add(l1);c.add(t1);c.add(l2);c.add(t2);c.add(l3);c.add(ta1);c.add(b1);c.add(l4);
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        if(t1.getText().equals("") || t2.getText().equals("") || ta1.getText().equals("")) {
                            JOptionPane.showMessageDialog(f,"Fill Complete Information","Message",JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            op.reset();
                            op.writeObject(1);
                            propclientOwner p = new propclientOwner();
                            p.id = 0;
                            p.name = t1.getText();
                            p.phoneNo = t2.getText();
                            p.address = ta1.getText();
                            op.reset();
                            op.writeObject(p);

                            Timer t = new Timer(50, new ActionListener() {
                                int i=0;
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    if(i==0){
                                        l4.setText("Client added successfully");
                                    }
                                    else if(i==20){
                                        l4.setText("");
                                    }
                                    i++;

                                }
                            });
                            t.start();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
        }
        else if (e.getSource()==showcli) {
            try {
                op.reset();
                op.writeObject(6);
                arrclientowner = (ArrayList) ip.readObject();
                Object[][] data = {};
                String[] columnNames = {"ID","Client Name","Phone No.","Address"};
                model = new DefaultTableModel(data,columnNames);
                table =new JTable(model);
                for(propclientOwner i : arrclientowner){
                    int id = i.id;
                    String name = i.name;
                    String phone = i.phoneNo;
                    String addr = i.address;
                    Object[] newRow = {id,name,phone,addr};
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
        else if (e.getSource()==addow){
            l1 = new JLabel("Name ");
            l1.setBounds(250,50,150,20);
            t1 = new JTextField();
            t1.setBounds(350,50,150,20);
            l2 = new JLabel("Mob. No. ");
            l2.setBounds(250,100,100,20);
            t2 = new JTextField();
            t2.setBounds(350,100,150,20);
            l3 = new JLabel("Address ");
            l3.setBounds(250,150,100,20);
            ta1 = new JTextArea();
            ta1.setBounds(350,150,400,200);
            b1 = new JButton("Add");
            b1.setBounds(400,400,100,20);
            l4 = new JLabel("");
            l4.setFont(font);
            l4.setForeground(Color.RED);
            l4.setBounds(350,450,300,30);

            c.add(l1);c.add(t1);c.add(l2);c.add(t2);c.add(l3);c.add(ta1);c.add(b1);c.add(l4);
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        if(t1.getText().equals("") || t2.getText().equals("") || ta1.getText().equals("")) {
                            JOptionPane.showMessageDialog(f,"Fill Complete Information","Message",JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            op.reset();
                            op.writeObject(2);
                            propclientOwner p = new propclientOwner();
                            p.id = 0;
                            p.name = t1.getText();
                            p.phoneNo = t2.getText();
                            p.address = ta1.getText();
                            op.reset();
                            op.writeObject(p);

                            Timer t = new Timer(50, new ActionListener() {
                                int i=0;
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    if(i==0){
                                        l4.setText("Owner added successfully");
                                    }
                                    else if(i==20){
                                        l4.setText("");
                                    }
                                    i++;

                                }
                            });
                            t.start();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
        }
        else if (e.getSource()==showow) {
            try {
                op.reset();
                op.writeObject(7);
                arrclientowner = (ArrayList) ip.readObject();
                Object[][] data = {};
                String[] columnNames = {"ID","Owner Name","Phone No.","Address"};
                model = new DefaultTableModel(data,columnNames);
                table =new JTable(model);
                for(propclientOwner i : arrclientowner){
                    int id = i.id;
                    String name = i.name;
                    String phone = i.phoneNo;
                    String addr = i.address;
                    Object[] newRow = {id,name,phone,addr};
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
        else if(e.getSource()==addprop){
            try {
                op.reset();
                op.writeObject(7);
                arrclientowner = (ArrayList) ip.readObject();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
            String[] owid = {};
            cb1 = new JComboBox(owid);
            for(propclientOwner i : arrclientowner){
                cb1.addItem(i.id);
            }

            l1 = new JLabel("Owner ID");
            l1.setBounds(300,50,70,20);
            cb1.setBounds(400,50,70,30);

            l2 = new JLabel("Property Type : ");
            l2.setBounds(300,100,150,30);
            r1 = new JRadioButton("Commercial");
            r1.setBounds(150,150,120,20);
            r2 = new JRadioButton("Residential");
            r2.setBounds(270,150,120,20);
            r3 = new JRadioButton("Industrial");
            r3.setBounds(420,150,100,20);
            r4 = new JRadioButton("Land");
            r4.setBounds(570,150,80,20);
            bg1 = new ButtonGroup();
            bg1.add(r1);bg1.add(r2);bg1.add(r3);bg1.add(r4);
            l3 = new JLabel("Area(in sqrfeet)");
            l3.setBounds(100,200,120,20);
            t1 = new JTextField();
            t1.setBounds(230,200,100,30);
            l4 = new JLabel("Price");
            l4.setBounds(350,200,80,20);
            t2 = new JTextField();
            t2.setBounds(430,200,100,30);
            l5 = new JLabel("Age(in yrs)");
            l5.setBounds(540,200,80,20);
            t3 = new JTextField();
            t3.setBounds(630,200,100,30);
            l6 = new JLabel("Deal type");
            l6.setBounds(200,250,100,20);
            r5 = new JRadioButton("Sell");
            r6 = new JRadioButton("Rent");
            bg2 = new ButtonGroup();
            bg2.add(r5);bg2.add(r6);
            r5.setBounds(350,250,80,30);
            r6.setBounds(450,250,80,30);

            l7 = new JLabel("Address");
            l7.setBounds(100,300,80,30);
            ta1 = new JTextArea();
            ta1.setBounds(200,300,220,100);
            l8 = new JLabel("Location");
            l8.setBounds(450,300,100,20);
            t4 = new JTextField();
            t4.setBounds(550,300,100,30);
            b1 = new JButton("Add");
            b1.setBounds(370,450,80,30);
            l9 = new JLabel("");
            l9.setFont(font);
            l9.setForeground(Color.RED);
            l9.setBounds(350,490,300,30);


            c.add(l1);c.add(cb1);c.add(l2);c.add(r1);c.add(r2);c.add(r3);c.add(r4);
            c.add(l3);c.add(t1);c.add(l4);c.add(t2);c.add(l5);c.add(t3);
            c.add(l6);c.add(r5);c.add(r6);
            c.add(l7);c.add(ta1);c.add(l8);c.add(t4);c.add(b1);c.add(l9);

            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        if (t1.getText().equals("") || t2.getText().equals("") || t3.getText().equals("") || t4.getText().equals("") || ta1.getText().equals("")) {
                            JOptionPane.showMessageDialog(f, "Fill Complete Information", "Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            prop po = new prop();
                            po.id = 0;
                            po.ownerId = arrclientowner.get(cb1.getSelectedIndex()).id;
                            if (r1.isSelected())
                                po.type = "Commercial";
                            else if (r2.isSelected())
                                po.type = "Residential";
                            else if (r3.isSelected())
                                po.type = "Industrial";
                            else
                                po.type = "Land";
                            po.sqrfeet = Long.parseLong(t1.getText());
                            po.price = Long.parseLong(t2.getText());
                            po.age = Integer.parseInt(t3.getText());
                            if (r5.isSelected())
                                po.status = "Sell";
                            else
                                po.status = "Rent";
                            po.address = ta1.getText();
                            po.area = t4.getText();

                            op.reset();
                            op.writeObject(3);
                            op.reset();
                            op.writeObject(po);

                            Timer t = new Timer(50, new ActionListener() {
                                int i=0;
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    if(i==0){
                                        l9.setText("Property added successfully");
                                    }
                                    else if(i==20){
                                        l9.setText("");
                                    }
                                    i++;
                                }
                            });
                            t.start();
                        }
                    } catch(IOException ioException){
                        ioException.printStackTrace();
                    }
                }
            });

        }
        else if (e.getSource()==showprop){
            try {
                op.reset();
                op.writeObject(8);
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
        else if (e.getSource()==edit){
            try {
                op.reset();
                op.writeObject(8);
                arrprop = (ArrayList) ip.readObject();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
            String[] owid = {};
            String[] propid = {};
            cb1 = new JComboBox(owid);
            cb2 = new JComboBox(propid);
            for(prop i : arrprop){
                cb1.addItem(i.ownerId);
                cb2.addItem(i.id);
            }

            l1 = new JLabel("Owner ID");
            l1.setBounds(400,50,100,20);
            cb1.setBounds(500,50,70,30);
            l10 = new JLabel("Property ID");
            l10.setBounds(100,50,100,20);
            cb2.setBounds(200,50,70,20);

            l2 = new JLabel("Property Type : ");
            l2.setBounds(300,100,150,30);
            r1 = new JRadioButton("Commercial");
            r1.setBounds(150,150,120,20);
            r2 = new JRadioButton("Residential");
            r2.setBounds(270,150,120,20);
            r3 = new JRadioButton("Industrial");
            r3.setBounds(420,150,100,20);
            r4 = new JRadioButton("Land");
            r4.setBounds(570,150,80,20);
            bg1 = new ButtonGroup();
            bg1.add(r1);bg1.add(r2);bg1.add(r3);bg1.add(r4);
            l3 = new JLabel("Area(in sqrfeet)");
            l3.setBounds(100,200,120,20);
            t1 = new JTextField();
            t1.setBounds(230,200,100,30);
            l4 = new JLabel("Price");
            l4.setBounds(350,200,80,20);
            t2 = new JTextField();
            t2.setBounds(430,200,100,30);
            l5 = new JLabel("Age(in yrs)");
            l5.setBounds(540,200,80,20);
            t3 = new JTextField();
            t3.setBounds(630,200,100,30);
            l6 = new JLabel("Deal type");
            l6.setBounds(200,250,100,20);
            r5 = new JRadioButton("Sell");
            r6 = new JRadioButton("Rent");
            bg2 = new ButtonGroup();
            bg2.add(r5);bg2.add(r6);
            r5.setBounds(350,250,80,30);
            r6.setBounds(450,250,80,30);

            l7 = new JLabel("Address");
            l7.setBounds(100,300,80,30);
            ta1 = new JTextArea();
            ta1.setBounds(200,300,220,100);
            l8 = new JLabel("Location");
            l8.setBounds(450,300,100,20);
            t4 = new JTextField();
            t4.setBounds(550,300,100,30);
            b1 = new JButton("Update");
            b1.setBounds(370,450,120,30);
            l9 = new JLabel("");
            l9.setFont(font);
            l9.setForeground(Color.RED);
            l9.setBounds(350,490,300,30);


            c.add(l1);c.add(cb1);c.add(l2);c.add(r1);c.add(r2);c.add(r3);c.add(r4);c.add(l10);c.add(cb2);
            c.add(l3);c.add(t1);c.add(l4);c.add(t2);c.add(l5);c.add(t3);
            c.add(l6);c.add(r5);c.add(r6);
            c.add(l7);c.add(ta1);c.add(l8);c.add(t4);c.add(b1);c.add(l9);

            cb2.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    po = new prop();
                    po = arrprop.get(cb2.getSelectedIndex());
                    cb1.setSelectedItem(po.ownerId);
                    if(po.type.equals("Commercial")){
                        r1.setSelected(true);
                    }
                    else if(po.type.equals("Residential")){
                        r2.setSelected(true);
                    }
                    else if(po.type.equals("Industrial")){
                        r3.setSelected(true);
                    }
                    else if(po.type.equals("Land")){
                        r4.setSelected(true);
                    }
                    t1.setText(po.sqrfeet.toString());
                    t2.setText(po.price.toString());
                    t3.setText(String.valueOf(po.age));
                    if(po.status.equals("Sell")){
                        r5.setSelected(true);
                    }
                    else if(po.status.equals("Rent")){
                        r6.setSelected(true);
                    }
                    ta1.setText(po.address);
                    t4.setText(po.area);
                }
            });

            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        if (t1.getText().equals("") || t2.getText().equals("") || t3.getText().equals("") || t4.getText().equals("") || ta1.getText().equals("")) {
                            JOptionPane.showMessageDialog(f, "Fill Complete Information", "Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            prop po = new prop();
                            po.id = arrprop.get(cb2.getSelectedIndex()).id;
                            po.ownerId = arrprop.get(cb1.getSelectedIndex()).ownerId;
                            if (r1.isSelected())
                                po.type = "Commercial";
                            else if (r2.isSelected())
                                po.type = "Residential";
                            else if (r3.isSelected())
                                po.type = "Industrial";
                            else
                                po.type = "Land";
                            po.sqrfeet = Long.parseLong(t1.getText());
                            po.price = Long.parseLong(t2.getText());
                            po.age = Integer.parseInt(t3.getText());
                            if (r5.isSelected())
                                po.status = "Sell";
                            else
                                po.status = "Rent";
                            po.address = ta1.getText();
                            po.area = t4.getText();

                            op.reset();
                            op.writeObject(4);
                            op.reset();
                            op.writeObject(po);

                            Timer t = new Timer(50, new ActionListener() {
                                int i=0;
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    if(i==0){
                                        l9.setText("Property updated successfully");
                                    }
                                    else if(i==20){
                                        l9.setText("");
                                    }
                                    i++;
                                }
                            });
                            t.start();
                        }
                    } catch(IOException ioException){
                        ioException.printStackTrace();
                    }
                }
            });
        }
        else if (e.getSource()==logout) {
            try {
                op.reset();
                op.writeObject(10);
                s.close();
                op.close();
                ip.close();
                System.exit(1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
        else if (e.getSource()==chatbox) {
            try {
                op.reset();
                op.writeObject(6);
                arrclientowner = (ArrayList) ip.readObject();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
            l1 = new JLabel("Client Name");
            l1.setBounds(70,50,100,20);
            cb1 = new JComboBox();
            cb1.setBounds(170,50,100,30);
            for (propclientOwner i : arrclientowner){
                cb1.addItem(i.name);
            }
            ta1 = new JTextArea();
            ta1.setEditable(false);
            ta1.setBounds(300,50,300,300);

            cb1.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    try {
                        op.reset();
                        op.writeObject(5);
                        op.writeObject(arrclientowner.get(cb1.getSelectedIndex()).name);
                        msg = (ArrayList) ip.readObject();
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < msg.size(); i += 2) {
                            if ((int) msg.get(i) == 0) {
                                s.append("<- ").append(msg.get(i + 1)).append("\n");
                            } else {
                                s.append("-> ").append(msg.get(i + 1)).append("\n");
                            }
                        }
                        ta1.setText(String.valueOf(s));
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }

                }
            });

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
                        op.writeObject(9);
                        op.writeObject(t1.getText());
                        op.writeObject(arrclientowner.get(cb1.getSelectedIndex()).name);
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
                        op.writeObject(5);
                        op.writeObject(arrclientowner.get(cb1.getSelectedIndex()).name);
                        msg = (ArrayList) ip.readObject();
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < msg.size(); i += 2) {
                            if ((int) msg.get(i) == 0) {
                                s.append("<- ").append(msg.get(i + 1)).append("\n");
                            } else {
                                s.append("-> ").append(msg.get(i + 1)).append("\n");
                            }
                        }
                        ta1.setText(String.valueOf(s));
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            c.add(l1);c.add(cb1);c.add(ta1);c.add(l2);c.add(t1);c.add(b1);c.add(b2);
        }
    }
}