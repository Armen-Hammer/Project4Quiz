import java.io.Serial;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TFquestion extends Question implements Serializable {
    private boolean answer;

    TFquestion(String question, boolean answer) {
        this.answer = answer;
        this.question = question;
    }

    public boolean check(boolean answer) {
        return this.answer == answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFquestion that = (TFquestion) o;
        return answer == that.answer && this.question.equals(that.question);
    }


    public AnsweredQuestion getAnswer(Scanner scanner) { //return true if it is the right answer
        String prompt = question + "\n" + String.format("1.True\n2.False");

        int num = 0;
        boolean readInt = false;

        do {
            try {
                System.out.println(prompt);
                num = scanner.nextInt();

                if (num <= 2 && num >= 1) {
                    readInt = true;
                } else {
                    System.out.printf("Please enter a valid integer from %d to %d\n", 1, 2);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Please enter a valid integer from %d to %d\n", 1, 2);
            }
        } while (!readInt);


        boolean right;
        String ans;
        if (num == 1) {
            ans = "true";
            right = check(true);
        } else {
            ans = "false";
            right = check(false);
        }

        return new AnsweredQuestion(this, ans, right);

    }

}