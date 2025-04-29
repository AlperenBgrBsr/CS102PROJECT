import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddAdvertScene extends JFrame implements ActionListener{
    JPanel mainPanel;

    Image uploadedImage; 

    JRadioButton lectureMaterial;
    JRadioButton cloth;
    JRadioButton other;

    JButton imageUploadButton;
    JButton addAdvertButton;

    JTextField titleField;
    JFormattedTextField priceField;
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

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(lectureMaterial);
        buttonGroup.add(cloth);
        buttonGroup.add(other);

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

        NumberFormat doubleFormat = NumberFormat.getNumberInstance();
        priceField = new JFormattedTextField(doubleFormat);
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
        if (e.getSource() == addAdvertButton) {
            //Add advert (later)
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
}
