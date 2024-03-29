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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client extends JFrame {
    
    private Window window;
    private ButtonPane buttonPane;
    private int width;
    private int height;
    
    private PrintWriter out;
    private InputStreamReader isr;
    private BufferedReader in;
    
    private ObjectInputStream inObject = null;
    private ObjectOutputStream outObject = null;
    
    private int id;
    private String username = "test_username";
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

    private Card card1;
    private Card card2;
        
    private int currentBet = 0;
    
    public Client() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();
        height -= (height / 20);        
        
        //this.username = JOptionPane.showInputDialog(null, "Username: ", "Enter Username", JOptionPane.INFORMATION_MESSAGE);
        
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
            Message message = getMessage();
            processRequest(message);
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

    private void establishConnection() throws IOException{
        System.out.println("Establishing a connection with the server...");
        socket = new Socket(SERVERIP, SERVERPORT);

        outObject = new ObjectOutputStream(socket.getOutputStream());
        inObject = new ObjectInputStream(socket.getInputStream());

        System.out.println("Connection successful");
    }
     
    //Decide what to do with the recieved message
    public void processRequest(Message m){
        String message = m.getMessage();
        
        System.out.println("Message from Server: " + message);
        
        if (message.equals("nexthand")){
            playing = true;
        } else if (message.startsWith("dealcomplete")){
            //decrypt the cards. format will be: valueNumber:secondValueSecondNumber e.g., JH4S

            //Get each cardValue and suit

            card1 = new Card(message.substring(14, 15), message.substring(13, 14));
            card2 = new Card(message.substring(17, 18), message.substring(16, 17));           

            //Now show the cards in the game
            displayCards();
        } else if (message.equals("getaction")){
            showActionButtons();
        } else if (message.startsWith("addplayer")){
            
            System.out.println("Got a request to add a player");
            //command format: addplayer|username:position (which is an int)
            int colon = message.indexOf(":");
            
            String user = message.substring(10, colon++ );
            int position = Integer.parseInt(message.substring(colon));
            
            if (!user.equals(this.username)){
                displayCardsAt(user, position);
            }
        }
    }
    
    private void showActionButtons(){        
        btnCall.setEnabled(true);
        btnFold.setEnabled(true);
        btnCheck.setEnabled(true);
        btnRaise.setEnabled(true);
    }
    
    private void sendMessage(String message) {
        try {
            Message m = new Message(username, message);
            outObject.writeObject(m);
            System.out.println("Sent message: " + m.getMessage());
        } catch (Exception e) {
            System.out.println("Error sending message object");
        }
        
    }
    
    private void sendMessageWithUserName(String message){
        out.println("<" + this.username + "> " + message);
    }
    
    private Message getMessage(){
        try{
            return (Message) inObject.readObject();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Cannot read server message.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return new Message(this.username, "Error");
        }
        
    }
    
    private void displayCards(){
        window.readyToDrawCards = true;

        window.setCard1(card1);
        window.setCard2(card2);
        window.repaint();        
    }
    
    private void displayCardsAt(String user, int position) {        
        System.out.println("|" + user + "|" + position +  "|");
        switch (position) {
            case 1:
                window.readyDrawOtherCards1 = true;
                break;
            case 2:
                window.readyDrawOtherCards2 = true;
                break;
            case 3:
                window.readyDrawOtherCards3 = true;
                break;
            case 4:
                window.readyDrawOtherCards4 = true;
                break;
            case 5:
                window.readyDrawOtherCards5 = true;
                break;
            case 6:
                window.readyDrawOtherCards6 = true;
                break;
            case 7:
                window.readyDrawOtherCards7 = true;
                break;
            case 8:
                window.readyDrawOtherCards8 = true;
                break;
            default:
                System.out.println("Wrong position in displayCardsAt method");
                break;
        }
        
        window.repaint();
    }
    
    private void displayOtherPlayer1Cards(){
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