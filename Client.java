//Client for Texas Code 'Em
//Should be started seperartly fromt the Server from an entirely different computer or directory

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    
    private Window window;
    private ButtonPane buttonPane;
    private int width;
    private int height;
    
    private PrintWriter out;
    private InputStreamReader isr;
    private BufferedReader in;
    
    private int id;
    private String username;
    private int chipCount = 5000;
    
    private Socket socket;
    private final String SERVERIP = "localhost";
    private final int SERVERPORT = 1492;

    private JTextArea chatBox;
    private JScrollPane jScrollPane1;
    private JTextField textField;
    JButton btnCheck = new JButton("Check");
    JButton btnCall = new JButton("Call");
    JButton btnRaise = new JButton("Raise");
    JButton btnFold = new JButton("Fold");
    
    private boolean playing = false;
    
    private String card1Val;
    private String card1Suit;
    private String card2Val;
    private String card2Suit;
        
    private int currentBet = 0;
    
    public Client() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();
        height -= (height / 20);        
        
        //this.username = JOptionPane.showInputDialog(null, "Username: ", "Enter Username", JOptionPane.INFORMATION_MESSAGE);
        this.username = "ScrubSandwich";
        
        try{            
            setUpUserInterface();
            establishConnection();
            btnCall.setEnabled(false);
            btnFold.setEnabled(false);
            btnCheck.setEnabled(false);
            btnRaise.setEnabled(false);
            
            //First thing a client does is sends its username
            sendMessage(this.username);            
        }
        catch (Exception e){
            System.out.println(e);
        }        
    }
    
    private void run(){
        while (true){
            // Read server response
            String message = getMessage();
            
            System.out.println("Message: " + message);
            
            processRequest(message);            
        }
    }
    
    //Decide what to do with the recieved message
    public void processRequest(String message){
        
        if (message.equals("nexthand")){
            playing = true;
        } else if (message.startsWith("dealcomplete")){
            //decrypt the cards. format will be: valueNumber:secondValueSecondNumber e.g., JH4S

            //Get each cardValue and suit
            this.card1Val = message.substring(13, 14);
            this.card1Suit = message.substring(14, 15);

            this.card2Val = message.substring(16, 17);
            this.card2Suit = message.substring(17, 18);

            //Now show the cards in the game
            displayCards();
        } else if (message.equals("getaction")){
            System.out.println("Message is getaction");
            showActionButtons();
        } else if (message.startsWith("addplayer")){
            //command format: addplayer|username:chipCount:position (which is an int)
            
            displayOtherPlayer1Cards();
        }
    }

    private void setUpUserInterface(){        
        setTitle("Texas Code 'Em");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        Point newLocation = new Point((this.getWidth() / 2), (this.getHeight() / 2));
        setLocation(newLocation);
        
        setLayout(new BorderLayout());       
        setSize(width, height); 
        
        window = new Window(width, height, this.username);
        add(window, BorderLayout.CENTER);
        
        buttonPane = new ButtonPane();
        buttonPane.setLayout(new GridLayout(0, 6, 10, 0));
        add(buttonPane, BorderLayout.SOUTH);
                
        //buttonPane.add(btnCheck);
        buttonPane.add(btnCall);
        buttonPane.add(btnRaise);
        buttonPane.add(btnFold);
        buttonPane.add(btnCheck);
        
        btnCheck.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                //sendMessage("HI");
                System.out.println("Check Button Pressed");
            }       
        });
        
        btnCall.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                //sendMessage("HI");
                System.out.println("Call Button Pressed");
            }       
        });
        
        btnRaise.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                //sendMessage("HI");
                System.out.println("Raise Button Pressed");
            }          
        });
        
        btnFold.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                //sendMessage("HI");
                System.out.println("Fold Button Pressed");
            }          
        });
        
        setVisible(true);       
    }
    
    @Override
    public void repaint(){
        //window.repaint();
    }
    
    private void showActionButtons(){
        
        btnCall.setEnabled(true);
        btnFold.setEnabled(true);
        btnCheck.setEnabled(true);
        btnRaise.setEnabled(true);
    }
    
    private void establishConnection() throws IOException{
        System.out.println("Establishing a connection with the server...");
        socket = new Socket(SERVERIP, SERVERPORT);

        out = new PrintWriter(socket.getOutputStream(), true);
        isr = new InputStreamReader(socket.getInputStream());
        in = new BufferedReader(isr);
    }
    
    private void sendMessage(String message){
        out.println(message);
    }
    
    private void sendMessageWithUserName(String message){
        out.println("<" + this.username + "> " + message);
    }
    
    private String getMessage(){
        try{
            return in.readLine();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Cannot read server message.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return "Cannot get Server Reply";
        }
        
    }
    
    private void displayCards(){
        System.out.println("Card values for this client: " + this.card1Val + this.card1Suit + " " + this.card2Val + card2Suit);
        window.readyToDrawCards = true;
        window.setCard1Val(this.card1Val);
        window.setCard1Suit(this.card1Suit);
        window.setCard2Val(this.card2Val);
        window.setCard2Suit(this.card2Suit);
        window.repaint();
        
    }
    
    private void displayOtherPlayer1Cards(){
        window.readyDrawOtherCards1 = true;
        window.repaint();
    }

    private void updateChatBox(String message){
        this.chatBox.append(message + "\n");
    }
    
    public String getUsername(){
        return this.username;
    }
    
    //returns true if the client is currently playing in the hand
    public boolean isPlaying(){
        return this.playing;
    }
    
    public static void main(String[] args){        
        Client client = new Client();
        client.run();       
    }    
}