import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;

public class ImprovedChatGUI extends JPanel 
{
    private JPanel contactsPanel;
    private JPanel chatPanel;
    private JPanel messagesPanel;
    private JTextArea inputField; // Changed from JTextField to JTextArea for word wrap
    private JButton sendButton;
    private JScrollPane messageScrollPane;
    private JScrollPane contactsScrollPane;
    private ArrayList<Contact> contacts;
    private Contact currentContact;
    private Color selectionColor = new Color(75, 75, 255); // light blue
    private Color sentBubbleColor = new Color(75, 110, 175); // darker blue for better contrast with white text
    private Color receivedBubbleColor = new Color(240, 240, 240); // light gray
    
    static class Message {
        private String content;
        private boolean isSent;
        private Date timestamp;
        
        public Message(String content, boolean isSent) {
            this.content = content;
            this.isSent = isSent;
            this.timestamp = new Date();
        }
        
        public String getContent() {
            return content;
        }
        
        public boolean isSent() {
            return isSent;
        }
        
        public Date getTimestamp() {
            return timestamp;
        }
    }
    
    static class Contact 
    {
        private String name;
        private ArrayList<Message> messages;
        private boolean isSelected;
        private boolean isOnline;
        private Color avatarColor;

        public Contact(String name, boolean isOnline) {
            this.name = name;
            this.messages = new ArrayList<>();
            this.isSelected = false;
            this.isOnline = isOnline;
            
            // Random color for avatar
            Random random = new Random();
            this.avatarColor = new Color(
                100 + random.nextInt(155),
                100 + random.nextInt(155),
                100 + random.nextInt(155)
            );
        }

        public String getName() {
            return name;
        }

        public ArrayList<Message> getMessages() {
            return messages;
        }

        public void addMessage(String content, boolean isSent) {
            this.messages.add(new Message(content, isSent));
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        
        public boolean isOnline() {
            return isOnline;
        }
        
        public void setOnline(boolean online) {
            isOnline = online;
        }
        
        public Color getAvatarColor() {
            return avatarColor;
        }
    }
    
    public ImprovedChatGUI() 
    {
        super(); 
        
        setLayout(new BorderLayout());
        
        initializeContacts();
        
        // UI components
        createContactsPanel();
        createChatPanel();
        
        add(contactsPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(1000, 600));

        try {
            FlatCobalt2IJTheme.setup();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    private void initializeContacts() 
    {
        contacts = new ArrayList<>();
        
        // Adding Contacts for testing - reduced to 10 contacts
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            boolean isOnline = random.nextBoolean();
            Contact contact = new Contact("Contact " + i, isOnline);
            
            // Removed placeholder fixed messages
            
            contacts.add(contact);
        }
        
        // Set the first contact as selected by default
        if (!contacts.isEmpty()) 
        {
            currentContact = contacts.get(0);
            currentContact.setSelected(true);
        }
    }
    
    private void createContactsPanel()
    {
        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder()); 
        
        // Contact Panel
        JPanel contactButtonsPanel = new JPanel();
        contactButtonsPanel.setLayout(new BoxLayout(contactButtonsPanel, BoxLayout.Y_AXIS));
        
        // Creating contact buttons 
        if (contacts != null) 
        {
            for (Contact contact : contacts) 
            {
                JPanel contactButton = createContactButton(contact);
                contactButtonsPanel.add(contactButton);
            }
        }
        
        // Scroll pane for contacts list
        contactsScrollPane = new JScrollPane(contactButtonsPanel);
        contactsScrollPane.setPreferredSize(new Dimension(160, getHeight()));
        contactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contactsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        contactsScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        contactButtonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        contactsPanel.add(contactsScrollPane);
    }
    
    private JPanel createContactButton(Contact contact) 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        panel.setMaximumSize(new Dimension(150, 80));
        panel.setPreferredSize(new Dimension(150, 80));
        
        panel.setBackground(contact.isSelected() ? selectionColor : UIManager.getColor("Panel.background"));
        
        JPanel contactInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        contactInfo.setBackground(panel.getBackground());
        
