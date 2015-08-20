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
import processing.core.PImage;

/**
 * Use this class to load an image file asynchronously. You will rather use the
 * function loadImageAsync() instead of using this class directly.
 * 
 * Class is based on AsyncImageLoader class from the book "Visualizing Data"
 * from Ben Fry.
 */
class AsyncImageLoader extends Thread {
	static int loaderMax = 4;
	static int loaderCount;

	PApplet parent;
	String path;
	PImage vessel;

	/**
	 * Initializes a new thread for loading the given image.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this".
	 * @param thePath
	 *            Path/filename of the image file.
	 * @param theResult
	 *            Empty PImage that will contain the data after loading is
	 *            done.
	 */
	public AsyncImageLoader(PApplet theParent, String thePath, PImage theResult) {
		this.parent = theParent;
		this.path = thePath;
		this.vessel = theResult;
		this.start();
	}

	@Override
	public void run() {
		while (loaderCount == loaderMax) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		loaderCount++;

		PImage actual = parent.loadImage(path);
		if (actual == null) {
			vessel.width = -1;
			vessel.height = -1;

		} else {
			vessel.width = actual.width;
			vessel.height = actual.height;
			vessel.format = actual.format;
			vessel.pixels = actual.pixels;
		}

		loaderCount--;
	}
}
