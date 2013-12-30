package org.klim.hellokittycarlwp;

import java.util.Random;

import org.klim.hellokittycarlwp.HelloKitty;
import org.klim.hellokittycarlwp.LWPConfig;
import org.klim.hellokittycarlwp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.Toast;

public class SceneService extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		return new SceneEngine();
	}
	
	class SceneEngine extends Engine {
		private Handler scene_handler;
		private LWPConfig config;
		private Bitmap background;
		
		private boolean visible;
		private float offset = 0;
		private long start_time;
		private long hk_move_time;
		private long bird_anim_timer;
		private boolean birdFly = false;
		private boolean hellokittywave_visible = false;
		
		private Random random;
		
		private HelloKitty hellokitty;
		private HelloKitty hellokittywave;
		private Bird bird;
	
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);

			//initialise global LWP variables
			config = new LWPConfig();

			//get display width and height in pixels
			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			config.screen_width = display.getWidth(); 
			config.screen_height = display.getHeight();

			//set screen resolution variable
			setScreenResolution(config.screen_height, config.screen_width);

			//set everything up in advance
			initializeScene();
		}

		private void setScreenResolution(int screen_height, int screen_width) {

			if (screen_height == 854 || screen_width == 854) {
				config.screen_type = LWPConfig.ScreenType.HDPI854;
			} else if (screen_height == 480 || screen_width == 480) {
				config.screen_type = LWPConfig.ScreenType.MDPI;
			} else {
				config.screen_type = LWPConfig.ScreenType.HDPI;
			}

		}

		private void initializeScene() {

			scene_handler = new Handler();

			//set background based on screen resolution
			if (config.screen_type == LWPConfig.ScreenType.HDPI854) {
				background = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.fwvga_bg); 
			} else if (config.screen_type == LWPConfig.ScreenType.MDPI) {
				background = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.img_bg); 
			} else {
				background = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.img_bg); 
			}

			//create array list of heart objects, randomize the sizes
			random = new Random();

