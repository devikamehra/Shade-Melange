package awe.devikamehra.shademelange;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import awe.devikamehra.shademelange.Enum.DecorationEnum;
import awe.devikamehra.shademelange.Enum.ShadeEnum;
import awe.devikamehra.shademelange.Interface.OnDialogButtonClickListener;
import awe.devikamehra.shademelange.Interface.OnShadeMelangeDialogCancelListener;
import awe.devikamehra.shademelange.Interface.OnShadeSelectedListener;
import awe.devikamehra.shademelange.RecyclerViewUtil.RecyclerItemClickListener;
import awe.devikamehra.shademelange.RecyclerViewUtil.ShadeAdapter;
import awe.devikamehra.shademelange.RecyclerViewUtil.SimpleGridDecoration;

/**
 * Created by Devika on 27-12-2015.
 */
public class ShadeMelangeDialog extends AlertDialog implements DialogInterface.OnCancelListener{

    Context context;
    private ArrayList<Shade> shades = new ArrayList<>();
    private ArrayList<Shade> moreShades = null;
    private ArrayList<Shade> replaceShades = null;
    private boolean addInStarting = false;
    private int columns = 0;
    private OnShadeMelangeDialogCancelListener onShadeMelangeDialogCancelListener;
    private OnShadeSelectedListener onShadeSelectedListener;
    private OnDialogButtonClickListener onPositiveButtonClickListener = null;
    private OnDialogButtonClickListener onNegativeButtonClickListener = null;
    private OnDialogButtonClickListener onNeutralButtonClickListener = null;
    private RecyclerView recyclerView;
    private ShadeAdapter shadeAdapter;
    private int decoration = -1;
    private RecyclerView.ItemDecoration itemDecoration = null;
    private boolean showRectangularShell = false;
    private String positiveButtonText = null;
    private String negativeButtonText = null;
    private String neutralButtonText = null;
    private int textColor = Color.BLACK;

    public ShadeMelangeDialog(Context context) {
        super(context);
        this.context = context;
    }

