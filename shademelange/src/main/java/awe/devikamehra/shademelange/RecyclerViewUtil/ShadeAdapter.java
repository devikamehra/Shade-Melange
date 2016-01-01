package awe.devikamehra.shademelange.RecyclerViewUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.ArrayList;

import awe.devikamehra.shademelange.R;
import awe.devikamehra.shademelange.Shade;

/**
 * Created by Devika on 23-12-2015.
 */
public class ShadeAdapter extends RecyclerView.Adapter<ShadeAdapter.ViewHolder> {

    ArrayList<Shade> shades = new ArrayList<>();
    Context context;
    int selected = -1;
    ViewHolder viewHolder;
    boolean setRectShell = false;
    int textColor = Color.BLACK;

    public boolean isRectShell() {
        return setRectShell;
    }

    public void setRectShell(boolean setRectShell) {
        this.setRectShell = setRectShell;
    }

    public ShadeAdapter(ArrayList<Shade> shades) {
        this.shades = shades;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shade_shell, viewGroup, false);
        context = viewGroup.getContext();
        viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.unselectedShell.setImageDrawable(shadeTheDrawable(convertingShadeToFilter(shades.get(i).getShadeCode())));

        if((shades.get(i).getShadeCode() / -10000) == 1677) {

            viewHolder.selectedShell.setImageDrawable(tickDrawable(Color.WHITE));

        }else{

            viewHolder.selectedShell.setImageDrawable(tickDrawable(Color.BLACK));

        }

        if(getSelected() == i){

            viewHolder.selectedShell.setVisibility(View.VISIBLE);

        }else {

            viewHolder.selectedShell.setVisibility(View.GONE);

        }
        viewHolder.shadeName.setText(shades.get(i).getShadeName());
        viewHolder.shadeName.setTextColor(getTextColor());
        viewHolder.shadeName.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }

    private ColorFilter convertingShadeToFilter(int shadeCode) {

        int red   = (shadeCode & 0xFF0000) / 0xFFFF;
        int green = (shadeCode & 0xFF00) / 0xFF;
        int blue  = shadeCode & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        return new ColorMatrixColorFilter(matrix);
    }

    private Drawable shadeTheDrawable(ColorFilter colorFilter){
        LayerDrawable drawable;
        Drawable shell, boundary;
        if(isRectShell()) {

            shell = setDrawable(R.drawable.rect_shell_unselected);
            shell.setColorFilter(colorFilter);
            boundary = setDrawable(R.drawable.rect_boundary_of_shell);

        }else{

            shell = setDrawable(R.drawable.shell_unselected);
            shell.setColorFilter(colorFilter);
            boundary = setDrawable(R.drawable.boundary_of_shell);
        }

        drawable = new LayerDrawable(new Drawable[]{shell, boundary});

        return drawable;
    }

    private Drawable setDrawable(int drawable){

        if(Build.VERSION.SDK_INT > 20) {

            return context.getResources().getDrawable(drawable, context.getTheme());

        }else{

            return context.getResources().getDrawable(drawable);

        }
    }

    @Override
    public int getItemCount() {
        return shades.size();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private Drawable tickDrawable(int color) {
        return MaterialDrawableBuilder.with(context)
                .setColor(color)
                .setIcon(MaterialDrawableBuilder.IconValue.CHECK)
                .setSizeDp(32)
                .build();
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView unselectedShell, selectedShell;
        TextView shadeName;

        public ViewHolder(View itemView) {
            super(itemView);
            unselectedShell = (ImageView) itemView.findViewById(R.id.shell);
            selectedShell = (ImageView) itemView.findViewById(R.id.shell_selected);
            //selectedShell.setImageDrawable(tickDrawable(Color.BLACK));
            shadeName = (TextView) itemView.findViewById(R.id.shade_name);
            shadeName.setSingleLine(false);
        }

    }

}
