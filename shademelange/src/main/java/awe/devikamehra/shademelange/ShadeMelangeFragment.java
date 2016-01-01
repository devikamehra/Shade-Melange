package awe.devikamehra.shademelange;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import awe.devikamehra.shademelange.Enum.DecorationEnum;
import awe.devikamehra.shademelange.Enum.ShadeEnum;
import awe.devikamehra.shademelange.Interface.OnShadeSelectedListener;
import awe.devikamehra.shademelange.RecyclerViewUtil.RecyclerItemClickListener;
import awe.devikamehra.shademelange.RecyclerViewUtil.ShadeAdapter;
import awe.devikamehra.shademelange.RecyclerViewUtil.SimpleGridDecoration;


/**
 * Created by Devika on 23-12-2015.
 */

public class ShadeMelangeFragment extends Fragment {

    private Context context;
    private ArrayList<Shade> shades = new ArrayList<>();
    private ArrayList<Shade> moreShades = null;
    private ArrayList<Shade> replaceShades = null;
    private boolean addInStarting = false;
    private int columns = 0;
    private OnShadeSelectedListener onShadeSelectedListener;
    private ShadeMelangeFragment shadeMelangeFragment = this;
    private ShadeAdapter shadeAdapter;
    private RecyclerView recyclerView;
    private boolean showRectangularShell = false;
    private int decoration = -1;
    private RecyclerView.ItemDecoration itemDecoration = null;
    private int textColor = Color.BLACK;

    public ShadeMelangeFragment with(Context context){
        this.context = context;
        return this;
    }

    public ShadeMelangeFragment columns(int columns){
        this.columns = columns;
        return this;
    }

    public ShadeMelangeFragment() {
    }

    public ShadeMelangeFragment addMoreShades(ArrayList<Shade> moreShades){
        if (moreShades != null){
            this.moreShades = moreShades;
        }else{
            Log.d("addMoreShades Method", "Cannot add null ArrayList of shades.");
        }
        return this;
    }

    public ShadeMelangeFragment addMoreShades(ArrayList<Shade> moreShades, boolean addInStarting){
        if (moreShades != null){
            this.moreShades = moreShades;
            this.addInStarting = addInStarting;
        }else{
            Log.d("addMoreShades Method", "Cannot add null ArrayList of shades.");
        }
        return this;
    }

    public ShadeMelangeFragment replaceTheShades(ArrayList<Shade> replaceShades){
        if(replaceShades != null) {
            this.replaceShades = replaceShades;
        }else{
            Log.d("replaceTheShades Method", "Cannot replace with empty ArrayList of shades");
        }
        return this;
    }

    public ShadeMelangeFragment showRectangularShell(boolean showRectangularShell){
        this.showRectangularShell = showRectangularShell;
        return this;
    }

    public ShadeMelangeFragment applyDecoration(DecorationEnum decorationEnum){
        this.decoration = decorationEnum.getDecoration();
        return this;
    }

    public ShadeMelangeFragment customDecoration(RecyclerView.ItemDecoration itemDecoration){
        this.itemDecoration = itemDecoration;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_shade_melange, container, false);

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


        recyclerView = (RecyclerView)rootView.findViewById(R.id.melange);
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

        shadeAdapter.setTextColor(textColor);

        recyclerView.setAdapter(shadeAdapter);

        if(decoration == DecorationEnum.SIMPLE_GRID_DECORATION.getDecoration()){

            recyclerView.addItemDecoration(new SimpleGridDecoration(getActivity()));

        }else if(decoration == DecorationEnum.CUSTOM_DECORATION.getDecoration()){

            if (itemDecoration != null){

                recyclerView.addItemDecoration(itemDecoration);

            }else{

                Log.e("Exception", "No Custom Item Decoration attached. Try again");

            }

        }
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                selectShade(position);
                if (ShadeMelangeFragment.this.onShadeSelectedListener != null) {

                    ShadeMelangeFragment.this.onShadeSelectedListener
                            .onShadeSelected(shades.get(position).getShadeCode(), shades.get(position).getShadeName());

                }
            }
        }));

        return rootView;
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

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    public ShadeMelangeFragment setOnShadeSelectListener(OnShadeSelectedListener onShadeSelectListener){
        this.onShadeSelectedListener = onShadeSelectListener;
        return this;
    }

    public ShadeMelangeFragment setTextColor(int textColor){
        this.textColor = textColor;
        return this;
    }

    public ShadeMelangeFragment setTitle(String title){
        if(getActivity() != null) {

            getActivity().setTitle(title);

        }

        return this;
    }

}
