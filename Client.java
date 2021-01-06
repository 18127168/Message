//Multithread 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

//GUI
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Cursor;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;

public class Client {
    private InetAddress host;
    private int port;

    GridBagConstraints gridBag = new GridBagConstraints();
    JFrame mainFrame = new JFrame();
    JPanel warningPanel, midPanel, botPanel;

    public Client(InetAddress host, int port){
        this.host = host;
        this.port = port;
        mainFrame = new JFrame();
        mainFrame.setSize(1200, 700);
        mainFrame.setLayout(new GridBagLayout());
        prepareGUI(0);
    }

    private void setGridBag(int gdx, int gdy, int idx, int idy, int gdh, int gdw){
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
        setGridBag(0, 0, 0, 0, 0, 0);
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

                if (!password.equals(rePassword)) {
                    warningLabel.setText("Password doesn't match!");
                }

                if (user.contains(" ") || password.contains(" ")){
                    warningLabel.setText("Account and password cannot contain space!");
                }     
                
                
            }  
        });  
        botPanel.add(button, gridBag);
    }
    
    public void sendConnectMessage(ArrayList<String> userInfor) throws IOException{

    }

    public void execute() throws IOException{
        Socket client = new Socket(host, port);
        readClient read = new readClient(client);
        read.start();
        writeClient write = new writeClient(client);
        write.start();
    }

    public static void main(String[] args) throws IOException{
        Client client = new Client(InetAddress.getLocalHost(), 5000);
        client.execute();
    }
}

class readClient extends Thread{
    private Socket client;

    public readClient(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                String message = dis.readUTF();
                System.out.println(message);
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

class writeClient extends Thread{
    private Socket client;

    public writeClient(Socket client) {
        this.client = client;
    }

    public void run() {
        DataOutputStream dos = null;
        Scanner sc = null;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            sc = new Scanner(System.in);
            while(true){
                String message = sc.nextLine();
                dos.writeUTF(message);
            }
        } catch(Exception e) {
            try {
                dos.close();
            } catch (IOException ex){
                System.out.println("Disconnected");
            }
        }
    }
}