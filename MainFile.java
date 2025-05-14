import javax.swing.UIManager;
import java.awt.Color;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;

public class MainFile 
{
    public static User currentUserForAll; //This is just for debugging purposses, can delete it or change if needed on your own
    public static LoginScreen log;
    
    public static void main(String[] args) 
    {   
        try {
            FlatCobalt2IJTheme.setup();
            FlatLaf.updateUI();
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }

        // Apply custom properties after setting up the theme
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextArea.foreground", Color.WHITE);
        UIManager.put("PasswordField.foreground", Color.WHITE);
        UIManager.put("FormattedTextField.foreground", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("List.foreground", Color.WHITE);
        UIManager.put("Table.foreground", Color.WHITE);
        
        UIManager.put("TextField.caretForeground", Color.WHITE);
        UIManager.put("TextArea.caretForeground", Color.WHITE);
        UIManager.put("PasswordField.caretForeground", Color.WHITE);

        Database.createConnection();
        log = new LoginScreen();
        RegisterScreen reg = new RegisterScreen(log);
        reg.setVisible(false);
        log.connect(reg);
    
    }
}
