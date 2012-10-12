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
    public enum PopupCorner{
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    private Activity mContext;

    private int mLayout;
    private int mWidth;
    private int mHeight;
    private boolean mMustWrap;
    private PopupLocation mLocation;
    private PopupCorner mPopupCorner;
    private int mOffset;
    private View mPopupLayout;
    private PopupWindow mWindow;
    private PopupWindow.OnDismissListener mDismissListener;

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
        mPopupCorner = builder.corner;
        mContext = builder.context;
        mDismissListener = builder.listener;
        generateWindow();
    }



    public static class PopupBuilder{
        private int hintLayout;
        private int width;
        private int height;
        private boolean wrap = true;
        private PopupLocation location = PopupLocation.CENTER;
        private PopupCorner corner = PopupCorner.TOP_LEFT;
        private int offset = 0;
        private Activity context;
        private PopupWindow.OnDismissListener listener;

        public PopupBuilder(Activity context){
            this.context = context;
        }

        public PopupBuilder location(PopupLocation location, PopupCorner corner, int offset){
            this.location = location;
            this.corner = corner;
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


        public PopupBuilder dismiss(PopupWindow.OnDismissListener l){
            this.listener = l;
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
        if(mDismissListener != null){
            mWindow.setOnDismissListener(mDismissListener);
        }
    }


    /**
     * assuming that the view has been correctly displayed, shows the hint
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

        mPopupLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int[] newCoords = adjustLocationByCorner(x, y);
        mWindow.update(newCoords[0], newCoords[1], mWindow.getWidth(), mWindow.getHeight());



    }

    /**
     * Returns the coordinates to place the popup into related to a given view
     * @param v the view to place the hit near to
     * @return a 2 element array with x,y coordinates
     */
    private int[] getPopupLocation(final View v){
        int[] viewLocation = new int[2];
        v.getLocationOnScreen(viewLocation);
        int hintX;
        int hintY;
        switch (mLocation){
            case TOP_LEFT:
                hintX = viewLocation[0];
                hintY = viewLocation[1];
                break;
            case TOP_RIGHT:
                hintX = viewLocation[0] + v.getWidth();
                hintY = viewLocation[1];
                break;
            case BOTTOM_LEFT:
                hintX = viewLocation[0];
                hintY = viewLocation[1] + v.getHeight();
                break;
            case BOTTOM_RIGHT:
                hintX = viewLocation[0] + v.getWidth();
                hintY = viewLocation[1] + v.getHeight();
                break;
            case CENTER:
                hintX = viewLocation[0] + v.getWidth() / 2;
                hintY = viewLocation[1] + v.getHeight() / 2;
                break;
            case BOTTOM_CENTER:
                hintX = viewLocation[0] + v.getWidth() / 2;
                hintY = viewLocation[1] + v.getHeight();
            break;
            default:
                hintX = hintY = 0;
        }

        adjustLocationByCorner(hintX, hintY);

        hintX = hintX + mOffset;
        hintY = hintY + mOffset;
        int[] res = {hintX, hintY};
        return res;
    }


    private int[] adjustLocationByCorner(int x, int y){
       switch(mPopupCorner){
            case TOP_LEFT:
                // already in place
            break;
            case TOP_RIGHT:
                x = x - mPopupLayout.getMeasuredWidth();
            break;
            case BOTTOM_LEFT:
                y = y - mPopupLayout.getMeasuredHeight();
            break;
            case BOTTOM_RIGHT:
                x = x - mPopupLayout.getMeasuredWidth();
                y = y - mPopupLayout.getMeasuredHeight();
            break;
        }
        int[] res = {x, y};
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
                int[] coords = getPopupLocation(v);
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

    /**
     * returns a direct subview of the hint layout
     * its a shortcut for setting strings without using getPopupView()
     * @param id
     * @return the view with the given id
     */
    public View getHintSubview(int id){
        return mPopupLayout.findViewById(id);
    }

}
