package com.mygdx.boids.ecs.components;

import com.badlogic.gdx.graphics.Color;

public class CircleShapeComponent implements Component{
	public Color m_color;

	public CircleShapeComponent(Color color){
		m_color = color;
	}
}
