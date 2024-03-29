// Session.java | The main class for each client that connects

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Session extends Thread {

    private volatile boolean running = true;
    
    private final int id;
    private boolean gameOver = false;

    Socket socket;
    ObjectInputStream inObject;
    ObjectOutputStream outObject;
    
    //integers representing position at the table
    final int UTG = 1;
    final int UTG1 = 2;
    final int UTG2 = 3;
    final int LOJACK = 4;
    final int HIGHJACK = 5;
    final int CUTOFF = 6;
    final int BUTTON = 7;
    final int SMALLBLIND = 8;
    final int BIGBLIND = 9;
    
    private String username;
    private Card card1;
    private Card card2;
    private int position;
    private boolean inHand = true;
    
    private int chipCount = 5000;
    
    public Session(int id, Socket socket) throws IOException{
        this.id = id;
        this.socket = socket;
        
        inObject = new ObjectInputStream(socket.getInputStream());
        outObject = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Successfully open object input and output streams for a session");
        
        //Decide what blind this player is
        position = id + 1;

    }
    
    @Override
    public void run() {        
        try {
            //First message recieved from a client is it's username            
            String first = getMessage();            
            this.setUsername(first);
            
            // addAllPlayers();
            
            while (running){
                try {
                    //Read any client response
                    String clientMessage = getMessage();
                    
                    //Decide what to do with the message recieved from the player
                    
                    //do smoething here////
                } catch (Exception e) {
                    //sendMessage(this.username + " has left the table");
                    System.out.println(this.username + " has left the table");
                    TexasCodeEm.clientNumber--;
                    stopRunning();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addAllPlayers() {
        for (Session player : TexasCodeEm.players){                    
            //if (!player.equals(this)){
                System.out.println("Adding player: " + player.getUsername());
                player.sendMessage("addplayer|" + player.getUsername() +  ":" + player.getPosition());
            //}                    
        }
    }
    
    public void sendMessage(String message) {
        try {
            Message m = new Message(username, message);
            outObject.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error sending message object");
        }
    }
    
    private String getMessage() {
        String answer = "";
        try {
            Message message = (Message) inObject.readObject();
            answer = message.getMessage();
        } catch (Exception e) {
            System.out.println("Client closed.");
            stopRunning();
            return "Client exited";
        }

        return answer;
    }

    public void stopRunning() {
        System.out.println("Closing session connection");
        this.running = false;
        try {
            socket.close();
            outObject.close();
            inObject.close();
            this.stop();
        } catch (Exception e) {

        }
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
    
    public void setChipCount(int v){
        this.chipCount = v;
    }
    
    public int getChipCount(){
        return this.chipCount;
    }    
}