/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the function angleDifference().
 */
 
import generativedesign.*;

void setup() {
  int a1 = 70;
  int a2 = 30;
  float d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

  a1 = 30;
  a2 = 70;
  d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

  a1 = 30;
  a2 = -20;
  d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

  a1 = 30;
  a2 = 340;
  d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

  a1 = -30;
  a2 = 290;
  d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

  a1 = -30;
  a2 = 340;
  d = GenerativeDesign.angleDifference(radians(a1), radians(a2));
  println (a1 + " - " + a2 + " = " + round(degrees(d)));

}
