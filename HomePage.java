import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HomePage extends JFrame{

    JPanel middlePanel;
    
    public HomePage() {
        this.setTitle("BilMart HomePage");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024,768);
        this.setLayout(new BorderLayout());

        this.add(new ItemsBar(), BorderLayout.NORTH);

        middlePanel = new JPanel();
        
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}