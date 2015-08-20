/**
 * part of the example files of the generativedesign library.
 *
 * shows how to use the functions sortColors() and saveASE().
 */

import generativedesign.*;

color[] colors = new color[5];
String[] names = new String[5];


void setup(){
  colorMode(HSB, 360, 100, 100, 100);
  
  // fill array
  names[0] = "Yellow";
  colors[0] = color(60, 100, 100);

  names[1] = "Purple";
  colors[1] = color(280, 100, 100);

  names[2] = "Green";
  colors[2] = color(130, 100, 100);

  names[3] = "Red";
  colors[3] = color(0, 100, 100);

  names[4] = "Blue";
  colors[4] = color(230, 100, 100);


  // save ASE with custom names
  GenerativeDesign.saveASE(this, colors, names, "example.ase");
  println("File example.ase saved");


  // sort colors (attention: the names will not be sorted)
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.RED);
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.GREEN);
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.BLUE);
  colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.HUE);
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.SATURATION);
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.BRIGHTNESS);
  //colors = GenerativeDesign.sortColors(this, colors, GenerativeDesign.GRAYSCALE);
  

  // save ASE with default names
  GenerativeDesign.saveASE(this, colors, "example_defaultNames.ase");
  println("File example_defaultNames.ase saved");

}



