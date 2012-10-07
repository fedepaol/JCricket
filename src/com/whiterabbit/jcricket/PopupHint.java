package com.whiterabbit.jcricket;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.*;
import android.widget.PopupWindow;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 10/6/12
 * Time: 10:07 AM
 */
public class PopupHint {
    public enum PopupLocation {
        CENTER, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER
    }
    private Activity mContext;

    private int mLayout;
    private int mWidth;
    private int mHeight;
    private boolean mMustWrap;
    private PopupLocation mLocation;
    private int mOffset;
    private View mPopupLayout;
    private PopupWindow mWindow;

    /**
     * Constructor (using builder pattern)
     * @param builder
     */
    private PopupHint(PopupBuilder builder){
        mLayout = builder.hintLayout;
        mMustWrap = builder.wrap;
        if(!mMustWrap){
            mWidth = builder.width;
            mHeight = builder.height;
        }
        mLocation = builder.location;
        mOffset = builder.offset;
    }



    public static class PopupBuilder{
        private int hintLayout;
        private int width;
        private int height;
        private boolean wrap = true;
        private PopupLocation location = PopupLocation.CENTER;
        private int offset = 0;

        public PopupBuilder location(PopupLocation location, int offset){
            this.location = location;
            this.offset = offset;
            return this;
        }

        public PopupBuilder layout(int layout){
            hintLayout = layout;
            return this;
        }

        public PopupBuilder size(int width, int height){
            this.wrap = false;
            this.width = width;
            this.height = height;
            return this;
        }

        public PopupHint build(){
            return new PopupHint(this);
        }
    }


    /**
     * Generates the popup window (still to be placed)
     */
    private void generateWindow(){
        final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupLayout = layoutInflater.inflate(mLayout, null);

        mWindow = new PopupWindow(mContext);
        mWindow.setContentView(mPopupLayout);

        if(mMustWrap){
            mWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            int popupWidth = (int) (mWidth * mContext.getResources().getDisplayMetrics().density);
            int popupHeight = (int) (mHeight * mContext.getResources().getDisplayMetrics().density);
            mWindow.setWidth(popupWidth);
            mWindow.setHeight(popupHeight);
        }

        mWindow.setFocusable(true);
        mWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources()));


    }


    /**
     * assuming that the view has been correctly displayed,
     * @param v
     * @param x
     * @param y
     */
    private void displayHint(final View v, final int x, final int y) {
        mWindow.showAtLocation(mPopupLayout, Gravity.NO_GRAVITY, x, y);
        mWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mWindow.dismiss();
                return true;
            }
        });


    }

    /**
     * Returns the coordinates to place the popup into related to a given view
     * @param v the view to place the hit near to
     * @param location how to place the hint related to the given view
     * @return a 2 element array with x,y coordinates
     */
    private int[] getPopupLocation(final View v, PopupLocation location){
        int[] viewLocation = new int[2];
        v.getLocationOnScreen(viewLocation);
        final int hintX;
        final int hintY;
        switch (location){
            case TOP_LEFT:
                hintX = viewLocation[0] + mOffset;
                hintY = viewLocation[1] + mOffset;
                break;
            case TOP_RIGHT:
                hintX = viewLocation[0] + mOffset + v.getWidth();
                hintY = viewLocation[1] + mOffset;
                break;
            case BOTTOM_LEFT:
                hintX = viewLocation[0] + mOffset;
                hintY = viewLocation[1] + mOffset + v.getHeight();
                break;
            case BOTTOM_RIGHT:
                hintX = viewLocation[0] + mOffset + v.getWidth();
                hintY = viewLocation[1] + mOffset + v.getHeight();
                break;
            case CENTER:
                hintX = viewLocation[0] + v.getWidth() / 2 + mOffset;
                hintY = viewLocation[1] + v.getHeight() / 2 + mOffset;
                break;
            case BOTTOM_CENTER:
                hintX = viewLocation[0] + v.getWidth() / 2 + mOffset;
                hintY = viewLocation[1] + v.getHeight() + mOffset;
            break;
            default:
                hintX = hintY = 0;
        }
        int[] res = {hintX, hintY};
        return res;
    }


    /**
     * Shows the hint for the given view
     * @param v the view to show the hint for
     */
    public void showHint(final View v) {
        v.post(new Runnable() { // this because all lifecycle events must be finished
            @Override
            public void run() {
                int[] coords = getPopupLocation(v, mLocation);
                displayHint(v, coords[0], coords[1]);
            }
        });
    }


    /**
     * Returns the layout inflated into the popup window.
     * Useful to setup strings or to link listeners to buttons
     * @return
     */
    public View getPopupView(){
        return mPopupLayout;
    }

}
