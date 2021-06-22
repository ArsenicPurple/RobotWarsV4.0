package co.basin;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Robot Wars V4.0 ,
 *     Copyright (C) 2021 Samuel Devers
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

public class RobotWars extends ApplicationAdapter {

	private Viewport viewport;
	private OrthographicCamera camera;

	Texture texture;
	ShapeDrawer drawer;
	SpriteBatch batch;

	private GUI gui;
	private EntityManager em = new EntityManager();

	@Override
	public void create () {
//		Gdx.app.getInput().setInputProcessor(new InputProcessor() {
//			@Override
//			public boolean keyDown(int keycode) {
//				return false;
//			}
//
//			@Override
//			public boolean keyUp(int keycode) {
//				return false;
//			}
//
//			@Override
//			public boolean keyTyped(char character) {
//				return false;
//			}
//
//			@Override
//			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//				float min = Integer.MAX_VALUE;
//				Robot r = null;
//				for (Robot robot : em.getRobots()) {
//					float dst = robot.getPosition().dst(screenX, screenY);
//					if (dst < min) {
//						min = dst;
//						r = robot;
//					}
//				}
//				if (r != null) {
//					System.out.println(r.name);
//					System.out.println(r.getScanner().contains(screenX, screenY));;
//				}
//				return false;
//			}
//
//			@Override
//			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//				return false;
//			}
//
//			@Override
//			public boolean touchDragged(int screenX, int screenY, int pointer) {
//				return false;
//			}
//
//			@Override
//			public boolean mouseMoved(int screenX, int screenY) {
//				return false;
//			}
//
//			@Override
//			public boolean scrolled(float amountX, float amountY) {
//				return false;
//			}
//		});
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 900, 900);
		viewport = new FitViewport(900, 900, camera);
		batch = new SpriteBatch();
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		texture = new Texture(pixmap); //remember to dispose of later
		pixmap.dispose();
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
		drawer = new ShapeDrawer(batch, region);

		Robot[] startingRobots = new Robot[Constants.robots.length];
		for (int i = 0; i < Constants.robots.length; i++) {
			String className = "co.basin.BotAI." + Constants.robots[i];
			try {
				Random rand = new Random();
				Method method = Class.forName(className).getMethod("init", float.class, float.class, EntityManager.class, String.class, int.class, Color.class);
				startingRobots[i] = (Robot) method.invoke(null, rand.nextInt(900), rand.nextInt(900), em, Constants.robots[i], i, Constants.colors[i % 9]);
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				System.out.println("Error Initializing Robot of Class: " + Constants.robots[i]);
				e.printStackTrace();
			}
		}
		gui = new GUI(em, batch);
		em.init(startingRobots);
	}

	public void update() {
		em.update();
		em.finish();
	}

	@Override
	public void render () {
		update();
		ScreenUtils.clear(Constants.darkGray3);
		batch.setTransformMatrix(camera.view);
		batch.setProjectionMatrix(camera.projection);
		batch.begin();
		em.render(drawer);
		gui.render(drawer);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		camera.update();
	}


	@Override
	public void dispose () {
		texture.dispose();
	}

	public static Rectangle getDimensions() { return new Rectangle(0, 0, 900, 900); }
}
