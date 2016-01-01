package awe.devikamehra.shademelange;

/**
 * Created by Devika on 23-12-2015.
 */
public class Shade {

    int shadeCode;
    String shadeName;

    public Shade(int shadeCode, String shadeName) {
        this.shadeCode = shadeCode;
        this.shadeName = shadeName;
    }

    public int getShadeCode() {
        return shadeCode;
    }

    public void setShadeCode(int shadeCode) {
        this.shadeCode = shadeCode;
    }

    public String getShadeName() {
        return shadeName;
    }

    public void setShadeName(String shadeName) {
        this.shadeName = shadeName;
    }
}
