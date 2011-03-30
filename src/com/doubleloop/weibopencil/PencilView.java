package com.doubleloop.weibopencil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;


public class PencilView extends View {

	private float pencilX;
	private float pencilY;

	private Paint mPaint; // Ink
	private Paint mPencilPaint; // Line representing a pencil - Disabled.
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Canvas mPencilCanvas;
	private Path mPath;
	private Paint mDataPaint;

	private Paint mBitmapPaint;
	private float mPressureThreshold;
	private float mPressureValue;
	
	public Paint getPencilPaint(){
		return mPaint;
	}
	public void setPencilPaintColor(int color){
		mPaint.setColor(color);
		mPaint.setAlpha(50);
	}

	public PencilView(Context context) {
		super(context);
		pencilX = 0f;
		pencilY = 0f;
		mPaint = new Paint();
		mPressureThreshold = 0.0f;

		mDataPaint = new Paint();

		mPaint.setColor(Color.BLACK);
		mPaint.setAlpha(50);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.BEVEL);
		mPaint.setStrokeCap(Paint.Cap.BUTT);
		mPaint.setStrokeWidth(5);

		mPencilPaint = new Paint();

		mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPencilCanvas = new Canvas();
		mPath = new Path();
		mPath.setFillType(FillType.EVEN_ODD);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		// mPaint.setAlpha(50);
		// Additional path effect
		// pe = new PathEffect();
		// pe.
		// mPaint.setPathEffect(pe);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y, float pressure) {
		mPath.reset();
		mX = x - pencilX;
		mY = y - pencilY;
		mPath.moveTo(mX, mY);
		invalidate();

	}
	
	public void setPencil(boolean enabled){
		if(enabled){
			
		}
		else{
			
		}
	}

	private void touch_move(float x, float y, float pressure) {
		// float dx = Math.abs(x - mX);
		// float dy = Math.abs(y - mY);
		// if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
		// mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);

		if (pressure > mPressureThreshold) {
			// Move to line start point
			mPath.moveTo(mX, mY);
			// Draw line
			mPath.lineTo(x - pencilX, y - pencilY);
			mCanvas.drawPath(mPath, mPaint);
			mPath.reset();
			mPath.moveTo(x - pencilX, y - pencilY);
		}
		// Save line end point
		mX = x - pencilX;
		mY = y - pencilY;
	}

	private void touch_up(float pressure) {

		if (pressure > mPressureThreshold) {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw, since they have been drew
			// to
			// bitmap
			mPath.reset();

		}
		// set pressure to 0
		mPressureValue = 0;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// set pressue value
		mPressureValue = event.getPressure();

		// draw lines according to user's touch
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y, mPressureValue);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y, mPressureValue);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up(mPressureValue);
			invalidate();
			break;
		}
		return true;
	}

	public Bitmap background = null;
	
	@Override
	protected void onDraw(Canvas canvas) {

		//canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
//		if (null != background) { 
//			background.draw(canvas);
//		}
		
		// Draw picture from Weibo
		

		canvas.drawText("Pressure :" + String.valueOf(mPressureValue), 10,
				30, mDataPaint);
		canvas.drawText("Threshold :" + String.valueOf(mPressureThreshold),
				10, 50, mDataPaint);
//
//		//
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		
		// canvas.drawLine(50f, 50f, 150f, 150f, mPaint);
		//
		if (mPressureValue > mPressureThreshold) {
			mPencilPaint.setColor(0xff880088);
			mPencilPaint.setStrokeWidth(5);
		} else {
			mPencilPaint.setColor(Color.GREEN);
			mPencilPaint.setStyle(Paint.Style.STROKE);
			mPencilPaint.setStrokeWidth(8);
		}
		canvas.drawLine(mX, mY, mX + pencilX, mY + pencilY, mPencilPaint);
		canvas.drawPath(mPath, mPaint);
	}

	private void myInvalidate(float x, float y) {

	}
}
