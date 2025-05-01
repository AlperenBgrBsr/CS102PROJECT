import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddAdvertScene extends JFrame implements ActionListener{
    JPanel mainPanel;

    Image uploadedImage = null; 

    JRadioButton lectureMaterial;
    JRadioButton cloth;
    JRadioButton other;

    ButtonGroup selectedTypeGroup;

    JButton imageUploadButton;
    JButton addAdvertButton;

    JTextField titleField;
    JTextField priceField;
    JTextArea informationArea;

    JComboBox<Character> currencies;

    JLabel imageLabel;

    final int fontSize = 18;
    final Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

    public AddAdvertScene() {
        this.setTitle("Add Advert");
        this.setSize(1024, 768);
        this.setIconImage(new ImageIcon("icons\\BilMartIcon.png").getImage());
        this.setVisible(true);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setPreferredSize(new Dimension(1024, 100));
        topPanel.setBackground( new Color(21,50,80));
        ImageIcon a = new ImageIcon("icons\\BilMart.png");
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(a.getImage().getScaledInstance(220, 40,Image.SCALE_SMOOTH)));
        topPanel.add(label);
        this.add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(1024, 550));
        mainPanel.add(addNamePanel());
        mainPanel.add(itemTypePanel());
        mainPanel.add(pricePanel());
        mainPanel.add(addInfoPanel());
        mainPanel.add(imageUploadPanel());

        addAdvertButton();
        mainPanel.add(addAdvertButton);    
        this.add(mainPanel);
        SwingUtilities.invokeLater(() -> {
            other.requestFocusInWindow();
        });
    }

    private JPanel addNamePanel() {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(1024, 80));

        titleField = new JTextField("Enter the title of the advert");
        titleField.setPreferredSize(new Dimension(350, 45));
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setFont(new Font("Arial", Font.ITALIC, fontSize));
        titleField.setOpaque(true);
        titleField.setBorder(blackBorder);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals("Enter the title of the advert")) {
                    titleField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (titleField.getText().trim().isEmpty()) {
                    titleField.setText("Enter the title of the advert");
                }
            }
        });

        wrapper.add(titleField);
        return wrapper;
    }

    private JPanel itemTypePanel() {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(1024, 30));
        wrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 30, 0, 30);

        JLabel itemLabel = new JLabel("Choose item type:");
        itemLabel.setFont(new Font("Arial", 0, fontSize));

        lectureMaterial = new JRadioButton("Lecture Material");
        lectureMaterial.setFont(new Font("Arial", Font.ITALIC, fontSize));
        cloth = new JRadioButton("Cloth");
        cloth.setFont(new Font("Arial", Font.ITALIC, fontSize));
        other = new JRadioButton("Other");
        other.setFont(new Font("Arial", Font.ITALIC, fontSize));

        lectureMaterial.setActionCommand("Lecture Material");
        cloth.setActionCommand("Cloth");
        other.setActionCommand("Other");
        selectedTypeGroup = new ButtonGroup();
        selectedTypeGroup.add(lectureMaterial);
        selectedTypeGroup.add(cloth);
        selectedTypeGroup.add(other);

        wrapper.add(itemLabel, gbc);
        wrapper.add(lectureMaterial, gbc);
        wrapper.add(cloth, gbc);
        wrapper.add(other, gbc);

        return wrapper;
    }

    private JPanel pricePanel() {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(1024, 70));
        wrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);

        JLabel itemLabel = new JLabel("Enter the price of the advert:");
        itemLabel.setFont(new Font("Arial", Font.ITALIC, fontSize));

        priceField = new JTextField();
        priceField.setText("0");
        priceField.setColumns(7);
        priceField.setPreferredSize(new Dimension(350, 40));
        priceField.setHorizontalAlignment(JTextField.CENTER);
        priceField.setFont(new Font("Arial", Font.BOLD, fontSize));
        priceField.setOpaque(true);
        priceField.setBorder(blackBorder);

        Character[] currenciesString = { '₺', '$', '€' };
        currencies = new JComboBox<>(currenciesString);
        currencies.setFont(new Font("Arial", Font.BOLD, fontSize));
        currencies.setOpaque(true);
        currencies.setBackground(Color.white);
        currencies.setFocusable(false);

        wrapper.add(itemLabel, gbc);
        wrapper.add(priceField, gbc);
        wrapper.add(currencies);

        return wrapper;
    }

    private JPanel addInfoPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(1024, 170));

        informationArea = new JTextArea("Enter detailed information about the item");
        informationArea.setFont(new Font("Arial", Font.ITALIC, fontSize - 5));
        informationArea.setOpaque(true);
        informationArea.setLineWrap(true);
        informationArea.setBorder(blackBorder);
        informationArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (informationArea.getText().equals("Enter detailed information about the item")) {
                    informationArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (informationArea.getText().trim().isEmpty()) {
                    informationArea.setText("Enter detailed information about the item");
                }
            }
        });
        JScrollPane scroll = new JScrollPane(informationArea);
        scroll.setPreferredSize(new Dimension(600, 150));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        wrapper.add(scroll);

        return wrapper;
    }

    private JPanel imageUploadPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setPreferredSize(new Dimension(1980,150));
        wrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(40, 30, 60, 30);

        JLabel itemLabel = new JLabel("Upload Advert Image:");
        itemLabel.setFont(new Font("Arial", Font.BOLD, fontSize + 4));

        imageUploadButton = new JButton("Select a File");
        imageUploadButton.setFont(new Font("Arial", Font.BOLD, fontSize + 4));
        imageUploadButton.setFocusable(false);
        imageUploadButton.setBackground(Color.lightGray);
        imageUploadButton.setFocusPainted(false);
        imageUploadButton.addActionListener(this);

        imageLabel = new JLabel();
        
        wrapper.add(itemLabel,gbc);
        wrapper.add(imageLabel, gbc);
        wrapper.add(imageUploadButton,gbc);

        return wrapper;
    }

    private void addAdvertButton() {
        addAdvertButton = new JButton("Add Advert");
        addAdvertButton.setPreferredSize(new Dimension(200,45));
        addAdvertButton.setFont(new Font("Arial", Font.BOLD, fontSize + 4));
        addAdvertButton.setFocusable(false);
        addAdvertButton.setFocusPainted(false);
        addAdvertButton.setBackground(new Color(151,12,16)); 
        addAdvertButton.setForeground(Color.white);
        addAdvertButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addAdvertButton) { //Add database checks later

            if (uploadedImage == null) {
                JOptionPane.showMessageDialog(null, "You must upload an image of your advert", "Invalid Image", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
                if (price < 0) {
                    throw new NumberFormatException("Negative value");
                }
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Entered price must be a non-negative number", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedType;
            ButtonModel selectedModel = selectedTypeGroup.getSelection();
            if (selectedModel != null) {
                selectedType = selectedModel.getActionCommand(); 
            } else {
                JOptionPane.showMessageDialog(null, "You must select a type for the advert", "No Type Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Title cannot be empty", "Missing Title", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Advert advert = new Advert(toBufferedImage(uploadedImage), titleField.getText(), price + " " + (Character) currencies.getSelectedItem(), informationArea.getText(), LoginScreen.getCurrentUser().getUsername(), LoginScreen.getCurrentUser().getIsAvailable(), selectedType); 
            JOptionPane.showMessageDialog(null, "You have successfully added the advert", "", JOptionPane.INFORMATION_MESSAGE);
            //advert.addToDatabase(); //Alper halledersin
            this.dispose();
        }
        if (e.getSource() == imageUploadButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an Image");

            // Only allow image files
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "png", "jpeg", "bmp"
            );
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String imagePath = selectedFile.getAbsolutePath();

                try {
                    uploadedImage = ImageIO.read(selectedFile);
                    if (uploadedImage == null) {
                        // ImageIO.read returns null if the file is not a known image type
                        JOptionPane.showMessageDialog(null, "The selected file is not a valid image.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ImageIcon imageIcon = new ImageIcon(uploadedImage);
                    imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(72 + (uploadedImage.getWidth(null) / 15),
                     72 + (uploadedImage.getHeight(null) / 15), Image.SCALE_SMOOTH)); //For now might change scaling later (maybe strict scaling)
                    imageLabel.setIcon(imageIcon);
                    System.out.println("Image loaded successfully: " + imagePath);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "An error occurred while loading the image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }     
        }
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage bufferedImage) {
            return bufferedImage;
        }

        // Make sure the image is fully loaded
        img = new ImageIcon(img).getImage();

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }      
}
