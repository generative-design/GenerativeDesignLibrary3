/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the functions cartesianToPolar() and polarToCartesian().
 */
 
import generativedesign.*;
import processing.opengl.*;


void setup() {

  println("Converting to polar: x=40, y=30");
  float[] res = GenerativeDesign.cartesianToPolar(40, 30);
  println("length: " + res[0]);
  println("angle : " + degrees(res[1]));
  println();

  println("Converting to cartesian: l=50, a=36.0");
  res = GenerativeDesign.polarToCartesian(50, radians(36));
  println("x: " + res[0]);
  println("y: " + res[1]);
  println();

  println("Converting to polar: x=40, y=20, z=30");
  res = GenerativeDesign.cartesianToPolar(40, 20, 30);
  println("length: " + res[0]);
  println("angleY: " + degrees(res[1]));
  println("angleZ: " + degrees(res[2]));
  println();

  println("Converting to cartesian: l=110, a1=36, a2=63");
  res = GenerativeDesign.polarToCartesian(110, radians(36), radians(63));
  println("x: " + res[0]);
  println("y: " + res[1]);
  println("z: " + res[2]);
  println();
}


