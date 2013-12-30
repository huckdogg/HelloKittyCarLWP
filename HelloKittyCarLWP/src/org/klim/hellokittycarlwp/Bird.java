package org.klim.hellokittycarlwp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bird {
	
	//Bird's Properties
	private Bitmap image;
	private int x;
	private int y;
	private float velocity;
	private int width;
	private int height;
	private int wing_position;
	
	//Bird's Constructor
	public Bird(int x, int y, float velocity, int drawable, Context context) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		
		image = BitmapFactory.decodeResource(context.getResources(), drawable); 
		width = image.getWidth();
		height = image.getHeight();
		wing_position = 0;
	}
	
	public void move(int last_update) {
		this.x -= (int) (last_update*velocity);
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(int drawable, Context context) {
		image = BitmapFactory.decodeResource(context.getResources(), drawable);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWing_Position() {
		return wing_position;
	}
	
	public void setWing_Position(int wing_position) {
		this.wing_position = wing_position;
	}
	
	public void reset(int x, int y, float velocity) {
		
		this.y = y;
		this.x = x;
		this.velocity = velocity;
		this.wing_position = 0;
		
	}
	
}
