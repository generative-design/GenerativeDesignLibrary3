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
import processing.core.PVector;

/**
 * Basic class for calculating and drawing a mesh. A mesh is a two dimensional
 * grid (u/v-coordinates) which is deformed by using mathematical formulas
 * resulting in three dimensional surfaces.
 */
public class Mesh extends PApplet {

	private PApplet parent;

	// ------ constants ------

	public final static int PLANE = CUSTOM;
	public final static int TUBE = 1;
	public final static int SPHERE = 2;
	public final static int TORUS = 3;
	public final static int PARABOLOID = 4;
	public final static int STEINBACHSCREW = 5;
	public final static int SINE = 6;
	public final static int FIGURE8TORUS = 7;
	public final static int ELLIPTICTORUS = 8;
	public final static int CORKSCREW = 9;
	public final static int BOHEMIANDOME = 10;
	public final static int BOW = 11;
	public final static int MAEDERSOWL = 12;
	public final static int ASTROIDALELLIPSOID = 13;
	public final static int TRIAXIALTRITORUS = 14;
	public final static int LIMPETTORUS = 15;
	public final static int HORN = 16;
	public final static int SHELL = 17;
	public final static int KIDNEY = 18;
	public final static int LEMNISCAPE = 19;
	public final static int TRIANGULOID = 20;
	public final static int SUPERFORMULA = 21;

	// ------ mesh parameters ------

	public int form = PARABOLOID;

	public float uMin = -PI;
	public float uMax = PI;
	public int uCount = 50;

	public float vMin = -PI;
	public float vMax = PI;
	public int vCount = 50;

	public float[] params = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	public int drawMode = TRIANGLE_STRIP;
	public float minHue = 0;
	public float maxHue = 0;
	public float minSaturation = 0;
	public float maxSaturation = 0;
	public float minBrightness = 100;
	public float maxBrightness = 100;
	public float meshAlpha = 100;

	public float meshDistortion = 0;

	PVector[][] points;

	// ------ constructors ------

	/**
	 * Creates a mesh with default form and parameters.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 */
	public Mesh(PApplet theParent) {
		parent = theParent;
		form = CUSTOM;
		update();
	}

	/**
	 * Creates a mesh with the given form and default parameters.
	 * 
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * 
	 * @param theForm
	 *            One of the constants or CUSTOM.
	 */
	public Mesh(PApplet theParent, int theForm) {
		parent = theParent;
		if (theForm >= 0) {
			form = theForm;
		}
		update();
	}

	/**
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * 
	 * @param theForm
	 *            One of the constants or CUSTOM.
	 * @param theUCount
	 *            Number of tiles in u direction
	 * @param theVCount
	 *            Number of tiles in v direction
	 */
	public Mesh(PApplet theParent, int theForm, int theUCount, int theVCount) {
		parent = theParent;
		if (theForm >= 0) {
			form = theForm;
		}
		uCount = max(theUCount, 1);
		vCount = max(theVCount, 1);
		update();
	}

	/**
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * 
	 * @param theForm
	 *            One of the constants or CUSTOM.
	 * @param theUMin
	 *            Start value for u
	 * @param theUMax
	 *            End value for u
	 * @param theVMin
	 *            Start value for v
	 * @param theVMax
	 *            End value for v
	 */
	public Mesh(PApplet theParent, int theForm, float theUMin, float theUMax,
			float theVMin, float theVMax) {
		parent = theParent;
		if (theForm >= 0) {
			form = theForm;
		}
		uMin = theUMin;
		uMax = theUMax;
		vMin = theVMin;
		vMax = theVMax;
		update();
	}

	/**
	 * @param theParent
	 *            Reference to a PApplet. Typically use "this"
	 * 
	 * @param theForm
	 *            One of the constants or CUSTOM.
	 * @param theUCount
	 *            Number of tiles in u direction
	 * @param theVCount
	 *            Number of tiles in v direction
	 * @param theUMin
	 *            Start value for u
	 * @param theUMax
	 *            End value for u
	 * @param theVMin
	 *            Start value for v
	 * @param theVMax
	 *            End value for v
	 */
	public Mesh(PApplet theParent, int theForm, int theUCount, int theVCount,
			float theUMin, float theUMax, float theVMin, float theVMax) {
		parent = theParent;
		if (theForm >= 0) {
			form = theForm;
		}
		uCount = max(theUCount, 1);
		vCount = max(theVCount, 1);
		uMin = theUMin;
		uMax = theUMax;
		vMin = theVMin;
		vMax = theVMax;
		update();
	}

