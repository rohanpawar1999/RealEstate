import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;

public class clientHandler implements Runnable {
    final ObjectOutputStream op;
    final ObjectInputStream ip;
    final Socket s;
    final Connection c;
    boolean flag = false;
    String username;
    propclientOwner p;
    prop po;
    String query;
    PreparedStatement st;
    Statement cst;
    ResultSet rs;
    ArrayList<prop> arrprop;
    ArrayList<Object> msg;
    String area;
    String cname;
    String m;

    public clientHandler (Socket ss, ObjectInputStream oip, ObjectOutputStream oop, Connection conn){
        this.s = ss;
        this.op = oop;
        this.ip = oip;
        this.c = conn;
    }

    @Override
    public void run() {
        try {
            username = (String) ip.readObject();
            query = "select * from client where cliname='" + username + "'";
            cst = c.createStatement();
            rs = cst.executeQuery(query);
            if (rs.next() == false) {
                op.writeObject("Invalid");
                return;
            } else {
                op.writeObject("OK");
                cname = username;
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        if (cname != null) {
            do {
                int ch = 0;
                try {
                    ch = (int) ip.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    break;
                }

                switch (ch) {
                    case 1:
                        try {
                            area = (String) ip.readObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        query = "select * from prop where proparea='" + area + "'";
                        try {
                            cst = c.createStatement();
                            rs = cst.executeQuery(query);
                            arrprop = new ArrayList<>();

                            while (rs.next()) {
                                po = new prop();
                                po.id = rs.getInt(1);
                                po.ownerId = rs.getInt(2);
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
                    case 2:
                        query = "select * from prop";
                        try {
                            cst = c.createStatement();
                            rs = cst.executeQuery(query);
                            arrprop = new ArrayList<>();

                            while (rs.next()) {
                                po = new prop();
                                po.id = rs.getInt(1);
                                po.ownerId = rs.getInt(2);
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
                    case 3:
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
                    case 4:
                        try {
                            m = (String) ip.readObject();
                            query = "insert into messages values (?,?,?)";
                            st = c.prepareStatement(query);
                            st.setString(1, cname);
                            st.setString(2, m);
                            st.setInt(3, 1);

                            st.executeUpdate();

                            System.out.println("Message sent from "+cname+" to admin");
                        } catch (IOException | ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            s.close();
                            ip.close();
                            op.close();
                            c.close();
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                        }
                        flag = true;
                        break;
                    default:
                        break;
                }
            } while (!flag);
        }
    }
}
