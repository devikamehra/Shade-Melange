package awe.devikamehra.shademelangesample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import awe.devikamehra.shademelange.Enum.DecorationEnum;
import awe.devikamehra.shademelange.Enum.SelectionModeEnum;
import awe.devikamehra.shademelange.Interface.OnDialogButtonClickListener;
import awe.devikamehra.shademelange.Interface.OnShadeSelectedListener;
import awe.devikamehra.shademelange.ShadeMelangeDialog;
import awe.devikamehra.shademelange.ShadeMelangeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ShadeMelangeFragment fragment = new ShadeMelangeFragment();

        fragment.with(this)
                .applyDecoration(DecorationEnum.SIMPLE_GRID_DECORATION)
                .setTextColor(Color.RED)
                .columns(2)
                .showRectangularShell(true)
                .setSelectionMode(SelectionModeEnum.SINGLE_SELECTION_MODE)
                .setOnShadeSelectListener(new OnShadeSelectedListener() {
                    @Override
                    public void onShadeSelected(int color, String name) {
                        Toast.makeText(MainActivity.this, "You selected " + name + " shade.", Toast.LENGTH_SHORT).show();
                        Log.d("Selected Shade", fragment.getSelectedShade() + "");
                    }
                });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment, "frag")
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_dialog){

            final ShadeMelangeDialog dialog = new ShadeMelangeDialog(this);
            dialog.title("Shade Melange Dialog")
                    .setOnShadeSelectListener(new OnShadeSelectedListener() {
                        @Override
                        public void onShadeSelected(int color, String name) {

                            Toast.makeText(MainActivity.this, "You selected " + name + " shade.", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setPositiveButton("Select", new OnDialogButtonClickListener() {
                        @Override
                        public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {

                            Toast.makeText(getBaseContext(), "Shade Selected!!", Toast.LENGTH_SHORT).show();

                        }
                    })

                    .setNegativeButton("Cancel", new OnDialogButtonClickListener() {
                        @Override
                        public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {

                            Toast.makeText(getBaseContext(), "Dialog Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    });
            dialog.showMelange();

        }
        return super.onOptionsItemSelected(item);

    }

}