	// ------ calculate points ------

	/**
	 * Updates the points of the mesh. This is automatically called when
	 * creating the mesh. It has to be called manually every time that one of
	 * the parameters uCount, vCount, uMin, uMax, vMin or vMax has changed.
	 */
	public void update() {
		points = new PVector[vCount + 1][uCount + 1];

		float u, v;
		for (int iv = 0; iv <= vCount; iv++) {
			for (int iu = 0; iu <= uCount; iu++) {
				u = map(iu, 0, uCount, uMin, uMax);
				v = map(iv, 0, vCount, vMin, vMax);

				switch (form) {
				case CUSTOM:
					points[iv][iu] = calculatePoints(u, v);
					break;
				case TUBE:
					points[iv][iu] = calculateTube(u, v);
					break;
				case SPHERE:
					points[iv][iu] = calculateSphere(u, v);
					break;
				case TORUS:
					points[iv][iu] = calculateTorus(u, v);
					break;
				case PARABOLOID:
					points[iv][iu] = calculateParaboloid(u, v);
					break;
				case STEINBACHSCREW:
					points[iv][iu] = calculateSteinbachScrew(u, v);
					break;
				case SINE:
					points[iv][iu] = calculateSine(u, v);
					break;
				case FIGURE8TORUS:
					points[iv][iu] = calculateFigure8Torus(u, v);
					break;
				case ELLIPTICTORUS:
					points[iv][iu] = calculateEllipticTorus(u, v);
					break;
				case CORKSCREW:
					points[iv][iu] = calculateCorkscrew(u, v);
					break;
				case BOHEMIANDOME:
					points[iv][iu] = calculateBohemianDome(u, v);
					break;
				case BOW:
					points[iv][iu] = calculateBow(u, v);
					break;
				case MAEDERSOWL:
					points[iv][iu] = calculateMaedersOwl(u, v);
					break;
				case ASTROIDALELLIPSOID:
					points[iv][iu] = calculateAstroidalEllipsoid(u, v);
					break;
				case TRIAXIALTRITORUS:
					points[iv][iu] = calculateTriaxialTritorus(u, v);
					break;
				case LIMPETTORUS:
					points[iv][iu] = calculateLimpetTorus(u, v);
					break;
				case HORN:
					points[iv][iu] = calculateHorn(u, v);
					break;
				case SHELL:
					points[iv][iu] = calculateShell(u, v);
					break;
				case KIDNEY:
					points[iv][iu] = calculateKidney(u, v);
					break;
				case LEMNISCAPE:
					points[iv][iu] = calculateLemniscape(u, v);
					break;
				case TRIANGULOID:
					points[iv][iu] = calculateTrianguloid(u, v);
					break;
				case SUPERFORMULA:
					points[iv][iu] = calculateSuperformula(u, v);
					break;

				default:
					points[iv][iu] = calculatePoints(u, v);
					break;
				}
			}
		}
	}

	// ------ functions for calculating the mesh points ------

	/**
	 * Default function for calculating the points of the mesh. It will result
	 * in a simple plane. Override this method, if you want to define custom
	 * forms (see sketch Mesh_Custom.pde).
	 */
	public PVector calculatePoints(float u, float v) {
		float x = u;
		float y = v;
		float z = 0;

		return new PVector(x, y, z);
	}

	public PVector calculateTube(float u, float v) {
		float x = (sin(u));
		float y = params[0] * v;
		float z = (cos(u));

		return new PVector(x, y, z);
	}

	public PVector calculateSphere(float u, float v) {
		v /= 2;
		v += HALF_PI;
		float x = 2 * (sin(v) * sin(u));
		float y = 2 * (params[0] * cos(v));
		float z = 2 * (sin(v) * cos(u));

		return new PVector(x, y, z);
	}

	public PVector calculateTorus(float u, float v) {
		float x = 1 * ((params[1] + 1 + params[0] * cos(v)) * sin(u));
		float y = 1 * (params[0] * sin(v));
		float z = 1 * ((params[1] + 1 + params[0] * cos(v)) * cos(u));

		return new PVector(x, y, z);
	}

