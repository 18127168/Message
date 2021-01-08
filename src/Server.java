import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class Server {
    private int port;
    static Connection connection = null;
    static Statement stmt = null;
    static String CreateSql = null;
    public static ArrayList<String> listOnlineUser;
    public static HashMap <String, Socket> listSK;

    public  Server(int port) {
        this.port = port;  
        Connect();
    }

    protected void finalize() throws SQLException {
        stmt.close();
        connection.close();
    }

    public static void Connect() {
        String url = "jdbc:postgresql://ec2-54-156-73-147.compute-1.amazonaws.com:5432/d3jin8vk8l0oce";
        String user = "tcwqysuhuqrrmj";
        String password = "0aaa2e2c622797132360debb3dd3a9ea40be6805e3d8eb6276c282e7ba6beeb3";

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("CONNECTED");
            
            stmt = connection.createStatement();
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Boolean Login(String account, String password){
        try {
            ResultSet rs = stmt.executeQuery("SELECT password FROM users where users.username = '" + account + "'"); if (rs.next()){};
            if (rs.getString("password").equals(password)){
                return true;
            } else {    return false;   }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Boolean Register(String account, String password){
        try {
            stmt.executeUpdate("insert into users values('" + account  + "','" + password +"');");
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        // writeServer write = new writeServer();
        // write.start();
        System.out.println("Server is listening... ");
        while(true) {
            Socket socket = server.accept();
            System.out.println("Connected with " + socket);
            readServer read = new readServer(socket);
            read.start();
        }
    }

    public static void main(String[] args) throws IOException{
        Server.listOnlineUser = new ArrayList<>();
        Server.listSK = new HashMap<String, Socket>();

        Server server = new Server(5000);
        server.execute();
    }
}

class readServer extends Thread{
    private Socket client;
    private String user;

    public readServer(Socket client){
        this.client = client;
    }

    public void sendConnectedPacket(){
        String mess = "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + "connect" + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@";
        DataOutputStream dos = null;

        for (String user : Server.listOnlineUser) { mess = mess + "&" + user; } 
        try {
            for (String user : Server.listOnlineUser) {
                Socket item = Server.listSK.get(user);
                dos = new DataOutputStream(item.getOutputStream());
                dos.writeUTF(mess);
            }
            System.out.println(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessagetoEnemy(String enemy, String content){
        DataOutputStream dos = null;
    
        String mess = "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + "message" + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + user + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + content;
        try {
            Socket item = Server.listSK.get(enemy);
            dos = new DataOutputStream(item.getOutputStream());
            dos.writeUTF(mess);
            
            System.out.println(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                String mess = dis.readUTF();
                String[] splitMess = mess.split("@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@");
                
                if (splitMess[1].equals("connect")){     
                    sendConnectedPacket();
                }
                else if (splitMess[1].equals("message")){
                    sendMessagetoEnemy(splitMess[2], splitMess[3]);
                }
                else {
                    if (splitMess[1].equals("login")){
                        if( Server.Login(splitMess[2], splitMess[3])){
                            mess = "Login accept";
                            user = splitMess[2];
                            Server.listOnlineUser.add(splitMess[2]);
                            Server.listSK.put(splitMess[2], client);
                        }else {
                            mess = "Login refuse";
                        }
                    } else if (splitMess[1].equals("register")){
                        if(Server.Register(splitMess[2], splitMess[3])){
                            mess = "Register accept";
                            user = splitMess[2];
                            Server.listOnlineUser.add(splitMess[2]);
                            Server.listSK.put(splitMess[2], client);
                        }else {
                            mess = "Register refuse";
                        }
                    } else {
                        mess = "Error type!";
                    }

                    DataOutputStream dos = null;
                    try {
                        dos = new DataOutputStream(client.getOutputStream());
                        dos.writeUTF(mess); 
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(Exception e){
            try {
                dis.close();
            } catch (IOException ex){
                System.out.println("Disconnected");
            }
        } finally {
            String user = new String();
            for (int i = 0; i < Server.listOnlineUser.size(); i++) {
                user = Server.listOnlineUser.get(i);
                if (Server.listSK.get(user) == this.client){
                    Server.listSK.remove(user);
                    break;
                }
            }
            Server.listOnlineUser.remove(user);
            System.out.println("User " + user + " disconnected to this server!");
            sendConnectedPacket();
            try {
                client.close();
            } catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}