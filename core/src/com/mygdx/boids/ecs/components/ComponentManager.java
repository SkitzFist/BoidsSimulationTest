package com.mygdx.boids.ecs.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: Sort EntityComponentPairs and use a binary search to find the correct item.
//TODO: Rebuild system or I'll have to deal with null all over the place.

public enum ComponentManager {
	get;

	private class EntityComponentPair{
		public final int entityId;
		public final Component component;

		public EntityComponentPair(int entityID, Component component){
			this.entityId = entityID;
			this.component = component;
		}

		public boolean isEqual(final EntityComponentPair other){
			return this.entityId == other.entityId;
		}

		public boolean isEqual(final int entityId){
			return this.entityId == entityId;
		}
	}

	private final static HashMap<Class<? extends Component>, List<EntityComponentPair>> m_components = new HashMap<>();

	public void addComponent(int entityId, Component component){
		if(!m_components.containsKey(component.getClass())){
			m_components.put(component.getClass(), new ArrayList<EntityComponentPair>());
		}

		m_components.get(component.getClass()).add(new EntityComponentPair(entityId, component));
	}

	public <T extends Component> T getComponent(int entityId, Class<T> componentClass) {
		if (m_components.containsKey(componentClass)) {
			for (EntityComponentPair pair : m_components.get(componentClass)) {
				if (pair.isEqual(entityId)) {
					return componentClass.cast(pair.component);
				}
			}
		}
		return null;
	}
}