    private void init() {

        View view = getLayoutInflater().inflate(R.layout.fragment_shade_melange, null);

        setView(view);

        recyclerView = (RecyclerView) view.findViewById(R.id.melange);


        if (replaceShades != null){

            shades = replaceShades;

        }else {

            for(ShadeEnum shadeEnum : ShadeEnum.values()) {
                shades.add(new Shade(shadeEnum.getShade(), Util.convertToTitleCase(shadeEnum.name())));
            }

            if (moreShades != null){

                if (addInStarting){

                    shades.addAll(0, moreShades);

                }else{

                    shades.addAll(moreShades);

                }

            }

        }


        recyclerView.setHasFixedSize(true);

        if(columns == 0) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(context, columns));
        }

        shadeAdapter = new ShadeAdapter(shades);

        if (showRectangularShell){

            shadeAdapter.setRectShell(true);

        }
        recyclerView.setAdapter(shadeAdapter);

        if(decoration == DecorationEnum.SIMPLE_GRID_DECORATION.getDecoration()){

            recyclerView.addItemDecoration(new SimpleGridDecoration(context));

        }else if(decoration == DecorationEnum.CUSTOM_DECORATION.getDecoration()){

            if (itemDecoration != null){

                recyclerView.addItemDecoration(itemDecoration);

            }else{

                Log.e("Exception", "No Custom Item Decoration attached. Applying no decoration");

            }

        }else if (decoration == DecorationEnum.NO_DECORATION.getDecoration()){

            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }
            });

        }
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                selectShade(position);
                if(ShadeMelangeDialog.this.onShadeSelectedListener != null) {
                    ShadeMelangeDialog.this.onShadeSelectedListener
                            .onShadeSelected(shades.get(position).getShadeCode(), shades.get(position).getShadeName());
                }
            }
        }));

    }


    private void selectShade(int position) {
        ImageView shellSelected;
        shadeAdapter.setSelected(position);

        for (int i = 0; i < shadeAdapter.getItemCount(); i++){
            if(recyclerView.getChildAt(i) != null) {
                shellSelected = ((ImageView) recyclerView.getChildAt(i).findViewById(R.id.shell_selected));
                if (shellSelected.getVisibility() == View.VISIBLE) {
                    shellSelected.setVisibility(View.GONE);
                }
            }
        }

        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        View view = recyclerView.getChildAt(position - layoutManager.findFirstVisibleItemPosition());
        view.findViewById(R.id.shell_selected).setVisibility(View.VISIBLE);

    }

    public ShadeMelangeDialog columns(int columns){
        this.columns = columns;
        return this;
    }

    public ShadeMelangeDialog addMoreShades(ArrayList<Shade> moreShades){
        try {
            shades.addAll(moreShades);
            shadeAdapter.notifyDataSetChanged();
        }
        catch (NullPointerException e){
            Log.d("Exception", "Added Empty set of shades. Try again.");
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public ShadeMelangeDialog replaceTheShades(ArrayList<Shade> shades){
        try {
            this.shades = shades;
            shadeAdapter.notifyItemRangeInserted(0, shades.size());
        }
        catch (NullPointerException e){
            Log.d("Exception", "Replaced with an empty set of shades. Try again.");
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public ShadeMelangeDialog setOnShadeSelectListener(OnShadeSelectedListener onShadeSelectListener){
        this.onShadeSelectedListener = onShadeSelectListener;
        return this;
    }

    public ShadeMelangeDialog showRectangularShell(boolean showRectangularShell){
        this.showRectangularShell = showRectangularShell;
        return this;
    }

    public ShadeMelangeDialog applyDecoration(DecorationEnum decorationEnum){
        this.decoration = decorationEnum.getDecoration();
        return this;
    }

    public ShadeMelangeDialog customDecoration(RecyclerView.ItemDecoration itemDecoration){
        this.itemDecoration = itemDecoration;
        return this;
    }

    public ShadeMelangeDialog setMelangeCancelable(boolean cancelable){
        setCancelable(cancelable);
        return this;
    }

    public ShadeMelangeDialog setMelangeCancelable(boolean cancelable,
                                                  OnShadeMelangeDialogCancelListener onShadeMelangeDialogCancelListener){
        setCancelable(true);
        this.onShadeMelangeDialogCancelListener = onShadeMelangeDialogCancelListener;
        setOnCancelListener(this);
        return this;
    }

    public void cancelMelange(){
        cancel();
    }

    public void dismissMelange(){
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (dialogInterface != null){
            this.onShadeMelangeDialogCancelListener.OnShadeMelangeDialogCancelled(this);
        }
    }

    public ShadeMelangeDialog title(String title){
        setTitle(title);
        return this;
    }

    public ShadeMelangeDialog setPositiveButton(String positiveButtonText, OnDialogButtonClickListener onDialogButtonClickListener){
        this.onPositiveButtonClickListener = onDialogButtonClickListener;
        this.positiveButtonText = positiveButtonText;

        if (positiveButtonText != null){
            if(onPositiveButtonClickListener != null){
                setButton(BUTTON_POSITIVE, positiveButtonText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogInterface != null){
                            ShadeMelangeDialog.this.onPositiveButtonClickListener.onButtonClicked(ShadeMelangeDialog.this);
                        }
                    }
                });
            }else{
                setButton(BUTTON_POSITIVE, positiveButtonText, (OnClickListener) null);            }
        }else{
            Log.d("Positive Button", "Null String passed as positive button text");
        }
        return this;
    }


    public ShadeMelangeDialog setNegativeButton(String negativeButtonText, OnDialogButtonClickListener onDialogButtonClickListener){
        this.onNegativeButtonClickListener = onDialogButtonClickListener;
        this.negativeButtonText = negativeButtonText;

        if (negativeButtonText != null){
            if(onNegativeButtonClickListener != null){
                setButton(BUTTON_NEGATIVE, negativeButtonText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogInterface != null){
                            ShadeMelangeDialog.this.onNegativeButtonClickListener.onButtonClicked(ShadeMelangeDialog.this);
                        }
                    }
                });
            }else{
                setButton(BUTTON_NEGATIVE, negativeButtonText, (OnClickListener) null);
            }
        }else{
            Log.d("Negative Button", "Null String passed as positive button text");
        }

        return this;
    }


    public ShadeMelangeDialog setNeutralButton(String neutralButtonText, OnDialogButtonClickListener onDialogButtonClickListener){
        this.onNeutralButtonClickListener = onDialogButtonClickListener;
        this.neutralButtonText = neutralButtonText;

        if (neutralButtonText != null){
            if(onNeutralButtonClickListener != null){
                setButton(BUTTON_NEUTRAL, neutralButtonText, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogInterface != null){
                            ShadeMelangeDialog.this.onNeutralButtonClickListener.onButtonClicked(ShadeMelangeDialog.this);
                        }
                    }
                });
            }else{
                setButton(BUTTON_NEUTRAL, neutralButtonText, (OnClickListener) null);
            }
        }else{
            Log.d("Neutral Button", "Null String passed as positive button text");
        }

        return this;
    }

    public ShadeMelangeDialog setMelangeIcon(Drawable drawable){
        setIcon(drawable);
        return this;
    }

    public ShadeMelangeDialog setMelangeIcon(int resId){
        setIcon(resId);
        return this;
    }

    public void showMelange(){
        init();
        try {

                show();


            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public ShadeMelangeDialog setTextColor(int textColor){
        this.textColor = textColor;
        return this;
    }

    public String getPositiveButtonText(){
        return positiveButtonText;
    }

    public String getNegativeButtonText(){
        return negativeButtonText;
    }

    public String getNeutralButtonText(){
        return neutralButtonText;
    }


}
