import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainSystemFrame extends JFrame {
    public MainSystemFrame() {
        this.setVisible(true);
        JLabel label = new JLabel("Welcome to the main system! ");
        this.add(label);
        this.setLocationRelativeTo(null); 
        this.pack();
    }
}
