package com.example.widget;

import com.example.sortlistview.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 右侧首字母列表bar
 * @author Jack
 * @version 创建时间：2014-2-6 下午3:37:33
 */
public class SideBar extends View {

	/** 触摸事件 */
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	/** 26个字母 */
	public static String[] characters = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z", "#" };
	private int ifSelected = -1; // 选中
	private Paint paint = new Paint();
	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
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

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight(); // 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / characters.length; // 获取每一个字母的高度

		for (int i = 0; i < characters.length; i++) {

			// 设置颜色
			paint.setColor(Color.rgb(33, 65, 98));
			// 设置字体
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			// 设置抗锯齿
			paint.setAntiAlias(true);
			// 设置字体大小
			paint.setTextSize(20);
			// 选中的状态
			if (i == ifSelected) {

				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(characters[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(characters[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldSelected = ifSelected;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int selected = (int) (y / getHeight() * characters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		
			case MotionEvent.ACTION_UP:

				setBackgroundDrawable(new ColorDrawable(0x00000000));
				ifSelected = -1;//
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				
				setBackgroundResource(R.drawable.sidebar_background);
				if (oldSelected != selected) {
					if (selected >= 0 && selected < characters.length) {
						if (listener != null) {
							listener.onTouchingLetterChanged(characters[selected]);
						}
						if (mTextDialog != null) {
							mTextDialog.setText(characters[selected]);
							mTextDialog.setVisibility(View.VISIBLE);
						}
						ifSelected = selected;
						invalidate();
					}
				}
				break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 触碰右侧列表时的回调接口
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}