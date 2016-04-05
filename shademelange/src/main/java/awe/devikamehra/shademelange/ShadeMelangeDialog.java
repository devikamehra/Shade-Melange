package awe.devikamehra.shademelange;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import awe.devikamehra.shademelange.Enum.DecorationEnum;
import awe.devikamehra.shademelange.Enum.MaterialShadeEnum;
import awe.devikamehra.shademelange.Enum.SelectionModeEnum;
import awe.devikamehra.shademelange.Enum.ShadeEnum;
import awe.devikamehra.shademelange.Enum.ShadeTypeEnum;
import awe.devikamehra.shademelange.Interface.OnDialogButtonClickListener;
import awe.devikamehra.shademelange.Interface.OnShadeMelangeDialogCancelListener;
import awe.devikamehra.shademelange.Interface.OnShadeSelectedListener;
import awe.devikamehra.shademelange.RecyclerViewUtil.RecyclerItemClickListener;
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
    private DecorationEnum decoration = DecorationEnum.NO_DECORATION;
    private RecyclerView.ItemDecoration itemDecoration = null;
    private boolean showRectangularShell = false;
    private String positiveButtonText = null;
    private String negativeButtonText = null;
    private String neutralButtonText = null;
    private int textColor = Color.BLACK;
    private SelectionModeEnum selectionMode = SelectionModeEnum.MULTIPLE_SELECTION_MODE;
    private ArrayList<Integer> selected = new ArrayList<>();
    private ShadeTypeEnum shadeType = ShadeTypeEnum.NORMAL_SHADES;

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

            if (shadeType == ShadeTypeEnum.MATERIAL_SHADES){

                for(MaterialShadeEnum shadeEnum : MaterialShadeEnum.values()) {
                    shades.add(new Shade(shadeEnum.getShade(), Util.convertToTitleCase(shadeEnum.name())));
                }

            }else {

                for(ShadeEnum shadeEnum : ShadeEnum.values()) {
                    shades.add(new Shade(shadeEnum.getShade(), Util.convertToTitleCase(shadeEnum.name())));
                }

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

        shadeAdapter.setSelectionModeEnum(getSelectionMode());

        if(decoration == DecorationEnum.SIMPLE_GRID_DECORATION){

            recyclerView.addItemDecoration(new SimpleGridDecoration(context));

        }else if(decoration == DecorationEnum.CUSTOM_DECORATION){

            if (itemDecoration != null){

                recyclerView.addItemDecoration(itemDecoration);

            }else{

                throw new RuntimeException("No Custom Item Decoration attached. Try Again");

            }

        }else if (decoration == DecorationEnum.NO_DECORATION){

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

    public ShadeMelangeDialog setShadeType(ShadeTypeEnum shadeType){
        this.shadeType = shadeType;
        return this;
    }

    private void selectShade(int position) {
        ImageView shellSelected;

        for (int i = 0; i < shadeAdapter.getItemCount(); i++){
            if(recyclerView.getChildAt(i) != null) {
                shellSelected = ((ImageView) recyclerView.getChildAt(i).findViewById(R.id.shell_selected));
                if (shellSelected.getVisibility() == View.VISIBLE) {
                    shellSelected.setVisibility(View.GONE);
                }
            }
        }

        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        if (getSelectionMode() == SelectionModeEnum.SINGLE_SELECTION_MODE) {

            View view = recyclerView.getChildAt(position - layoutManager.findFirstVisibleItemPosition());

            if (shadeAdapter.getSelectedShade() == position){
                shadeAdapter.setSelectedShade(-1);

                view.findViewById(R.id.shell_selected).setVisibility(View.INVISIBLE);

            }else {
                shadeAdapter.setSelectedShade(position);

                view.findViewById(R.id.shell_selected).setVisibility(View.VISIBLE);

            }

        }else if(getSelectionMode() == SelectionModeEnum.MULTIPLE_SELECTION_MODE){

            if(selected.contains(position)){
                selected.remove((Integer) position);

            }else {
                selected.add(position);

            }

            shadeAdapter.setSelected(selected);

            for(int item: shadeAdapter.getSelected()){

                if(item >= layoutManager.findFirstVisibleItemPosition() && item <= layoutManager.findLastVisibleItemPosition()) {

                    View view = recyclerView.getChildAt(item - layoutManager.findFirstVisibleItemPosition());
                    view.findViewById(R.id.shell_selected).setVisibility(View.VISIBLE);

                }
            }

        }
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

            throw new RuntimeException( "Added Empty set of shades. Try again.");

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
            throw new RuntimeException("Replaced with an empty set of shades. Try again.");
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
        this.decoration = decorationEnum;
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
            throw new RuntimeException("Null String passed as positive button text");
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
            throw new RuntimeException("Null String passed as positive button text");
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
            throw new RuntimeException("Null String passed as positive button text");
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

    public SelectionModeEnum getSelectionMode() {
        return selectionMode;
    }

    public ShadeMelangeDialog setSelectionMode(SelectionModeEnum selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    public ArrayList<Shade> getListOfSelectedShades(){
        if(getSelectionMode() == SelectionModeEnum.MULTIPLE_SELECTION_MODE) {
            ArrayList<Shade> selectedShades = new ArrayList<>();
            for (int item : shadeAdapter.getSelected()) {
                selectedShades.add(shades.get(item));
            }
            return selectedShades;
        }else{
            throw new RuntimeException("Cannot return list of selected shades in single selection mode. Use getSelectedShade() method.");
        }
    }

    public Shade getSelectedShade(){
        if (getSelectionMode() == SelectionModeEnum.SINGLE_SELECTION_MODE){
            if(shadeAdapter.getSelectedShade() != -1) {
                return shades.get(shadeAdapter.getSelectedShade());
            }else{
                return null;
            }
        } else{
            throw new RuntimeException("Cannot return a single shade in multi-selection mode. Use getListOfSelectedShades() method.");
        }
    }
}
