package cr.ac.itcr.andreifuentes.flappybirdclase;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;

	Texture background;
	Texture topTube;
	Texture bottomTube;
	Texture[] birds;
	Texture gameOver;
	Texture facil;
	Texture intermedio;
	Texture dificil;
	Sprite facilSprite;
	Sprite intermedioSprite;
	Sprite dificilSprite;
	Sprite birdSprite;
	Music musica;
	int birdState;
	float gap;
	float birdY;
	float velocity;
	float gravity;
	int numberOfPipes = 4;
	float pipeX[] = new float[numberOfPipes];
	float pipeYOffset[] = new float[numberOfPipes];
	float distance;
	float pipeVelocity = 5;
	Random random;
	float maxLine;
	float minLine;
	int score;
	int pipeActivo;
	BitmapFont font;
	int game_state;
	int birdSize;
	boolean bajando;

	Circle birdCircle;
	Rectangle[] topPipes;
	Rectangle[] bottomPipes;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameOverOriginal.png");
		facil = new Texture("facil.jpg");
		intermedio = new Texture("intermedio.jpg");
		dificil = new Texture("dificil.jpg");
		musica = Gdx.audio.newMusic(Gdx.files.internal("1.mp3"));


		facilSprite = new Sprite(facil);
		facilSprite.setSize(400, 150);
		facilSprite.setPosition(Gdx.graphics.getWidth()/2-facilSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-facilSprite.getHeight()/2) - 200);
		intermedioSprite = new Sprite(intermedio);
		intermedioSprite.setSize(400, 150);
		intermedioSprite.setPosition(Gdx.graphics.getWidth()/2-intermedioSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-intermedioSprite.getHeight()/2) - 500);
		dificilSprite = new Sprite(dificil);
		dificilSprite.setSize(400, 150);
		dificilSprite.setPosition(Gdx.graphics.getWidth()/2-facilSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-dificilSprite.getHeight()/2) - 800);



		birdCircle = new Circle();
		topPipes = new Rectangle[numberOfPipes];
		bottomPipes = new Rectangle[numberOfPipes];

		birdState = 0;
		game_state = 0;
		gap = 500;
		velocity = 0;
		gravity = 0.7f;
		random = new Random();
		distance = Gdx.graphics.getWidth() * 3/5;
		maxLine = Gdx.graphics.getHeight()* 4/5;
		minLine = Gdx.graphics.getHeight()* 1/4;
		score = 0;
		pipeActivo = 0;
		birdSize = 0;

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[birdState].getHeight()/2;
		for (int i = 0; i<numberOfPipes; i++){
			pipeYOffset[i] = (random.nextFloat()*(maxLine-minLine)+minLine);
			pipeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth() + Gdx.graphics.getWidth() + distance*i;

			// inicializamos cada uno de los Shapes
			topPipes[i] = new Rectangle();
			bottomPipes[i] = new Rectangle();
		}
	}

	public void drawMenu() {
		/*facilSprite = new Sprite(facil);
		facilSprite.setSize(400, 150);
		facilSprite.setPosition(Gdx.graphics.getWidth()/2-facilSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-facilSprite.getHeight()/2) - 200);
		intermedioSprite = new Sprite(intermedio);
		facilSprite.setSize(400, 150);
		intermedioSprite.setPosition(Gdx.graphics.getWidth()/2-intermedioSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-intermedioSprite.getHeight()/2) - 500);
		dificilSprite = new Sprite(dificil);
		facilSprite.setSize(400, 150);
		dificilSprite.setPosition(Gdx.graphics.getWidth()/2-facilSprite.getWidth()/2,(Gdx.graphics.getHeight()/2-dificilSprite.getHeight()/2) - 800);*/


		facilSprite.setSize(400, 150);
		intermedioSprite.setSize(400, 150);
		dificilSprite.setSize(400, 150);
		facilSprite.draw(batch);
        intermedioSprite.draw(batch);
        dificilSprite.draw(batch);


    }

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawMenu();

		facilSprite.draw(batch);
		intermedioSprite.draw(batch);
		dificilSprite.draw(batch);
        // no iniciado
		if (game_state == 0){
			if (Gdx.input.justTouched()){
                if (facilSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                    gravity = 0.4f;
                	game_state = 1;


                }
				if (intermedioSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
					gravity = 0.6f;
                	game_state = 1;
				}
				if (dificilSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
					gravity = 0.8f;
                	game_state = 1;
				}

				//facilSprite.setSize(0,0);
				//intermedioSprite.setSize(0,0);
				//dificilSprite.setSize(0,0);
				facilSprite.flip(false, true);
               // facilSprite.setColor(0);
               // intermedioSprite.setColor(0);
               // dificilSprite.setColor(0);
			    //game_state = 1;
			}
		}
		// jugando
		else if (game_state == 1){
			musica.setLooping(true);
			musica.play();
			if (pipeX[pipeActivo] < Gdx.graphics.getWidth()/2 - topTube.getWidth()){
				score++;

				if (pipeActivo < numberOfPipes - 1){
					pipeActivo++;
				}
				else {
					pipeActivo = 0;
				}

				Gdx.app.log("score", Integer.toString(score));
			}


			birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[birdState].getHeight()/2, birds[birdState].getWidth()/2);

