package com.mygdx.boids.ecs.renderSystems;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.boids.ecs.components.CircleShapeComponent;
import com.mygdx.boids.ecs.components.ComponentManager;
import com.mygdx.boids.ecs.components.TransformComponent;
import com.mygdx.boids.ecs.entity.Entity;
import com.mygdx.boids.ecs.entity.EntityManager;

import java.util.List;

public class CircleShapeRenderSystem implements RenderSystem{

	private final ShapeRenderer m_shapeRenderer;

	public CircleShapeRenderSystem(ShapeRenderer shapeRenderer){
		m_shapeRenderer = shapeRenderer;
	}

	@Override
	public void update() {
		List<Entity> globalEntities = EntityManager.get.getGlobalEntities();

		for(Entity entity : globalEntities){
			CircleShapeComponent circleShapeComponent = ComponentManager.get.getComponent(entity.getId(), CircleShapeComponent.class);
			TransformComponent transformComponent = ComponentManager.get.getComponent(entity.getId(), TransformComponent.class);

			if(circleShapeComponent != null && transformComponent != null){
				renderEntity(circleShapeComponent, transformComponent);
			}
		}
	}

	private void renderEntity(CircleShapeComponent circleShapeComponent, TransformComponent transformComponent){
		m_shapeRenderer.setColor(circleShapeComponent.m_color);

		m_shapeRenderer.circle(transformComponent.position.x, transformComponent.position.y, transformComponent.size.x);
	}
}
