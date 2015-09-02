/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the Tablet class.
 */
 
import generativedesign.*;

Tablet tablet;


void setup() {
  size(640, 480);
 
  tablet = new Tablet(this);
  
  background(0);
  stroke(255);  
}


void draw() {
  if (mousePressed) {
    strokeWeight(30 * tablet.getPressure());
    line(pmouseX, pmouseY, mouseX, mouseY);
  }
  print("TiltX: " + nfs(tablet.getTiltX(), 1, 3) + "   " + "TiltY: " + nfs(tablet.getTiltY(), 1, 3) + "   ");
  
  float[] res = new float[2];
  println("Azimuth: " + nfs(tablet.getAzimuth(), 1, 3) + "   " + "Altitude: " + nfs(tablet.getAltitude(), 1, 3));  
}
