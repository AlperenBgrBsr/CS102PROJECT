import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UserPage extends JFrame {
    public User user;

    public UserPage(User user) {
        this.user = user;
        this.setTitle("UserPage");
        this.setSize(1024,768);
        this.setIconImage(new ImageIcon("icons\\BilMartIcon.png").getImage());
        this.setVisible(true);
        
        //ItemsBar
        ItemsBar items = new ItemsBar(true);
        items.setBounds(0, 0, 1024, 90);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setSize(new Dimension(1024,100));
        topPanel.add(items, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);
    }
}
