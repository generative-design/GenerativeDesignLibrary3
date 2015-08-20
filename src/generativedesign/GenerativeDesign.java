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

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.XML;


/**
 * This class collects static methods that are used in several sketches of the
 * book. 'Static' means, that you don't need to create an instance of this class
 * to use one of the methods. For example, if you want to print the version
 * number of this library just type: println(GenerativeDesign.version());
 */
public class GenerativeDesign {

	public static String VERSION = "1.0.6 (7)";

	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String RED = "red";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String GREEN = "green";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String BLUE = "blue";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String HUE = "hue";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String SATURATION = "saturation";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String BRIGHTNESS = "brightness";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String GRAYSCALE = "grayscale";
	/**
	 * Constant to be used with the function sortColors.
	 */
	public static String ALPHA = "alpha";

	/**
	 * Constant to be used as a mode with the class AsyncHTMLLoader. Using the
	 * mode HTML_PLAIN will return the plain html code, including the header and
	 * all other tags.
	 */
	public static int HTML_PLAIN = 0;
	/**
	 * Constant to be used as a mode with the class AsyncHTMLLoader. Using the
	 * mode HTML_CONTENT will return just the text that is shown on the website.
	 */
	public static int HTML_CONTENT = 1;

	/**
	 * @return Actual version of the generativedesign library.
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * @return String with the current date and time in the format YYMMDD_HHMMSS
	 *         (year, month, day, hours, minutes, seconds)
	 */
	public static String timestamp() {
		Calendar now = Calendar.getInstance();
		return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now);
	}

	/**
	 * Converts a RGB color to HSB. Uses the built in java function
	 * Color.RGBtoHSB for the conversions.
	 * 
	 * @param theR
	 *            Red value (0-255)
	 * @param theG
	 *            Green value (0-255)
	 * @param theB
	 *            Blue value (0-255)
	 * @return Array of three float values representing hue (0-366), saturation
	 *         (0-100) and brightness (0-100)
	 */
	public static float[] RGBtoHSB(float theR, float theG, float theB) {
		float hsb[] = new float[3];
		// Color.HSBtoRGB uses component ranges of 0.0 to 1.0.
		Color.RGBtoHSB((int) theR, (int) theG, (int) theB, hsb);
		hsb[0] *= 360;
		hsb[1] *= 100;
		hsb[2] *= 100;
		return hsb;
	}

	/**
	 * Converts a HSB color to RGB. Uses the built in java function
	 * Color.HSBtoRGB for the conversions.
	 * 
	 * @param theH
	 *            Hue value (0-360)
	 * @param theS
	 *            Saturation value (0-100)
	 * @param theB
	 *            Brightness value (0-100)
	 * @return Array of three float values between 0-255 representing red, green
	 *         and blue
	 */
	public static float[] HSBtoRGB(float theH, float theS, float theB) {
		float rgb[] = new float[3];
		// Color.HSBtoRGB uses component ranges of 0.0 to 1.0.
		Color t = new Color(Color.HSBtoRGB(theH / 360, theS / 100, theB / 100));
		rgb[0] = t.getRed();
		rgb[1] = t.getGreen();
		rgb[2] = t.getBlue();
		return rgb;
	}

	/**
	 * Sorts a list of colors
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theColors
	 *            Array of colors to sort
	 * @param theMethod
	 *            Sorting method: one of the constants RED, GREEN, BLUE, HUE,
	 *            SATURATION, BRIGHTNESS, ALPHA, GRAYSCALE
	 * @return Sorted color array
	 */
	public static int[] sortColors(PApplet theParent, int[] theColors,
			String theMethod) {
		theMethod = theMethod.toLowerCase();
		// create a special kind of "color object" for the sorting process
		ColorObject[] tmpColors = new ColorObject[theColors.length];
		for (int i = 0; i < tmpColors.length; i++) {
			if (theMethod.equals(GenerativeDesign.GRAYSCALE)) {
				// there is no "perfect" conversion from RGB to grayscale ...
				// intensity = float(red(c)*0.222 + green(c)*0.707 +
				// blue(c)*0.071);
				// GIMP use this one intensity = 0.3R + 0.59G + 0.11B
				// common for NTSC intensity = 0.2989*red + 0.5870*green +
				// 0.1140*blue
				tmpColors[i] = new ColorObject(theParent, theColors[i], 0.3f,
						0.59f, 0.11f);
			} else {
				tmpColors[i] = new ColorObject(theParent, theColors[i]);
			}
		}

		// switch according to style parameter
		// sort everything with comperators and Arrays.sort()
		if (theMethod.equals(GenerativeDesign.HUE)
				|| theMethod.equals(GenerativeDesign.RED)) {
			// first component channel
			xComperator xComparison = new xComperator();
			Arrays.sort(tmpColors, xComparison);
		}

		else if (theMethod.equals(GenerativeDesign.SATURATION)
				|| theMethod.equals(GenerativeDesign.GREEN)) {
			// second component channel
			yComperator yComparison = new yComperator();
			Arrays.sort(tmpColors, yComparison);
		}

		else if (theMethod.equals(GenerativeDesign.BRIGHTNESS)
				|| theMethod.equals(GenerativeDesign.BLUE)) {
			// third component channel
			zComperator zComparison = new zComperator();
			Arrays.sort(tmpColors, zComparison);
		}

		else if (theMethod.equals(GenerativeDesign.ALPHA)) {
			// alpha channel
			aComperator aComparison = new aComperator();
			Arrays.sort(tmpColors, aComparison);
		}

		else if (theMethod.equals(GenerativeDesign.GRAYSCALE)) {
			// grayscale
			grayscaleComperator grayscaleComparison = new grayscaleComperator();
			Arrays.sort(tmpColors, grayscaleComparison);
		}

		// copy back the new order
		for (int i = 0; i < tmpColors.length; i++) {
			theColors[i] = tmpColors[i].c;
		}
		return theColors;
	}

	/**
	 * Exports an .ase (Adobe Swatch Echange) file with default names
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theColors
	 *            color values (rgb 0-255) of the swatches
	 * @param theFileName
	 *            filename of the .ase file
	 */
	public static void saveASE(PApplet theParent, int[] theColors,
			String theFileName) {

		String[] names = new String[theColors.length];

		for (int i = 0; i < theColors.length; i++) {
			int rDec = (theColors[i] >> 16) & 0xFF;
			int gDec = (theColors[i] >> 8) & 0xFF;
			int bDec = theColors[i] & 0xFF;

			names[i] = "R=" + PApplet.nf(rDec, 3) + " G=" + PApplet.nf(gDec, 3)
					+ " B=" + PApplet.nf(bDec, 3);
		}

		saveASE(theParent, theColors, names, theFileName);
	}

	/**
	 * Exports an .ase (Adobe Swatch Echange) file
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theColors
	 *            color values (rgb 0-255) of the swatches
	 * @param theNames
	 *            names of the swatches
	 * @param theFileName
	 *            filename of the .ase file
	 */
	public static void saveASE(PApplet theParent, int[] theColors,
			String[] theNames, String theFileName) {
		String NUL = new Character((char) 0).toString();
		String SOH = new Character((char) 1).toString();

		int countColors = theColors.length;

		String ase = "ASEF" + NUL + SOH + NUL + NUL;
		for (int i = 24; i >= 0; i -= 8) {
			ase += new Character((char) ((countColors >> i) & 0xFF)).toString();
		}
		ase += NUL;

		for (int i = 0; i < countColors; i++) {
			ase += SOH + NUL + NUL + NUL;
			ase += new Character(
					(char) ((((theNames[i].length() + 1) * 2) + 20)))
					.toString()
					+ NUL;
			ase += new Character((char) (theNames[i].length() + 1)).toString()
					+ NUL;

			for (int j = 0; j < theNames[i].length(); j++) {
				ase += theNames[i].substring(j, j + 1) + NUL;
			}

			// extract red, green, and blue components from colors[i]
			float rDec = (theColors[i] >> 16) & 0xFF;
			float gDec = (theColors[i] >> 8) & 0xFF;
			float bDec = theColors[i] & 0xFF;

			String r = new String(floatTobytes(rDec / 255f));
			String g = new String(floatTobytes(gDec / 255f));
			String b = new String(floatTobytes(bDec / 255f));

			ase += NUL + "RGB ";
			ase += r.substring(0, 1) + r.substring(1, 2) + r.substring(2, 3)
					+ NUL;
			ase += g.substring(0, 1) + g.substring(1, 2) + g.substring(2, 3)
					+ NUL;
			ase += b.substring(0, 1) + b.substring(1, 2) + b.substring(2, 3)
					+ NUL;
			if ((i + 1) != countColors) {
				ase += NUL + NUL + NUL;
			}
		}
		ase += NUL + NUL;

		byte[] bytes = ase.getBytes();
		theParent.saveBytes(theFileName, bytes);
	}

	// mini helper function for "saveASE"
	private static byte[] floatTobytes(float theNumber) {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putFloat(theNumber);
		return buf.array();
	}

	/**
	 * Loads an image file asynchronously
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param thePath
	 *            Path/filename to load
	 * @return Empty XMLElement in which the data is loaded
	 */
	public static PImage loadImageAsync(PApplet theParent, String thePath) {
		PImage vessel = new PImage();
		new AsyncImageLoader(theParent, thePath, vessel);
		return vessel;
	}

	/**
	 * Loads a XML-File asynchronously
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theURL
	 *            URL or filename to load
	 * @return Empty XMLElement in which the data is loaded
	 */
	public static XML loadXMLAsync(PApplet theParent, String theURL) {
		//Node vessel = new Node();
		//try {
			XML vessel = new XML("test");
			new AsyncXMLLoader(theParent, theURL, vessel);
			return vessel;
		//} catch (Exception e) {
		//}
		
		//return null;
	}


	/**
	 * Loads a HTML-File asynchronously
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * @param theURL
	 *            URL or filename to load
	 * @return Empty String array in which the data is loaded
	 */
	public static ArrayList<String> loadHTMLAsync(PApplet theParent,
			String theURL) {
		ArrayList<String> vessel = new ArrayList<String>();
		new AsyncHTMLLoader(theParent, theURL, vessel);
		return vessel;
	}

	/**
	 * Loads a HTML-File asynchronously
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
	public static ArrayList<String> loadHTMLAsync(PApplet theParent,
			String theURL, int theMode) {
		ArrayList<String> vessel = new ArrayList<String>();
		new AsyncHTMLLoader(theParent, theURL, vessel, theMode);
		return vessel;
	}

	/**
	 * Calculates the minimal difference of two angles given in radians.
	 * 
	 * @param theAngle1
	 *            Angle to subtract from
	 * @param theAngle2
	 *            Angle to subtract
	 * @return Angle between -PI and PI (-180 deg and 180 deg)
	 */
	public static float angleDifference(float theAngle1, float theAngle2) {
		float a1 = (theAngle1 % PConstants.TWO_PI + PConstants.TWO_PI)
				% PConstants.TWO_PI;
		float a2 = (theAngle2 % PConstants.TWO_PI + PConstants.TWO_PI)
				% PConstants.TWO_PI;

		if (a2 > a1) {
			float d1 = a2 - a1;
			float d2 = a1 + PConstants.TWO_PI - a2;
			if (d1 <= d2) {
				return -d1;
			} else {
				return d2;
			}
		} else {
			float d1 = a1 - a2;
			float d2 = a2 + PConstants.TWO_PI - a1;
			if (d1 <= d2) {
				return d1;
			} else {
				return -d2;
			}
		}
		// return (PApplet.min(d1, d2) % PApplet.TWO_PI);
	}

	/**
	 * Converts 2D cartesian coordinates to polar coordinates
	 * 
	 * @param theX
	 *            X-coordinate
	 * @param theY
	 *            Y-coordinate
	 * @return Array containing 'length' and 'angle', so that rotating a point
	 *         (length, 0) around the origin with angle relults in point (theX,
	 *         theY)
	 */
	public static float[] cartesianToPolar(float theX, float theY) {
		float[] res = new float[2];
		res[0] = PApplet.mag(theX, theY);
		res[1] = PApplet.atan2(theY, theX);
		return res;
	}

	/**
	 * Converts 3D cartesian coordinates to polar coordinates
	 * 
	 * @param theX
	 *            X-coordinate
	 * @param theY
	 *            Y-coordinate
	 * @param theZ
	 *            Z-coordinate
	 * @return Array containing 'length', 'angleY' and 'angleZ', so that
	 *         rotating a point (length, 0, 0) first around the y-axis with
	 *         angleY and then around the z-axis with angleZ results in point
	 *         (theX, theY, theZ)
	 */
	public static float[] cartesianToPolar(float theX, float theY, float theZ) {
		float[] res = new float[3];
		res[0] = PApplet.mag(theX, theY, theZ);
		if (res[0] > 0) {
			res[1] = -PApplet.atan2(theZ, theX);
			res[2] = PApplet.asin(theY / res[0]);
		} else {
			res[1] = 0f;
			res[2] = 0f;
		}
		return res;
	}

	/**
	 * Converts 3D cartesian coordinates to polar coordinates
	 * 
	 * @param theVector
	 *            vector to convert
	 * @return Vector containing 'length', 'angleY' and 'angleZ', so that
	 *         rotating a point (length, 0, 0) first around the y-axis with
	 *         angleY and then around the z-axis with angleZ results in point
	 *         (theX, theY, theZ)
	 */
	public static PVector cartesianToPolar(PVector theVector) {
		PVector res = new PVector();
		res.x = theVector.mag();
		if (res.x > 0) {
			res.y = -PApplet.atan2(theVector.z, theVector.x);
			res.z = PApplet.asin(theVector.y / res.x);
		} else {
			res.y = 0f;
			res.z = 0f;
		}
		return res;
	}

	/**
	 * Converts 2D polar coordinates to cartesian coordinates
	 * 
	 * @param theLength
	 *            Distance of the point to the origin
	 * @param theAngle
	 *            Angle
	 * @return Array containing 'x' and 'y'
	 */
	public static float[] polarToCartesian(float theLength, float theAngle) {
		float[] res = new float[2];
		res[0] = PApplet.cos(theAngle) * theLength;
		res[1] = PApplet.sin(theAngle) * theLength;
		return res;
	}

	/**
	 * Converts 3D polar coordinates to cartesian coordinates
	 * 
	 * @param theLength
	 *            Distance of the point to the origin
	 * @param theAngleY
	 *            Angle for rotation around y-axis
	 * @param theAngleZ
	 *            Angle for rotation around z-axis
	 * @return Array containing 'x', 'y' and 'z'
	 */
	public static float[] polarToCartesian(float theLength, float theAngleY,
			float theAngleZ) {
		float[] res = new float[3];
		res[0] = PApplet.cos(theAngleZ) * PApplet.cos(theAngleY) * theLength;
		res[1] = PApplet.sin(theAngleZ) * theLength;
		res[2] = -PApplet.cos(theAngleZ) * PApplet.sin(theAngleY) * theLength;
		return res;
	}

	/**
	 * Converts 3D polar coordinates to cartesian coordinates
	 * 
	 * @param theVector
	 *            Vector containing (length, angleY, angleZ), where 'length' is
	 *            distance of the point to the origin, 'angleY' is the angle for
	 *            rotation around y-axis and 'angleZ' is the angle for rotation
	 *            around z-axis
	 * @return PVector
	 */
	public static PVector polarToCartesian(PVector theVector) {
		PVector res = new PVector();
		res.x = PApplet.cos(theVector.z) * PApplet.cos(theVector.y)
				* theVector.x;
		res.y = PApplet.sin(theVector.z) * theVector.x;
		res.z = -PApplet.cos(theVector.z) * PApplet.sin(theVector.y)
				* theVector.x;
		return res;
	}

	/**
	 * Takes an ArrayList and reorders the elements randomly.
	 * 
	 * @param theList
	 *            ArrayList to unsort
	 */
	public static void unsort(ArrayList<Object> theList) {
		for (int i = 0; i < theList.size(); i++) {
			Object oi = theList.get(i);
			int ir = (int) (Math.random() * theList.size());
			Object oir = theList.get(ir);
			theList.set(i, oir);
			theList.set(ir, oi);
		}
	}

}
