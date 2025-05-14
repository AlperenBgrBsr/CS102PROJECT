import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;

public class ChatGUI extends JPanel 
{
    // Main UI Panels
    private JPanel contactsPanel;
    private JPanel chatPanel;
    private JPanel messagesPanel;
    private JTextArea messageInputField;
    private JButton sendMessageButton;
    private JScrollPane messageScrollPane;
    private JScrollPane contactsScrollPane;
    
    // Data
    private ArrayList<Contact> contactList;
    private Contact activeContact;
    
    // Colors
    private Color selectedContactColor = new Color(75, 75, 255);
    private Color availableStatusColor = new Color(189, 246, 254);
    private Color awayStatusColor = new Color(200, 100, 0);
    

    private class MessageUpdater extends SwingWorker<ArrayList<Message>, Void > {

        @Override
        protected ArrayList<Message> doInBackground() throws Exception {

            ArrayList<Message> messages = new ArrayList<>();

            if ( messagesPanel != null && activeContact != null){

                messages = new ArrayList<>();
                try {
                    PreparedStatement getMessagesStatement = Database.databaseConnection.prepareStatement("SELECT * FROM messages WHERE (senderUsername = ?  AND recieverUsername = ?) OR (senderUsername =? AND recieverUsername = ?)");
                    getMessagesStatement.setString(1, activeContact.getName());
                    getMessagesStatement.setString(2, MainFile.currentUserForAll.getUsername());
                    getMessagesStatement.setString(3, MainFile.currentUserForAll.getUsername());
                    getMessagesStatement.setString(4, activeContact.getName());
                    ResultSet getMessagesRs = getMessagesStatement.executeQuery();
                    while ( getMessagesRs.next() ){
                        messages.add(new Message(getMessagesRs.getString("senderUsername"), getMessagesRs.getString("recieverUsername"), getMessagesRs.getString("message"), new Date(getMessagesRs.getTimestamp("date").getTime())));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            return messages;
           
        }
        
        @Override
        protected void done(){
            
            ArrayList<Message> messages = new ArrayList<>();
            try {
                messages = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (messagesPanel != null && activeContact != null) {
                messagesPanel.removeAll();
            
                // Use vertical layout for message list
                messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
                messagesPanel.setBackground(Color.lightGray);
            
                // Add each message to the display
                for(Message message : messages) {
                    JPanel messageItem = createMessageItem(message);
                
                    // Ensure message items span full width
                    messageItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageItem.getPreferredSize().height));
                    messageItem.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                    messagesPanel.add(messageItem);
                    messagesPanel.add(Box.createVerticalStrut(10)); // Space between messages
                }
            
                // Push messages to the top if there are few
                messagesPanel.add(Box.createVerticalGlue());
            
                scrollToBottom();
            
                messagesPanel.revalidate();
                messagesPanel.repaint();
            
            }
        }
        
    }

    static class Contact 
    {
        private String displayName;
        private ArrayList<Message> messageHistory;
        private boolean isSelected;
        private boolean isAvailable;
        private BufferedImage profilePicture;

        public Contact(String displayName, boolean isAvailable, BufferedImage profilePicture) {
            this.displayName = displayName;
            this.messageHistory = new ArrayList<>();
            this.isSelected = false;
            this.isAvailable = isAvailable;
            this.profilePicture = profilePicture;
            
            
        }
        
        public BufferedImage getProfilePicture(){
            return profilePicture;
        }

        public String getName() {
            return displayName;
        }

        public ArrayList<Message> getMessages() {
            messageHistory = new ArrayList<>();
            try {
                PreparedStatement getMessagesStatement = Database.databaseConnection.prepareStatement("SELECT * FROM messages WHERE (senderUsername = ?  AND recieverUsername = ?) OR (senderUsername =? AND recieverUsername = ?)");
                getMessagesStatement.setString(1, displayName);
                getMessagesStatement.setString(2, MainFile.currentUserForAll.getUsername());
                getMessagesStatement.setString(3, MainFile.currentUserForAll.getUsername());
                getMessagesStatement.setString(4, displayName);
                ResultSet getMessagesRs = getMessagesStatement.executeQuery();
                while ( getMessagesRs.next() ){
                    messageHistory.add(new Message(getMessagesRs.getString("senderUsername"), getMessagesRs.getString("recieverUsername"), getMessagesRs.getString("message"), new Date(getMessagesRs.getTimestamp("date").getTime())));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }



            return messageHistory;
        }

        public void addMessage(String senderUsername, String recieverUsername,String content, Date timestamp) {
            Database.addToDatabase(new Message(senderUsername,recieverUsername,content,timestamp));
            this.messageHistory.add(new Message(content, recieverUsername, content, timestamp));
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        
        public boolean isAvailable() {
            return isAvailable;
        }
        
        public void setAvailable(boolean available) {
            isAvailable = available;
        }
        
       
    }
    
    // Helper method to load and scale an image
    private ImageIcon loadAndScaleIcon(String imagePath, int width, int height) 
    {
        try {
            // Try multiple locations for finding the image
            URL imageUrl = getClass().getResource("/" + imagePath);
            if (imageUrl == null) {
                imageUrl = getClass().getResource(imagePath);
            }
            
            if (imageUrl == null) {
                // Try absolute path as last resort
                ImageIcon originalIcon = new ImageIcon(imagePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
            // Return an empty icon as fallback
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }
    }
    
    // Constructor
    public ChatGUI() 
    {
        super(); 
        setLayout(new BorderLayout());
        
        initializeContacts();
        createContactsPanel();
        createChatPanel();
        
        add(contactsPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(1000, 600));

        
        Timer timer = new Timer(5000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new MessageUpdater().execute();
            }
            
        });
        timer.start();

        

    }
    
    /**
     * Creates test contacts for the application
     */
    private void initializeContacts() 
    {
        contactList = new ArrayList<>();
        
       
        try {
            PreparedStatement getContactsStatement = Database.databaseConnection.prepareStatement("SELECT * FROM contacts WHERE username1 = ? OR username2 = ?");
            getContactsStatement.setString(1, MainFile.currentUserForAll.getUsername());
            getContactsStatement.setString(2, MainFile.currentUserForAll.getUsername());
            ResultSet getContactsRs = getContactsStatement.executeQuery();
            while ( getContactsRs.next() ){

                String currentContactUsername = "";
                if ( getContactsRs.getString("username1").equalsIgnoreCase(MainFile.currentUserForAll.getUsername())){
                    currentContactUsername = getContactsRs.getString("username2");
                }
                else{
                    currentContactUsername = getContactsRs.getString("username1");
                }

                PreparedStatement getCurrentContactDetailsStatement = Database.databaseConnection.prepareStatement("SELECT * FROM users WHERE username = ?");
                getCurrentContactDetailsStatement.setString(1, currentContactUsername);
                ResultSet getCurrentContactDetailsRs = getCurrentContactDetailsStatement.executeQuery();
                if (getCurrentContactDetailsRs.next()){

                    BufferedImage profilePicture = null;

                    byte[] imageBytes = getCurrentContactDetailsRs.getBytes("profilePicture");
                    if (imageBytes != null) {
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(imageBytes);
                        try {
                            profilePicture = ImageIO.read(byteInputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } 
                    }

                    contactList.add(new Contact(currentContactUsername, getCurrentContactDetailsRs.getBoolean("available"), profilePicture));

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        // Select the first contact by default
        if (!contactList.isEmpty()) 
        {
            activeContact = contactList.get(0);
            activeContact.setSelected(true);
        }
    }
    
    /**
     * Creates the left panel showing the list of contacts
     */
    private void createContactsPanel()
    {
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder()); 
        
        JPanel contactButtonsPanel = new JPanel();
        contactButtonsPanel.setLayout(new BoxLayout(contactButtonsPanel, BoxLayout.Y_AXIS));
        
        // Create buttons for all contacts
        if (contactList != null) 
        {
            for (Contact contact : contactList) 
            {
                JPanel contactButton = createContactButton(contact);
                contactButtonsPanel.add(contactButton);
            }
        }
        
        // Add scrolling for contacts list
        contactsScrollPane = new JScrollPane(contactButtonsPanel);
        contactsScrollPane.setPreferredSize(new Dimension(160, getHeight()));
        contactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contactsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactsScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        contactButtonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        contactsPanel.add(contactsScrollPane);
    }
    
    /**
     * Creates a button for a single contact in the contacts list
     */
    private JPanel createContactButton(Contact contact) 
    {
        JPanel contactContainer = new JPanel();
        contactContainer.setLayout(new BorderLayout());
        contactContainer.setMaximumSize(new Dimension(150, 80));
        contactContainer.setPreferredSize(new Dimension(150, 80));
        contactContainer.setBackground(contact.isSelected() ? selectedContactColor : UIManager.getColor("Panel.background"));
        
        // Contact info display (avatar + name)
        JPanel contactInfoDisplay = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        contactInfoDisplay.setBackground(contactContainer.getBackground());
        
        // Create avatar circle with contact's initial
        JPanel avatarCircle = createAvatarCircle(contact, 40);
        contactInfoDisplay.add(avatarCircle);
        
        // Contact name label
        JLabel nameLabel = new JLabel(contact.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contactInfoDisplay.add(nameLabel);
        
        contactContainer.add(contactInfoDisplay, BorderLayout.CENTER);
        contactContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        // Add click handler for selecting this contact
        contactContainer.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Contact c : contactList) {
                    c.setSelected(c == contact);
                }
                activeContact = contact;
                updateContactDisplay();
            }
        });
        
        return contactContainer;
    }
    
    /**
     * Creates the avatar circle for a contact with their initial
     */
    private JPanel createAvatarCircle(Contact contact, int size) 
    {
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(contact.getProfilePicture(),0, 0, getWidth(), getHeight(),null);
                
            }
        };
        avatarCircle.setPreferredSize(new Dimension(size, size));
        return avatarCircle;
    }
    
    /**
     * Creates the main chat panel that displays messages and input field
     */
    private void createChatPanel() 
    {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatPanel.setBackground(UIManager.getColor("Panel.background"));
        
        // Header panel with contact info
        JPanel contactInfoPanel = createContactInfoPanel();
        chatPanel.add(contactInfoPanel, BorderLayout.NORTH);
        
        // Messages display area
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(UIManager.getColor("Panel.background"));
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Scrolling for messages
        messageScrollPane = new JScrollPane(messagesPanel);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        messageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatPanel.add(messageScrollPane, BorderLayout.CENTER);
        
        // Message input area at bottom
        JPanel inputPanel = createMessageInputPanel();
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        
        updateMessageDisplay();
    }
    
    /**
     * Creates the bottom panel with text area and send button
     */
    private JPanel createMessageInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        
        // Text area for typing messages
        JTextArea textArea = new JTextArea("Type your message here...");
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(2);
        
        textArea.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(2, 2, 2, 2),
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true)));
        
        textArea.addFocusListener(new FocusAdapter() 
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (textArea.getText().equals("Type your message here...")) {
                    textArea.setText("");
                    textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                    textArea.setForeground(Color.WHITE);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) 
            {
                if (textArea.getText().isEmpty()) {
                    textArea.setText("Type your message here...");
                    textArea.setFont(new Font("Arial", Font.ITALIC, 14));
                    textArea.setForeground(Color.ORANGE);
                }
            }
        });
        
        // Setup keyboard shortcuts for sending
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "send-action");
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "insert-break");
        
        textArea.getActionMap().put("send-action", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        // Add Ctrl+Enter shortcut
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }
            }
        });
        
        // Scrollable text area for longer messages
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
        textAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        //reference to text area for later use
        messageInputField = textArea;
        
        // Sbutton
        sendMessageButton = new JButton("Send");
        sendMessageButton.setPreferredSize(new Dimension(80, 30));
        sendMessageButton.setBackground(UIManager.getColor("Component.accentColor"));
        UIManager.put("Button.arc", 12);
        sendMessageButton.setForeground(Color.WHITE);
        sendMessageButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendMessageButton.addActionListener(e -> sendMessage());
        
        inputPanel.add(textAreaScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendMessageButton, BorderLayout.EAST);
        
        return inputPanel;
    }
    
    /**
     * Creates the header panel showing current contact info and status
     */
    private JPanel createContactInfoPanel() 
    {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        if (activeContact != null) 
        {
            // Left side - contact avatar and name
            JPanel contactInfoSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            contactInfoSection.setBackground(UIManager.getColor("Panel.background"));
            
            // Create avatar circle
            JPanel avatarCircle = createAvatarCircle(activeContact, 40);
            
            // Contact name
            JLabel nameLabel = new JLabel(activeContact.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            
            contactInfoSection.add(avatarCircle);
            contactInfoSection.add(nameLabel);
            
            // Right side - status indicator with icon and text
            JPanel statusSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
            statusSection.setBackground(UIManager.getColor("Panel.background"));
            
            // Status icon - home for available, coffee for away
            String statusIconFile = activeContact.isAvailable() ? "icons\\homeIcon.png" : "icons\\awayIcon.png";
            ImageIcon statusIcon = loadAndScaleIcon(statusIconFile, 16, 16);
            JLabel iconLabel = new JLabel(statusIcon);
            
            // Status text
            JLabel statusText = new JLabel(activeContact.isAvailable() ? "Available" : "Away");
            statusText.setFont(new Font("Arial", Font.PLAIN, 14));
            statusText.setForeground(activeContact.isAvailable() ? availableStatusColor : awayStatusColor);
            
            statusSection.add(iconLabel);
            statusSection.add(statusText);
            
            // Add both sections to header panel
            headerPanel.add(contactInfoSection, BorderLayout.WEST);
            headerPanel.add(statusSection, BorderLayout.EAST);
        }
        
        return headerPanel;
    }
    
    /**
     * Creates a UI component for displaying a single message
     */
    private JPanel createMessageItem(Message message) 
    {
        // Container for the entire message
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BorderLayout(10, 5));
        messageContainer.setBackground(UIManager.getColor("Panel.background"));
        messageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create avatar for message sender
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Use appropriate avatar color
                BufferedImage profilePicture = message.isSentByUser(MainFile.currentUserForAll) ? 
                    MainFile.currentUserForAll.getProfilePicture() : activeContact.getProfilePicture();
         
                g2d.drawImage(profilePicture,0, 0, getWidth(), getHeight(),null);
                
            }
        };
        avatarCircle.setPreferredSize(new Dimension(40, 40));
        
        // Header with avatar and sender name
        JPanel messageHeader = new JPanel(new BorderLayout(10, 0));
        messageHeader.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel senderNameLabel = new JLabel(message.isSentByUser(MainFile.currentUserForAll) ? "Me" : activeContact.getName());
        senderNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        messageHeader.add(avatarCircle, BorderLayout.WEST);
        messageHeader.add(senderNameLabel, BorderLayout.CENTER);
        
        // Message text content
        JTextArea messageText = new JTextArea(message.getMessageContent());
        messageText.setFont(new Font("Arial", Font.PLAIN, 14));
        messageText.setForeground(Color.WHITE);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);
        messageText.setBackground(UIManager.getColor("Panel.background"));
        messageText.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 10)); // Indent to align with name
        
        // Timestamp footer
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JLabel timestampLabel = new JLabel(dateFormatter.format(message.getTimestamp()));
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        timestampLabel.setForeground(Color.LIGHT_GRAY);
        timestampLabel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Indent for alignment
        
        // Assemble message components
        messageContainer.add(messageHeader, BorderLayout.NORTH);
        messageContainer.add(messageText, BorderLayout.CENTER);
        messageContainer.add(timestampLabel, BorderLayout.SOUTH);
        
        return messageContainer;
    }
    
    /**
     * Updates the message display panel with current contact's messages
     */
    private void updateMessageDisplay() {
        if (messagesPanel != null && activeContact != null) {
            messagesPanel.removeAll();
            
            // Use vertical layout for message list
            messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
            messagesPanel.setBackground(Color.lightGray);
            
            // Add each message to the display
            for (Message message : activeContact.getMessages()) {
                JPanel messageItem = createMessageItem(message);
                
                // Ensure message items span full width
                messageItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageItem.getPreferredSize().height));
                messageItem.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                messagesPanel.add(messageItem);
                messagesPanel.add(Box.createVerticalStrut(10)); // Space between messages
            }
            
            // Push messages to the top if there are few
            messagesPanel.add(Box.createVerticalGlue());
            
            scrollToBottom();
            
            messagesPanel.revalidate();
            messagesPanel.repaint();
        }
    }
    
    /**
     * Helper method to scroll the message panel to the bottom
     */
    private void scrollToBottom() 
    {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = messageScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            
            // Double invocation ensures scrolling works reliably 
            SwingUtilities.invokeLater(() -> {
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            });
        });
    }
    
    /**
     * Sends a message to the current contact and gets a simulated response
     */
    private void sendMessage() 
    {
        String messageText = messageInputField.getText();
        boolean isValidMessage = !messageText.isEmpty() && !messageText.equals("Type your message here...");
        
        if (isValidMessage && activeContact != null) {
            // Send user's message
            
            activeContact.addMessage(MainFile.currentUserForAll.getUsername(), activeContact.getName(), messageText, new Date());
            updateMessageDisplay();
            
            // Clear input and keep focus
            messageInputField.setText("");
            messageInputField.requestFocus();
            scrollToBottom();
            
        }
    }
    
    
    /**
     * Updates the UI when a different contact is selected
     */
    public void updateContactDisplay() 
    {
        if (contactsPanel != null) 
        {
            contactsPanel.removeAll();
        
            JPanel contactButtonsPanel = new JPanel();
            contactButtonsPanel.setLayout(new BoxLayout(contactButtonsPanel, BoxLayout.Y_AXIS));
            
            if (contactList != null) 
            {
                for (Contact contact : contactList) 
                {
                    JPanel contactButton = createContactButton(contact);
                    contactButtonsPanel.add(contactButton);
                }
            }
        
            // Create scrollable contacts list
            JScrollPane newContactsScrollPane = new JScrollPane(contactButtonsPanel);
            newContactsScrollPane.setPreferredSize(new Dimension(160, getHeight()));
            newContactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
            newContactsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            newContactsScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            contactButtonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            
            contactsPanel.add(newContactsScrollPane);
        }
        
        // Update the contact info header
        if (chatPanel != null) 
        {
            // Remove existing header
            Component[] components = chatPanel.getComponents();
            for (Component component : components) 
            {
                if (component instanceof JPanel && 
                    component.getBounds().height == 80) 
                {
                chatPanel.remove(component);
                break;
                }
            }
            chatPanel.add(createContactInfoPanel(), BorderLayout.NORTH);
        }
        updateMessageDisplay();
        revalidate();
        repaint();
    }
    
}
