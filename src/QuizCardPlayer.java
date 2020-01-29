import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class QuizCardPlayer {
    private ArrayList<QuizCard> cardList = new ArrayList<>();
    private QuizCard currentCard;
    private int currentCardIndex = 0;
    private boolean displayAnsFlag = false;
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private JButton button1 = new JButton("Show question");
    private JTextArea textCard = new JTextArea(6, 20);

    public QuizCardPlayer() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class NextCardListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            // if this is a question, show the answer
            if(displayAnsFlag) {
                if (currentCard != null) {
                    textCard.setText("Answer: " + currentCard.getAnswer());
                    button1.setText("Next question");
                    displayAnsFlag = false;
                }
            }
            // otherwise, show next question
            else {
                updateCard();
                if (currentCard != null) {
                    textCard.setText("Question: " + currentCard.getQuestion());
                    button1.setText("Show answer");
                    displayAnsFlag = true;
                }
                else {
                    textCard.setText("No questions available");
                    button1.setText("Bye!");
                }

            }
        }
    }

    class OpenMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            // bring up a file dialog box, open card file
            JFileChooser fileOpen  = new JFileChooser();
            fileOpen.showSaveDialog(frame);
            loadFile(fileOpen.getSelectedFile());
            button1.setText("Show question");
            textCard.setText("");
        }
    }

    class NewMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            resetGame();
        }
    }

    private void resetGame() {
        textCard.setText("");
        button1.setText("Show question");
        currentCard = null;
        cardList.clear();
        currentCardIndex = 0;
    }

    private void updateCard() {
        if(currentCardIndex < cardList.size()) {
            this.currentCard = cardList.get(currentCardIndex);
            currentCardIndex++;
        }
        else {
            this.currentCard = cardList.get(0);
            currentCardIndex = 0;
        }
    }

    private void loadFile(File file) {
        // buid an ArrayList of cards reading then from a file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeCard(String lineToParse) {
        // takes a line from text file, parses into question and answer and add to card list
        String[] sep = lineToParse.split("/");
        this.cardList.add(new QuizCard(sep[0], sep[1]));
    }

    public void run() {

        textCard.setLineWrap(true);
        textCard.setWrapStyleWord(true);

        Font bigFont = new Font("sanserif", Font.BOLD, 24);
        textCard.setFont(bigFont);

        JScrollPane questScroll = new JScrollPane(textCard);
        questScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        questScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        button1.addActionListener(new NextCardListener());

        panel.add(questScroll);
        panel.add(button1);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        newMenuItem.addActionListener(new NewMenuListener());
        loadMenuItem.addActionListener(new OpenMenuListener());

        fileMenu.add(newMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.setSize(450, 450);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.run();
    }
}
