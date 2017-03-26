import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//Jacob Miecznikowski | Texas Code 'Em | February 9th, 2017 \\

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
    
    private final int PORT = 1492;
    private int clientNumber = -1;  
    private final int MAX_PLAYERS = 5;
    private int numPlayers = 0;
    public boolean gameOver = false;
    public boolean handOver = true;
    
    final int DEALER = 0;
    final int SB = 1;
    final int BB = 2;
    final int NB = 3;
            
    public TexasCodeEm(){
        startGame();
    }        
    
    public void startGame(){
        prepareDeck();
        startServer();
    }
    
    public void startServer(){
        try {           
            
            System.out.println("Texas Code 'Em Server V. 1.0.0");
            System.out.println("______________________________");
            System.out.println();
            System.out.println("Starting server...");

            listener = new ServerSocket(PORT);
            System.out.println("Server sucessfully started on port " + this.PORT);

            while (!gameOver) {
                try {
                    // Wait for next client connection
                    socket = listener.accept();
                    System.out.println("New player joined and assigned the ID: " + (++clientNumber));

                    Session sesh = new Session(clientNumber, socket);                
                    players.add(sesh);
                    sesh.start();  
                    
                    if (clientNumber == 0){
                        dealer = sesh;
                    } else if (clientNumber == 1){
                        smallBlind = sesh;
                    } else if (clientNumber == 2){
                        bigBlind = sesh;
                    }
                    
                    //change this line after to ZERO not -1/////////////////////////////////////////////////////////////    <-  LOOK
                    if (numPlayers >= -1 && handOver){
                        handOver = false;
                        
                        //prob wrong placement of this car but,
                        //loop through each session player and set the inHand variable equal to true
                        for (Session player : players){
                            player.setInHand(true);
                        }
                        
                        dealToTable();
                        
                        //Now send action to the first person after the big blind, and go clockwise
                        //from that player
                        preflopAction();
                        
                        handOver = true;
                    }

                } catch (Exception e){
                    System.out.println("Server Error: " + e);
                    this.gameOver = true;
                    closeServerSocket();
                    sendGameOverSignal();
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
    
    //Get action from everyone
    public void preflopAction(){
        //Find which player is the Big Blind, and then start action at the player after
        int startIndex = 0;
        
        //fix this when we get a lot of players
        
//        for (Session player : TexasCodeEm.players){
//            if (player.getPosition() == BB){
//                break;
//            }
//            startIndex++;
//        }
        
        //send action to the person at index of startIndex
        TexasCodeEm.players.get(startIndex).sendMessage("getaction");
        
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
    
    //Deal each person at the table two cards
    //TODO: change the order of the deal so it goes one card
    //to each person around the table, then gives them a second card
    //the second time around
    public void dealToTable(){
        for (Session player : TexasCodeEm.players){
            if (player.isInHand()){
                player.addCard1(deck.deal());
                player.addCard2(deck.deal());
            }            
        }
        
        //send each client a signal to let them know that cards are dealt
        //so they can be displayed
        //Example formats for duece of diamonds and ace of spaces: dealcomplete|7DAS
        for (Session player : TexasCodeEm.players){
            if (player.isInHand()){
                 player.sendMessage("dealcomplete|" + player.getCard1().toString() + ":" + player.getCard2().toString());
            }
           
        }
    }
    
    public void prepareDeck(){
        deck = new Deck();
        deck.shuffle();
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
