package com.mygdx.boids.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component{
	public Vector2 velocity;
	public float maxSpeed;
	public float minSpeed;

	public VelocityComponent(Vector2 velocity, float maxSpeed, float minSpeed){
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
	}
}
