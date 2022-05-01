import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Course implements Serializable {
    private ArrayList < Quiz > quizzes;
    private String courseName;
    private int courseNumber;

    public Course(String courseName, int courseNumber) {
        quizzes = new ArrayList < Quiz > ();
        this.courseName = courseName;
        this.courseNumber = courseNumber;
    }

    public ArrayList < Quiz > getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(ArrayList < Quiz > quizzes) {
        this.quizzes = quizzes;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < quizzes.size(); i++) {
            if (i != quizzes.size() - 1) {
                str += String.format("%d.%s%n", i + 1, quizzes.get(i).getQuizName());
            } else {
                str += String.format("%d.%s", i + 1, quizzes.get(i).getQuizName());
            }
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseNumber == course.courseNumber && Objects.equals(courseName, course.courseName); //two can't have the same name and number
    }

    public Quiz getQuiz(int index) {
        return quizzes.get(index);
    }

    public void removeQuiz(int index) {
        quizzes.remove(index);
    }
    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public int getQuizCount() {
        return quizzes.size();
    }

}