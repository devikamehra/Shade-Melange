package awe.devikamehra.shademelange.RecyclerViewUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import awe.devikamehra.shademelange.R;

/**
 * Created by Devika on 26-12-2015.
 */
public class SimpleGridDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public SimpleGridDecoration(Context context) {
        if (Build.VERSION.SDK_INT > 20)
            mDivider = context.getResources().getDrawable(R.drawable.line_divider, context.getTheme());
        else
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top, bottom;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            top = child.getBottom() + params.bottomMargin;
            bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

        }

        top = parent.getPaddingTop();
        bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            left = child.getLeft() + params.leftMargin;
            right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

        }
    }

}