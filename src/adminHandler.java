import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;

public class adminHandler implements Runnable{
    final ObjectOutputStream op;
    final ObjectInputStream ip;
    final Socket s;
    final Connection c;
    boolean flag = false;
    propclientOwner p;
    prop po;
    static int clientID = 1;
    static int ownerID = 1;
    static int propID = 1;
    String query;
    PreparedStatement st;
    Statement cst;
    ResultSet rs;
    ArrayList<propclientOwner> arrclientowner;
    ArrayList<prop> arrprop;
    ArrayList<Object> msg;
    String m;
    String cname;
    int poid;

    public adminHandler (Socket ss, ObjectInputStream oip, ObjectOutputStream oop, Connection conn){
        this.s = ss;
        this.op = oop;
        this.ip = oip;
        this.c = conn;
    }

    @Override
    public void run() {
        do {
            int ch = 0;
            try {
                ch = (int)ip.readObject();
            } catch (IOException | ClassNotFoundException e) {
                break;
            }

            switch (ch){
                case 1:
                    try {
                        p = (propclientOwner) ip.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    query = "insert into client values (?,?,?,?)";
                    try {
                        st = c.prepareStatement(query);
                        st.setInt(1,clientID);
                        st.setString(2,p.name);
                        st.setString(3,p.phoneNo);
                        st.setString(4,p.address);

                        st.executeUpdate();

                        System.out.println("New client Added...");
                        clientID++;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        p = (propclientOwner) ip.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    query = "insert into owner values (?,?,?,?)";
                    try {
                        st = c.prepareStatement(query);
                        st.setInt(1,ownerID);
                        st.setString(2,p.name);
                        st.setString(3,p.phoneNo);
                        st.setString(4,p.address);

                        st.executeUpdate();

                        System.out.println("New Owner Added...");
                        ownerID++;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 3:
                    po = new prop();
                    try {
                        po = (prop) ip.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    query = "insert into prop values (?,?,?,?,?,?,?,?,?)";
                    try {
                        st = c.prepareStatement(query);
                        st.setInt(1,propID);
                        st.setInt(2,po.ownerId);
                        st.setString(3,po.type);
                        st.setLong(4,po.sqrfeet);
                        st.setLong(5,po.price);
                        st.setInt(6,po.age);
                        st.setString(7,po.address);
                        st.setString(8,po.area);
                        st.setString(9,po.status);

                        st.executeUpdate();

                        System.out.println("New Property Added...");
                        propID++;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        po = (prop) ip.readObject();
                        query = "update prop set ownerid=?,proptype=?,propsqrfeet=?,propprice=?,propage=?,propadd=?,proparea=?,propstatus=? where propid=?";
                        st = c.prepareStatement(query);
                        st.setInt(1,po.ownerId);
                        st.setString(2,po.type);
                        st.setLong(3,po.sqrfeet);
                        st.setLong(4,po.price);
                        st.setInt(5,po.age);
                        st.setString(6,po.address);
                        st.setString(7,po.area);
                        st.setString(8,po.status);
                        st.setInt(9,po.id);
                        st.executeUpdate();
                    } catch (IOException | ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Property "+ poid + " is updated");
                    break;
                case 5:
                    try {
                        String n = (String) ip.readObject();
                        query = "select msg,status from messages where cli='"+n+"'";
                        cst = c.createStatement();
                        rs = cst.executeQuery(query);
                        msg = new ArrayList<>();
                        while (rs.next()){
                            msg.add(rs.getInt(2));
                            msg.add(rs.getString(1));
                        }
                        op.reset();
                        op.writeObject(msg);
                    } catch (SQLException | IOException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 6:
                    query = "select * from client";
                    try {
                        cst = c.createStatement();
                        rs = cst.executeQuery(query);
                        arrclientowner = new ArrayList<>();

                        while (rs.next()) {
                            p = new propclientOwner();
                            p.id = rs.getInt(1);
                            p.name = rs.getString(2);
                            p.phoneNo = rs.getString(3);
                            p.address = rs.getString(4);
                            arrclientowner.add(p);
                        }
                        op.reset();
                        op.writeObject(arrclientowner);
                    } catch (SQLException | IOException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 7:
                    query = "select * from owner";
                    try {
                        cst = c.createStatement();
                        rs = cst.executeQuery(query);
                        arrclientowner = new ArrayList<>();

                        while (rs.next()) {
                            p = new propclientOwner();
                            p.id = rs.getInt(1);
                            p.name = rs.getString(2);
                            p.phoneNo = rs.getString(3);
                            p.address = rs.getString(4);
                            arrclientowner.add(p);
                        }
                        op.reset();
                        op.writeObject(arrclientowner);
                    } catch (SQLException | IOException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 8:
                    query = "select * from prop";
                    try {
                        cst = c.createStatement();
                        rs = cst.executeQuery(query);
                        arrprop = new ArrayList<>();

                        while (rs.next()) {
                            po = new prop();
                            po.id = rs.getInt(1);
                            po.ownerId =rs.getInt(2);
                            po.type = rs.getString(3);
                            po.sqrfeet = rs.getLong(4);
                            po.price = rs.getLong(5);
                            po.age = rs.getInt(6);
                            po.address = rs.getString(7);
                            po.area = rs.getString(8);
                            po.status = rs.getString(9);
                            arrprop.add(po);
                        }
                        op.reset();
                        op.writeObject(arrprop);
                    } catch (SQLException | IOException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case 9:
                    try {
                        m = (String) ip.readObject();
                        cname = (String) ip.readObject();
                        System.out.println(m + " "+ cname);
                        query = "insert into messages values (?,?,?)";
                        st = c.prepareStatement(query);
                        st.setString(1, cname);
                        st.setString(2, m);
                        st.setInt(3, 0);

                        st.executeUpdate();

                        System.out.println("Message sent from admin to "+cname);
                    } catch (IOException | ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    try {
                        s.close();
                        ip.close();
                        op.close();
                        c.close();
                        System.out.println("Closing connection");
                    } catch (IOException | SQLException e) {
                        break;
                    }
                    flag = true;
                    break;
                default:
                    break;
            }
        }while (!flag);

        System.out.println("Code End");
    }
}