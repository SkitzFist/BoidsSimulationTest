package com.mygdx.boids.ecs.updateSystems;

import com.mygdx.boids.World;
import com.mygdx.boids.ecs.components.ComponentManager;
import com.mygdx.boids.ecs.components.TransformComponent;
import com.mygdx.boids.ecs.entity.Entity;
import com.mygdx.boids.ecs.entity.EntityManager;

public class ClampToWorldSystem implements UpdateSystem {
	@Override
	public void update() {
		for(Entity entity : EntityManager.get.getGlobalEntities()){
			TransformComponent transformComponent = ComponentManager.get.getComponent(
					entity.getId(), TransformComponent.class
			);

			if(transformComponent != null){
				processEntity(transformComponent);
			}
		}
	}

	private void processEntity(TransformComponent transformComponent){
		if(transformComponent.position.x < (0.f-(transformComponent.size.x * 2.f))){
			transformComponent.position.x = (World.WIDTH + transformComponent.size.x);
		}else if(transformComponent.position.x > World.WIDTH + (transformComponent.size.x * 2.f)){
			transformComponent.position.x = 0.f - transformComponent.size.x;
		}

		if(transformComponent.position.y < (0.f-(transformComponent.size.y * 2.f))){
			transformComponent.position.y = (World.HEIGHT + transformComponent.size.y);
		}else if(transformComponent.position.y > World.HEIGHT + (transformComponent.size.y * 2.f)){
			transformComponent.position.y = 0.f - transformComponent.size.y;
		}
	}
}