	public PVector calculateParaboloid(float u, float v) {
		float pd = params[0];
		if (pd == 0) {
			pd = 0.0001f;
		}
		float x = power((v / pd), 0.5f) * sin(u);
		float y = v;
		float z = power((v / pd), 0.5f) * cos(u);

		return new PVector(x, y, z);
	}

	public PVector calculateSteinbachScrew(float u, float v) {
		float x = u * cos(v);
		float y = u * sin(params[0] * v);
		float z = v * cos(u);

		return new PVector(x, y, z);
	}

	public PVector calculateSine(float u, float v) {
		float x = 2 * sin(u);
		float y = 2 * sin(params[0] * v);
		float z = 2 * sin(u + v);

		return new PVector(x, y, z);
	}

	public PVector calculateFigure8Torus(float u, float v) {
		float x = 1.5f * cos(u)
				* (params[0] + sin(v) * cos(u) - sin(2 * v) * sin(u) / 2);
		float y = 1.5f * sin(u)
				* (params[0] + sin(v) * cos(u) - sin(2 * v) * sin(u) / 2);
		float z = 1.5f * sin(u) * sin(v) + cos(u) * sin(2 * v) / 2;

		return new PVector(x, y, z);
	}

	public PVector calculateEllipticTorus(float u, float v) {
		float x = 1.5f * (params[0] + cos(v)) * cos(u);
		float y = 1.5f * (params[0] + cos(v)) * sin(u);
		float z = 1.5f * sin(v) + cos(v);

		return new PVector(x, y, z);
	}

	public PVector calculateCorkscrew(float u, float v) {
		float x = cos(u) * cos(v);
		float y = sin(u) * cos(v);
		float z = sin(v) + params[0] * u;

		return new PVector(x, y, z);
	}

	public PVector calculateBohemianDome(float u, float v) {
		float x = 2 * cos(u);
		float y = 2 * sin(u) + params[0] * cos(v);
		float z = 2 * sin(v);

		return new PVector(x, y, z);
	}

	public PVector calculateBow(float u, float v) {
		u /= TWO_PI;
		v /= TWO_PI;
		float x = (2 + params[0] * sin(TWO_PI * u)) * sin(2 * TWO_PI * v);
		float y = (2 + params[0] * sin(TWO_PI * u)) * cos(2 * TWO_PI * v);
		float z = params[0] * cos(TWO_PI * u) + 3 * cos(TWO_PI * v);

		return new PVector(x, y, z);
	}

	public PVector calculateMaedersOwl(float u, float v) {
		float x = 0.4f * (v * cos(u) - 0.5f * params[0] * power(v, 2)
				* cos(2 * u));
		float y = 0.4f * (-v * sin(u) - 0.5f * params[0] * power(v, 2)
				* sin(2 * u));
		float z = 0.4f * (4 * power(v, 1.5f) * cos(3 * u / 2) / 3);

		return new PVector(x, y, z);
	}

	public PVector calculateAstroidalEllipsoid(float u, float v) {
		u /= 2;
		float x = 3 * power(cos(u) * cos(v), 3 * params[0]);
		float y = 3 * power(sin(u) * cos(v), 3 * params[0]);
		float z = 3 * power(sin(v), 3 * params[0]);

		return new PVector(x, y, z);
	}

	public PVector calculateTriaxialTritorus(float u, float v) {
		float x = 1.5f * sin(u) * (1 + cos(v));
		float y = 1.5f * sin(u + TWO_PI / 3 * params[0])
				* (1 + cos(v + TWO_PI / 3 * params[0]));
		float z = 1.5f * sin(u + 2 * TWO_PI / 3 * params[0])
				* (1 + cos(v + 2 * TWO_PI / 3 * params[0]));

		return new PVector(x, y, z);
	}

	public PVector calculateLimpetTorus(float u, float v) {
		float x = 1.5f * params[0] * cos(u) / (sqrt(2) + sin(v));
		float y = 1.5f * params[0] * sin(u) / (sqrt(2) + sin(v));
		float z = 1.5f * 1 / (sqrt(2) + cos(v));

		return new PVector(x, y, z);
	}

