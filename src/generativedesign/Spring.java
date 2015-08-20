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

import processing.core.PVector;

/**
 * Elasic connection between two nodes.
 */
public class Spring {

	// if needed, an ID for the spring
	public String id = "";

	public Node fromNode;
	public Node toNode;

	public float length = 100;
	public float stiffness = 0.6f;
	public float damping = 0.9f;

	// ------ constructors ------
	/**
	 * @param theFromNode
	 *            Node from which the spring starts
	 * @param theToNode
	 *            Node to which the spring goes
	 */
	public Spring(Node theFromNode, Node theToNode) {
		this.fromNode = theFromNode;
		this.toNode = theToNode;
	}

	/**
	 * @param theFromNode
	 *            Node from which the spring starts
	 * @param theToNode
	 *            Node to which the spring goes
	 * @param theLength
	 *            Target length of the spring
	 * @param theStiffness Value from 0 to 1. 0: no forces will be applied, 1: high spring forces
	 * @param theDamping Value from 0 to 1. 0: no damping of force, 1: complete damping of force
	 */
	public Spring(Node theFromNode, Node theToNode, float theLength,
			float theStiffness, float theDamping) {
		this.fromNode = theFromNode;
		this.toNode = theToNode;

		this.length = theLength;
		this.stiffness = theStiffness;
		this.damping = theDamping;
	}

	/**
	 * Apply forces on spring and attached nodes
	 */
	public void update() {
		// calculate the target position
		// target = normalize(to - from) * length + from
		PVector diff = PVector.sub(toNode, fromNode);
		diff.normalize();
		diff.mult(length);
		PVector target = PVector.add(fromNode, diff);

		PVector force = PVector.sub(target, toNode);
		force.mult(0.5f);
		force.mult(stiffness);
		force.mult(1 - damping);

		toNode.velocity.add(force);
		fromNode.velocity.add(PVector.mult(force, -1));
	}

	// ------ getters and setters ------
	public String getID() {
		return id;
	}

	public void setID(String theID) {
		this.id = theID;
	}

	public Node getFromNode() {
		return fromNode;
	}

	public void setFromNode(Node theFromNode) {
		this.fromNode = theFromNode;
	}

	public Node getToNode() {
		return toNode;
	}

	public void setToNode(Node theToNode) {
		this.toNode = theToNode;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float theLength) {
		this.length = theLength;
	}

	public float getStiffness() {
		return stiffness;
	}

	public void setStiffness(float theStiffness) {
		this.stiffness = theStiffness;
	}

	public float getDamping() {
		return damping;
	}

	public void setDamping(float theDamping) {
		this.damping = theDamping;
	}

}
