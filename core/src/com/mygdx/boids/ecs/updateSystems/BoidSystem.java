package com.mygdx.boids.ecs.updateSystems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.boids.Log;
import com.mygdx.boids.MathHacks;
import com.mygdx.boids.ecs.components.BoidComponent;
import com.mygdx.boids.ecs.components.ComponentManager;
import com.mygdx.boids.ecs.components.TransformComponent;
import com.mygdx.boids.ecs.components.VelocityComponent;
import com.mygdx.boids.ecs.entity.Entity;
import com.mygdx.boids.ecs.entity.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class BoidSystem implements UpdateSystem{
	@Override
	public void update() {
		List<Entity> boids = EntityManager.get.getGlobalEntities();

		for(Entity boid : boids){
			processBoid(boid);
		}
	}

	private void processBoid(Entity boid){
		TransformComponent transform = ComponentManager.get.getComponent(boid.getId(), TransformComponent.class);
		VelocityComponent velocityComponent = ComponentManager.get.getComponent(boid.getId(), VelocityComponent.class);
		BoidComponent boidComponent = ComponentManager.get.getComponent(boid.getId(), BoidComponent.class);

		if(transform == null || velocityComponent == null || boidComponent == null){
			return;
		}

		List<Entity> neighbours = getBoidsQuadTree(transform, boidComponent);
		//List<Entity> neighbours = getBoidsLinear(transform, boidComponent.lookRange);

		Vector2 cohesion = getCohesion(neighbours, boid, transform.position);
		cohesion.scl(boidComponent.cohesionFactor);

		Vector2 alignment = getAlignment(neighbours, boid);
		alignment.scl(boidComponent.alignmentFactor);

		Vector2 seperation = getSeperation(neighbours, boid, boidComponent.lookRange / 3.f);
		seperation.scl(boidComponent.seperationFactor);

		Vector2 velocityToAdd = new Vector2();
		velocityToAdd.add(alignment);
		velocityToAdd.add(cohesion);
		velocityToAdd.add(seperation);

		velocityComponent.velocity.add(velocityToAdd);

	}

	private List<Entity> getBoidsQuadTree(TransformComponent transform, BoidComponent boidComponent){
		Vector2 fixedSize = new Vector2(
				boidComponent.lookRange / 2.f,
				boidComponent.lookRange / 2.f
		);
		Rectangle range = new Rectangle(
				transform.position.x - (fixedSize.x / 2.f),
				transform.position.y + (fixedSize.y / 2.f),
				fixedSize.x, fixedSize.y
		);
		List<Entity> neighbours = EntityManager.get.getEntitiesInRange(range);

		return neighbours;
	}

	private List<Entity> getBoidsLinear(TransformComponent transform, float lookRange){
		List<Entity> neighbours = new ArrayList<>();

		for(Entity entity : EntityManager.get.getGlobalEntities()){
			TransformComponent neighbourTransform = ComponentManager.get.getComponent(entity.getId(), TransformComponent.class);
			if(neighbourTransform == null){
				continue;
			}

			float dX = neighbourTransform.position.x - transform.position.x;
			float dY = neighbourTransform.position.y - transform.position.y;

			float distance = MathHacks.fastSqrt((dX * dX) + (dY*dY));
			if(distance <= lookRange){
				neighbours.add(entity);
			}
		}
		return neighbours;
	}

	private Vector2 getCohesion(List<Entity> neighbours, Entity boid, Vector2 boidPos){
		Vector2 cohesion = new Vector2();
		for(Entity neighbour : neighbours){
			if(neighbour.getId() == boid.getId()){
				continue;
			}

			TransformComponent neighbourTransform = ComponentManager.get.getComponent(
					neighbour.getId(),TransformComponent.class);
			if(neighbourTransform == null){
				continue;
			}

			cohesion.add(neighbourTransform.position);
		}

		if(neighbours.size() > 1){
			cohesion.scl(1.f/(neighbours.size() - 1));
			cohesion.sub(boidPos);
		}

		return cohesion;
	}

	private Vector2 getAlignment(List<Entity> neighbours, Entity boid){
		Vector2 alignment = new Vector2();

		for(Entity neighbour : neighbours){
			if(boid.getId() == neighbour.getId()){
				continue;
			}

			VelocityComponent neighbourVelocity = ComponentManager.get.getComponent(neighbour.getId(), VelocityComponent.class);
			if(neighbourVelocity == null){
				continue;
			}

			alignment.add(neighbourVelocity.velocity);
		}

		if(neighbours.size() > 1){
			alignment.scl(1.f / (neighbours.size() - 1));
		}

		return alignment;
	}

	private Vector2 getSeperation(List<Entity> neigbours, Entity boid, float seperationRange){
		Vector2 seperation = new Vector2();

		TransformComponent boidTransform = ComponentManager.get.getComponent(boid.getId(), TransformComponent.class);

		for(Entity neighbour : neigbours){
			if(boid.getId() == neighbour.getId()){
				continue;
			}

			TransformComponent neighbourTransform = ComponentManager.get.getComponent(neighbour.getId(), TransformComponent.class);

			if(neighbourTransform == null || boidTransform == null){
				continue;
			}

			Vector2 diff = new Vector2(
					boidTransform.position.x - neighbourTransform.position.x,
					boidTransform.position.y - neighbourTransform.position.y
			);

			float distance = MathHacks.fastSqrt((diff.x * diff.x) + (diff.y * diff.y));
			if(distance <= seperationRange){
				seperation.add(diff);
			}
		}

		return seperation;
	}
}
