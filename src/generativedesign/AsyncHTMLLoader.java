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

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import processing.core.PApplet;

/**
 * Use this class to load a html file asynchronously. You will rather use the
 * function loadHTMLAsync() instead of using this class directly.
 * 
 * Class is based on AsyncImageLoader class from the book "Visualizing Data"
 * from Ben Fry.
 */
class AsyncHTMLLoader extends Thread {
	static int loaderMax = 4;
	static int loaderCount;

	PApplet parent;
	String path;
	ArrayList<String> vessel;
	String[] actual = new String[0];
	int mode = 0;

	/**
	 * Initializes a new thread for loading the given filename or url.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this".
	 * @param thePath
	 *            Filename or URL of the HTML file.
	 * @param theResult
	 *            Empty ArrayList that will contain the data after loading is
	 *            done.
	 */
	public AsyncHTMLLoader(PApplet theParent, String thePath,
			ArrayList<String> theResult) {
		this(theParent, thePath, theResult, GenerativeDesign.HTML_PLAIN);
	}

	/**
	 * Initializes a new thread for loading the given filename or url.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theURL
	 *            URL or filename to load
	 * @param theMode
	 *            The mode for the text that will be returned. Either HTML_PLAIN
	 *            (plain html code with all tags) or HTML_CONTENT (just the text
	 *            that is shown on the website)
	 * @return Empty String array in which the data is loaded
	 */
	public AsyncHTMLLoader(PApplet theParent, String thePath,
			ArrayList<String> theResult, int theMode) {
		this.parent = theParent;
		this.path = thePath;
		this.vessel = theResult;
		this.mode = theMode;

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

		if (mode == GenerativeDesign.HTML_PLAIN) {
			// load plain html text
			try {
				actual = (String[]) parent.loadStrings(path);
			} catch (Exception e) {
				System.out.println(path + " not accessible");
			}

			synchronized (vessel) {
				if (actual != null) {
					for (int i = 0; i < actual.length; i++) {
						vessel.add(actual[i]);
					}
				}
			}
		}

		if (mode == GenerativeDesign.HTML_CONTENT) {
			try {
				URL url = new URL(path);
				Reader HTMLReader = new InputStreamReader(url.openConnection()
						.getInputStream(), "UTF-8");
				new ParserDelegator().parse(HTMLReader, callback, true);

			} catch (Exception e) {
			}

		}

		loaderCount--;
	}

	HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
		public void handleText(char[] data, int pos) {
			try {
				String s = new String(data);
				actual = PApplet.append(actual, s);
			} catch (Exception e) {
			}
		}

		public void handleEndOfLineString(String eol) {
			synchronized (vessel) {
				if (actual != null) {
					for (int i = 0; i < actual.length; i++) {
						vessel.add(actual[i]);
					}
				}
			}

		}
	};

}
