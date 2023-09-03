package com.mygdx.boids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.boids.ecs.components.*;
import com.mygdx.boids.ecs.entity.Entity;

public enum BoidFactory {
	get;

	private static int m_idCounter = 0;
	private final static CircleShapeComponent m_circleShapeComponent = new CircleShapeComponent(Color.WHITE);

	public Entity create() {
		Entity entity = new Entity(m_idCounter++);

		ComponentManager.get.addComponent(entity.getId(), m_circleShapeComponent);

		float size = 10.f;
		Vector2 sizeVector = new Vector2(size, size);
		Vector2 position = new Vector2(
				MathUtils.random(size, World.WIDTH - size),
				MathUtils.random(size, World.HEIGHT - size)
		);
		ComponentManager.get.addComponent(entity.getId(), new TransformComponent(position, sizeVector));

		float maxSpeed = 150.f;
		float minSpeed = 50.f;
		float speed = MathUtils.random(minSpeed, maxSpeed);
		Vector2 velocity = new Vector2(
				MathUtils.random(-speed, speed),
				MathUtils.random(-speed, speed)
		);
		ComponentManager.get.addComponent(entity.getId(), new VelocityComponent(velocity, maxSpeed, minSpeed));

		float lookRange = 100.f;
		float alignmentFactor = MathUtils.random(0.1f, 1.f);
		float cohesionFactor = MathUtils.random(0.1f, 1.f);
		float seperationFactor = MathUtils.random(0.1f, 1.f);
		ComponentManager.get.addComponent(entity.getId(), new BoidComponent(lookRange,cohesionFactor, seperationFactor, alignmentFactor));

		return entity;
	}
}
