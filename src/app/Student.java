package app;

public class Student {
  String name;
  int id, grade;

  public Student(int id, String name, int grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public int getId() { return id; }

  public void setId(int id) { this.id = id; }

  public int getGrade() { return grade; }

  public void setGrade(int grade) { this.grade = grade; }
}
