/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the function loadImageAsync().
 */
 
import generativedesign.*;

PImage myImage;


void setup() {
  // start loading
  myImage = GenerativeDesign.loadImageAsync(this, "image.jpg");
}


void draw() {
  // is the image already loaded?

  if (myImage.width == 0) {
    println("image not loaded yet");
  
  } else if (myImage.width > 0) {
    println("loaded: " + myImage);
    image(myImage, 0, 0, width, height);
    noLoop();

  } else {
    println("could not load image!");
    noLoop();
  
  }
} 