//			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//			shapeRenderer.setColor(Color.MAGENTA);
//			shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//

			// Posicionamiento de los pipes
			for (int i = 0; i<numberOfPipes; i++) {

				if (pipeX[i] < -topTube.getWidth()){
					pipeYOffset[i] = (random.nextFloat()*(maxLine-minLine)+minLine);
					pipeX[i] += distance*(numberOfPipes);
				}
				else {
					pipeX[i] = pipeX[i] - pipeVelocity;
				}

				batch.draw(topTube,
						pipeX[i],
						pipeYOffset[i]+gap/2,
						topTube.getWidth(),
						topTube.getHeight());
				batch.draw(bottomTube,
						pipeX[i],
						pipeYOffset[i]-bottomTube.getHeight()-gap/2,
						bottomTube.getWidth(),
						bottomTube.getHeight());

				topPipes[i] = new Rectangle(pipeX[i],
						pipeYOffset[i]+gap/2,
						topTube.getWidth(),
						topTube.getHeight());
				bottomPipes[i] = new Rectangle(pipeX[i],
						pipeYOffset[i]-bottomTube.getHeight()-gap/2,
						bottomTube.getWidth(),
						bottomTube.getHeight());

//				shapeRenderer.rect(topPipes[i].x, topPipes[i].y, topTube.getWidth(),
//						topTube.getHeight());
//				shapeRenderer.rect(bottomPipes[i].x, bottomPipes[i].y, bottomTube.getWidth(),
//						bottomTube.getHeight());

				if (Intersector.overlaps(birdCircle, topPipes[i])){
					Gdx.app.log("Intersector", "top pipe overlap");
					game_state = 2;
				}
				else if (Intersector.overlaps(birdCircle, bottomPipes[i])){
					Gdx.app.log("Intersector", "bottom pipe overlap");
					game_state = 2;
				}
			}

			if (Gdx.input.justTouched()){
				velocity = velocity - 15;

			}

			birdState = birdState == 0 ? 1 : 0;


			velocity = velocity + gravity;


			if (birdY < 0){
				game_state = 2;
			}
			else {
				birdY = birdY - velocity;
			}

//			shapeRenderer.end();


		}
		// game over
		else if (game_state == 2){
			drawMenu();
		    musica.stop();
			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);
			if (Gdx.input.justTouched()){
                /*if (facilSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                    gravity = 0.4f;
                    game_state = 1;


                }
                if (intermedioSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                    gravity = 0.6f;
                    game_state = 1;
                }
                if (dificilSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())) {
                    gravity = 0.8f;
                    game_state = 1;
                }

                facilSprite.setColor(0);
                intermedioSprite.setColor(0);
                dificilSprite.setColor(0);*/
			    game_state = 1;
				score = 0;
				pipeActivo = 0;
				velocity = 0;
				startGame();
			}
		}

		birdSprite = new Sprite(birds[birdState]);
		birdSprite.setSize(100, 100);
		birdSprite.setPosition(Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth()/2, birdY);

		if(birdY > (Gdx.graphics.getHeight() / 2) - 150){
			birdSprite.rotate(45);
			bajando = false;
		}
		else if(birdY < (Gdx.graphics.getHeight() / 2) + 150){
			birdSprite.rotate(-45);
			bajando = true;
		}
		else {
			if(bajando) birdSprite.rotate(45);
			else birdSprite.rotate(-45);
		}


		birdSprite.draw(batch);
		//batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth()/2,  birdY,  birds[birdState].getWidth() + birdSize, birds[birdState].getHeight() + birdSize);
		font.draw(batch, Integer.toString(score), Gdx.graphics.getWidth()*1/8, Gdx.graphics.getHeight()*9/10);
		//birdSize += 1;
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
