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
        final String content =
            String.format("%s;%d", student.getName(), student.getGrade());

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

  public static void run(final GUI currentGUI) { initialize(currentGUI); }

  public static ArrayList<Student> getStudents() { return students; }

  public static GUI getGUI() { return gui; }
}