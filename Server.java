import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
    public static final int TAKE_QUIZ = -8;
    public static final int ENTER_COURSE = -7;
    public static final int GET_QUESTIONS_STR = -5;
    public static final int SET_CURRENT_QUIZ = -4;
    public static final int SET_CURRENT_COURSE = -3;
    public static final int GET_QUIZZES_STR = -2;
    public static final int GET_COURSES_STR = -1;
    public static final int CREATE_STUDENT = 0;
    public static final int CREATE_TEACHER = 1;
    public static final int SIGN_IN = 2;
    public static final int MAKE_COURSE = 3;
    public static final int EDIT_COURSE = 4;
    public static final int DELETE_COURSE = 5;
    public static final int EDIT_USERNAME = 6;
    public static final int EDIT_PASSWORD = 7;
    public static final int LOGOUT = 8;
    public static final int DELETE_ACCOUNT = 9;
    public static final int CREATE_MC_QUESTION = 10;
    public static final int CREATE_TF_QUESTION = 11;
    public static final int CREATE_FB_QUESTION = 12;
    public static final int CREATE_QUIZ = 13;
    public static final int DELETE_QUIZ = 14;
    public static final int EDIT_QUIZ = 15;
    public static final int DELETE_QUESTION = 16;
    public static final int EDIT_QUESTION = 17;


    private static ArrayList < User > users = User.readFromFile("Usersinfo.txt");
    //private static ArrayList<Course> courses = new ArrayList<>(); //fixme
    private static ArrayList < Course > courses = StoreData.readCourses(); //fixme

    private static ArrayList < Session > sessions = new ArrayList < Session > ();
    private static int sessionNum = 0;

    Socket socket;
    Session session;

    public Server(Socket socket) {
        this.socket = socket;
        this.session = new Session(sessionNum++);

    }

    public void run() {
        System.out.printf("connection received from %s\n", socket);
        PrintWriter pw = null;
        //Scanner in = new Scanner(socket.getInputStream());
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        //Scanner in = new Scanner(socket.getInputStream());
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            pw = new PrintWriter(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!socket.isClosed()) {
            try {
                // socket open: make PrinterWriter and Scanner from it...

                // read from input, and echo output...

                int path = 0;
                try {
                    path = (Integer) ois.readObject();
                } catch (java.net.SocketException e) {
                    break;
                }

                boolean completed = true;
                User proposedUser = null;

                switch (path) {
                    case CREATE_STUDENT: //create a student
                        proposedUser = (User) ois.readObject();

                        for (User u: users) {
                            if (u.getUsername().equals(proposedUser.getUsername())) {
                                oos.writeObject("Try another username, it is already taken");
                                oos.flush();
                                completed = false;
                                break;
                            }
                        }
                        if (completed) {
                            oos.writeObject("Success");
                            oos.flush();
                            session.setUser(new Student(proposedUser.getUsername(), proposedUser.getPassword()));
                            users.add(session.getUser()); //FIXME make student
                            session.setLoggedIn(true);
                        }
                        Utils.reWriterUserFile(users, "UsersInfo.txt");
                        break;
                    case CREATE_TEACHER:
                        proposedUser = (User) ois.readObject();

                        for (User u: users) {
                            if (u.getUsername().equals(proposedUser.getUsername())) {
                                oos.writeObject("Try another username, it is already taken");
                                oos.flush();
                                completed = false;
                                break;

                            }
                        }
                        if (completed) {
                            oos.writeObject("Success");
                            oos.flush();
                            session.setUser(new Teacher(proposedUser.getUsername(), proposedUser.getPassword()));
                            users.add(session.getUser()); //FIXME make student
                            session.setLoggedIn(true);
                        }
                        Utils.reWriterUserFile(users, "UsersInfo.txt");
                        break;
                    case SIGN_IN:

                        proposedUser = (User) ois.readObject();

                        if (users.contains(proposedUser)) {
                            if (users.get(users.indexOf(proposedUser)) instanceof Student) {
                                oos.writeObject("S");
                            } else {
                                oos.writeObject("T");
                            }
                            session.setLoggedIn(true);
                            session.setUser(users.get(users.indexOf(proposedUser)));
                        } else {
                            oos.writeObject("We could not find an account with that login info");
                        }
                        oos.flush();
                        break;
                    case EDIT_USERNAME:

                        String newUsername = (String) ois.readObject();
                        if (newUsername.contains(",")) {
                            oos.writeObject("Usernames can not contain a comma");
                        } else {
                            session.getUser().setUsername(newUsername);
                            Utils.reWriterUserFile(users, "UsersInfo.txt");
                            oos.writeObject("Success");
                        }
                        oos.flush();

                        Utils.reWriterUserFile(users, "UsersInfo.txt");
                        break;
                    case EDIT_PASSWORD:
                        String newPassword = (String) ois.readObject();
                        if (newPassword.contains(",")) {
                            oos.writeObject("Passwords can not contain a comma");
                        } else {
                            session.getUser().setPassword(newPassword);
                            Utils.reWriterUserFile(users, "UsersInfo.txt");
                            oos.writeObject("Success");
                        }
                        oos.flush();

                        Utils.reWriterUserFile(users, "UsersInfo.txt");
                        break;
                    case MAKE_COURSE:

                        Course c = (Course) ois.readObject();
                        courses.add(c);
                        oos.writeObject("Success");

                        StoreData.storeCourseData(courses);
                        break;

                    case LOGOUT:
                        session.logout();
                        oos.writeObject("Success");
                        oos.flush();
                        break;
                    case DELETE_ACCOUNT:
                        users.remove(session.getUser());
                        Utils.reWriterUserFile(users, "Usersinfo.txt");
                        session.logout();
                        oos.writeObject("Success");
                        oos.flush();
                        break;
                    case GET_COURSES_STR:
                        if (courses.size() == 0) {
                            oos.writeObject("There are no courses made");
                            break;
                        }
                        oos.writeObject("Success");
                        ArrayList < String > courseListStr = new ArrayList < > ();
                        for (int i = 0; i < courses.size(); i++) {
                            courseListStr.add(String.format("%d.%s %s%n", i + 1, courses.get(i).getCourseName(), courses.get(i).getCourseNumber()));
                        }
                        oos.writeObject(courseListStr);
                        oos.flush();
                        break;
                    case GET_QUIZZES_STR:
                        if (session.getCurrentCourse().getQuizzes().size() == 0) {
                            oos.writeObject("There are no quizzes made");
                            break;
                        }
                        oos.writeObject("Success");
                        ArrayList < String > quizListStr = new ArrayList < > ();

                        for (int i = 1; i <= session.getCurrentCourse().getQuizzes().size(); i++) {
                            quizListStr.add(String.format("%d.%s%n", i, session.getCurrentCourse().getQuiz(i - 1).getQuizName()));
                        }
                        oos.writeObject(quizListStr);
                        oos.flush();
                        break;
                    case GET_QUESTIONS_STR:
                        if (session.getCurrentQuiz().getQuestionCount() == 0) {
                            oos.writeObject("There are no questions made");
                            break;
                        }
                        oos.writeObject("Success");
                        ArrayList < String > questionListStr = new ArrayList < > ();

                        for (int i = 1; i <= session.getCurrentQuiz().getQuestionCount(); i++) {
                            questionListStr.add(String.format("%d.%s%n", i, session.getCurrentQuiz().getQuestions().get(i - 1).getQuestion()));
                        }
                        oos.writeObject(questionListStr);
                        oos.flush();
                        break;

                    case DELETE_COURSE:
                        int index = (int) ois.readObject();
                        System.out.println("Remove " + index);
                        courses.remove(index);
                        oos.writeObject("Success");

                        StoreData.storeCourseData(courses);
                        break;
                    case CREATE_MC_QUESTION:
                        Question q = null;
                        q = (MCquestion) ois.readObject();

                        session.getCurrentQuiz().addQuestion(q);
                        StoreData.storeCourseData(courses);
                        break;
                    case CREATE_TF_QUESTION:
                        Question tfQ = null;

                        tfQ = (TFquestion) ois.readObject();

                        session.getCurrentQuiz().addQuestion(tfQ);
                        StoreData.storeCourseData(courses);
                        break;
                    case CREATE_FB_QUESTION:
                        Question fbQ = null;
                        fbQ = (FillBlankQuestion) ois.readObject();

                        session.getCurrentQuiz().addQuestion(fbQ);
                        StoreData.storeCourseData(courses);
                        break;
                    case CREATE_QUIZ:
                        Quiz quiz = null;

                        quiz = (Quiz) ois.readObject();

                        session.setCurrentQuiz(quiz);
                        session.getCurrentCourse().addQuiz(quiz);
                        oos.writeObject("Success");
                        StoreData.storeCourseData(courses);
                        break;
                    case DELETE_QUIZ:
                        int delIndex = (Integer) ois.readObject();
                        session.getCurrentCourse().removeQuiz(delIndex); //subtract 1 since you start at 1 instead of 0
                        session.setCurrentQuiz(null);
                        oos.writeObject("Success");
                        oos.flush();
                        StoreData.storeCourseData(courses);
                        break;
                    case SET_CURRENT_COURSE:
                        session.setCurrentCourse(courses.get((Integer) ois.readObject()));
                        break;
                    case SET_CURRENT_QUIZ:
                        session.setCurrentQuiz(session.getCurrentCourse().getQuiz((Integer) ois.readObject()));
                        break;
                    case EDIT_QUIZ:
                        break;
                    case DELETE_QUESTION:
                        session.getCurrentQuiz().deleteQuestion((Integer) ois.readObject());
                        StoreData.storeCourseData(courses);
                        break;
                    case TAKE_QUIZ:
                        oos.writeObject(session.getCurrentQuiz().getQuestions());
                        break;

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        // input done, close connections...
        System.out.println("closing");
        pw.close();
        try {
            oos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {


        ServerSocket serverSocket = new ServerSocket(4242);
        System.out.printf("socket open, waiting for connections on %s\n",
            serverSocket);

        // infinite server loop: accept connection,
        // spawn thread to handle...
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            new Thread(server).start();
        }


    }
}