	public PVector calculateHorn(float u, float v) {
		u /= PI;
		// v /= PI;
		float x = (2 * params[0] + u * cos(v)) * sin(TWO_PI * u);
		float y = (2 * params[0] + u * cos(v)) * cos(TWO_PI * u) + 2 * u;
		float z = u * sin(v);

		return new PVector(x, y, z);
	}

	public PVector calculateShell(float u, float v) {
		float x = params[1] * (1 - (u / TWO_PI)) * cos(params[0] * u)
				* (1 + cos(v)) + params[3] * cos(params[0] * u);
		float y = params[1] * (1 - (u / TWO_PI)) * sin(params[0] * u)
				* (1 + cos(v)) + params[3] * sin(params[0] * u);
		float z = params[2] * (u / TWO_PI) + params[0] * (1 - (u / TWO_PI))
				* sin(v);

		return new PVector(x, y, z);
	}

	public PVector calculateKidney(float u, float v) {
		u /= 2;
		float x = cos(u) * (params[0] * 3 * cos(v) - cos(3 * v));
		float y = sin(u) * (params[0] * 3 * cos(v) - cos(3 * v));
		float z = 3 * sin(v) - sin(3 * v);

		return new PVector(x, y, z);
	}

	public PVector calculateLemniscape(float u, float v) {
		u /= 2;
		float cosvSqrtAbsSin2u = cos(v) * sqrt(abs(sin(2 * params[0] * u)));
		float x = cosvSqrtAbsSin2u * cos(u);
		float y = cosvSqrtAbsSin2u * sin(u);
		float z = 3 * (power(x, 2) - power(y, 2) + 2 * x * y * power(tan(v), 2));
		x *= 3;
		y *= 3;
		return new PVector(x, y, z);
	}

	public PVector calculateTrianguloid(float u, float v) {
		float x = 0.75f * (sin(3 * u) * 2 / (2 + cos(v)));
		float y = 0.75f * ((sin(u) + 2 * params[0] * sin(2 * u)) * 2 / (2 + cos(v
				+ TWO_PI)));
		float z = 0.75f * ((cos(u) - 2 * params[0] * cos(2 * u)) * (2 + cos(v)) * ((2 + cos(v
				+ TWO_PI / 3)) * 0.25f));

		return new PVector(x, y, z);
	}

	public PVector calculateSuperformula(float u, float v) {
		v /= 2;

		// superformula 1
		float a = params[0];
		float b = params[1];
		float m = (params[2]);
		float n1 = (params[3]);
		float n2 = (params[4]);
		float n3 = (params[5]);
		float r1 = pow(pow(abs(cos(m * u / 4) / a), n2)
				+ pow(abs(sin(m * u / 4) / b), n3), -1 / n1);

		// superformula 2
		a = params[6];
		b = params[7];
		m = (params[8]);
		n1 = (params[9]);
		n2 = (params[10]);
		n3 = (params[11]);
		float r2 = pow(pow(abs(cos(m * v / 4) / a), n2)
				+ pow(abs(sin(m * v / 4) / b), n3), -1 / n1);

		float x = 2 * (r1 * sin(u) * r2 * cos(v));
		float y = 2 * (r2 * sin(v));
		float z = 2 * (r1 * cos(u) * r2 * cos(v));

		return new PVector(x, y, z);
	}

	// ------ definition of some mathematical functions ------

	/**
	 * The processing function pow() works a bit differently for negative bases.
	 * 
	 * @param b
	 *            Base
	 * @param e
	 *            Exponent
	 * @return b to the power of e 
	 */
	public float power(float b, float e) {
		if (b >= 0 || PApplet.parseInt(e) == e) {
			return pow(b, e);
		} else {
			return -pow(-b, e);
		}
	}

	/**
	 * The processing function log() does not take negative numbers. Sometimes
	 * it is convenient to have this form.
	 * 
	 * @param v
	 *            Number (may also be negative)
	 * @return Natural logarithm of the number.
	 */
	public float logE(float v) {
		if (v >= 0) {
			return log(v);
		} else {
			return -log(-v);
		}
	}

	// ------ draw mesh ------

