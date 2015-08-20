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
 * The node class supplies a basic object for physical simulations. It has a
 * position (which can be constrained), a velocity vector with damping and it
 * can attract or repulse other nodes.
 */
public class Node extends PVector {

	private static final long serialVersionUID = 1L;
	
	// ------ public properties ------
	// if needed, an ID for the node
	public String id = "";
	
	/**
	 * Diameter of the node (useful if a click on the node has to be checked)
	 */
	public float diameter = 0;

	/**
	 * Minimum x position
	 */
	public float minX = -Float.MAX_VALUE;
	/**
	 * Maximum x position
	 */
	public float maxX = Float.MAX_VALUE;
	/**
	 * Minimum y position
	 */
	public float minY = -Float.MAX_VALUE;
	/**
	 * Maximum y position
	 */
	public float maxY = Float.MAX_VALUE;
	/**
	 * Minimum z position
	 */
	public float minZ = -Float.MAX_VALUE;
	/**
	 * Maximum z position
	 */
	public float maxZ = Float.MAX_VALUE;

	/**
	 * Velocity vector (speed)
	 */
	public PVector velocity = new PVector();
	PVector pVelocity = new PVector();
	/**
	 * Maximum length of the velocity vector (default = 10)
	 */
	public float maxVelocity = 10;
	/**
	 * Damping of the velocity: 0=no damping, 1=full damping (default = 0.5)
	 */
	public float damping = 0.5f;
	// inertia of node: 0=no inertia (not used for now)
	// public float inertia = 0.5f;

	// radius of impact
	public float radius = 200;
	// strength: positive for attraction, negative for repulsion (default for
	// Nodes)
	public float strength = -1;
	// parameter that influences the form of the function
	public float ramp = 1.0f;

	// ------ constructors ------
	public Node() {
	}

	public Node(float theX, float theY) {
		x = theX;
		y = theY;
	}

	public Node(float theX, float theY, float theZ) {
		x = theX;
		y = theY;
		z = theZ;
	}

	public Node(PVector theVector) {
		x = theVector.x;
		y = theVector.y;
		z = theVector.z;
	}

	// ------ rotate position around origin ------
	public void rotateX(float theAngle) {
		float newy = y * PApplet.cos(theAngle) - z * PApplet.sin(theAngle);
		float newz = y * PApplet.sin(theAngle) + z * PApplet.cos(theAngle);
		y = newy;
		z = newz;
	}

	public void rotateY(float theAngle) {
		float newx = x * PApplet.cos(-theAngle) - z * PApplet.sin(-theAngle);
		float newz = x * PApplet.sin(-theAngle) + z * PApplet.cos(-theAngle);
		x = newx;
		z = newz;
	}

	public void rotateZ(float theAngle) {
		float newx = x * PApplet.cos(theAngle) - y * PApplet.sin(theAngle);
		float newy = x * PApplet.sin(theAngle) + y * PApplet.cos(theAngle);
		x = newx;
		y = newy;
	}

	// ------ calculate attraction ------
	public void attract(Node[] theNodes) {
		// attraction or repulsion part
		for (int i = 0; i < theNodes.length; i++) {
			Node otherNode = theNodes[i];
			// stop when empty
			if (otherNode == null)
				break;
			// not with itself
			if (otherNode == this)
				continue;

			this.attract(otherNode);
		}
	}

	public void attract(Node theNode) {
		float d = PVector.dist(this, theNode);

		if (d > 0 && d < radius) {
			float s = PApplet.pow(d / radius, 1 / ramp);
			float f = s * 9 * strength * (1 / (s + 1) + ((s - 3) / 4)) / d;
			PVector df = PVector.sub(this, theNode);
			df.mult(f);

			theNode.velocity.x += df.x;
			theNode.velocity.y += df.y;
			theNode.velocity.z += df.z;
		}
	}

	// ------ update positions ------
	public void update() {
		update(false, false, false);
	}
	
	@SuppressWarnings("deprecation")
	public void update(boolean theLockX, boolean theLockY, boolean theLockZ) {

		velocity.limit(maxVelocity);

		// prevent oscillating by reducing velocity if angle to previous
		// velocity is very large
		/*float da = PVector.angleBetween(velocity, pVelocity);
		if (!Float.isNaN(da)) {
			da = PApplet.abs(1 - (da / PApplet.PI));
			//da = PApplet.pow(da, 4);
			if (da < 0.5) da = 0;
			PApplet.println(id + ", " + da);
			velocity.mult(da);
		}*/
		pVelocity = velocity.get();

		if (!theLockX) x += velocity.x;
		if (!theLockY) y += velocity.y;
		if (!theLockZ) z += velocity.z;

		if (x < minX) {
			x = minX - (x - minX);
			velocity.x = -velocity.x;
		}
		if (x > maxX) {
			x = maxX - (x - maxX);
			velocity.x = -velocity.x;
		}

		if (y < minY) {
			y = minY - (y - minY);
			velocity.y = -velocity.y;
		}
		if (y > maxY) {
			y = maxY - (y - maxY);
			velocity.y = -velocity.y;
		}

		if (z < minZ) {
			z = minZ - (z - minZ);
			velocity.z = -velocity.z;
		}
		if (z > maxZ) {
			z = maxZ - (z - maxZ);
			velocity.z = -velocity.z;
		}

		// x = PApplet.constrain(x, minX, maxX);
		// y = PApplet.constrain(y, minY, maxY);
		// z = PApplet.constrain(z, minZ, maxZ);

		velocity.mult(1 - damping);
	}

	// ------ getters and setters ------
	public String getID() {
		return id;
	}

	public void setID(String theID) {
		this.id = theID;
	}

	public float getDiameter() {
		return diameter;
	}

	public void setDiameter(float theDiameter) {
		this.diameter = theDiameter;
	}

	public void setBoundary(float theMinX, float theMinY, float theMinZ,
			float theMaxX, float theMaxY, float theMaxZ) {
		this.minX = theMinX;
		this.maxX = theMaxX;
		this.minY = theMinY;
		this.maxY = theMaxY;
		this.minZ = theMinZ;
		this.maxZ = theMaxZ;
	}

	public void setBoundary(float theMinX, float theMinY, float theMaxX,
			float theMaxY) {
		this.minX = theMinX;
		this.maxX = theMaxX;
		this.minY = theMinY;
		this.maxY = theMaxY;
	}

	public float getMinX() {
		return minX;
	}

	public void setMinX(float theMinX) {
		this.minX = theMinX;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float theMaxX) {
		this.maxX = theMaxX;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float theMinY) {
		this.minY = theMinY;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float theMaxY) {
		this.maxY = theMaxY;
	}

	public float getMinZ() {
		return minZ;
	}

	public void setMinZ(float theMinZ) {
		this.minZ = theMinZ;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public void setMaxZ(float theMaxZ) {
		this.maxZ = theMaxZ;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public void setVelocity(PVector theVelocity) {
		this.velocity = theVelocity;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float theMaxVelocity) {
		this.maxVelocity = theMaxVelocity;
	}

	public float getDamping() {
		return damping;
	}

	public void setDamping(float theDamping) {
		this.damping = theDamping;
	}

	/*
	 * public float getInertia() { return inertia; }
	 * 
	 * public void setInertia(float theInertia) { this.inertia = theInertia; }
	 */

	public float getRadius() {
		return radius;
	}

	public void setRadius(float theRadius) {
		this.radius = theRadius;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float theStrength) {
		this.strength = theStrength;
	}

	public float getRamp() {
		return ramp;
	}

	public void setRamp(float theRamp) {
		this.ramp = theRamp;
	}

}
