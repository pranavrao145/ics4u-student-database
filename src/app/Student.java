/******************************************************************************
Program: Student Class (Student Database)

Description: This is the Student class. It contains a name inherited from its
parent, Person, as well as an additional grade attribute.

Author: Pranav Rao

Date: March 24, 2022
*******************************************************************************/

package app; // declare this class as a part of the app package

public class Student {
  String name;   // declare name attribute
  int id, grade; // declare grade and id attributes

  /**
   * Constructor method - this method is called when an object of this class
   * is made. This constructor takes a parameter for each of the available
   * attributes.
   *
   * @param id - the id with which to make the new student
   * @param name - the name with which to make the new Student
   * @param grade - the grade with which to make the new Student
   */
  public Student(final int id, final String name, final int grade) {
    this.id = id;       // set the id of this object to the id provided
    this.name = name;   // set the name of this object to the name provided
    this.grade = grade; // set the grade of this object to the grade provided
  }

  // getters and setters

  public String getName() { return name; }

  public void setName(final String name) { this.name = name; }

  public int getId() { return id; }

  public void setId(final int id) { this.id = id; }

  public int getGrade() { return grade; }

  public void setGrade(final int grade) { this.grade = grade; }
}