	/* 
	 * Call this function to draw the mesh.
	 */
	public void draw() {
		int iuMax, ivMax;

		if (drawMode == QUADS || drawMode == TRIANGLES) {
			iuMax = uCount - 1;
			ivMax = vCount - 1;
		} else {
			iuMax = uCount;
			ivMax = vCount - 1;
		}

		// store previously set colorMode
		parent.pushStyle();
		parent.colorMode(HSB, 360, 100, 100, 100);

		float minH = minHue;
		float maxH = maxHue;
		if (abs(maxH - minH) < 20)
			maxH = minH;
		float minS = minSaturation;
		float maxS = maxSaturation;
		if (abs(maxS - minS) < 10)
			maxS = minS;
		float minB = minBrightness;
		float maxB = maxBrightness;
		if (abs(maxB - minB) < 10)
			maxB = minB;

		for (int iv = 0; iv <= ivMax; iv++) {

			if (drawMode == TRIANGLES) {

				for (int iu = 0; iu <= iuMax; iu++) {
					parent.fill(random(minH, maxH), random(minS, maxS), random(
							minB, maxB), meshAlpha);
					parent.beginShape(drawMode);
					float r1 = random(-meshDistortion, meshDistortion);
					float r2 = random(-meshDistortion, meshDistortion);
					float r3 = random(-meshDistortion, meshDistortion);
					parent.vertex(points[iv][iu].x + r1, points[iv][iu].y + r2,
							points[iv][iu].z + r3);
					parent.vertex(points[iv + 1][iu + 1].x + r1,
							points[iv + 1][iu + 1].y + r2,
							points[iv + 1][iu + 1].z + r3);
					parent.vertex(points[iv + 1][iu].x + r1,
							points[iv + 1][iu].y + r2, points[iv + 1][iu].z
									+ r3);
					parent.endShape();

					parent.fill(random(minH, maxH), random(minS, maxS), random(
							minB, maxB), meshAlpha);
					parent.beginShape(drawMode);
					r1 = random(-meshDistortion, meshDistortion);
					r2 = random(-meshDistortion, meshDistortion);
					r3 = random(-meshDistortion, meshDistortion);
					parent.vertex(points[iv + 1][iu + 1].x + r1,
							points[iv + 1][iu + 1].y + r2,
							points[iv + 1][iu + 1].z + r3);
					parent.vertex(points[iv][iu].x + r1, points[iv][iu].y + r2,
							points[iv][iu].z + r3);
					parent.vertex(points[iv][iu + 1].x + r1,
							points[iv][iu + 1].y + r2, points[iv][iu + 1].z
									+ r3);
					parent.endShape();
				}

			} else if (drawMode == QUADS) {
				for (int iu = 0; iu <= iuMax; iu++) {
					parent.fill(random(minH, maxH), random(minS, maxS), random(
							minB, maxB), meshAlpha);
					parent.beginShape(drawMode);

					float r1 = random(-meshDistortion, meshDistortion);
					float r2 = random(-meshDistortion, meshDistortion);
					float r3 = random(-meshDistortion, meshDistortion);
					parent.vertex(points[iv][iu].x + r1, points[iv][iu].y + r2,
							points[iv][iu].z + r3);
					parent.vertex(points[iv + 1][iu].x + r1,
							points[iv + 1][iu].y + r2, points[iv + 1][iu].z
									+ r3);
					parent.vertex(points[iv + 1][iu + 1].x + r1,
							points[iv + 1][iu + 1].y + r2,
							points[iv + 1][iu + 1].z + r3);
					parent.vertex(points[iv][iu + 1].x + r1,
							points[iv][iu + 1].y + r2, points[iv][iu + 1].z
									+ r3);

					parent.endShape();
				}
			} else {
				// draw Strips
				parent.fill(random(minH, maxH), random(minS, maxS), random(
						minB, maxB), meshAlpha);
				parent.beginShape(drawMode);

				for (int iu = 0; iu <= iuMax; iu++) {
					float r1 = random(-meshDistortion, meshDistortion);
					float r2 = random(-meshDistortion, meshDistortion);
					float r3 = random(-meshDistortion, meshDistortion);
					parent.vertex(points[iv][iu].x + r1, points[iv][iu].y + r2,
							points[iv][iu].z + r3);
					parent.vertex(points[iv + 1][iu].x + r1,
							points[iv + 1][iu].y + r2, points[iv + 1][iu].z
									+ r3);
				}

				parent.endShape();
			}

		}
		parent.popStyle();
	}

	// ------ getters and setters ------

	public int getForm() {
		return form;
	}

