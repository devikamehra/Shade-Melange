package awe.devikamehra.shademelange.Enum;

/**
 * Created by Devika on 28-12-2015.
 */
public enum DecorationEnum {

    NO_DECORATION(0),
    SIMPLE_GRID_DECORATION(1),
    CUSTOM_DECORATION(2);

    int decoration;

    DecorationEnum(int decoration){
        this.decoration = decoration;
    }

    public int getDecoration(){
        return decoration;
    }
    
}
