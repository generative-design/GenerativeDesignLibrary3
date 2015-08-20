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
 * This class extends the functionality of the Node class to provide some more
 * functions that make working with the attractor easier.  
 */
public class Attractor extends Node {
	
	private static final long serialVersionUID = 1L;
	
	// ------ constants ------
	/**
	 * Basic attractor function
	 */
	public final static int BASIC = 0;
	/**
	 * Attractor function with a smooth transition at the attractor radius
	 */
	public final static int SMOOTH = 1;
	/**
	 * Same as SMOOTH, but perpendicular to the attraction vector
	 */
	public final static int TWIRL = 2;

	
	// ------ public properties ------
	/**
	 * One of the constants BASIC, SMOOTH (default), TWIRL.
	 */
	public int mode = SMOOTH;

	/**
	 * Attraction strength (default = 1). Positive numbers mean attraction, negative numbers result in repulsion.
	 */
	public float strength = 1;

	// ------ private properties ------
	// an array to store nodes that should be affected by this attractor
	Node[] nodes = new Node[0];

	// ------ constructors ------
	/**
	 * Creates an attractor at default position (0, 0, 0).
	 */
	public Attractor() {
	}

	/**
	 * Creates an attractor position (theX, theY, 0).
	 */
	public Attractor(float theX, float theY) {
		super(theX, theY);
	}

	/**
	 * Creates an attractor position (theX, theY, theZ).
	 */
	public Attractor(float theX, float theY, float theZ) {
		super(theX, theY, theZ);
	}

	/**
	 * Creates an attractor at the position given by theVector.
	 */
	public Attractor(PVector theVector) {
		super(theVector);
	}

	// ------ public methods ------
	/**
	 * Adds a node to this attractor
	 * @param theNode Node to attach
	 */
	public void attachNode(Node theNode) {
		nodes = (Node[]) PApplet.append(nodes, theNode);
	}

	/**
	 * Performs attraction for all attached nodes.
	 */
	public void attract() {
		for (int i = 0; i < nodes.length; i++) {
			attract(nodes[i]);
		}
	}

	/**
	 * Performs attraction for theNode.
	 * @param theNode Node to attract
	 */
	/* (non-Javadoc)
	 * @see generativedesign.Node#attract(generativedesign.Node)
	 */
	@Override
	public void attract(Node theNode) {
		float d = this.dist(theNode);
		float f = 0;

		switch (mode) {
		case BASIC:
			if (d > 0 && d < radius) {
				float s = d / radius;
				f = (1 / PApplet.pow(s, (float) 0.5 * ramp) - 1);
				f = strength * f / radius;
			}
			break;

		case SMOOTH:
			if (d > 0 && d < radius) {
				float s = PApplet.pow(d / radius, 1 / ramp);
				f = s * 9 * strength * (1 / (s + 1) + ((s - 3) / 4)) / d;
			}
			break;

		case TWIRL:
			if (d > 0 && d < radius) {
				float s = PApplet.pow(d / radius, 1 / ramp);
				f = s * 9 * strength * (1 / (s + 1) + ((s - 3) / 4)) / d;
			}
			break;
		}

		// update velocity of the node
		PVector df = PVector.sub(this, theNode);
		df.mult(f);

		if (mode != TWIRL) {
			theNode.velocity.x += df.x;
			theNode.velocity.y += df.y;
			theNode.velocity.z += df.z;
		} else {
			theNode.velocity.x += df.y;
			theNode.velocity.y -= df.x;
			theNode.velocity.z += df.z;
		}

	}

	// ------ getters and setters ------
	public int getMode() {
		return mode;
	}

	public void setMode(int theMode) {
		this.mode = theMode;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float theStrength) {
		this.strength = theStrength;
	}
	
	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

}