	public void setForm(int theValue) {
		form = theValue;
	}

	public String getFormName() {
		switch (form) {
		case CUSTOM:
			return "Custom";
		case TUBE:
			return "Tube";
		case SPHERE:
			return "Sphere";
		case TORUS:
			return "Torus";
		case PARABOLOID:
			return "Paraboloid";
		case STEINBACHSCREW:
			return "Steinbach Screw";
		case SINE:
			return "Sine";
		case FIGURE8TORUS:
			return "Figure 8 Torus";
		case ELLIPTICTORUS:
			return "Elliptic Torus";
		case CORKSCREW:
			return "Corkscrew";
		case BOHEMIANDOME:
			return "Bohemian Dome";
		case BOW:
			return "Bow";
		case MAEDERSOWL:
			return "Maeders Owl";
		case ASTROIDALELLIPSOID:
			return "Astoidal Ellipsoid";
		case TRIAXIALTRITORUS:
			return "Triaxial Tritorus";
		case LIMPETTORUS:
			return "Limpet Torus";
		case HORN:
			return "Horn";
		case SHELL:
			return "Shell";
		case KIDNEY:
			return "Kidney";
		case LEMNISCAPE:
			return "Lemniscape";
		case TRIANGULOID:
			return "Trianguloid";
		case SUPERFORMULA:
			return "Superformula";
		}
		return "";
	}

	public float getUMin() {
		return uMin;
	}

	public void setUMin(float theValue) {
		uMin = theValue;
	}

	public float getUMax() {
		return uMax;
	}

	public void setUMax(float theValue) {
		uMax = theValue;
	}

	public int getUCount() {
		return uCount;
	}

	public void setUCount(int theValue) {
		uCount = theValue;
	}

	public float getVMin() {
		return vMin;
	}

	public void setVMin(float theValue) {
		vMin = theValue;
	}

	public float getVMax() {
		return vMax;
	}

	public void setVMax(float theValue) {
		vMax = theValue;
	}

	public int getVCount() {
		return vCount;
	}

	public void setVCount(int theValue) {
		vCount = theValue;
	}

	public float[] getParams() {
		return params;
	}

	public void setParams(float[] theValues) {
		params = theValues;
	}

	public float getParam(int theIndex) {
		return params[theIndex];
	}

	public void setParam(int theIndex, float theValue) {
		params[theIndex] = theValue;
	}

	public int getDrawMode() {
		return drawMode;
	}

	public void setDrawMode(int theMode) {
		drawMode = theMode;
	}

	public float getMeshDistortion() {
		return meshDistortion;
	}

	public void setMeshDistortion(float theValue) {
		meshDistortion = theValue;
	}

	public void setColorRange(float theMinHue, float theMaxHue,
			float theMinSaturation, float theMaxSaturation,
			float theMinBrightness, float theMaxBrightness, float theMeshAlpha) {
		minHue = theMinHue;
		maxHue = theMaxHue;
		minSaturation = theMinSaturation;
		maxSaturation = theMaxSaturation;
		minBrightness = theMinBrightness;
		maxBrightness = theMaxBrightness;
		meshAlpha = theMeshAlpha;
	}

	public float getMinHue() {
		return minHue;
	}

	public void setMinHue(float minHue) {
		this.minHue = minHue;
	}

	public float getMaxHue() {
		return maxHue;
	}

	public void setMaxHue(float maxHue) {
		this.maxHue = maxHue;
	}

	public float getMinSaturation() {
		return minSaturation;
	}

	public void setMinSaturation(float minSaturation) {
		this.minSaturation = minSaturation;
	}

	public float getMaxSaturation() {
		return maxSaturation;
	}

	public void setMaxSaturation(float maxSaturation) {
		this.maxSaturation = maxSaturation;
	}

	public float getMinBrightness() {
		return minBrightness;
	}

	public void setMinBrightness(float minBrightness) {
		this.minBrightness = minBrightness;
	}

	public float getMaxBrightness() {
		return maxBrightness;
	}

	public void setMaxBrightness(float maxBrightness) {
		this.maxBrightness = maxBrightness;
	}

	public float getMeshAlpha() {
		return meshAlpha;
	}

	public void setMeshAlpha(float meshAlpha) {
		this.meshAlpha = meshAlpha;
	}

}
