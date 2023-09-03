package com.mygdx.boids.ecs.entity;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.boids.QuadTree;
import com.mygdx.boids.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum EntityManager {
	get;

	private final QuadTree m_quadTree = new QuadTree(0, new Rectangle(0.f,0.f, World.WIDTH, World.HEIGHT));
	private final List<Entity> m_entities = new ArrayList<>();

	public QuadTree getTree(){
		return m_quadTree;
	}

	public void addEntity(Entity entity){
		m_entities.add(entity);
	}

	public void addEntities(List<Entity> entities){
		for(Entity entity : entities){
			m_quadTree.insert(entity);
		}
	}

	public final List<Entity> getGlobalEntities(){
		return Collections.unmodifiableList(m_entities);
	}

	public List<Entity> getEntitiesInRange(Rectangle range){
		return m_quadTree.queryRange(range);
	}

	public void clearTree(){
		m_quadTree.clear();
	}

	public void reinsertEntities(){
		addEntities(m_entities);
	}

}
