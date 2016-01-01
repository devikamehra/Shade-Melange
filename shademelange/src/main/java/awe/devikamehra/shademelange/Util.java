package awe.devikamehra.shademelange;

/**
 * Created by Devika on 28-12-2015.
 */
public class Util {

    public static String convertToTitleCase(String string){
        String s[] = string.toLowerCase().split("_");
        String titleCase = "";
        for (String st : s){
            st = Character.toUpperCase(st.charAt(0)) + st.substring(1);
            titleCase = titleCase.concat(st + " ");
        }

        return titleCase;
    }


}
