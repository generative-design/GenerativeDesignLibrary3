/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the function loadXMLAsync2().
 * This function needed to be implemented for Processing 2.X because the
 * previously used class XMLElement was renamed to XML.
 */

import generativedesign.*;
import processing.data.XML;

XML myXML;


void setup() {
  // start loading
  myXML = GenerativeDesign.loadXMLAsync2(this, "http://feeds.delicious.com/v2/rss/tag/generative?count=3");
}


void draw() {
  // is the xml file already loaded?

  if (myXML.getChildCount() == 0) {
    println("not loaded yet");
  
  } else {
    println("loaded: " + myXML.toString());
    noLoop();
  }
} 



