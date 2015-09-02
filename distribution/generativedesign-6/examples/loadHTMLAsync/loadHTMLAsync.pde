/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the function loadHTMLAsync().
 */

import generativedesign.*;

ArrayList myHTML;


void setup() {
  // start loading
  myHTML = GenerativeDesign.loadHTMLAsync(this, "http://de.wikipedia.org/wiki/Satz_von_Lagrange", GenerativeDesign.HTML_CONTENT);
}


void draw() {
  // is the html file already loaded?

  if (myHTML.size() == 0) {
    println("html not loaded yet");
  } 
  else {
    println("loaded: " + myHTML);
    noLoop();
  }
} 




