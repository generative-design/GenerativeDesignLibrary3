/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the function loadXMLAsync().
 */

import generativedesign.*;

XMLElement myElement;


void setup() {
  // start loading
  myElement = GenerativeDesign.loadXMLAsync(this, "http://feeds.delicious.com/v2/rss/tag/generative?count=3");
}


void draw() {
  // is the xml file already loaded?

  if (myElement.hasChildren() == false) {
    println("not loaded yet");
  
  } else {
    println("loaded: " + myElement);
    noLoop();
  }
} 



