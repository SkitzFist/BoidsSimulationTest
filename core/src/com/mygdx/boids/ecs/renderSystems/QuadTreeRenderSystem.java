package com.mygdx.boids.ecs.renderSystems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.boids.QuadTree;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeRenderSystem implements RenderSystem{

	private final ShapeRenderer m_shapeRenderer;
	private final QuadTree m_quadTree;

	public QuadTreeRenderSystem(ShapeRenderer shapeRenderer, QuadTree quadTree){
		m_shapeRenderer = shapeRenderer;
		m_quadTree = quadTree;
	}

	@Override
	public void update() {
		List<Rectangle> boundries = new ArrayList<>();
		getBoundries(m_quadTree, boundries);
		render(boundries);
	}

	private void render(List<Rectangle> boundries){
		m_shapeRenderer.setColor(Color.GOLDENROD);

		for(Rectangle boundry : boundries){
			m_shapeRenderer.rect(boundry.x, boundry.y, boundry.width, boundry.height);
		}
	}

	private void getBoundries(QuadTree node, List<Rectangle> boundries){
		if(node.getLevel() == QuadTree.MAX_LEVEL){
			if(node.getEntitiesSize() > 0){
				boundries.add(node.getBounds());
			}
			return;
		}

		if(node.getEntitiesSize() > 0){
			boundries.add(node.getBounds());
		}

		for(QuadTree childNode : node.getNodes()){
			if(childNode == null){
				continue;
			}

			getBoundries(childNode, boundries);
		}
	}
}
