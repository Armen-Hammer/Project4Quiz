import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Quiz implements Serializable {
    private String quizName;
    private ArrayList < Question > questions;
    private ArrayList < QuizSubmission > submissions;

    public Quiz(String quizName) {
        this.quizName = quizName;
        this.questions = new ArrayList < Question > ();
        this.submissions = new ArrayList < QuizSubmission > ();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public boolean deleteQuestion(Question q) {

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).equals(q)) {
                questions.remove(i); //FIXME, look into just passing the object into remove
                return true; //the question was removed
            }
        }
        return false; //the question was not in the quiz

    }

    public void deleteQuestion(int index) {
        questions.remove(index);
    }

    public void setQuestion(int index, Question q) {
        questions.set(index, q);
    }

    public ArrayList < Question > getQuestions() {
        return questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(quizName, quiz.quizName);
    }

    public String getQuizName() {
        return quizName;
    }


    public String toString() {
        String str = "";
        for (int i = 0; i < questions.size(); i++) {
            if (i != questions.size() - 1) {
                str += String.format("%d.%s%n", i + 1, questions.get(i).getQuestion());
            } else {
                str += String.format("%d.%s", i + 1, questions.get(i).getQuestion());
            }
        }
        return str;
    }

    public void addSubmission(QuizSubmission submission) {
        this.submissions.add(submission);
    }

    public ArrayList < QuizSubmission > getSubmissions() {
        return submissions;
    }

    public static Quiz fromFile(String fileName) {
        String line;
        String question;
        Quiz quiz = null;

        try {
            File f = new File(fileName);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            quiz = new Quiz(line);

            line = br.readLine();


            while (line != null) {

                switch (line) {
                    case "MC":
                        String c1, c2, c3, c4;
                        int answer;
                        question = br.readLine();
                        c1 = br.readLine();
                        c2 = br.readLine();
                        c3 = br.readLine();
                        c4 = br.readLine();
                        answer = Integer.parseInt(br.readLine());
                        quiz.addQuestion(new MCquestion(question, c1, c2, c3, c4, answer));
                        break;
                    case "TF":
                        question = br.readLine();
                        if (br.readLine().equals("2")) {
                            quiz.addQuestion(new TFquestion(question, false));
                        } else {
                            quiz.addQuestion(new TFquestion(question, true));
                        }
                        break;
                    case "FB":
                        question = br.readLine();
                        String blank = br.readLine();
                        quiz.addQuestion(new FillBlankQuestion(question, blank));
                        break;
                }


                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return quiz;
    }


}