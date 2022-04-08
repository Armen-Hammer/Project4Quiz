import java.util.*;
import java.io.*;
//Requires a "UsersInfo.txt" file to function properly
public class ControlFlow {
    public static String USER_INFO_FILE_NAME = "UsersInfo.txt";

    public static String WELCOME_MESSAGE = "Welcome to Quiz Manager!";
    public static String NOT_LOGGED_IN_OPTIONS =  "1.Log In\n2.Sign Up\n3.Quit";
    public static String SUCCESSFUL_LOGIN = "Login Successful!";
    public static String USER_TYPE_OPTIONS = "1.Student\n2.Teacher";
    public static String EXIT_MESSAGE = "Quiz manager shutting down...Have a great day!";
    public static String LOGGED_IN_TEACHER_OPTIONS ="1.Make a quiz\n2.Edit a quiz\n3.Delete quiz\n4.Edit username\n5.Edit password\n6.Logout\n7.Delete Account";
    public static String QUESTION_TYPES = "1.Multiple Choice\n2.True or False\n3.Fill in the blank";
    public static String QUESTION_TYPES_WITH_EXIT = "1.Multiple Choice\n2.True or False\n3.Fill in the blank\n4.Exit";

    public static String QUIZ_NAME_PROMPT = "What is the name of the quiz?";
    public static String QUESTION_PROMPT = "What is the the question you want to ask?";
    public static String FIRST_MC_CHOICE = "What is the value of the first choice: ";
    public static String SECOND_MC_CHOICE = "What is the value of the second choice: ";
    public static String THIRD_MC_CHOICE = "What is the value of the third choice: ";
    public static String FOURTH_MC_CHOICE = "What is the value of the fourth choice: ";
    public static String ANSWER_MC_PROMPT = "What is the answer for this question";




