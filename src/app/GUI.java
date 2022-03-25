/******************************************************************************
Program: GUI (Student Database)

Description: This is GUI for this app. This class contains all of the
methods to run the GUI of the app, such as to draw and update the GUI,
as well as to attach listeners.

Author: Pranav Rao

Date: March 24, 2022
*******************************************************************************/

package app; // delcare this class as part of the app package

// import all necessary Swing components

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class GUI {
  // the following set of fields represent the various GUI components used in
  // this App. To simplify the identification of components, all components are
  // named using this convention: componentType_panelNameComponentName
  // For example, a button to save on the add student panel would be named
  // btn_addStudentSave

  private JFrame frame;
  private Container
      contentPane; // this is a special variable that will store the content
                   // pane of the frame so it does not need to be re-read every
                   // time we want to use it
  private CardLayout masterLayout; // this CardLayout is being stored because it
                                   // will be essential in switching views

  // rest of the components, grouped by the panel they are on and following the
  // naming convention above

  private JPanel panel_addStudent, panel_editStudent,
      panel_editStudentSelection, panel_viewStudents, panel_deleteStudent;

  private JLabel lbl_addStudentName, lbl_addStudentTitle, lbl_addStudentGrade,
      lbl_addStudentId;
  private JButton btn_addStudentSave, btn_addStudentCancel;
  private JTextField textField_addStudentName, textField_addStudentId;
  private JComboBox<Integer> comboBox_addStudentGrade;

  private JLabel lbl_editStudentSelection;
  private JTextField textField_editStudentSelection;
  private JButton btn_editStudentSelectionCancel, btn_editStudentSelectionEdit;

  private JTextField textField_editStudentName, textField_editStudentId;
  private JLabel lbl_editStudentTitle, lbl_editStudentName,
      lbl_editStudentGrade, lbl_editStudentId;
  private JComboBox<Integer> comboBox_editStudentGrade;
  private JButton btn_editStudentCancel, btn_editStudentSave;
  private int currentlyEditingStudent;

  private JLabel lbl_viewStudentsTitle;
  private JTable table_viewStudents;
  private JButton btn_viewStudentsDelete, btn_viewStudentsCreate,
      btn_viewStudentsEditSelection;

  private JButton btn_deleteStudentCancel, btn_deleteStudentDelete;
  private JTextField textField_deleteStudent;
  private JLabel lbl_deleteStudent;

  // this is a  special variable that represents the models used to power the
  // grade selection comboboxes
  private DefaultComboBoxModel<Integer> gradeOptions;

  /**
   * This is a constructor for the GUI. When the GUI is made in the App class,
   * this method will be called.
   */
  public GUI() {
    initializeValues();     // assigns initial values to some variables declared
                            // above
    setupGUI();             // sets up and draws the GUI
    attachListeners();      // attaches the listeners to various GUI components
    frame.setVisible(true); // sets the frame to visible so the user can see it
  }

  /**
   * This method is responsible for setting intial values for some variables
   * above, specifically the option models for the comboBox components.
   */
  private void initializeValues() {
    gradeOptions =
        new DefaultComboBoxModel<Integer>(new Integer[] {9, 10, 11, 12});
  }

  /**
   * This method is a GUI utility method to temporarily change a given label
   * to have the new text, and then change it back. Useful for displaying
   * errors. Overloaded below for a JButton instead of a label.
   *
   * @param label - the label to temporarily change
   * @param newText - the text to change the label to
   */
  public static void tempChangeLabel(final JLabel label, final String newText) {
    final String oldText =
        label.getText();    // get the current text of the label, which we will
                            // need to revert the label to later
    label.setText(newText); // set the label's text to the new text supplied

    // using a SwingWorker object, asynchronously wait for 1 second and then
    // reset the text of the button the old text
    final SwingWorker<Object, Object> worker =
        new SwingWorker<Object, Object>() {
          /**
           * This method sleeps for 1000 milliseconds in a separate thread
           *
           * @return - this method returns a generic Object
           * @throws - this method throws an exception if it fails
           */
          @Override
          protected Object doInBackground() throws Exception {
            Thread.sleep(1000);
            return null;
          }

          /**
           * This method will automatically fire when the above background
           * task is done. It will set the label back to the old text.
           */
          @Override
          protected void done() {
            label.setText(oldText);
            super.done();
          }
        };

    worker.execute(); // this method call executes the SwingWorker and the
                      // declared methods
  }

  /**
   * This method is a GUI utility method to temporarily change a given label
   * to have the new text, and then change it back. Useful for displaying
   * errors. Overloaded above for a JLabel instead of a button. See above
   * overload for more detailed documentation.
   *
   * @param button - the button to temporarily change
   * @param newText - the text to change the button to
   */
  public static void tempChangeLabel(final JButton button,
                                     final String newText) {
    final String oldText = button.getText();
    button.setText(newText);

    final SwingWorker<Object, Object> worker =
        new SwingWorker<Object, Object>() {
          @Override
          protected Object doInBackground() throws Exception {
            Thread.sleep(1000);
            return null;
          }

          @Override
          protected void done() {
            button.setText(oldText);
            super.done();
          }
        };

    worker.execute();
  }

  /**
   * This method is a GUI utility that method that will update the table
   * containing all the students according to the student records stored
   *
   * @param table - the variable for the table to update
   */
  public void updateStudentViewTable(final JTable table) {
    // get the table model of the given table (needed to manipulate table data)
    final DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
    // remove all elements from the table (including the title row)
    tableModel.getDataVector().removeAllElements();

    tableModel.addRow(new Object[] {
        "Id", "Name", "Grade"}); // add the title row for student to the table
    for (final Student student :
         Engine.getStudents()) { // for each of the students currently stored
      tableModel.addRow(new Object[] {
          student.getId(), student.getName(),
          student.getGrade()}); // add a row to the table which contains the
                                // information for that particular student
    }
  }

  /**
   * This method draws the GUI itself (i.e. it initializes the components
   * above). It will essentially create various panels for each view and put
   * them all into a CardLayout (masterLayout variable above) so that it is
   * possible to easily switch between the panels
   */
  private void setupGUI() {
    frame = new JFrame();
    frame.setResizable(false);
    contentPane = frame.getContentPane();
    masterLayout = new CardLayout();

    frame.setBounds(100, 100, 450, 300);
    frame.setDefaultCloseOperation(
        JFrame.DO_NOTHING_ON_CLOSE); // overriding default close behaviour so
                                     // custom close methods can run

    contentPane.setLayout(masterLayout);

    panel_addStudent = new JPanel();
    panel_addStudent.setLayout(null);
    frame.getContentPane().add(panel_addStudent, "panel_addStudent");

    lbl_addStudentTitle = new JLabel("Create Student");
    lbl_addStudentTitle.setBounds(12, 12, 416, 17);
    panel_addStudent.add(lbl_addStudentTitle);

    lbl_addStudentName = new JLabel("Name:");
    lbl_addStudentName.setBounds(12, 65, 107, 17);
    panel_addStudent.add(lbl_addStudentName);

    lbl_addStudentGrade = new JLabel("Grade:");
    lbl_addStudentGrade.setBounds(12, 149, 107, 17);
    panel_addStudent.add(lbl_addStudentGrade);

    textField_addStudentName = new JTextField();
    textField_addStudentName.setColumns(10);
    textField_addStudentName.setBounds(62, 63, 114, 21);
    panel_addStudent.add(textField_addStudentName);

    comboBox_addStudentGrade = new JComboBox<Integer>(gradeOptions);
    comboBox_addStudentGrade.setBounds(62, 144, 57, 26);
    panel_addStudent.add(comboBox_addStudentGrade);

    btn_addStudentCancel = new JButton("Cancel");
    btn_addStudentCancel.setBounds(106, 231, 105, 27);
    panel_addStudent.add(btn_addStudentCancel);

    btn_addStudentSave = new JButton("Save");
    btn_addStudentSave.setBounds(223, 231, 105, 27);
    panel_addStudent.add(btn_addStudentSave);

    lbl_addStudentId = new JLabel("ID:");
    lbl_addStudentId.setBounds(223, 65, 60, 17);
    panel_addStudent.add(lbl_addStudentId);

    textField_addStudentId = new JTextField();
    textField_addStudentId.setBounds(251, 63, 114, 21);
    panel_addStudent.add(textField_addStudentId);
    textField_addStudentId.setColumns(10);

    panel_viewStudents = new JPanel();
    panel_viewStudents.setLayout(null);
    frame.getContentPane().add(panel_viewStudents, "panel_viewStudents");

    masterLayout.show(contentPane, "panel_viewStudents");

    final DefaultTableModel viewStudentsModel =
        new DefaultTableModel(new Object[] {"Id", "Name", "Grade"}, 0);
    table_viewStudents = new JTable(viewStudentsModel);
    viewStudentsModel.addRow(new Object[] {"Id", "Name", "Grade"});
    table_viewStudents.setEnabled(false);
    table_viewStudents.setBounds(0, 34, 440, 186);
    panel_viewStudents.add(table_viewStudents);

    lbl_viewStudentsTitle = new JLabel("All Students");
    lbl_viewStudentsTitle.setBounds(12, 12, 84, 17);
    panel_viewStudents.add(lbl_viewStudentsTitle);

    btn_viewStudentsCreate = new JButton("Create New Student");
    btn_viewStudentsCreate.setBounds(10, 232, 154, 27);
    panel_viewStudents.add(btn_viewStudentsCreate);

    btn_viewStudentsDelete = new JButton("Delete Student");
    btn_viewStudentsDelete.setBounds(298, 232, 130, 27);
    panel_viewStudents.add(btn_viewStudentsDelete);

    btn_viewStudentsEditSelection = new JButton("Edit Student");
    btn_viewStudentsEditSelection.setBounds(165, 232, 132, 27);
    panel_viewStudents.add(btn_viewStudentsEditSelection);

    panel_editStudentSelection = new JPanel();
    panel_editStudentSelection.setLayout(null);
    frame.getContentPane().add(panel_editStudentSelection,
                               "panel_editStudentSelection");

    btn_editStudentSelectionCancel = new JButton("Cancel");
    btn_editStudentSelectionCancel.setBounds(108, 231, 105, 27);
    panel_editStudentSelection.add(btn_editStudentSelectionCancel);

    btn_editStudentSelectionEdit = new JButton("Edit");
    btn_editStudentSelectionEdit.setBounds(225, 231, 105, 27);
    panel_editStudentSelection.add(btn_editStudentSelectionEdit);

    lbl_editStudentSelection = new JLabel("Enter a student ID to edit:");
    lbl_editStudentSelection.setBounds(12, 12, 416, 17);
    panel_editStudentSelection.add(lbl_editStudentSelection);

    textField_editStudentSelection = new JTextField();
    textField_editStudentSelection.setBounds(149, 109, 133, 21);
    panel_editStudentSelection.add(textField_editStudentSelection);
    textField_editStudentSelection.setColumns(10);

    panel_editStudent = new JPanel();
    panel_editStudent.setLayout(null);
    frame.getContentPane().add(panel_editStudent, "panel_editStudent");

    lbl_editStudentTitle = new JLabel("Edit Student");
    lbl_editStudentTitle.setBounds(12, 12, 416, 17);
    panel_editStudent.add(lbl_editStudentTitle);

    lbl_editStudentName = new JLabel("Name:");
    lbl_editStudentName.setBounds(12, 65, 107, 17);
    panel_editStudent.add(lbl_editStudentName);

    lbl_editStudentGrade = new JLabel("Grade:");
    lbl_editStudentGrade.setBounds(12, 149, 107, 17);
    panel_editStudent.add(lbl_editStudentGrade);

    textField_editStudentName = new JTextField();
    textField_editStudentName.setColumns(10);
    textField_editStudentName.setBounds(62, 63, 114, 21);
    panel_editStudent.add(textField_editStudentName);

    comboBox_editStudentGrade = new JComboBox<Integer>(gradeOptions);
    comboBox_editStudentGrade.setBounds(62, 144, 57, 26);
    panel_editStudent.add(comboBox_editStudentGrade);

    btn_editStudentCancel = new JButton("Cancel");
    btn_editStudentCancel.setBounds(106, 231, 105, 27);
    panel_editStudent.add(btn_editStudentCancel);

    btn_editStudentSave = new JButton("Save");
    btn_editStudentSave.setBounds(223, 231, 105, 27);
    panel_editStudent.add(btn_editStudentSave);

    lbl_editStudentId = new JLabel("ID:");
    lbl_editStudentId.setBounds(223, 65, 60, 17);
    panel_editStudent.add(lbl_editStudentId);

    textField_editStudentId = new JTextField();
    textField_editStudentId.setColumns(10);
    textField_editStudentId.setBounds(251, 63, 114, 21);
    panel_editStudent.add(textField_editStudentId);

    panel_deleteStudent = new JPanel();
    panel_deleteStudent.setLayout(null);
    frame.getContentPane().add(panel_deleteStudent, "panel_deleteStudent");

    btn_deleteStudentCancel = new JButton("Cancel");
    btn_deleteStudentCancel.setBounds(108, 231, 105, 27);
    panel_deleteStudent.add(btn_deleteStudentCancel);

    btn_deleteStudentDelete = new JButton("Delete");
    btn_deleteStudentDelete.setBounds(225, 231, 105, 27);
    panel_deleteStudent.add(btn_deleteStudentDelete);

    lbl_deleteStudent = new JLabel("Enter a student ID to delete:");
    lbl_deleteStudent.setBounds(12, 12, 416, 17);
    panel_deleteStudent.add(lbl_deleteStudent);

    textField_deleteStudent = new JTextField();
    textField_deleteStudent.setBounds(146, 112, 133, 21);
    panel_deleteStudent.add(textField_deleteStudent);
    textField_deleteStudent.setColumns(10);
  }

  /**
   * This method attaches the appropriate listeners to all components defined
   * above
   */
  private void attachListeners() {
    /*************************************************************************
     * GENERAL LISTENERS
     *************************************************************************/

    // add an action listener to do actions when the quit button is clicked
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        Engine.saveDataToFile(); // save the data in the database to the file
        frame.dispose();         // dispose of the current frame
        System.exit(0);          // exit
      }
    });

    /*************************************************************************
     * STUDENT LISTENERS
     *************************************************************************/

    // listners for the view students screen

    // add an action listener to the Create button on the view students
    // panel which will go to the create student screen on click
    btn_viewStudentsCreate.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_addStudent");
      }
    });

    // add an action listener to the Edit button on the view students
    // panel which will go to the edit student selection screen on click
    btn_viewStudentsEditSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        // if there are no students
        if (Engine.getStudents().size() == 0) {
          tempChangeLabel(btn_viewStudentsEditSelection,
                          "No students yet!"); // display an error message
        } else { // if there are students to display
          // update the student selection combobox and switch to the edit
          // student selection screen
          masterLayout.show(contentPane, "panel_editStudentSelection");
        }
      }
    });

    // add an action listener to the Delete button on the view students
    // panel which will go to the delete student screen on click
    btn_viewStudentsDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        // if there are no students
        if (Engine.getStudents().size() == 0) {
          tempChangeLabel(btn_viewStudentsDelete,
                          "No students yet!"); // display an error message
        } else { // if there are students to display
          // update the student selection combobox and switch to the delete
          // student selection screen
          masterLayout.show(contentPane, "panel_deleteStudent");
        }
      }
    });

    // listeners for the add student screen

    // add an action listener to the Cancel button on the add student
    // panel which will go back to the view student screen on click
    btn_addStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    // add an action listener to the Cancel button on the add student
    // panel which will save a new student with the current entered information
    btn_addStudentSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        final String newStudentId = textField_addStudentId.getText(),
                     newStudentName = textField_addStudentName.getText();

        // if the text field for the name on the add student screen is empty
        // when the save button is clicked
        if (newStudentName.equals("")) {
          tempChangeLabel(lbl_addStudentTitle,
                          "Please enter a name!"); //  display an error message
                                                   //  and prevent saving
        } else if (newStudentId.equals("") ||
                   !newStudentId.matches(
                       "\\d+")) { // else if the id is null or not a number
          tempChangeLabel(
              lbl_addStudentTitle,
              "Please enter a positive numerical id!"); // display an error and
                                                        // prevent saving
        } else if (!Engine.isIdUnique(Integer.parseInt(
                       newStudentId))) { // if the id entered is not unique
          tempChangeLabel(
              lbl_addStudentTitle,
              "That id is already taken!"); // display error and prevent saving
        } else { // if all preconditions for saving are satisfied
          Engine.insertSorted(new Student(
              Integer.parseInt(newStudentId), newStudentName,
              (int)comboBox_addStudentGrade
                  .getSelectedItem())); // insert the student into the correct
                                        // position in the already sorted list
          // update the students table for with the new students and go back to
          // the students view panel
          updateStudentViewTable(table_viewStudents);
          masterLayout.show(contentPane, "panel_viewStudents");
        }
      }
    });

    // listeners for the student edit selection screen

    // add an action listener to the Edit button on the edit student selection
    // panel which will go to the edit panel with the info filled in for the
    // selected student
    btn_editStudentSelectionEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        // get the id that the user has inputted
        final String studentId = textField_editStudentSelection.getText();

        // if there is no student id given or it is not a positive integer
        if (studentId.equals("") ||
            !studentId.matches("\\d+")) { // else if the new id is
                                          // null or is not an integer
          tempChangeLabel(
              lbl_editStudentSelection,
              "Please enter a positive numerical id!"); // display an error and
                                                        // prevent moving on to
                                                        // eidt
        } else { // if the id is in valid format
          // attempt to find the index of the student with the given id using
          // binary search and store it in a global variable for later use
          currentlyEditingStudent = Engine.binarySearchStudentsList(
              Integer.parseInt(studentId), 0, Engine.getStudents().size() - 1);

          // if the index is NOT -1 (meaning the student was found)
          if (currentlyEditingStudent != -1) {
            final Student student =
                Engine.getStudents().get(currentlyEditingStudent);

            // fill in the edit fields with the existent information
            textField_editStudentName.setText(student.getName());
            comboBox_editStudentGrade.setSelectedItem(
                (Object)student.getGrade());
            textField_editStudentId.setText(String.valueOf(student.getId()));

            // go to the edit student panel with the newly filled in information
            masterLayout.show(contentPane, "panel_editStudent");
          } else { // if the student is NOT found
            tempChangeLabel(
                lbl_editStudentSelection,
                "A student with that ID does not exist!"); // display an error
                                                           // and prevent moving
                                                           // to the edit screen
          }
        }
      }
    });

    // add an action listener to the Cancel button on the edit student
    // selection panel which will go back to the view students panel
    btn_editStudentSelectionCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    // listeners for the edit student screen

    // add an action listener to the Cancel button on the edit student
    // panel which will go back to the view students panel
    btn_editStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    // add an action listener to the Save button on the edit student
    // panel which edit the current student with the given information and
    // return to the view students screen
    btn_editStudentSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final String newStudentId = textField_editStudentId.getText(),
                     newStudentName = textField_editStudentName.getText();

        if (newStudentName.equals(
                "")) { // if there is no name in the name field
          tempChangeLabel(
              lbl_editStudentTitle,
              "Please enter a name!"); // display an error and prevent saving
        } else if (newStudentId.equals("") ||
                   !newStudentId.matches("\\d+")) { // else if the new id is
                                                    // null or is not an integer
          tempChangeLabel(
              lbl_editStudentTitle,
              "Please enter a positive numerical id!"); // display an error
                                                        // and prevent saving
        } else if (!Engine.isIdUnique(Integer.parseInt(
                       newStudentId))) { // else if the new id is not unique
          tempChangeLabel(lbl_editStudentTitle,
                          "That id is already taken!"); // display an error
                                                        // and prevent saving
        } else { // if all preconditions for saving are fulfilled
          // get the student object to alter
          final Student currentStudent =
              Engine.getStudents().get(currentlyEditingStudent);

          // update the attributes of the student object to what is in the
          // input fields
          currentStudent.setName(textField_editStudentName.getText());
          currentStudent.setGrade(
              (int)comboBox_editStudentGrade.getSelectedItem());
          currentStudent.setId(
              Integer.parseInt(textField_editStudentId.getText()));

          // re-sort the students array, to account for the case where the
          // user edits the id of the student such that the student is no
          // longer in place
          Engine.sortStudents();

          // update the students table for with the new students and go back
          // to the students view panel
          updateStudentViewTable(table_viewStudents);
          masterLayout.show(contentPane, "panel_viewStudents");
        }
      }
    });

    // listeners for the delete student screen

    // add an action listener to the Cancel button on the delete student
    // panel which will go back to the view students panel
    btn_deleteStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    // add an action listener to the Delete button on the delete student
    // panel which delete the selected student
    btn_deleteStudentDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        // get the id that the user has inputted
        final String studentId = textField_deleteStudent.getText();

        // if there is no student id given or it is not a positive integer
        if (studentId.equals("") ||
            !studentId.matches("\\d+")) { // else if the new id is
                                          // null or is not an integer
          tempChangeLabel(
              lbl_deleteStudent,
              "Please enter a positive numerical id!"); // display an error and
                                                        // prevent deleting
        } else { // if the id is in valid format
          // attempt to find the index of the student with the given id using
          // binary search
          final int currentlyDeletingStudent = Engine.binarySearchStudentsList(
              Integer.parseInt(studentId), 0, Engine.getStudents().size() - 1);

          // if the index is NOT -1 (meaning the student was found)
          if (currentlyDeletingStudent != -1) {
            Engine.getStudents().remove(Engine.getStudents().get(
                currentlyDeletingStudent)); // remove the selected student from
                                            // the students array

            // update the students table and go back to the view students panel
            updateStudentViewTable(table_viewStudents);
            masterLayout.show(contentPane, "panel_viewStudents");
          } else { // if the student is NOT found
            tempChangeLabel(
                lbl_deleteStudent,
                "A student with that ID does not exist!"); // display an error
                                                           // and prevent
                                                           // deletion
          }
        }
      }
    });
  }

  // getters and setters

  public JTable getTableViewStudents() { return table_viewStudents; }
}
