# Shade-Melange

Shade Melange is a library that contains a colour palette with a wide range of pre-defined colours with their names. The implementation of the grid has been done using RecyclerView for memory efficient performance. The library consists of two main classes:

* ShadeMelangeFragment.java - Fragment that can be used directly by attaching it with your activity.
* ShadeMelangeDialog.java - Dialog that beyond it's basic implementation contains a colour palatte.

## Including in your project

Add JitPack to repositories in your project's root `build.gradle` file:

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency to your module's `build.gradle` file:

```
dependencies {
    ...
    compile 'com.github.devikamehra:Shade-Melange:v1.0'
}
```

## Usage

#### ShadeMelangeFragment

Just like another fragment, this fragment can be attached to your activity by using your default Fragment Manager

``` java

ShadeMelangeFragment fragment = new ShadeMelangeFragment();

        fragment.with(this)
                .columns(2)                                            // gridview columns
                .setTextColor(Color.RED)                               // name of the colour textColor
                .applyDecoration(DecorationEnum.SIMPLE_GRID_DECORATION) // recyclerview item decoration. Can be customized. 
                .showRectangularShell(true)                             // Shape of shell (Circular or Rectangular)  
                .setOnShadeSelectListener(new OnShadeSelectedListener() { // onClickShades Listener 
                    @Override
                    public void onShadeSelected(int color, String name) {
                        Toast.makeText(MainActivity.this, "You selected " + name + " shade." , Toast.LENGTH_SHORT).show();
                    }
                });

        getSupportFragmentManager()                                   // easily add it using Fragment Manager
                .beginTransaction()
                .add(R.id.container, fragment, "frag")
                .commit();

```

#### ShadeMelangeDialog

The Dialog contains a colour palatte which can be customized according to your specifications

``` java

ShadeMelangeDialog dialog = new ShadeMelangeDialog(this);
            dialog.title("Shade Melange Dialog")
                    .setOnShadeSelectListener(new OnShadeSelectedListener() {    // onShadeClick Listener
                        @Override
                        public void onShadeSelected(int color, String name) {
                            Toast.makeText(MainActivity.this, "You selected " + name + " shade.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("Select", new OnDialogButtonClickListener() {      // add various buttons 
                        @Override
                        public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {

                            Toast.makeText(getBaseContext(), "Shade Selected!!", Toast.LENGTH_SHORT).show();

                        }
                    });
                    
            dialog.showMelange();       // Remember to use showMelange() and not the default show()

```

Refer to the sample to appreciate the simplistic implementation of the library.

## License

Copyright 2016 Devika Mehra

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.











