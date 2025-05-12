import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
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
    

    static class Message 
    {
        private String messageContent;
        private boolean sentByUser;
        private Date messageTime;
        
        public Message(String messageContent, boolean sentByUser) {
            this.messageContent = messageContent;
            this.sentByUser = sentByUser;
            this.messageTime = new Date();
        }
        
        public String getContent() {
            return messageContent;
        }
        
        public boolean isSentByUser() {
            return sentByUser;
        }
        
        public Date getTimestamp() {
            return messageTime;
        }
    }
    
    static class Contact 
    {
        private String displayName;
        private ArrayList<Message> messageHistory;
        private boolean isSelected;
        private boolean isAvailable;
        private Color avatarColor;

        public Contact(String displayName, boolean isAvailable) {
            this.displayName = displayName;
            this.messageHistory = new ArrayList<>();
            this.isSelected = false;
            this.isAvailable = isAvailable;
            
            // Generate random avatar color
            Random random = new Random();
            this.avatarColor = new Color(
                100 + random.nextInt(155),
                100 + random.nextInt(155),
                100 + random.nextInt(155)
            );
        }

        public String getName() {
            return displayName;
        }

        public ArrayList<Message> getMessages() {
            return messageHistory;
        }

        public void addMessage(String content, boolean sentByUser) {
            this.messageHistory.add(new Message(content, sentByUser));
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
        
        public Color getAvatarColor() {
            return avatarColor;
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

        // Modern UI theme
        try {
            FlatCobalt2IJTheme.setup();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /**
     * Creates test contacts for the application
     */
    private void initializeContacts() 
    {
        contactList = new ArrayList<>();
        
        // Create 10 test contacts with random availability
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            boolean isAvailable = random.nextBoolean();
            Contact contact = new Contact("Contact " + i, isAvailable);
            contactList.add(contact);
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
                g2d.setColor(contact.getAvatarColor());
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Display first letter of contact name
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                String letter = contact.getName().substring(0, 1).toUpperCase();
                int letterWidth = fm.stringWidth(letter);
                int letterHeight = fm.getAscent();
                g2d.drawString(letter, (getWidth() - letterWidth) / 2, 
                              (getHeight() - letterHeight) / 2 + letterHeight);
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
            String statusIconFile = activeContact.isAvailable() ? "homeIcon.png" : "awayIcon.png";
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
                Color avatarColor = message.isSentByUser() ? 
                    new Color(100, 149, 237) : activeContact.getAvatarColor();
                g2d.setColor(avatarColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Draw initial
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();
                String letter = message.isSentByUser() ? "M" : activeContact.getName().substring(0, 1).toUpperCase();
                int letterWidth = fm.stringWidth(letter);
                int letterHeight = fm.getAscent();
                g2d.drawString(letter, (getWidth() - letterWidth) / 2, 
                              (getHeight() - letterHeight) / 2 + letterHeight);
            }
        };
        avatarCircle.setPreferredSize(new Dimension(40, 40));
        
        // Header with avatar and sender name
        JPanel messageHeader = new JPanel(new BorderLayout(10, 0));
        messageHeader.setBackground(UIManager.getColor("Panel.background"));
        
        JLabel senderNameLabel = new JLabel(message.isSentByUser() ? "Me" : activeContact.getName());
        senderNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        messageHeader.add(avatarCircle, BorderLayout.WEST);
        messageHeader.add(senderNameLabel, BorderLayout.CENTER);
        
        // Message text content
        JTextArea messageText = new JTextArea(message.getContent());
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
            activeContact.addMessage(messageText, true);
            updateMessageDisplay();
            
            // Clear input and keep focus
            messageInputField.setText("");
            messageInputField.requestFocus();
            scrollToBottom();
            
            // Simulate response after short delay
            simulateContactResponse();
        }
    }
    
    /**
     * Simulates a response from the contact after a delay (PLACEHOLDER, JUST FOR TESTING)
     */
    private void simulateContactResponse() {
        // Create a delayed response to simulate conversation
        Timer responseTimer = new Timer(1500, e -> 
        {
            String[] possibleResponses = {
                "That's interesting!",
                "I see what you mean.",
                "Thanks for sharing that.",
                "Let me think about that.",
                "I'll get back to you soon.",
                "Could you tell me more about that?",
                "I've been wondering about the same thing lately.",
                "That's exactly what I was thinking about yesterday when I was walking through the park and noticed how beautiful the sunset was."
            };
            
            // Choose random response
            Random random = new Random();
            String responseText = possibleResponses[random.nextInt(possibleResponses.length)];
            
            // Add contact's response
            activeContact.addMessage(responseText, false);
            updateMessageDisplay();
            scrollToBottom();
        });
        
        responseTimer.setRepeats(false);
        responseTimer.start();
    }//just for test
    
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
    
    /**
     * Main method to Test
     */
    public static void main(String[] args)
    {  
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame mainWindow = new JFrame("BILMART Chat");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ImprovedChatGUI chatInterface = new ImprovedChatGUI();
            mainWindow.add(chatInterface);
            
            mainWindow.pack();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);
        });
    }
}
