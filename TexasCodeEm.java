import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Jacob Miecznikowski | Texas Code 'Em | February 9th, 2017 \\

public class TexasCodeEm {
    private Deck deck;
    public static ArrayList<Session> players = new ArrayList<>();
    
    public static Session dealer;
    public static Session bigBlind;
    public static Session smallBlind;
    
    ServerSocket listener;
    Socket socket;
    InputStreamReader isr;
    BufferedReader in;
    PrintWriter out;

    ObjectInputStream inObject = null;
    ObjectOutputStream outObject = null;
    
    private final int PORT = 1492;
    public static int clientNumber = -1;  
    private final int MAX_PLAYERS = 5;
    public boolean gameOver = false;
    public boolean handOver = true;
    
    //  Integers representing the position at the table
    final int DEALER = 0;
    final int SB = 1;
    final int BB = 2;
    final int UTG1 = 3;
    final int UTG2 = 4;
    final int UTG3 = 5;
    final int HJ = 6;
    final int LJ = 7;
    final int CO = 8;
            
    public TexasCodeEm(){
        startGame();
    }
    
    // The main game loop
    private void run() {
        while (!gameOver) {
            try {
                getNextClient();

                // If we have enought players and the hand isn't currently happening    
                if (players.size() > 1 && handOver){
                    
                    dealToTable();
                    while (!handOver) {                            
                        getAction();
                    }                        
                }
                
            } catch (Exception e){
                try {
                    endGame();
                    System.out.println(e);
                } catch (Exception ex) {
                }                
            }
        }
    }
    
    // Wait for next client connection
    private void getNextClient() throws Exception {
        System.out.println("Waiting for another client to join...");
        socket = listener.accept();
        System.out.println("New player joined and assigned the ID: " + (++clientNumber));

        Session sesh = new Session(clientNumber, socket);                
        players.add(sesh);
        sesh.start();  

        if (clientNumber == 0){
            dealer = sesh;
        } else if (clientNumber == 1) {
            smallBlind = sesh;
        } else if (clientNumber == 2) {
            bigBlind = sesh;
        } else if (clientNumber == 3) {

        }
    }
    
    public void getAction(){        
        //send action to the person at index of startIndex
        TexasCodeEm.players.get(0).sendMessage("getaction");        
    }
    
    public void dealToTable(){  
        System.out.println("Dealing to " + TexasCodeEm.players.size() + " players");
        for (Session player : TexasCodeEm.players){
            if (player.isInHand()){
                player.addCard1(deck.deal());
                player.addCard2(deck.deal());
            }            
        }
        
        //send each client a signal to let them know that cards are dealt
        //so they can be displayed
        //Example formats for duece of diamonds and ace of spaces: dealcomplete|7D:AS
        for (Session player : TexasCodeEm.players){
            if (player.isInHand()){
                 player.sendMessage("dealcomplete|" + player.getCard1().toString() + ":" + player.getCard2().toString());
            }
           
        }
    }

    public void startGame(){
        prepareDeck();
        startServer();
    }
    
    public void prepareDeck(){
        deck = new Deck();
        deck.shuffle();
    }
    
    public void startServer(){
        try {            
            System.out.println("Texas Code 'Em Server V. 1.0.0");
            System.out.println("______________________________");
            System.out.println();
            System.out.println("Starting server...");

            listener = new ServerSocket(PORT);
            System.out.println("Server sucessfully started on port " + this.PORT);
            
            run();
            
        } catch (Exception e){
            System.out.println("Error: " + e + "\nPort number " + PORT + " is already in use.");
        }
    }
    
    private void endGame() throws Exception {         
        System.out.println("Server Error");
        this.gameOver = true;
        closeServerSocket();
        sendGameOverSignal();
    }

     private void closeServerSocket() throws IOException{
        listener.close();
        System.out.println("Closed the Server Socket.");
    }

    private void sendGameOverSignal(){
        System.out.println("Closing all the clients");
        for (Session player : players){
            player.stopRunning();
        }
    }
    
    public void simulatePlay(){
        System.out.println("Player 1: ");
        System.out.println("Card 1: " + deck.deal().toString());
        System.out.println("Card 2: " + deck.deal().toString());
        System.out.println();
        
        System.out.println("Player 2: ");
        System.out.println("Card 1: " + deck.deal().toString());
        System.out.println("Card 2: " + deck.deal().toString());
        System.out.println();
        
        System.out.println("Player 3: ");
        System.out.println("Card 1: " + deck.deal().toString());
        System.out.println("Card 2: " + deck.deal().toString());
        System.out.println();
  
        
        System.out.println("The Flop: ");
        System.out.println(deck.deal().toString());
        System.out.println(deck.deal().toString());
        System.out.println(deck.deal().toString());
        System.out.println();
        
        System.out.println("The Turn");
        System.out.println(deck.deal().toString());
        System.out.println();
        
        System.out.println("The River");
        System.out.println(deck.deal().toString());
        System.out.println();
    }
    
    public static void main(String[] args) {
        new TexasCodeEm();
    }
    
}
