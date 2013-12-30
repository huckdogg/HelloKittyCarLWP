package org.klim.hellokittycarlwp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HelloKitty {
	
	//HelloKitty's Properties
	private Bitmap image;
	private int x;
	private int y;
	private float velocity;
	private int width;
	private int height;
	
	//HelloKitty's Constructor
	public HelloKitty(int x, int y, float velocity, int drawable, Context context) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		
		image = BitmapFactory.decodeResource(context.getResources(), drawable); 
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public void move(int last_update) {
		this.x -= (int) (last_update*velocity);
	}
	
	public Bitmap getImage() {
		return image;
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
	
	public void reset(int x, int y, float velocity) {
		
		this.y = y;
		this.x = x;
		this.velocity = velocity;
		
	}
	
}
