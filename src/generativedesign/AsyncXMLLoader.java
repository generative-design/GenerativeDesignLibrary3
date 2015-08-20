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
import processing.data.XML;
//import java.io.File;
//import java.io.BufferedReader;


//import processing.xml.XMLElement;


/**
 * Use this class to load a xml file asynchronously. You will rather use the
 * function loadXMLAsync() instead of using this class directly.
 * 
 * Class is based on AsyncImageLoader class from the book "Visualizing Data"
 * from Ben Fry.
 */
class AsyncXMLLoader extends Thread {
	static int loaderMax = 4;
	static int loaderCount;

	PApplet parent;
	String path;
	XML vessel;

	/**
	 * Initializes a new thread for loading the given filename or url.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this".
	 * @param thePath
	 *            Filename of the XML file or a URL that returns an XML.
	 * @param theResult
	 *            Empty XMLElement that will contain the data after loading is
	 *            done.
	 */
	public AsyncXMLLoader(PApplet theParent, String thePath,
			XML theResult) {
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

		try {
			//BufferedReader reader = new XML(PApplet.createReader(path), null);
			//File file = new File(path);
			//XML actual = new XML(file);
		
			XML actual = new XML(this.parent.createReader(path), null);
			
			synchronized (vessel) {
				vessel.setName(actual.getName());
				vessel.setContent(actual.getContent());
				XML[] children = actual.getChildren();
				for (int i = 0; i < children.length; i++) {
					vessel.addChild(children[i]);
				}
			}
		} catch (Exception e) {
		}

		loaderCount--;
	}
}
