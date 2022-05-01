import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class QuizSubmission {
    private Quiz quiz;
    private String username;
    private Date quizStart;
    private Date quizEnd;
    private ArrayList < Question > questionsAsked;
    private ArrayList < AnsweredQuestion > answeredQuestions;

    public QuizSubmission(Quiz quiz, Scanner scanner, String username) {
        this.quiz = quiz;
        this.username = username;
        this.quizStart = new Date();
        this.questionsAsked = new ArrayList < > (quiz.getQuestions());
        this.answeredQuestions = new ArrayList < > ();

        Collections.shuffle(questionsAsked);

        int numCorrect = 0;
        for (int i = 0; i < questionsAsked.size(); i++) {
            AnsweredQuestion response = questionsAsked.get(i).getAnswer(scanner);
            if (response.isCorrect()) {
                numCorrect++;
            }
            answeredQuestions.add(response);
        }

        System.out.printf("You got a %d/%d on this quiz!\n", numCorrect, questionsAsked.size());

        quizEnd = new Date();
    }


    public QuizSubmission(Quiz quiz, String username) {
        this.quiz = quiz;
        this.username = username;
        this.quizStart = new Date();
        this.questionsAsked = new ArrayList < > (quiz.getQuestions());
        this.answeredQuestions = new ArrayList < > ();

        Collections.shuffle(questionsAsked);

        int numCorrect = 0;


        System.out.printf("You got a %d/%d on this quiz!\n", numCorrect, questionsAsked.size());

        quizEnd = new Date();
    }
    public String toString() {
        return String.format("%s done by %s: Finished on %s", quiz.getQuizName(), username, quizEnd);
    }

    public ArrayList < AnsweredQuestion > getAnsweredQuestions() {
        return answeredQuestions;
    }

}