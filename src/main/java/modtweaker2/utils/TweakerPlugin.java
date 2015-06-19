package modtweaker2.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;

public class TweakerPlugin {
    private static ArrayList<String> isLoaded = new ArrayList();
    public static List<ItemStack> changed = new ArrayList<ItemStack>();

    public static void register(String mod, Class clazz) {
        if (Loader.isModLoaded(mod)) {
            load(mod, clazz);
        }
    }

    public static void load(String mod, Class clazz) {
        try {
            clazz.newInstance();
            isLoaded.add(mod);
        } catch (Exception e) {
            isLoaded.remove(mod);
        }
    }

    public static boolean isLoaded(String string) {
        return isLoaded.contains(string);
    }
    
}
