import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Window extends JPanel{
    
    private int width;
    private int height;
    public BufferedImage background;
    
    //Player's Cards
    public BufferedImage card1;
    public BufferedImage card2;
    
    //Other Player's cards;
    public BufferedImage other1card1;
    public BufferedImage other1card2;
    public BufferedImage other2card1;
    public BufferedImage other2card2;
    public BufferedImage other3card1;
    public BufferedImage other3card2;
    public BufferedImage other4card1;
    public BufferedImage other4card2;
    
    private String card1Val;
    private String card1Suit;
    private String card2Val;
    private String card2Suit;
    
    private String other1card1Val;
    private String other1card1Suit;
    private String other1card2Val;
    private String other1card2Suit;
    private String other2card1Val;
    private String other2card1Suit;
    private String other2card2Val;
    private String other2card2Suit;
    private String other3card1Val;
    private String other3card1Suit;
    private String other3card2Val;
    private String other3card2Suit;
    private String other4card1Val;
    private String other4card1Suit;
    private String other4card2Val;
    private String other4card2Suit;
    
    public boolean readyToDrawCards = false;
    public boolean readyDrawOtherCards1 = false;
    public boolean readyDrawOtherCards2 = false;
    public boolean readyDrawOtherCards3 = false;
    public boolean readyDrawOtherCards4 = false;
    
    private String username;
    
    
    public Window(int width, int height, String user){
        this.width = width;
        this.height = height;  
        this.username = user;
        
        //open the background image
        try {                
            background = ImageIO.read(getClass().getResourceAsStream("/img/background.jpg"));
       } catch (IOException ex) {
            System.out.println(ex);// handle exception...
       }
    }
    
    @Override
    public void paint(Graphics g) {
               
        g.drawImage(background, 0, 0, this);
        
        //Card Variables
        int widthC = (width / 20);
        int heightC = (height / 9);
        int xC = width / 2;
        int yC = (height - (height / 5)) - 50;
        
        //Draw Hold Card outline
        //TODO draw backs of cards here
        g.setColor(Color.white);
        g.drawRect(xC - widthC - 15, yC, widthC, heightC);
        g.fillRect(xC - widthC - 15, yC, widthC, heightC);
        g.drawRect(xC, yC, widthC, heightC);
        g.fillRect(xC, yC, widthC, heightC);
        
        //Draw username
        g.setColor(Color.BLACK);
        Font font = new Font ("Courier New", Font.BOLD, 24);
        g.setFont(font);
        g.drawString(this.username, xC + widthC - 175, yC + 150);
        
        //draw the cards if they are dealt
        if (readyToDrawCards){
            try {                
                card1 = ImageIO.read(getClass().getResourceAsStream("/img/cards/" + this.card1Val + this.card1Suit + ".png"));
                card2 = ImageIO.read(getClass().getResourceAsStream("/img/cards/" + this.card2Val + this.card2Suit + ".png"));                                
            } catch (IOException ex) {
                System.out.println(ex); // handle exception...
            }
            //Draw Hole Cards
            g.drawImage(card1, xC - widthC - 15, yC, widthC, heightC, this);
            g.drawImage(card2, xC, yC, widthC, heightC, this);
        }    
        
        //draw the other player1's cards if the player exists.
        if (readyDrawOtherCards1){
            try{
                other1card1 = ImageIO.read(getClass().getResourceAsStream("/img/cards/back.png"));
                other1card2 = ImageIO.read(getClass().getResourceAsStream("/img/cards/back.png"));
            } catch (IOException ex){
                System.out.println(ex); // handle exception...
            }
            g.drawImage(other1card1, 200, 150, widthC, heightC, this);
            g.drawImage(other1card1, 200 + widthC + 15, 150, widthC, heightC, this);
            
        }
        
        
  }
    
  public void setReadyToDrawCards(int i){
      if (i == 1){
          this.readyToDrawCards = true;
      } else{
          this.readyToDrawCards = false;
      }
  }
  
    @Override
  public int getWidth(){
      return this.width;
  }
  
    @Override
  public int getHeight(){
      return this.height;
  }
  
  //Card fuctions for the Main player
  public void setCard1Val(String value){
      this.card1Val = value;
  }
  
  public void setCard1Suit(String s){
      this.card1Suit = s;
  }
  
  public void setCard2Val(String value){
      this.card2Val = value;
  }
  
  public void setCard2Suit(String s){
      this.card2Suit = s;
  }
  
  
  //Card Fuctions for the Other Player 1
  
    
}