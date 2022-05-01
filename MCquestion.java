import java.io.Serial;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class MCquestion extends Question implements Serializable {
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private int answer;

    public int MC_A = 1;
    public int MC_B = 2;
    public int MC_C = 3;
    public int MC_D = 4;


    public MCquestion(String question, String choiceA, String choiceB, String choiceC, String choiceD, int answer) {
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.answer = answer;
        this.question = question;
    }

    public boolean check(int guess) {
        return guess == answer;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MCquestion that = (MCquestion) o;
        return answer == that.answer && choiceA.equals(that.choiceA) && choiceB.equals(that.choiceB) && choiceC.equals(that.choiceC) && choiceD.equals(that.choiceD) && question == that.question;
    }


    public AnsweredQuestion getAnswer(Scanner scanner) { //return true if it is the right answer
        String prompt = question + "\n" + String.format("1.%s%n", choiceA) + String.format("2.%s%n", choiceB) + String.format("3.%s%n", choiceC) + String.format("4.%s", choiceD);

        int num = 0;
        boolean readInt = false;

        do {
            try {
                System.out.println(prompt);
                num = scanner.nextInt();

                if (num <= 4 && num >= 1) {
                    readInt = true;
                } else {
                    System.out.printf("Please enter a valid integer from %d to %d\n", 1, 4);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Please enter a valid integer from %d to %d\n", 1, 4);
            }
        } while (!readInt);

        String ans = null;

        switch (num) {
            case 1:
                ans = choiceA;
                break;
            case 2:
                ans = choiceB;
                break;
            case 3:
                ans = choiceC;
                break;
            case 4:
                ans = choiceD;
                break;
        }

        return new AnsweredQuestion(this, ans, check(num));
    }
}