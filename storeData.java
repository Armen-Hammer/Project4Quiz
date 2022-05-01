import java.io.*;
import java.util.ArrayList;

public class StoreData {

    //https://examples.javacodegeeks.com/core-java/io/fileoutputstream/how-to-write-an-object-to-file-in-java/

    //send course arraylist to CourseData.txt in the memory
    public static void storeCourseData(ArrayList < Course > courses) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CourseData.txt"))) {
            for (int i = 0; i < courses.size(); i++) {
                objectOutputStream.writeObject(courses.get(i));
                objectOutputStream.flush();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }




    //https://examples.javacodegeeks.com/core-java/io/file/how-to-read-an-object-from-file-in-java/

    //get course arraylist to CourseData.txt
    public static ArrayList < Course > readCourses() {
        ArrayList < Course > courses = new ArrayList < Course > ();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CourseData.txt"))) {
            Course courseRead = (Course) objectInputStream.readObject();

            while (courseRead != null) {
                courses.add(courseRead);
                courseRead = (Course) objectInputStream.readObject();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return courses;
    }
}