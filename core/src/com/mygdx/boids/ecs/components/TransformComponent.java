package com.mygdx.boids.ecs.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
	public Vector2 position;
	public Vector2 size;
	private Rectangle rect;
	public TransformComponent(Vector2 position, Vector2 size){
		this.position = position;
		this.size = size;
		rect = new Rectangle();
	}

	public Rectangle getRect(){
		rect.set(position.x, position.y, size.x, size.y);
		return rect;
	}
}
