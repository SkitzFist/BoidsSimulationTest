package com.mygdx.boids.ecs.inputSystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class CameraInputSystem implements InputSystem{

	private final OrthographicCamera m_camera;

	public CameraInputSystem(OrthographicCamera camera){
		m_camera = camera;
	}

	@Override
	public void update() {
		handleMovement();
		handleZoom();
	}

	private void handleMovement(){
		float step = 256.f * Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			m_camera.position.y += step;
		}else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			m_camera.position.y -= step;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			m_camera.position.x -= step;
		}else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			m_camera.position.x += step;
		}

		m_camera.update();
	}

	private void handleZoom(){
		float step = 0.5f * Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			m_camera.zoom += step;
		}else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			m_camera.zoom -= step;
		}

		m_camera.update();
	}
}