    public static int readInt(Scanner scanner, String prompt, int minVal, int maxVal) {
        int num = 0;
        boolean readInt = false;

        do {
            try {
                System.out.println(prompt);
                num = scanner.nextInt();

                if(num <= maxVal && num >= minVal) {
                    readInt = true;
                }
                else {
                    System.out.printf("Please enter a valid integer from %d to %d\n", minVal, maxVal);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Please enter a valid integer from %d to %d\n", minVal, maxVal);
            }
        } while (!readInt);

        return num;
    }


    public static MCquestion createMCquestion(Scanner scanner) {
        System.out.println(QUESTION_PROMPT);
        String prompt = scanner.nextLine();

        System.out.println(FIRST_MC_CHOICE);
        String choice1 = scanner.nextLine();

        System.out.println(SECOND_MC_CHOICE);
        String choice2 = scanner.nextLine();

        System.out.println(THIRD_MC_CHOICE);
        String choice3 = scanner.nextLine();

        System.out.println(FOURTH_MC_CHOICE);
        String choice4 = scanner.nextLine();

        int answer = readInt(scanner, ANSWER_MC_PROMPT + "\n1." + choice1 + "\n2." + choice2 + "\n3." + choice3 + "\n4." + choice4,1, 4);
        MCquestion question = new MCquestion(prompt, choice1, choice2, choice3, choice4, answer);

        return question;

    }

    public static Quiz createQuiz(Scanner scanner) {
        System.out.println(QUIZ_NAME_PROMPT);
        String quizName = scanner.nextLine();
        Quiz currentQuiz = new Quiz(quizName);

        int questionType = readInt(scanner, "What type of question do you want to add?\n" + QUESTION_TYPES, 1, 3);
        scanner.nextLine();

        while(questionType != 4) {
            if (questionType == 1) {
                currentQuiz.addQuestion(createMCquestion(scanner));
            }

            questionType = readInt(scanner, "What type of question do you want to add?\n" + QUESTION_TYPES_WITH_EXIT, 1, 4);

        }

        return currentQuiz;


    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String username;
        String password;

        int choice;
        boolean loggedIn = false;
        User currentUser = null;

        System.out.println(WELCOME_MESSAGE);
        choice = readInt(scanner, NOT_LOGGED_IN_OPTIONS, 1, 3);
        scanner.nextLine();



        ArrayList<User> users = new ArrayList<User>();
        ArrayList<Quiz> quizzes = new ArrayList<Quiz>();

        String line;
        try {
            File f = new File(USER_INFO_FILE_NAME);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");

                if (attributes[2].equals("Teacher")) {
                    users.add(new Teacher(username = attributes[0], password = attributes[1]));
                } else if (attributes[2].equals("Student")) {
                    //users.add(new Student(username=attributes[0], password=attributes[1])) FIXME
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {

            if (choice == 1) {

                System.out.println("Enter your username: ");
                username = scanner.nextLine();

                System.out.println("Enter your password: ");
                password = scanner.nextLine();

                if (users.contains(new User(username, password))) {
                    System.out.println(SUCCESSFUL_LOGIN);
                    loggedIn = true;
                } else {
                    System.out.println("Login failed");
                }

            }

            if (choice == 2) {
                int userType = readInt(scanner, "What type of account do you want to make?\n" + USER_TYPE_OPTIONS, 1,2);
                scanner.nextLine();

                boolean validUsername = false;
                boolean validPassword = false;
                do {
                    System.out.println("Enter a new username: ");
                    username = scanner.nextLine();
                    if (username.contains(",")) {
                        System.out.println("Usernames can not contain a comma, try again");
                    }
                    else {
                        validUsername = true;
                    }
                } while(!validUsername);

                do {
                    System.out.println("Enter a new password: ");
                    password = scanner.nextLine();
                    if (password.contains(",")) {
                        System.out.println("Passwords can not contain a comma, try again");
                    }
                    else {
                        validPassword = true;
                    }
                } while(!validPassword);

                if (userType == 1) {
                    currentUser = new Teacher(username, password);
                    users.add(currentUser);//fixme make student
                } else if (userType == 2) {
                    currentUser = new Teacher(username, password);
                    users.add(currentUser);
                }
                loggedIn = true;
                currentUser.toFile(USER_INFO_FILE_NAME);
            }

            if (loggedIn && currentUser instanceof Teacher) {
                int loggedInChoice = readInt(scanner, "What do you want to do\n" + LOGGED_IN_TEACHER_OPTIONS, 1,7);
                scanner.nextLine();

                switch(loggedInChoice) {
                    case 1:
                            Quiz quiz = createQuiz(scanner);
                            quizzes.add(quiz);
                            break;
                    case 2:
                        //FIXME
                        break;
                    case 3:
                        if (quizzes.size() == 0) {
                            System.out.println("There are no quizzes. You can't delete.");
                            break;
                        }
                        String listedQuizzes = "";
                        for (int i = 1; i < quizzes.size(); i++) {
                            listedQuizzes += String.format("%d.%s%n",i, quizzes.get(i));
                        }
                        int delIndex = readInt(scanner,"Which quiz do you want to delete?" + listedQuizzes, 1 ,quizzes.size());
                        quizzes.remove(delIndex);
                        break;
                    case 4:
                        boolean validUsername = false;
                        do {
                            System.out.println("Enter a new username: ");
                            username = scanner.nextLine();
                            if (username.contains(",")) {
                                System.out.println("Usernames can not contain a comma, try again");
                            }
                            else {
                                validUsername = true;
                            }
                        } while(!validUsername);
                        currentUser.setUsername(username);
                        break;
                    case 5:
                        boolean validPassword = false;
                        do {
                            System.out.println("Enter a new password: "); //maybe have them enter their password to be even allowed
                            password = scanner.nextLine();
                            if (password.contains(",")) {
                                System.out.println("Passwords can not contain a comma, try again");
                            }
                            else {
                                validPassword = true;
                            }
                        } while(!validPassword);
                        currentUser.setPassword(password);
                        break;
                    case 6:
                        loggedIn = false;
                        currentUser = null;
                        break;
                    case 7:
                        //FIXME
                        break;


                }
            }


            choice = readInt(scanner, NOT_LOGGED_IN_OPTIONS, 1, 3);
            scanner.nextLine();

            } while(choice != 3);

        System.out.println(EXIT_MESSAGE);
    }
}
