import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuizCardBuilder {
    private ArrayList<QuizCard> cardList = new ArrayList<>();
    private JTextArea questCard = new JTextArea(6, 20);
    private JTextArea ansCard = new JTextArea(6, 20);
    JFrame frame = new JFrame();

    public QuizCardBuilder() {
        ansCard.setLineWrap(true);
        ansCard.setWrapStyleWord(true);

        questCard.setLineWrap(true);
        questCard.setWrapStyleWord(true);
    }

    private class NextCardListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            addNewCard();
        }
    }

    private class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // clear the cards and remove all occurrences in card list
            eraseCards();
            cardList.clear();
        }
    }

    private class SaveMenuListener  implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // brings up a save file box, saves the set
            addNewCard();

            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(QuizCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        }
        catch (IOException ex) {
            System.out.println("Couldn't write out the card list");
            ex.printStackTrace();
        }
    }

    protected void eraseCards() {
        // erases text from the cards
        ansCard.setText("");
        questCard.setText("");
        questCard.requestFocus();
    }

    protected void addNewCard() {
        String questText = questCard.getText();
        if(!questText.isBlank()) {
            String ansText = ansCard.getText();
            if(!ansText.isBlank()) {
                cardList.add(new QuizCard(questText, ansText));
                eraseCards();
            }
            else {
                System.out.println("Cannot add card because answer is blank!");
            }
        }
        else {
            System.out.println("Cannot add card because question is blank!");
        }
    }

    public void run() {
        // creates and displays interface for builder
        JPanel panel = new JPanel();

        Font bigFont  = new Font("sanserif", Font.BOLD, 24);
        questCard.setFont(bigFont);
        ansCard.setFont(bigFont);

        JScrollPane questScroll = new JScrollPane(questCard);
        questScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        questScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane ansScroll = new JScrollPane(ansCard);
        ansScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        ansScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton button1 = new JButton("Next Card");
        button1.addActionListener(new NextCardListener());

        panel.add(new JLabel("Question"));
        panel.add(questScroll);
        panel.add(new JLabel("Answer"));
        panel.add(ansScroll);
        panel.add(button1);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        newMenuItem.addActionListener(new NewMenuListener());
        saveMenuItem.addActionListener(new SaveMenuListener());

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        frame.getContentPane().add(panel);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 550);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.run();
    }

}
