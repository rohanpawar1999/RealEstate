import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/building";
        String uname = "root";
        String pass = "Rsp1999@!";
        Connection con = DriverManager.getConnection(url,uname,pass);

        int port = 1234;
        ServerSocket serverSocket = new ServerSocket(port);

        ExecutorService service = Executors.newCachedThreadPool();

        while(true) {
            Socket s = null;
            s = serverSocket.accept();

            ObjectInputStream ip = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream op = new ObjectOutputStream(s.getOutputStream());

            int privilage = (int)ip.readObject();

            if(privilage == 0) {
                System.out.println("Admin logged in");
                service.execute(new adminHandler(s,ip,op,con));
            }
            else {
                System.out.println("Guest logged in");
                service.execute(new clientHandler(s,ip,op,con));
            }
        }
    }
}

