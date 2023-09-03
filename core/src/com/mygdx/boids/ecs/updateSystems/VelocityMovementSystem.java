package com.mygdx.boids.ecs.updateSystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.boids.ecs.components.ComponentManager;
import com.mygdx.boids.ecs.components.TransformComponent;
import com.mygdx.boids.ecs.components.VelocityComponent;
import com.mygdx.boids.ecs.entity.Entity;
import com.mygdx.boids.ecs.entity.EntityManager;

import java.util.List;

import static com.mygdx.boids.MathHacks.fastSqrt;

public class VelocityMovementSystem implements UpdateSystem {
	private final Vector2 tempVector = new Vector2();

	@Override
	public void update() {
		List<Entity> entities = EntityManager.get.getGlobalEntities();
		for(Entity entity : entities) {
			VelocityComponent velocityComponent = ComponentManager.get.getComponent(entity.getId(), VelocityComponent.class);
			TransformComponent transformComponent = ComponentManager.get.getComponent(entity.getId(), TransformComponent.class);

			if(velocityComponent != null && transformComponent != null){
				processEntity(velocityComponent, transformComponent);
			}
		}
	}

	private void processEntity(VelocityComponent velocityComponent, TransformComponent transformComponent) {
		limitSpeed(velocityComponent);

		tempVector.set(velocityComponent.velocity).scl(Gdx.graphics.getDeltaTime());
		transformComponent.position.add(tempVector);
	}

	private void limitSpeed(VelocityComponent velocityComponent){
		float vx = velocityComponent.velocity.x;
		float vy = velocityComponent.velocity.y;
		// Computing the squared length manually
		float currentSpeedSq = vx * vx + vy * vy;

		// Use fastSqrt to get the currentSpeed
		float currentSpeed = fastSqrt(currentSpeedSq);

		if(Math.abs(currentSpeed) < 1e-6) return;

		float minSpeed = velocityComponent.minSpeed;
		float maxSpeed = velocityComponent.maxSpeed;

		if(currentSpeed < minSpeed){
			velocityComponent.velocity.scl(minSpeed / currentSpeed);
		}
		else if(currentSpeed > maxSpeed){
			velocityComponent.velocity.scl(maxSpeed / currentSpeed);
		}
	}
}
