public class QuizCard {
    private String question;
    private String answer;

    public QuizCard(String quest, String ans) {
        this.question = quest;
        this.answer = ans;
    }

    public String getQuestion(){return this.question;}
    public String getAnswer() {return this.answer;}
}
