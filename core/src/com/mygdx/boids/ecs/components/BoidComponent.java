package com.mygdx.boids.ecs.components;

public class BoidComponent implements Component {
	public float lookRange;
	public float cohesionFactor;
	public float seperationFactor;
	public float alignmentFactor;

	public BoidComponent(float lookRange, float cohesionFactor, float seperationFactor, float alignmentFactor){
		this.lookRange = lookRange;
		this.cohesionFactor = cohesionFactor;
		this.seperationFactor = seperationFactor;
		this.alignmentFactor = alignmentFactor;
	}
}
