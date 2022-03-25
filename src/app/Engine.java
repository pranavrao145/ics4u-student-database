/******************************************************************************
Program: Engine Class (Student Database)

Description: This is the engine for this app. This class contains all of the
methods to run the app itself, such as to intialize and run the app, to search
and sort, as well as to read and write to the log files.

Author: Pranav Rao

Date: March 24, 2022
*******************************************************************************/

package app; // declare this class as part of the app package

// import all necessary utilities
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
  // this ArrayList will be used to store the students. An ArrayList was used to
  // ensure there is no limit on how many people can be stored at once
  private static ArrayList<Student> students;

  // this is the GUI variable, which will keep track of the GUI associated with
  // this engine
  private static GUI gui;

  /**
   * This method is responsible for initializing the application. It will
   * initialize all the variables above with appropriate default values, read
   * the necessary data from the log files, then update the student table on the
   * GUI to reflect the newly read information.
   *
   * @param currentGUI - represents the current GUI associated with this Engine.
   *     This parameter is passed because it is needed to register the current
   *     GUI in the gui variable above
   */
  private static void initialize(final GUI currentGUI) {
    // intialize student ArrayList to empty
    students = new ArrayList<Student>();
    gui = currentGUI;   // set the gui of this engine to the GUI passed to the
                        // method
    readDataFromFile(); // read the data in the log files and register them with
                        // the program
    // update the GUI student table with the newly read information
    currentGUI.updateStudentViewTable(currentGUI.getTableViewStudents());
  }

  /**
   * This method will save the data currently in the students ArrayList to the
   * log file in data/. This method will be automatically called just before the
   * window closes.
   */
  public static void saveDataToFile() {
    // attempt to do the following, catching the exception if it fails
    try {
      // create a file object for the log file so we can work with them in the
      // program
      final File studentFile = new File("data/student_logs.txt");

      // delete the file if it exists (we want to overwrite it)
      studentFile.delete();

      // create the necessary parent directories and the new file
      studentFile.getParentFile().mkdirs();
      studentFile.createNewFile();

      // create PrintWriter object which will be used to write to the file
      final PrintWriter studentWriter =
          new PrintWriter(new FileWriter(studentFile, true));

      // for each of the students, write to the student log file a comma
      // separated list of the student's values (id, name, grade)
      for (final Student student : students) {
        final String content = String.format(
            "%d;%s;%d", student.getId(), student.getName(), student.getGrade());

        studentWriter.println(content);
      }

      studentWriter
          .close(); // close the student writer to prevent resource leaks

    } catch (
        final Exception
            e) { // if for whatever reason, the above fails, catch the exception
      e.printStackTrace(); // print the exception to the console
    }
  }

  /**
   *  This method will take read the data currently stored in the log files
   *  and update the ArrayList of students with the information that it gets.
   *  This method will be called upon the initialization of the program,
   * allowing for data persistence.
   */
  public static void readDataFromFile() {
    // attempt to do the following, catching the exception if it fails
    try {
      // create a file object for the log file so we can work with them in the
      // program
      final File studentFile = new File("data/student_logs.txt");

      if (studentFile.exists()) { // if the student log file exists
        // create a new buffered reader object
        final BufferedReader studentReader =
            new BufferedReader(new FileReader(studentFile));

        // read the first line from the file
        String line = studentReader.readLine();

        while (line != null) { // while the last line that was read is not empty
          // split the current line by semicolons (the character by which the
          // info is delimited) and store it in an ArrayList
          final ArrayList<String> currentStudentInfo =
              new ArrayList<String>(Arrays.asList(line.split(";")));
          // add the information gathered and stored in the ArrayList to the
          // master ArrayList containing all the students
          students.add(
              new Student(Integer.parseInt(currentStudentInfo.get(0)),
                          currentStudentInfo.get(1),
                          Integer.parseInt(currentStudentInfo.get(2))));

          line = studentReader
                     .readLine(); // read the next line of the student logs
        }

        studentReader.close(); // once all reading operations have finished,
                               // close the student reader
      }

    } catch (
        final Exception
            e) { // if for whatever reason, the above fails, catch the exception
      e.printStackTrace(); // print the exception to the console
    }
  }

  /**
   * This method will insert the given student into the correct position in an
   * already sorted students list. This ensures that, after the insertion is
   * complete, the list is still sorted.
   *
   * @param student - the student to insert
   */
  public static void insertSorted(final Student student) {
    // create two int variables. The first will store the id of the student to
    // be inserted. The second is starts a counter at 0, which will be used to
    // figure out the student's place in the list
    final int studentId = student.getId();
    int currentIndex = 0;

    // while the current index is not the same as the
    // size of the students array AND the student to
    // insert has an ID GREATER than the student at
    // the current index
    while (currentIndex != students.size() &&
           studentId > students.get(currentIndex).getId()) {
      currentIndex++; // keep incrementing the current index by 1
    }

    // at this point, the current index will be at the correct position to
    // insert the student or at the end of the list (which means the student has
    // the greatest id yet)

    // if the current index is at the end (same as the size of the array)
    if (currentIndex == students.size()) {
      students.add(student); // add the student to the end of hte list
    } else {                 // else if it is somewhere else in the list
      students.add(currentIndex, student); // add the student at the correct
                                           // index as determined earlier
    }
  }

  /**
   * This method sorts the students list of this class by id, in ascending
   * order. This is a simple insertion sort, with a worst-case time complexity
   * of O(n^2), which may make it inefficient on larger data sets.
   */
  public static void sortStudents() {
    // get the size of the students array
    final int size = students.size();

    // for every index starting from 1 and moving towards the size of the list
    for (int i = 1; i < size; i++) {
      final Student student = students.get(i); // get the student at the given index
      int j = i - 1; // calculate the value of ONE INDEX BEFORE the current
                     // index and store it in a variable

      // shift elements the array from index 0 to the current index - 1 to the
      // next position IF their ids are greater than the current student's id
      while (j >= 0 && students.get(j).getId() > student.getId()) {
        students.set(j + 1, students.get(j));
        j--;
      }

      students.set(j + 1, student); // put in the student object at the current
                                    // index at the correct (now empty) position
    }
  }

  /**
   * This is a recursive implementation of the binary search algorithm which
   * searches the student list by default. This returns the index of the student
   * with the given id if found, else returns -1
   *
   * @param studentId - the student id to search for
   * @param start - the start point to search from in the array
   * @param end - the point uptil which to search in the array
   * @return - index of the student with the given id if found, else -1
   */
  public static int binarySearchStudentsList(final int studentId, final int start,
                                             final int end) {
    // if the ending index is greater than or equal to the starting index
    // (meaning there is still more to search)
    if (end >= start) {
      final int mid = start + (end - start); // calculate the middle index

      // if the student at the middle index is the right student, return the
      // middle index
      if (students.get(mid).getId() == studentId)
        return mid;

      // if the student at the middle index has a student id more than the
      // desired student id, call the method recursively on the right sublist
      if (students.get(mid).getId() > studentId)
        return binarySearchStudentsList(studentId, start, mid - 1);

      // if the student at the middle index has a student id less than the
      // desired student id, call the method recursively on the left sublist
      return binarySearchStudentsList(studentId, mid + 1, end);
    }

    return -1; // if all fails and the element is not found for this method
               // call, return -1
  }

  /**
   * This method is a simple utility method to check if the id given is already
   * used in the students array
   *
   * @param studentId - the id to check
   * @return - true if the id IS unique, else false
   */
  public static boolean isIdUnique(final int studentId) {
    // use a binary search to determine if a student with the id is already in
    // the list
    return binarySearchStudentsList(studentId, 0, students.size() - 1) == -1
        ? true
        : false;
  }

  /**
   * This method is the entry point for this class. It will be called by the
   * main class to start the entire program
   *
   * @param currentGUI - represents the current GUI associated with this Engine.
   *     This parameter is passed because it is needed to register the current
   *     GUI in the gui variable above
   */
  public static void run(final GUI currentGUI) { initialize(currentGUI); }

  // necessary getters and setters

  public static ArrayList<Student> getStudents() { return students; }

  public static GUI getGUI() { return gui; }
}