//			if (config.screen_type == LWPConfig.ScreenType.MDPI) {
//				hellokitty = new HelloKitty(random.nextInt(config.screen_width + 400), (random.nextInt(config.screen_height)), .1f, R.drawable.hellokitty, getBaseContext());
//			} else {
//				hellokitty = new HelloKitty(random.nextInt(config.screen_width + 800), (random.nextInt(config.screen_height)), .3f, R.drawable.hellokitty, getBaseContext());
//			}
			
			if (config.screen_type == LWPConfig.ScreenType.MDPI) {
				hellokitty = new HelloKitty(641, 250, .05f, R.drawable.hellokitty, getBaseContext());
			} else {
				hellokitty = new HelloKitty(961, 365, .1f, R.drawable.hellokitty, getBaseContext());
			}
			
			if (config.screen_type == LWPConfig.ScreenType.MDPI) {
				hellokittywave = new HelloKitty(641, 250, .05f, R.drawable.hellokitty_wave, getBaseContext());
			} else {
				hellokittywave = new HelloKitty(961, 365, .1f, R.drawable.hellokitty_wave, getBaseContext());
			}
			
			if (config.screen_type == LWPConfig.ScreenType.MDPI) {
				bird = new Bird(641, 250, .07f, R.drawable.bird_down, getBaseContext());
			} else {
				bird = new Bird(961, random.nextInt(275) + 25, .17f, R.drawable.bird_down, getBaseContext());
			}
			

			//Set Start time of live wallpaper scene
			start_time = System.currentTimeMillis();
		}
		
		@Override
        public void onDestroy() {
            super.onDestroy();
            //if the LWP is no longer running, clean up
            scene_handler.removeCallbacks(DrawSceneThread);

        }
		
		@Override
        public void onVisibilityChanged(boolean visible) {
        	this.visible = visible;
            if (visible) {
            	//screen was just turned on, restart animation
            	drawFrame();
            } else {
            	//screen is going to standby, stop animation
            	scene_handler.removeCallbacks(DrawSceneThread);
            }
        }
		
		@Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }
        
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            //if the LWP is no longer running, clean up
            visible = false;
            scene_handler.removeCallbacks(DrawSceneThread);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
        	//get the amount of movement when user is swiping between home screens
        	offset = xPixels;
        	
        }
        
        @Override
        public void onTouchEvent(MotionEvent event) {
        	
        	//get coordinates of touch event
        	int touchpoint_x = (int) event.getX();
        	int touchpoint_y = (int) event.getY();
        	
        	if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		//check to see if the user clicked on hellokitty
            	if (touchpoint_x > hellokitty.getX() &&
            			touchpoint_x < (hellokitty.getX() + hellokitty.getWidth()) &&
            			touchpoint_y > hellokitty.getY() &&
            			touchpoint_y < (hellokitty.getY() + hellokitty.getHeight())) {
            		
            			//hellokitty.reset(config.screen_width + 1, 365, .1f);
            			hk_move_time = start_time + 2000;
            			hellokittywave.setX(hellokitty.getX());
            			hellokittywave.setY(hellokitty.getY());
            			hellokittywave_visible = true;
            	}
            	
            	//check to see if sun was clicked
            	//add code for different screen sizes later
            	if (touchpoint_x > 268 &&
            			touchpoint_x < 372 &&
            			touchpoint_y > 175 &&
            			touchpoint_y < 280 &&
            			!birdFly) {
            		birdFly = true;
            		bird_anim_timer = start_time;
            	}
            	//Test
        		/*Context context = getApplicationContext();
        		CharSequence text = "" + touchpoint_x + " " + touchpoint_y;
        		int duration = Toast.LENGTH_SHORT;

        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();*/
        	}
        	
        	super.onTouchEvent(event);
        }
        
        private void drawFrame() {
        	//grab a canvas to draw on
        	final SurfaceHolder holder = getSurfaceHolder();
            final Rect frame = holder.getSurfaceFrame();
            final int width = frame.width();
            final int height = frame.height();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	//update physics and draw a new frame
                	updatePhysics();
                	drawScene(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            if (visible) {
            	//allow thread to sleep, set frame rate
            	scene_handler.postDelayed(DrawSceneThread, 25);
            }

        }
        
      //actual canvas drawing is done here for each frame
        private void drawScene(Canvas canvas) {
        	
        	//draw the bacground
        	canvas.drawBitmap(background, -240/*offset*/, 0, null);
        	
        	//draw hellokitty
        	if (!hellokittywave_visible)
        		canvas.drawBitmap(hellokitty.getImage(), hellokitty.getX(), hellokitty.getY(), null);
        	
        	//draw waving hellokitty
        	if (hellokittywave_visible)
        		canvas.drawBitmap(hellokittywave.getImage(), hellokittywave.getX(), hellokittywave.getY(), null);
        	
        	//draw bird with wing down
        	if (birdFly)
        		canvas.drawBitmap(bird.getImage(), bird.getX(), bird.getY(), null);
        	
        }
        
      //update the physics, specifically hellokitty and the bird
        private void updatePhysics() {
        	
        	//get current milliseconds
        	long now = System.currentTimeMillis();
        	
        	//get time since last physics update
        	int last_update = (int) (now - start_time);
        	if (now >= hk_move_time) {
        		//hellokittywave.reset(961, 365, .1f);
        		hellokittywave_visible = false;
	        	
	        	if (hellokitty.getX() < -351) {
					if (config.screen_type == LWPConfig.ScreenType.MDPI) {
						hellokitty.reset(config.screen_width + 1, config.screen_height - 150, .05f);
					} else {
						hellokitty.reset(config.screen_width + 1, 365, .1f);
					}
					
					//randomly set the bird to fly every time hellokitty resets
		        	if (!birdFly && random.nextInt(5) == 3) {
		        		birdFly = true;
		        	}
				} else {
					hellokitty.move(last_update);
				}
	        	
        	}
        	
        	if (birdFly) {
	        	if (bird.getX() < -75) {
					if (config.screen_type == LWPConfig.ScreenType.MDPI) {
						bird.reset(config.screen_width + 1, config.screen_height - 150, .07f);
					} else {
						bird.reset(config.screen_width + 1, random.nextInt(275) + 25, .17f);
					}
					birdFly = false;
				} else {
					if (start_time - bird_anim_timer > 500) {						
						switch (bird.getWing_Position()) {
						case 1:
							bird.setWing_Position(0);
							bird.setImage(R.drawable.bird_down, getBaseContext());
							break;
						case 0:
							bird.setWing_Position(1);
							bird.setImage(R.drawable.bird_up, getBaseContext());
							break;
						}
						bird_anim_timer = start_time;
					}
					bird.move(last_update);
				}
        	}

        	//set current time
        	start_time = now;
        }
        
        private final Runnable DrawSceneThread = new Runnable() {
            public void run() {
            	//Scene Thread that draws a new frame every time it is run
                drawFrame();
            }
        };
	
	}

}
