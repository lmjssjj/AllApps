package com.lmjssjj.allapplists.view;

import com.lmjssjj.allapplists.OnListViewScollListener;
import com.lmjssjj.allapplists.R;
import com.lmjssjj.allapplists.R.color;
import com.lmjssjj.allapplists.R.dimen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View implements OnListViewScollListener{

	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	private String[] b = null;
	
	public String[] getIndexs() {
		return b;
	}

	public void setIndexs(String[] b) {
		this.b = b;
		invalidate();
	}
	
	private View mFloatingActionButton;
	
	public void setFloatingActionButton(View floatingActionButton) {
		mFloatingActionButton = floatingActionButton;
	}

	private int init;
	private int singleHeight;
	private int choose = -1;
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private float mTranslationY = 0 ;
	private int mCurrentPosition = 0;
	
	private boolean mSearchMode = false;

	public void setTextView(TextView textDialog) {
		this.mTextDialog = textDialog;
		mTranslationY = mTextDialog.getTranslationY();
	}


	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context) {
		super(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth(); 
		if (b == null) {
			return;
		}
		singleHeight = getResources().getDimensionPixelSize(R.dimen.contact_list_side_bar_single_height);
		init = height / 2 - b.length * singleHeight /2 - getResources().getDimensionPixelSize(R.dimen.contact_list_side_bar_margin_top);

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.parseColor("#cacaca"));
			paint.setAntiAlias(true);
			paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.contact_list_side_bar_text_size));
			if (i == choose) {
				paint.setColor(getResources().getColor(R.color.primary_color_orange));
				//paint.setTypeface(Typeface.DEFAULT_BOLD);
				//paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos =  init + singleHeight * i + singleHeight;
			if (!b[i].equals("i")) {
				canvas.drawText(b[i], xPos, yPos, paint);
			}
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    int c = (int) ((y - init) / singleHeight);
		switch (action) {
		case MotionEvent.ACTION_UP:
			choose = mCurrentPosition;
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.GONE);
			}
			if (mFloatingActionButton != null && !mSearchMode) {
				mFloatingActionButton.setVisibility(View.VISIBLE);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (b == null) {
				return true;
			}
			if (event.getX() > 45) {
				choose = mCurrentPosition;
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				if (mFloatingActionButton != null) {
					mFloatingActionButton.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			if ( y < init  || y > init + b.length * singleHeight  ) {
				return true;
			}
			
			if (oldChoose != c) {
				if (mFloatingActionButton != null) {
					mFloatingActionButton.setVisibility(View.INVISIBLE);
				}
				
				if (c >= 0 && c < b.length) {
					if (b[c].equals("i")) {
						return true;
					}
					if (listener != null) {
						listener.onTouchingLetterChanged(c);
					}
					if (mTextDialog != null) {
						mTextDialog.setTranslationY(mTranslationY + 
								init - 39 + singleHeight / 2 + c * singleHeight );
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					
					//lmjssjjadd 
					mCurrentPosition = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (b == null) {
				return true;
			}
			if (event.getX() > 45) {
				choose = mCurrentPosition;
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				if (mFloatingActionButton != null) {
					mFloatingActionButton.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			if ( y < init  || y > init + b.length * singleHeight  ) {
				return true;
			}
			
			if (oldChoose != c) {
				if (mFloatingActionButton != null) {
					mFloatingActionButton.setVisibility(View.INVISIBLE);
				}
				
				if (c >= 0 && c < b.length) {
					if (b[c].equals("i")) {
						return true;
					}
					if (listener != null) {
						listener.onTouchingLetterChanged(c);
					}
					if (mTextDialog != null) {
						mTextDialog.setTranslationY(mTranslationY + 
								init - 39 + singleHeight / 2 + c * singleHeight );
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					//lmjssjj add
					mCurrentPosition = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.GONE);
			}
			break;

		default:

			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(int s);
	}

	@Override
	public void OnListViewScoll(int position) {
		Log.d("SideBar", "position = " +position);
		if (mCurrentPosition == position) {
			return;
		} else {
			mCurrentPosition = position;
		}
		choose = position;
		invalidate();
		
	}
	
	public void setChoose(int position) {
		choose = position;
		invalidate();
	}
	
	public void setSideBarTextVisible(int visibility){
		if(mTextDialog != null){
			mTextDialog.setVisibility(visibility);
		}
	}
	
	public void setSearchMode(boolean mSearchMode){
		this.mSearchMode = mSearchMode;
	}
}