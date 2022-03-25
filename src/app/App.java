/******************************************************************************
Program: App Class (Student Database)

Description: This is the driver code for this student database. This class' main
method will start the entire program.

Author: Pranav Rao

Date: March 24, 2022

LOCATIONS OF FULFILLED REQUIREMENTS:
- Sorting:
  - Method to insert into the array in a sorted manner: app/Engine.java:151,
used in app/GUI.java:491
  - Method to sort entire list: app/Engine.java:185, used in app/GUI.java:616
- Searching: binary search imlemented in app/Engine.java:218, used in muliple
places such as app/GUI.java:527 and app/GUI.java:656
*******************************************************************************/

package app; // declare this class as part of the app package

// import necessary utilities
import java.awt.EventQueue;

public class App {

  /**
   * This method is the driver code for the entire program. It creates a new
   * GUI and runs the engine.
   *
   * @param args - command line arguments for this program
   */
  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Engine.run(new GUI());
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
