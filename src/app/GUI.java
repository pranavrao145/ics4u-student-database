package app;

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
  private JComboBox<Integer> comboBox_editStudentSelection;
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
  private JLabel lbl_deleteStudent;
  private JComboBox<Integer> comboBox_deleteStudent;

  private DefaultComboBoxModel<Integer> defaultDeletionOptions,
      defaultEditOptions, gradeOptions;

  public GUI() {
    initializeValues();

    setupGUI();
    attachListeners();
    frame.setVisible(true);
  }

  private void initializeValues() {
    gradeOptions =
        new DefaultComboBoxModel<Integer>(new Integer[] {9, 10, 11, 12});

    defaultDeletionOptions = new DefaultComboBoxModel<Integer>();
    defaultEditOptions = new DefaultComboBoxModel<Integer>();
  }

  public static void tempChangeLabel(final JLabel label, final String newText) {
    final String oldText = label.getText();

    label.setText(newText);

    final SwingWorker<Object, Object> worker =
        new SwingWorker<Object, Object>() {
          @Override
          protected Object doInBackground() throws Exception {
            Thread.sleep(1000);
            return null;
          }

          @Override
          protected void done() {
            label.setText(oldText);
            super.done();
          }
        };

    worker.execute();
  }

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

  public void updateStudentViewTable(final JTable table) {
    final DefaultTableModel tableModel = (DefaultTableModel)table.getModel();

    tableModel.getDataVector().removeAllElements();

    tableModel.addRow(new Object[] {"Id", "Name", "Grade"});
    for (final Student student : Engine.getStudents()) {
      tableModel.addRow(new Object[] {student.getId(), student.getName(),
                                      student.getGrade()});
    }
  }

  private void updateStudentComboBox(final JComboBox<Integer> comboBox) {

    final DefaultComboBoxModel<Integer> comboBoxModel =
        (DefaultComboBoxModel<Integer>)comboBox.getModel();

    comboBoxModel.removeAllElements();

    for (final Student student : Engine.getStudents()) {
      comboBoxModel.addElement(student.getId());
    }
  }

  private void setupGUI() {
    frame = new JFrame();
    frame.setResizable(false);
    contentPane = frame.getContentPane();
    masterLayout = new CardLayout();

    frame.setBounds(100, 100, 450, 300);
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

    comboBox_editStudentSelection = new JComboBox<Integer>(defaultEditOptions);
    comboBox_editStudentSelection.setBounds(146, 109, 133, 26);
    panel_editStudentSelection.add(comboBox_editStudentSelection);

    lbl_editStudentSelection = new JLabel("Choose a student to edit:");
    lbl_editStudentSelection.setBounds(126, 80, 171, 17);
    panel_editStudentSelection.add(lbl_editStudentSelection);

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

    comboBox_deleteStudent = new JComboBox<Integer>(defaultDeletionOptions);
    comboBox_deleteStudent.setBounds(146, 109, 133, 26);
    panel_deleteStudent.add(comboBox_deleteStudent);

    lbl_deleteStudent = new JLabel("Choose a student to delete:");
    lbl_deleteStudent.setBounds(126, 80, 171, 17);
    panel_deleteStudent.add(lbl_deleteStudent);
  }

  private void attachListeners() {
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        Engine.saveDataToFile();
        frame.dispose();
        System.exit(0);
      }
    });

    btn_viewStudentsCreate.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_addStudent");
      }
    });

    btn_viewStudentsEditSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        if (Engine.getStudents().size() == 0) {
          tempChangeLabel(btn_viewStudentsEditSelection, "No students yet!");
        } else {

          updateStudentComboBox(comboBox_editStudentSelection);
          masterLayout.show(contentPane, "panel_editStudentSelection");
        }
      }
    });

    btn_viewStudentsDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        if (Engine.getStudents().size() == 0) {
          tempChangeLabel(btn_viewStudentsDelete, "No students yet!");
        } else {

          updateStudentComboBox(comboBox_deleteStudent);
          masterLayout.show(contentPane, "panel_deleteStudent");
        }
      }
    });

    btn_addStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    btn_addStudentSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        final String newStudentId = textField_addStudentId.getText(),
                     newStudentName = textField_addStudentName.getText();

        if (newStudentName.equals("")) {
          tempChangeLabel(lbl_addStudentTitle, "Please enter a name!");
        } else if (newStudentId.equals("") || !newStudentId.matches("\\d+")) {
          tempChangeLabel(lbl_addStudentTitle,
                          "Please enter a positive numerical id!");
        } else if (!Engine.isIdUnique(Integer.parseInt(newStudentId))) {
          tempChangeLabel(lbl_addStudentTitle, "That id is already taken!");
        } else {
          Engine.insertSorted(
              new Student(Integer.parseInt(newStudentId), newStudentName,
                          (int)comboBox_addStudentGrade.getSelectedItem()));

          updateStudentViewTable(table_viewStudents);
          masterLayout.show(contentPane, "panel_viewStudents");
        }
      }
    });

    btn_editStudentSelectionEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        currentlyEditingStudent = Engine.binarySearchStudentsList(
            (int)comboBox_editStudentSelection.getSelectedItem(), 0,
            Engine.getStudents().size() - 1);

        System.out.printf(
            "GUI#attachListeners#actionPerformed currentlyEditingStudent: %s \n",
            currentlyEditingStudent); // __AUTO_GENERATED_PRINT_VAR__

        final Student student =
            Engine.getStudents().get(currentlyEditingStudent);

        textField_editStudentName.setText(student.getName());
        comboBox_editStudentGrade.setSelectedItem((Object)student.getGrade());
        textField_editStudentId.setText(String.valueOf(student.getId()));

        masterLayout.show(contentPane, "panel_editStudent");
      }
    });

    btn_editStudentSelectionCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    btn_editStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    btn_editStudentSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final String newStudentId = textField_editStudentId.getText(),
                     newStudentName = textField_editStudentName.getText();

        if (newStudentName.equals("")) {
          tempChangeLabel(lbl_editStudentTitle, "Please enter a name!");
        } else if (newStudentId.equals("") || !newStudentId.matches("\\d+")) {
          tempChangeLabel(lbl_editStudentTitle,
                          "Please enter a positive numerical id!");
        } else if (!Engine.isIdUnique(Integer.parseInt(newStudentId))) {
          tempChangeLabel(lbl_editStudentTitle, "That id is already taken!");
        } else {
          final Student currentStudent =
              Engine.getStudents().get(currentlyEditingStudent);

          currentStudent.setName(textField_editStudentName.getText());

          currentStudent.setGrade(
              (int)comboBox_editStudentGrade.getSelectedItem());

          currentStudent.setId(
              Integer.parseInt(textField_editStudentId.getText()));

          Engine.sortStudents();

          updateStudentViewTable(table_viewStudents);

          masterLayout.show(contentPane, "panel_viewStudents");
        }
      }
    });

    btn_deleteStudentCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });

    btn_deleteStudentDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final int studentId = (int)comboBox_deleteStudent.getSelectedItem();
        final int index = Engine.binarySearchStudentsList(
            studentId, 0, Engine.getStudents().size() - 1);
        Engine.getStudents().remove(Engine.getStudents().get(index));

        updateStudentViewTable(table_viewStudents);
        masterLayout.show(contentPane, "panel_viewStudents");
      }
    });
  }

  public JTable getTableViewStudents() { return table_viewStudents; }
}
