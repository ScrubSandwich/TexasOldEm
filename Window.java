import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Window extends JPanel{
    
    private int width;
    private int height;
    public BufferedImage background;
    
    public BufferedImage card1;
    public BufferedImage card2;
    
    private String card1Val;
    private String card1Suit;
    private String card2Val;
    private String card2Suit;
    
    public boolean readyToDrawCards = false;
    
    public Window(int width, int height){
        this.width = width;
        this.height = height;  
        
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
        int yC = (height - (height / 5));
        
        //Draw Hold Card outline
        //TODO draw backs of cards here
        g.setColor(Color.white);
        g.drawRect(xC - widthC - 15, yC, widthC, heightC);
        g.fillRect(xC - widthC - 15, yC, widthC, heightC);
        g.drawRect(xC, yC, widthC, heightC);
        g.fillRect(xC, yC, widthC, heightC);
        
        //draw the cards if they are dealt
        if (readyToDrawCards){
            try {                
                card1 = ImageIO.read(getClass().getResourceAsStream("/img/cards/" + this.card1Val + this.card1Suit + ".png"));
                card2 = ImageIO.read(getClass().getResourceAsStream("/img/cards/" + this.card2Val + this.card2Suit + ".png"));                                
            } catch (IOException ex) {
                System.out.println(ex); // handle exception...
            }
        }                
        
        //Draw Hole Cards
        g.drawImage(card1, xC - widthC - 15, yC, widthC, heightC, this);
        g.drawImage(card2, xC, yC, widthC, heightC, this);
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
    
}