        // Contact avatar circle
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
        avatarCircle.setPreferredSize(new Dimension(40, 40));
        contactInfo.add(avatarCircle);
        
        // Contact name 
        JLabel nameLabel = new JLabel(contact.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contactInfo.add(nameLabel);
        
        panel.add(contactInfo, BorderLayout.CENTER);
        
        // Border for contact button panel
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        panel.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Contact c : contacts) {
                    c.setSelected(c == contact);
                }
                currentContact = contact;
                updateContactDisplay();
            }
        });
        
        return panel;
    }
    
    private void createChatPanel() 
    {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        chatPanel.setBackground(Color.WHITE);
        
        // Contact info panel that shows up when a contact is selected
        JPanel contactInfoPanel = createContactInfoPanel();
        chatPanel.add(contactInfoPanel, BorderLayout.NORTH);
        
        // Messages panel (replacing the JTextArea with custom messages panel)
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Color.WHITE);
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Scroll pane for messages panel
        messageScrollPane = new JScrollPane(messagesPanel);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        messageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatPanel.add(messageScrollPane, BorderLayout.CENTER);
        
        // Input panel (textfield + "send" button)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        
        // Input field (Message Box) - Using JTextArea instead of JTextField for word wrap
        JTextArea textArea = new JTextArea("Type your message here...");
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(2);
        
        // Add rounded border similar to JTextField's roundRect
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
                    textArea.setForeground(Color.WHITE);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) 
            {
                if (textArea.getText().isEmpty()) {
                    textArea.setText("Type your message here...");
                    textArea.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        
        // Setup keyboard shortcut for Enter to send and Shift+Enter for new line
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "send-action");
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "insert-break");
        
        // Define the send action
        textArea.getActionMap().put("send-action", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        // Keep Ctrl+Enter working too
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    e.consume();
                }
            }
        });
        
        // Use JScrollPane for the text area to handle overflow elegantly
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
        textAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Store the JTextArea reference for use in sendMessage()
        inputField = textArea;
        
        // Send button
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30));
        sendButton.setBackground(UIManager.getColor("Component.accentColor"));
        UIManager.put("Button.arc", 12);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        sendButton.addActionListener(e -> sendMessage());
        
        inputPanel.add(textAreaScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        
        updateMessageDisplay();
    }
    
    private JPanel createContactInfoPanel() 
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        panel.setPreferredSize(new Dimension(getWidth(), 80));
        
        if (currentContact != null) 
        {
            JPanel contactDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            
            // Contact avatar circle
            JPanel avatarCircle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(currentContact.getAvatarColor());
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                    
                    // Display first letter of contact name
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    FontMetrics fm = g2d.getFontMetrics();
                    String letter = currentContact.getName().substring(0, 1).toUpperCase();
                    int letterWidth = fm.stringWidth(letter);
                    int letterHeight = fm.getAscent();
                    g2d.drawString(letter, (getWidth() - letterWidth) / 2, 
                                   (getHeight() - letterHeight) / 2 + letterHeight);
                }
            };
            avatarCircle.setPreferredSize(new Dimension(40, 40));
            contactDetailsPanel.add(avatarCircle);
            
            // Contact name and status in a vertical layout
            JPanel nameStatusPanel = new JPanel(new GridLayout(2, 1));
            
            JLabel nameLabel = new JLabel(currentContact.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameStatusPanel.add(nameLabel);
            
            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            
            // Status circle - green for online, red for offline
            JPanel statusCircle = new JPanel() 
            {
                @Override
                protected void paintComponent(Graphics g) 
                {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(currentContact.isOnline() ? new Color(0, 180, 0) : Color.RED);
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            statusCircle.setPreferredSize(new Dimension(10, 10));
            
            // Status text
            JLabel statusText = new JLabel(currentContact.isOnline() ? "Online" : "Offline");
            statusText.setFont(new Font("Arial", Font.PLAIN, 12));
            statusText.setForeground(currentContact.isOnline() ? new Color(0, 180, 0) : Color.RED);
            
            statusPanel.add(statusCircle);
            statusPanel.add(statusText);
            
            nameStatusPanel.add(statusPanel);
            
            contactDetailsPanel.add(nameStatusPanel);
            panel.add(contactDetailsPanel, BorderLayout.WEST);
        }
        
        return panel;
    }
    
    private JPanel createMessageBubble(Message message) {
        JPanel bubblePanel = new JPanel();
        bubblePanel.setLayout(new BorderLayout(5, 0));
        bubblePanel.setOpaque(false);
        
        // Main message content in bubble
        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Set color based on sent/received
                g2d.setColor(message.isSent() ? sentBubbleColor : receivedBubbleColor);
                
                // Draw rounded rectangle
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        
        //  bubble content
        bubble.setLayout(new BorderLayout());
        bubble.setOpaque(false);
        bubble.setBorder(new EmptyBorder(8, 12, 8, 12));  // Reduced padding
        
        
        JTextArea messageText = new JTextArea(message.getContent());
        messageText.setFont(new Font("Arial", Font.PLAIN, 13));  // Smaller font
        messageText.setForeground(message.isSent() ? Color.WHITE : Color.BLACK);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);
        messageText.setOpaque(false);
        messageText.setBorder(null);
        
        
        int maxWidth = 500;  // Increased from 350 
        int minWidth = 60;   // Min width for any bubble
        
        
        int contentLength = message.getContent().length();
        
        int preferredWidth = Math.min(maxWidth, Math.max(minWidth, 
                                      contentLength * 7 + 50)); 
        
        messageText.setPreferredSize(new Dimension(preferredWidth, messageText.getPreferredSize().height));
        messageText.setSize(preferredWidth, Short.MAX_VALUE);
        
        bubble.add(messageText, BorderLayout.CENTER);
        
        // Avatar for the message - smaller size
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Enable antialiasing for smooth edges
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                
                g2d.setColor(getParent().getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw avatar circle
                Color avatarColor = message.isSent() ? new Color(100, 149, 237) : currentContact.getAvatarColor();
                g2d.setColor(avatarColor);
                g2d.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                // Display first letter
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));  
                FontMetrics fm = g2d.getFontMetrics();
                String letter = message.isSent() ? "M" : currentContact.getName().substring(0, 1).toUpperCase();
                int letterWidth = fm.stringWidth(letter);
                int letterHeight = fm.getAscent();
                g2d.drawString(letter, (getWidth() - letterWidth) / 2, 
                              (getHeight() - letterHeight) / 2 + letterHeight);
            }
            
            
            @Override
            public boolean isOpaque() 
            {
                return false;
            }
        };
        avatar.setPreferredSize(new Dimension(20, 20));  // Smaller avatar
        
        // Timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel timestampLabel = new JLabel(sdf.format(message.getTimestamp()));
        timestampLabel.setFont(new Font("Arial", Font.PLAIN, 9));  // Smaller timestamp
        timestampLabel.setForeground(Color.GRAY);
        
        JPanel timestampPanel = new JPanel(new FlowLayout(message.isSent() ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        timestampPanel.setOpaque(false);
        timestampPanel.add(timestampLabel);
        
        // Create a panel for the bubble with timestamp below
        JPanel bubbleWithTimestamp = new JPanel();
        bubbleWithTimestamp.setLayout(new BoxLayout(bubbleWithTimestamp, BoxLayout.Y_AXIS));
        bubbleWithTimestamp.setOpaque(false);
        bubbleWithTimestamp.add(bubble);
        bubbleWithTimestamp.add(timestampPanel);
        
        // Layout components with avatar at the top
        if (message.isSent()) {
            // Your messages (right side with avatar on top right)
            JPanel messageContent = new JPanel(new BorderLayout(5, 0));
            messageContent.setOpaque(false);
            
            JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            topRightPanel.setOpaque(false);
            topRightPanel.add(avatar);
            
            messageContent.add(topRightPanel, BorderLayout.NORTH);
            messageContent.add(bubbleWithTimestamp, BorderLayout.CENTER);
            
            bubblePanel.add(Box.createHorizontalGlue(), BorderLayout.WEST);
            bubblePanel.add(messageContent, BorderLayout.EAST);
        } 
        else {
            // Their messages (left side with avatar on top left)
            JPanel messageContent = new JPanel(new BorderLayout(5, 0));
            messageContent.setOpaque(false);
            
            JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            topLeftPanel.setOpaque(false);
            topLeftPanel.add(avatar);
            
            messageContent.add(topLeftPanel, BorderLayout.NORTH);
            messageContent.add(bubbleWithTimestamp, BorderLayout.CENTER);
            
            bubblePanel.add(messageContent, BorderLayout.WEST);
            bubblePanel.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        }
        
        return bubblePanel;
    }
    
    private void updateMessageDisplay() {
        if (messagesPanel != null && currentContact != null) {
            messagesPanel.removeAll();
            
            JPanel messageContainer = new JPanel();
            messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
            messageContainer.setOpaque(false);
            messageContainer.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to left side
            
            
            int containerWidth = messageScrollPane.getViewport().getWidth();
            if (containerWidth <= 0) containerWidth = 500; // Default width if not yet rendered
            
            for (Message message : currentContact.getMessages()) {
                JPanel messageBubble = createMessageBubble(message);
                
                // Make bubbles align left/right
                messageBubble.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                messageContainer.add(messageBubble);
                messageContainer.add(Box.createVerticalStrut(12)); // Slightly reduced spacing between messages
            }
            
    
            messageContainer.add(Box.createVerticalGlue()); // Add a glue at the end to push messages to the top when there are few messages
            
            messagesPanel.add(messageContainer);
        
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
            JScrollBar vertical = messageScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
            SwingUtilities.invokeLater(() -> {
                vertical.setValue(vertical.getMaximum());
            });
        });
    }
    
    private void sendMessage() 
    {
        String message = inputField.getText();
        if (!message.isEmpty() && !message.equals("Type your message here...")) 
        {
            // Add message to the contact's message list
            if (currentContact != null) {
                currentContact.addMessage(message, true);
                
                updateMessageDisplay();
                
                inputField.setText("");
                inputField.requestFocus(); // Keep focus on input field after sending
                
                scrollToBottom();
                
                // Simulate a response after a delay (just for demo)
                Timer timer = new Timer(1500, e -> 
                {
                    String[] responses = {
                        "That's interesting!",
                        "I see what you mean.",
                        "Thanks for sharing that.",
                        "Let me think about that.",
                        "I'll get back to you soon.",
                        "Could you tell me more about that?",
                        "I've been wondering about the same thing lately.",
                        "That's exactly what I was thinking about yesterday when I was walking through the park and noticed how beautiful the sunset was."
                    };
                    Random random = new Random();
                    String response = responses[random.nextInt(responses.length)];
                    currentContact.addMessage(response, false);
                    updateMessageDisplay();
                    
                    // Make sure we scroll to the bottom after receiving a message too
                    scrollToBottom();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    public void updateContactDisplay() 
    {
        if (contactsPanel != null) 
        {
            contactsPanel.removeAll();
        
            JPanel contactButtonsPanel = new JPanel();
            contactButtonsPanel.setLayout(new BoxLayout(contactButtonsPanel, BoxLayout.Y_AXIS));
            
            if (contacts != null) 
            {
                for (Contact contact : contacts) 
                {
                    JPanel contactButton = createContactButton(contact);
                    contactButtonsPanel.add(contactButton);
                }
            }
        
            JScrollPane contactsScrollPane = new JScrollPane(contactButtonsPanel);
            contactsScrollPane.setPreferredSize(new Dimension(160, getHeight()));
            contactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
            contactsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            contactsScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            contactButtonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            
            contactsPanel.add(contactsScrollPane);
        }
        
        // Update the contact info panel
        if (chatPanel != null) 
        {
            Component[] components = chatPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel && 
                    component.getBounds().height == 80) {
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
    
    public static void main(String[] args) // TO TEST
    {  
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("BILMART Chat");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ImprovedChatGUI chatGUI = new ImprovedChatGUI();
            frame.add(chatGUI);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}