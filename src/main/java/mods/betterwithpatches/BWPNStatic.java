package mods.betterwithpatches;

import java.util.Map;

public class BWPNStatic {
public static BWPNStatic parseID  =new BWPNStatic();

    private Map<String, String> propertyValues;



    public int parseID(String name) {
        try {
            return Integer.parseInt(this.propertyValues.get(name));
        }
        catch (NumberFormatException e) {
            if (this.propertyValues.get(name) == null) {
                throw new IllegalArgumentException("Unable to find property " + name );
            }
            else {
                throw new IllegalArgumentException("Invalid id value for property " + name + "Check for stray whitespace");
            }
        }
    }


}
