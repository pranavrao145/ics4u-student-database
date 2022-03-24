package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
  private static ArrayList<Student> students;

  private static GUI gui;

  private static void initialize(final GUI currentGUI) {
    students = new ArrayList<Student>();

    gui = currentGUI;

    readDataFromFile();

    currentGUI.updateStudentViewTable(currentGUI.getTableViewStudents());
  }

  public static void saveDataToFile() {

    try {
      final File studentFile = new File("data/student_logs.txt"),
                 teacherFile = new File("data/teacher_logs.txt"),
                 internationalStudentFile =
                     new File("data/international_student_logs.txt");

      studentFile.delete();
      teacherFile.delete();
      internationalStudentFile.delete();

      studentFile.getParentFile().mkdirs();
      studentFile.createNewFile();

      teacherFile.getParentFile().mkdirs();
      teacherFile.createNewFile();

      internationalStudentFile.getParentFile().mkdirs();
      internationalStudentFile.createNewFile();

      final PrintWriter studentWriter =
          new PrintWriter(new FileWriter(studentFile, true));

      for (final Student student : students) {
        final String content = String.format(
            "%d;%s;%d", student.getId(), student.getName(), student.getGrade());

        studentWriter.println(content);
      }

      studentWriter.close();

    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public static void readDataFromFile() {
    try {
      final File studentFile = new File("data/student_logs.txt");

      if (studentFile.exists()) {
        final BufferedReader studentReader =
            new BufferedReader(new FileReader(studentFile));

        String line = studentReader.readLine();

        while (line != null) {
          final ArrayList<String> currentStudentInfo =
              new ArrayList<String>(Arrays.asList(line.split(";")));

          students.add(
              new Student(Integer.parseInt(currentStudentInfo.get(0)),
                          currentStudentInfo.get(1),
                          Integer.parseInt(currentStudentInfo.get(2))));

          line = studentReader.readLine();
        }

        studentReader.close();
      }

    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public static void insertSorted(Student student) {
    int studentId = student.getId(), currentIndex = 0;

    while (currentIndex != students.size() &&
           studentId > students.get(currentIndex).getId()) {
      currentIndex++;
    }

    if (currentIndex == students.size()) {
      students.add(student);
      return;
    }

    students.add(currentIndex, student);
  }

  public static int binarySearchStudentsList(int studentId, int start,
                                             int end) {

    if (end >= start) {
      int mid = start + (end - start);

      if (students.get(mid).getId() == studentId)
        return mid;

      if (students.get(mid).getId() > studentId)
        return binarySearchStudentsList(studentId, start, mid - 1);

      return binarySearchStudentsList(studentId, mid + 1, end);
    }

    return -1;
  }

  public static void run(final GUI currentGUI) { initialize(currentGUI); }

  public static ArrayList<Student> getStudents() { return students; }

  public static GUI getGUI() { return gui; }
}
