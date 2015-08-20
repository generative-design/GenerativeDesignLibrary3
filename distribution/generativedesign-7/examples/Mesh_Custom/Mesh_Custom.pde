/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the mesh class, if you want to define your own forms.
 */


// imports
import generativedesign.*;
import processing.opengl.*;

// mesh
MyOwnMesh myMesh;


void setup() {
  size(1000,1000,OPENGL);

  // setup drawing style 
  colorMode(HSB, 360, 100, 100, 100);
  noStroke();

  // initialize mesh. class MyOwnMesh is defined below
  myMesh = new MyOwnMesh(this);
  myMesh.setUCount(100);
  myMesh.setVCount(100);
  myMesh.setColorRange(193, 193, 30, 30, 85, 85, 100);
  myMesh.update();
}


void draw() {
  background(255);

  // setup lights
  colorMode(RGB, 255, 255, 255, 100);
  lightSpecular(255, 255, 255); 
  directionalLight(255, 255, 255, 1, 1, -1); 
  shininess(5.0); 

  // setup view
  translate(width*0.5, height*0.5);
  scale(180);
  rotateX(radians(10)); 
  rotateY(radians(-10)); 

  // recalculate points and draw mesh
  myMesh.draw();
}



// define your own class that extends the Mesh class 
class MyOwnMesh extends Mesh {
  
  MyOwnMesh(PApplet theParent) {
    super(theParent);
  }

  // just override this function and put your own formulas inside
  PVector calculatePoints(float u, float v) {
    float A = 2/3.0;
    float B = sqrt(2);

    float x = A * (cos(u) * cos(2*v) + B * sin(u) * cos(v)) * cos(u) / (B - sin(2*u) * sin(3*v));
    float y = A * (cos(u) * sin(2*v) - B * sin(u) * sin(v)) * cos(u) / (B - sin(2*u) * sin(3*v));
    float z = B * cos(u) * cos(u) / (B - sin(2*u) * sin(3*v)); 

    return new PVector(x, y, z);
  }
}





