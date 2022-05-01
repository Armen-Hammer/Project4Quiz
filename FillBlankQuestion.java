import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class FillBlankQuestion extends Question implements Serializable {
    private String answer;

    public FillBlankQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer.toLowerCase();
    }

    public boolean check(String guess) {
        return guess.toLowerCase().equals(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillBlankQuestion that = (FillBlankQuestion) o;
        return answer.equals(that.answer) && question.equals(that.question);
    }

    public AnsweredQuestion getAnswer(Scanner scanner) { //return true if it is the right answer
        System.out.println(question);

        if (scanner.hasNext()) {
            scanner.nextLine(); //eat the newline char
        }
        String ans = scanner.nextLine();

        return new AnsweredQuestion(this, ans, check(ans));

    }


}