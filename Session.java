//Session | The main class for each client that connects

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session extends Thread{

    private volatile boolean running = true;
    
    private final int id;
    private boolean gameOver = false;
    
    Socket socket;
    InputStreamReader isr;
    BufferedReader in;
    PrintWriter out;
    
    //integers representing position at the table
    final int DEALER = 0;
    final int SB = 1;
    final int BB = 2;
    final int NB = 3;
    
    private String username;
    private Card card1;
    private Card card2;
    private int position;
    private boolean inHand = false;
    
    public Session(int id, Socket socket) throws IOException{
        this.id = id;
        this.socket = socket;
        
        isr = new InputStreamReader(socket.getInputStream());
        in = new BufferedReader(isr);
        out = new PrintWriter(socket.getOutputStream(), true);
        
        //Decide what blind this player is
        if (id == 0){
            position = DEALER;
        } else if (id == 1){
            position = SB;
        } else if (id == 2){
            position = BB;
        } else {
            position = NB;
        }

    }
    
    @Override
    public void run() {
        
        try {
            //First message recieved from a client is it's username
            this.setUsername(getMessage());
            
            while (running){
                try {
                    //Read any client response
                    String clientMessage = getMessage();
                    
                    //Decide what to do with the message recieved from the player
                    
                    //do smoething here////
                } catch (IOException e) {
                    //sendMessage(this.username + " has left the table");
                    System.out.println("Error in player with username: " + this.username);
                    stopRunning();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(String message){
        //for (Session clients : TexasCodeEm.players){
                this.out.println(message);
        //}
    }
    
    private String getMessage() throws IOException{
        return in.readLine();
    }

    public void stopRunning()    {
        this.running = false;
    }
    
    public void addCard1(Card cardInput){
        this.card1 = cardInput;
    }
    
    public void addCard2(Card cardInput){
        this.card2 = cardInput;
    }
    
    public void setUsername(String name){
        this.username = name;
    }
    
    public Card getCard1(){
        return this.card1;
    }
    
    public Card getCard2(){
        return this.card2;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public boolean isInHand(){
        return this.inHand;
    }
    
    public void setInHand(boolean b){
        this.inHand = b;
    }
    
    public int getPosition(){
        return this.position;
    }
    
    public void setPosition(int p){
        this.position = p;
    }
    
}