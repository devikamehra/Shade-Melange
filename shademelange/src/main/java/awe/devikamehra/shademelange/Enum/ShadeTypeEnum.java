package awe.devikamehra.shademelange.Enum;

/**
 * Created by Devika on 05-04-2016.
 */
public enum ShadeTypeEnum {

    NORMAL_SHADES(0),
    MATERIAL_SHADES(1);

    int shadeType;

    ShadeTypeEnum(int shadeType){
        this.shadeType = shadeType;
    }

    public int getShadeType(){
        return shadeType;
    }

}
