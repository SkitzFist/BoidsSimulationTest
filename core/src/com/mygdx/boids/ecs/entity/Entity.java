package com.mygdx.boids.ecs.entity;

public class Entity {
	final int m_id;

	public Entity(int id){
		m_id = id;
	}

	public int getId(){
		return m_id;
	}
}
