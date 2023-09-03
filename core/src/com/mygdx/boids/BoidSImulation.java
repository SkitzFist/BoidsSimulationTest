package com.mygdx.boids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.boids.ecs.entity.EntityManager;
import com.mygdx.boids.ecs.inputSystems.CameraInputSystem;
import com.mygdx.boids.ecs.inputSystems.InputSystem;
import com.mygdx.boids.ecs.renderSystems.CircleShapeRenderSystem;
import com.mygdx.boids.ecs.renderSystems.QuadTreeRenderSystem;
import com.mygdx.boids.ecs.renderSystems.RenderSystem;
import com.mygdx.boids.ecs.updateSystems.BoidSystem;
import com.mygdx.boids.ecs.updateSystems.ClampToWorldSystem;
import com.mygdx.boids.ecs.updateSystems.UpdateSystem;
import com.mygdx.boids.ecs.updateSystems.VelocityMovementSystem;

import java.util.ArrayList;
import java.util.List;

/*
	TODO:
		ComponentManager needs to sort entityComponent pairs, also a search is needed.
			1. Implement QuickSort
			2. Implement BinarySearch

 */

public class BoidSImulation extends ApplicationAdapter {

	private final List<InputSystem> m_inputSystem = new ArrayList<>();
	private final List<UpdateSystem> m_updateSystems = new ArrayList<>();
	private final List<RenderSystem> m_renderSystems = new ArrayList<>();
	private final List<RenderSystem> m_renderUiSystems = new ArrayList<>();

	private SpriteBatch m_batch;
	private ShapeRenderer m_shapeRenderer;
	private OrthographicCamera m_camera;
	
	@Override
	public void create () {
		m_batch = new SpriteBatch();
		m_shapeRenderer = new ShapeRenderer();

		m_camera = new OrthographicCamera();
		m_camera.setToOrtho(false, Settings.RESOLUTION_X, Settings.RESOLUTION_Y);

		addSystems();

		addBoids(500);
	}

	void addSystems(){
		//input
		m_inputSystem.add(new CameraInputSystem(m_camera));

		//update
		m_updateSystems.add(new BoidSystem());
		m_updateSystems.add(new VelocityMovementSystem());
		m_updateSystems.add(new ClampToWorldSystem());

		//render
		m_renderSystems.add(new CircleShapeRenderSystem(m_shapeRenderer));
		m_renderUiSystems.add(new QuadTreeRenderSystem(m_shapeRenderer, EntityManager.get.getTree()));

	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.BLACK);

		preUpdate();
		update();
		postUpdate();
	}

	private void preUpdate(){
		EntityManager.get.clearTree();
		EntityManager.get.reinsertEntities();

		m_camera.update();
	}

	private void update(){
		handleInputSystems();
		handleUpdateSystems();
		handleRenderSystems();
	}

	private void postUpdate(){

	}

	private void handleInputSystems(){
		for(InputSystem system : m_inputSystem){
			system.update();
		}
	}

	private void handleUpdateSystems(){
		for(UpdateSystem system : m_updateSystems){
			system.update();
		}
	}

	private void handleRenderSystems(){
		batchRender();
		shapeRender();
		m_camera.update();
	}

	private void shapeRender(){
		m_shapeRenderer.setAutoShapeType(true);
		m_shapeRenderer.setProjectionMatrix(m_camera.combined);
		m_camera.update();

		m_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for(RenderSystem system : m_renderSystems){
			system.update();
		}
		m_shapeRenderer.end();

		m_shapeRenderer.begin();
			for(RenderSystem uiSystem : m_renderUiSystems){
				uiSystem.update();
			}
			drawEdges();
		m_shapeRenderer.end();
	}

	private void drawEdges(){
		m_shapeRenderer.setColor(Color.GREEN);
		m_shapeRenderer.rect(1.f, 1.f, World.WIDTH -1.f, (World.HEIGHT -1.f));

		m_shapeRenderer.setColor(Color.GOLDENROD);
		m_shapeRenderer.rect(1.f, 1.f, Settings.RESOLUTION_X -1.f, Settings.RESOLUTION_Y-1.f);
	}

	private void batchRender(){
		m_batch.setProjectionMatrix(m_camera.combined);
		m_camera.update();
		m_batch.begin();

		m_batch.end();
	}

	void addBoids(int size){
		for(int i = 0; i < size; ++i){
			EntityManager.get.addEntity(BoidFactory.get.create());
		}
	}
	
	@Override
	public void dispose () {
		m_batch.dispose();
	}
}
