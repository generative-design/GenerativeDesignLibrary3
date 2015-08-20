/*
  This library is part of the book: 
  Generative Gestaltung, ISBN: 978-3-87439-759-9
  First Edition, Hermann Schmidt, Mainz, 2009
  Copyright (c) 2009 Hartmut Bohnacker, Benedikt Gross, Julia Laub, Claudius Lazzeroni

  http://www.generative-gestaltung.de

  This library is free software; you can redistribute it and/or modify it under the terms 
  of the GNU Lesser General Public License as published by the Free Software Foundation; 
  either version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
  See the GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this 
  library; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, 
  Boston, MA 02110, USA
*/

package generativedesign;

import processing.core.PApplet;

// --------------------------------------------------------------
// ColorObject class
class ColorObject {
	float x = 0, y = 0, z = 0, a = 0;
	float grayscale = 0;
	int c;

	// normal
	ColorObject(PApplet parent, int c) {
		// g.colorMode == 3 colorMode is set to HSB
		// g.colorMode == 1 colorMode is set to RGB
		if (parent.g.colorMode == PApplet.HSB) {
			this.x = parent.hue(c);
			this.y = parent.saturation(c);
			this.z = parent.brightness(c);
		} else {
			this.x = parent.red(c);
			this.y = parent.green(c);
			this.z = parent.blue(c);
		}
		this.a = parent.alpha(c);
		this.c = c;
	}

	// grayscale
	ColorObject(PApplet parent, int c, float fx, float fy, float fz) {
		if (parent.g.colorMode == PApplet.HSB) {
			float rgb[] = new float[3];
			rgb = GenerativeDesign.HSBtoRGB(parent.hue(c), parent.saturation(c), parent.brightness(c));
			this.grayscale = rgb[0] * fx + rgb[1] * fy + rgb[2] * fz;
		} else {
			this.grayscale = parent.red(c) * fx + parent.green(c) * fy + parent.blue(c) * fz;
		}

		this.a = parent.alpha(c);
		this.c = c;
	}
}
