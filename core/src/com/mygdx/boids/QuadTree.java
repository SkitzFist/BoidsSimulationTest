package com.mygdx.boids;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.boids.ecs.components.ComponentManager;
import com.mygdx.boids.ecs.components.TransformComponent;
import com.mygdx.boids.ecs.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuadTree {
	public static final int MAX_LEVEL = 6;
	private static final Rectangle CHILD_RECT_FOR_TESTING = new Rectangle();

	private final int m_level;
	private final List<Entity> m_entities;
	private final Rectangle m_bounds;

	private QuadTree[] m_nodes;

	public QuadTree(int level, Rectangle bounds){

		if(level != MAX_LEVEL){
			m_nodes = new QuadTree[4];
		}

		m_level = level;
		m_bounds = bounds;

		m_entities = new ArrayList<>();
	}

	public void clear(){
		m_entities.clear();

		if(m_level == MAX_LEVEL){
			return;
		}

		for(int i = 0; i < 4; ++i){
			if(m_nodes[i] != null){
				m_nodes[i].clear();
			}
		}
	}

	public void insert(Entity entity){
		Rectangle entityBounds = Objects.requireNonNull(ComponentManager.get.getComponent(
				entity.getId(), TransformComponent.class)).getRect();

		if(m_bounds.overlaps(entityBounds)){
			insert(entity, entityBounds);
		}
	}

	private int insert(Entity entity, Rectangle entityBounds){
		if(m_level == MAX_LEVEL){
			m_entities.add(entity);
			return MAX_LEVEL;
		}

		for(int i = 0; i < 4; ++i){
			if(containsInNode(m_bounds, i, entityBounds)){
				if(m_nodes[i] == null){
					m_nodes[i] = new QuadTree(m_level + 1, new Rectangle(CHILD_RECT_FOR_TESTING));
				}
				return m_nodes[i].insert(entity,entityBounds);
			}
		}

		m_entities.add(entity);
		return m_level;
	}

	private boolean containsInNode(Rectangle parentBounds, int direction, Rectangle entityBounds){
		float halfWidth = parentBounds.width / 2;
		float halfHeight = parentBounds.height / 2;
		switch (direction){
			case 0:
				return CHILD_RECT_FOR_TESTING.set(
						parentBounds.x, parentBounds.y + halfHeight,
						halfWidth, halfHeight
				).contains(entityBounds);
			case 1:
				return CHILD_RECT_FOR_TESTING.set(
						parentBounds.x + halfWidth,
						parentBounds.y + halfHeight,
						halfWidth, halfHeight
				).contains(entityBounds);
			case 2:
				return CHILD_RECT_FOR_TESTING.set(
						parentBounds.x, parentBounds.y,
						halfWidth, halfHeight
				).contains(entityBounds);
			case 3:
				return CHILD_RECT_FOR_TESTING.set(
						parentBounds.x + halfWidth,
						parentBounds.y,
						halfWidth, halfHeight
				).contains(entityBounds);
			default:
				return false;
		}
	}

	public List<Entity> queryRange(Rectangle range) {
		List<Entity> result = new ArrayList<>();

		if(m_bounds.overlaps(range)){
			queryRange(result, range);
		}

		return result;
	}

	private void queryRange(List<Entity> entities, Rectangle range) {

		int childIndex = -1;
		for (int i = 0; i < m_nodes.length; ++i) {
			if (m_nodes[i] == null) {
				continue;
			}

			if(m_nodes[i].m_bounds.contains(range)){
				childIndex = i;
			}
		}

		if(childIndex != -1){
			m_nodes[childIndex].queryRange(entities, range);
		}

	}

	private boolean childNodeContainsRange(QuadTree node, Rectangle range){
		return node.m_bounds.contains(range);
	}

	public Rectangle getBounds(){
		return m_bounds;
	}

	public int getEntitiesSize(){
		return m_entities.size();
	}

	public int getLevel(){
		return m_level;
	}

	public QuadTree[] getNodes(){
		return m_nodes;
	}
}
