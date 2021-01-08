//Multithread 
import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//GUI
import java.awt.event.*;
import java.awt.*;
 
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Client {
    public static HashMap <String, String> chatStorage = new HashMap <String, String>();
    public static ArrayList<String> listOnlineUser;
    public static String current_User;
    public static JFrame mainFrame = new JFrame();
    public static JTextArea chatArea;
    public static JPanel rightPanel, leftPanel, labelPanel, userPanel;
    public static GridBagConstraints gridBag = new GridBagConstraints();
    public static JButton chatButton;
    public static JTextField chatField;
    public static JLabel enemyLabel;

    Socket client = null;
    read_wirteClient read_write = null;
    JPanel warningPanel, midPanel, botPanel;


    public Client(InetAddress host, int port) throws IOException{
        client = new Socket(host, port);
        read_write = new read_wirteClient(client);
        execute();
    }

    private static void setGridBag(int gdx, int gdy, int idx, int idy, int gdh, int gdw){
        gridBag.gridx = gdx;
        gridBag.gridy = gdy;
        gridBag.ipadx = idx;
        gridBag.ipady = idy;
        gridBag.gridwidth = gdw;
        gridBag.gridheight = gdh;
    };

    private void prepareGUI(int type) {
        mainFrame.getContentPane().removeAll();
        
        setGridBag(0, 0, 0, 0, 1, 3);
        JLabel mainlabel = new JLabel("Login", JLabel.CENTER);
        if (type == 1) { mainlabel = new JLabel("Register"); }
        mainlabel.setFont(new Font("Georgia", Font.BOLD, 60));
        mainlabel.setForeground(new Color(110, 230, 75));
        mainFrame.add(mainlabel, gridBag);

        setGridBag(0, 1, 0, 20, 1, 1);
        JPanel tmpPanel = new JPanel();
        mainFrame.add(tmpPanel, gridBag);
        
        setGridBag(0, 2, 0, 10, 1, 3);
        warningPanel = new JPanel();
        warningPanel.setLayout(new GridBagLayout());
        mainFrame.add(warningPanel, gridBag);

        setGridBag(0, 4, 0, 0, 1, 3);
        midPanel = new JPanel();
        midPanel.setLayout(new GridBagLayout());
        mainFrame.add(midPanel, gridBag);
        
        setGridBag(0, 6, 0, 0, 1, 1);
        tmpPanel = new JPanel();
        mainFrame.add(tmpPanel, gridBag);
        
        setGridBag(0, 7, 0, 0, 1, 3);
        botPanel = new JPanel();
        botPanel.setLayout(new GridBagLayout());
        mainFrame.add(botPanel, gridBag);

        if (type == 0) { setPanelLogin(); }
        else if (type == 1) { setPanelRegister(); }

        mainFrame.setVisible(true);
        mainFrame.setTitle("Message - Login");
        if (type == 1) {mainFrame.setTitle("Message - Register");}
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void setPanelLogin() {
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        setGridBag(0, 1, 0, 10, 1, 1);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Georgia", Font.BOLD, 25));
        midPanel.add(userLabel, gridBag);

        setGridBag(0, 2, 0, 10, 1, 1);
        JTextField userField = new JTextField("", 20);
        userField.setFont(new Font("Georgia", Font.BOLD, 25));
        userField.setBorder(BorderFactory.createCompoundBorder(userField.getBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        midPanel.add(userField, gridBag);

        setGridBag(0, 3, 0, 20, 1, 1);
        JPanel tmpPanel = new JPanel();
        midPanel.add(tmpPanel, gridBag);

        gridBag.fill = GridBagConstraints.HORIZONTAL;
        setGridBag(0, 4, 0, 10, 1, 1);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Georgia", Font.BOLD, 25));
        midPanel.add(passwordLabel, gridBag);
        gridBag.fill = GridBagConstraints.NONE;  
        
        setGridBag(0, 5, 0, 10, 1, 3);
        JPasswordField passwordField = new JPasswordField("", 20);
        passwordField.setFont(new Font("Georgia", Font.BOLD, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        midPanel.add(passwordField, gridBag);
    
        setGridBag(0, 6, 0, 20, 1, 1);
        tmpPanel = new JPanel();
        midPanel.add(tmpPanel, gridBag);

        gridBag.fill = GridBagConstraints.HORIZONTAL;
        setGridBag(0, 7, 0, 50, 1, 3);
        JLabel registerLabel = new JLabel("<HTML><U>Click here to sign up</U></HTML>");
        registerLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(new Color(30, 37, 247));
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)  {
                prepareGUI(1);
            }
        });
        midPanel.add(registerLabel, gridBag);

        setGridBag(1, 1, 0, 10, 1, 1);
        JLabel warningLabel = new JLabel("");
        warningLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        warningLabel.setForeground(new Color(222, 0, 0));
        warningPanel.add(warningLabel, gridBag);
        
        gridBag.fill = GridBagConstraints.NONE;  
        setGridBag(0, 0, 0, 0, 1, 3);
        JButton button = new JButton("Sign in");
        button.setFont(new Font("Georgia", Font.BOLD, 30));
        button.setBorder(BorderFactory.createCompoundBorder(button.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        button.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                warningLabel.setText("");
                String user = userField.getText();
                char[] pw = passwordField.getPassword();
                String password = new String(pw);

                if (user.equals("") || password.equals("")){
                    warningLabel.setText("Account and password is required!");
                } else if (user.contains(" ") || password.contains(" ")){
                    warningLabel.setText("Account and password cannot contain space!");
                } else {
                    String result = read_write.askServer("login", user + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + password);
                    if(result.equals("Login accept")){
                        current_User = user;
                        prepareChatGUI();
                        read_write.connectedPacket();
                        chatStart();
                    } else {
                        warningLabel.setText("Invalid user or password!");
                    }
                }
            }  
        });  
        botPanel.add(button, gridBag);
    }

    private void setPanelRegister() {
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        setGridBag(0, 0, 0, 10, 1, 1);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        midPanel.add(userLabel, gridBag);

        setGridBag(0, 1, 0, 10, 1, 3);
        JTextField userTextField = new JTextField("", 20);
        userTextField.setFont(new Font("Georgia", Font.BOLD, 25));
        userTextField.setBorder(BorderFactory.createCompoundBorder(userTextField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        midPanel.add(userTextField, gridBag);

        setGridBag(0, 2, 0, 20, 1, 3);
        JPanel tmpPanel = new JPanel();
        midPanel.add(tmpPanel, gridBag);

        setGridBag(0, 3, 0, 10, 1, 1);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        midPanel.add(passwordLabel, gridBag);
        
        setGridBag(0, 4, 0, 10, 1, 3);
        JPasswordField passwordField = new JPasswordField("", 20);
        passwordField.setFont(new Font("Georgia", Font.BOLD, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        midPanel.add(passwordField, gridBag);

        setGridBag(0, 5, 0, 20, 1, 3);
        tmpPanel = new JPanel();
        midPanel.add(tmpPanel, gridBag);

        setGridBag(0, 6, 0, 10, 1, 1);
        JLabel rePasswordLabel = new JLabel("Re-enter Password");
        rePasswordLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        midPanel.add(rePasswordLabel, gridBag);
        
        setGridBag(0, 7, 0, 10, 1, 3);
        JPasswordField rePasswordField = new JPasswordField("", 20);
        rePasswordField.setFont(new Font("Georgia", Font.BOLD, 25));
        rePasswordField.setBorder(BorderFactory.createCompoundBorder(rePasswordField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        midPanel.add(rePasswordField, gridBag);

        setGridBag(0, 8, 0, 20, 1, 3);
        tmpPanel = new JPanel();
        midPanel.add(tmpPanel, gridBag);

        setGridBag(0, 9, 0, 0, 1, 3);
        JLabel registerLabel = new JLabel("<HTML><U>Click here to sign in</U></HTML>");
        registerLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(new Color(30, 37, 247));
        registerLabel.addMouseListener(new MouseAdapter() {  
            public void mouseClicked(MouseEvent e)  { prepareGUI(0); }  
        }); 
        midPanel.add(registerLabel, gridBag);

        setGridBag(1, 1, 0, 10, 1, 1);
        JLabel warningLabel = new JLabel("");
        warningLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        warningLabel.setForeground(new Color(222, 0, 0));
        warningPanel.add(warningLabel, gridBag);
        
        gridBag.fill = GridBagConstraints.NONE;  
        JButton button = new JButton("Sign up");
        setGridBag(0, 0, 0, 0, 1, 1);
        button.setFont(new Font("Georgia", Font.BOLD, 30));
        button.setBorder(BorderFactory.createCompoundBorder(button.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        button.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                warningLabel.setText("");

                String user = userTextField.getText();
                char[] pw = passwordField.getPassword();
                char[] rpw = rePasswordField.getPassword();
                String password = new String(pw);
                String rePassword = new String(rpw);

                if (user.equals("") || password.equals("") || rePassword.equals("")){
                    warningLabel.setText("Account and password is required!");
                }
                else if (!password.equals(rePassword)) {
                    warningLabel.setText("Password doesn't match!");
                }
                else if (user.contains(" ") || password.contains(" ")){
                    warningLabel.setText("Account and password cannot contain space!");
                }     
                else if (user.length() > 10 || user.length() < 3){
                    warningLabel.setText("Account beetween 3-10 characters!");
                }     
                else {
                    String result = read_write.askServer("register", user + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + password);
                    if(result.equals("Register accept")){
                        current_User = user;
                        prepareChatGUI();
                        read_write.connectedPacket();
                        chatStart();
                    } else {
                        warningLabel.setText("This account existed!");
                    }
                }
            }  
        });  
        botPanel.add(button, gridBag);
    }
    
    private void prepareChatGUI() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setLayout(new GridBagLayout());

        setGridBag(0, 0, 0, 0, 1, 3);
        labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());
        mainFrame.add(labelPanel, gridBag);

        setGridBag(2, 0, 0, 0, 1, 2);
        userPanel = new JPanel();
        userPanel.setLayout(new GridBagLayout());
        mainFrame.add(userPanel, gridBag);

        setGridBag(0, 1, 10, 0, 1, 1);
        JPanel tmpPanel = new JPanel();
        mainFrame.add(tmpPanel, gridBag);

        setGridBag(0, 2, 0, 0, 1, 1);
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());
        leftPanel.setBorder(new LineBorder(Color.BLACK, 3));
        mainFrame.add(leftPanel, gridBag);

        setGridBag(1, 2, 5, 0, 1, 1);
        tmpPanel = new JPanel();
        mainFrame.add(tmpPanel, gridBag);

        setGridBag(2, 2, 20, 0, 8, 1);
        gridBag.fill = GridBagConstraints.VERTICAL;
        rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout());
        rightPanel.setPreferredSize(new Dimension(150,100));
        mainFrame.add(new JScrollPane(rightPanel), gridBag);

        setGridBag(0, 3, 20, 0, 8, 1);
        botPanel = new JPanel();
        botPanel.setLayout(new FlowLayout());
        mainFrame.add(botPanel, gridBag);

        setChatGUI();

        mainFrame.setVisible(true);
        mainFrame.setTitle("Message");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void setChatGUI() {

        gridBag.fill = GridBagConstraints.HORIZONTAL;
        setGridBag(0, 0, 0, 0, 1, 3);
        JLabel mainlabel = new JLabel("Message", Label.LEFT);
        mainlabel.setFont(new Font("Georgia", Font.BOLD, 60));
        mainlabel.setForeground(new Color(110, 230, 75));
        labelPanel.add(mainlabel, gridBag);  

        setGridBag(0, 0, 0, 5, 1, 2);
        JLabel userlabel = new JLabel("User: " + current_User);
        userlabel.setFont(new Font("Georgia", Font.BOLD, 20));
        userlabel.setForeground(new Color(209, 42, 0));
        userPanel.add(userlabel, gridBag);

        setGridBag(0, 1, 0, 5, 1, 2);
        enemyLabel = new JLabel("Chatting: No one");
        enemyLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        enemyLabel.setForeground(new Color(209, 42, 0));
        userPanel.add(enemyLabel, gridBag);

        setGridBag(0, 2, 0, 0, 1, 2);
        JLabel friendlabel = new JLabel("Online user list");
        friendlabel.setFont(new Font("Georgia", Font.BOLD, 15));
        userPanel.add(friendlabel, gridBag);

        chatArea = new JTextArea(30,80);
        chatArea.setEditable (false);
        chatArea.setBorder(BorderFactory.createCompoundBorder(chatArea.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        leftPanel.add(new JScrollPane(chatArea), BorderLayout.PAGE_START);

        setGridBag(0, 0, 0, 10, 1, 3);
        chatField = new JTextField("", 36);
        chatField.setFont(new Font("Georgia", Font.BOLD, 20));
        chatField.setBorder(BorderFactory.createCompoundBorder(chatField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        chatField.setEnabled(false);
        chatField.setBackground(new Color(140, 141, 143));
        botPanel.add(chatField, gridBag);

        setGridBag(3, 0, 10, 0, 1, 1);
        JPanel tmpPanel = new JPanel();
        botPanel.add(tmpPanel, gridBag);

        gridBag.fill = GridBagConstraints.NONE;  
        chatButton = new JButton("Send");
        setGridBag(4, 0, 0, 0, 1, 1);
        chatButton.setFont(new Font("Georgia", Font.BOLD, 18));
        chatButton.setEnabled(false);
        chatButton.setBackground(new Color(101, 103, 105));
        chatButton.setBorder(BorderFactory.createCompoundBorder(chatButton.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        chatButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                String mess = chatField.getText();
                chatField.setText("");

                createMessage(true, current_User, mess);
                
                read_write.userSendMess(mess);
            }
        });

        botPanel.add(chatButton, gridBag);
    }

    public static void setOnlineUser(){
        rightPanel.removeAll();

        JButton btn;
        System.out.println(listOnlineUser);
        for (String user : listOnlineUser){
            if (!user.equals(current_User)) {
                btn = new JButton("" + user +"");
                btn.setFont(new Font("Georgia", Font.BOLD, 25));
                btn.addActionListener(new ActionListener(){  
                    public void actionPerformed(ActionEvent e){  
                        read_wirteClient.enemy = user;  
                        if (chatStorage.get(user) == null)  {
                            chatArea.setText("");
                        } else {
                            chatArea.setText(chatStorage.get(user));
                        }
                        enemyLabel.setText("Chat: " + user);
                        chatButton.setEnabled(true);
                        chatButton.setBackground(new Color(255, 255, 255));
                        chatField.setEnabled(true);
                        chatField.setBackground(new Color(255, 255, 255));
                    }
                });
                rightPanel.add(btn);
            }
        }

        mainFrame.setVisible(true);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void createMessage(Boolean owner, String user, String mess){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(" HH:mm dd-MM-yyyy");  
        String formattedDate = myDateObj.format(myFormatObj);  
        System.out.println("After formatting: " + formattedDate);  
        
        if (owner || user.equals(read_wirteClient.enemy)) {
            chatArea.append("\n" + formattedDate + " " + user + ": " + mess);
        }

        if (chatStorage.get(user) == null){
            chatStorage.put(user,"\n" + formattedDate + " " + user + ": " + mess);
        } else {
            chatStorage.put(user, chatStorage.get(user) + "\n" + formattedDate + " " + user + ": " + mess);
        }

        System.out.println(chatStorage.get(user));
    }

    public void execute(){
        mainFrame = new JFrame();
        mainFrame.setSize(1200, 700);
        mainFrame.setLayout(new GridBagLayout());
        prepareGUI(0);
    }

    public void chatStart(){   
        read_write.start();
    }

    public static void main(String[] args) throws IOException{
        Client client = new Client(InetAddress.getLocalHost(), 5000);
        // client.execute();
    }
}

class read_wirteClient extends Thread{
    private Socket client;
    public static String enemy = "";

    public read_wirteClient(Socket client){
        this.client = client;
    }

    public String listenAnswer(){
        DataInputStream distr = null;
        try {
            distr = new DataInputStream(client.getInputStream());
            while (true) {
                String receivedMessage = distr.readUTF();
                
                return receivedMessage;
            }
        } catch(Exception e){
            try {
                distr.close();
                return "false";
            } catch (IOException ex) {
                System.out.println(ex);
                return "false";
            }
        }
    }

    public void connectedPacket(){
        DataOutputStream dostr = null;
        
        try {
            dostr = new DataOutputStream(client.getOutputStream());
            dostr.writeUTF("@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + "connect" + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@");
        } catch(Exception e){
            try {
                dostr.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public String askServer(String type, String message){
        DataOutputStream dostr = null;
        
        try {
            dostr = new DataOutputStream(client.getOutputStream());
            dostr.writeUTF("@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + type + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + message);
            return listenAnswer();
        } catch(Exception e){
            try {
                dostr.close();
                return "false";
            } catch (IOException ex) {
                System.out.println(ex);
                return "false";
            }
        }
    }

    public void userSendMess(String mess){
        DataOutputStream dostr = null;
        
        try {
            dostr = new DataOutputStream(client.getOutputStream());
            dostr.writeUTF("@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + "message" + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + enemy + "@!~!~#~#~!#!a#~d~#mi~n!~!~!~!!@" + mess);
        } catch(Exception e){
            try {
                dostr.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
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
                    splitMess[2] = splitMess[2].replaceFirst("&", "");
                    String[] accountList = splitMess[2].split("&");
                    
                    Client.listOnlineUser = new ArrayList<String>();
                    for (int i = 0; i < accountList.length; i++){
                        Client.listOnlineUser.add(accountList[i]);  
                    }

                    Client.setOnlineUser();
                } else if (splitMess[1].equals("message")){
                    Client.createMessage(false ,splitMess[2], splitMess[3]);
                }
            }
        } catch(Exception e){
            try {
                dis.close();
            } catch (IOException ex){
                System.out.println("Disconnected");
            }
        }
    }
}