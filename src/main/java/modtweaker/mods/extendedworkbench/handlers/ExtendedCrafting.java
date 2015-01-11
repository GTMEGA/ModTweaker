package modtweaker.mods.extendedworkbench.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toStacks;
import static modtweaker.helpers.StackHelper.areEqual;

import java.util.Arrays;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import naruto1310.extendedWorkbench.crafting.ExtendedCraftingManager;
import naruto1310.extendedWorkbench.crafting.ExtendedShapedRecipes;
import naruto1310.extendedWorkbench.crafting.ExtendedShapelessRecipes;
import naruto1310.extendedWorkbench.crafting.IExtendedRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extendedworkbench")
public class ExtendedCrafting {
    @ZenMethod
    public static void addShaped(IItemStack output, IItemStack[][] ingredients) {
        MineTweakerAPI.apply(new Add(getShapedRecipe(output, ingredients)));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IItemStack[] ingredients) {
        MineTweakerAPI.apply(new Add(new ExtendedShapelessRecipes(toStack(output), Arrays.asList(toStacks(ingredients)))));
    }

    private static class Add extends BaseListAddition {
        public Add(IExtendedRecipe recipe) {
            super("Extended Workbench", ExtendedCraftingManager.getInstance().getRecipeList(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            Object out = ((IExtendedRecipe) recipe).getRecipeOutput();
            if (out != null) {
                return ((ItemStack) out).getDisplayName();
            } else return super.getRecipeInfo();
        }
    }

    private static IExtendedRecipe getShapedRecipe(IItemStack out, IItemStack[][] ingredients) {

        int width = 0;
        int height = ingredients.length;
        ItemStack[] recipe;

        for (int x = 0; x < ingredients.length; x++) {
            if (ingredients[x] != null && ingredients[x].length > width) width = ingredients[x].length;
        }

        recipe = new ItemStack[width * height];
        int counter = 0;
        for (int x = 0; x < ingredients.length; x++) {
            if (ingredients[x] != null) {
                for (int y = 0; y < ingredients[x].length; y++) {
                    recipe[counter++] = toStack(ingredients[x][y]);
                }
            }
        }

        return new ExtendedShapedRecipes(width, height, recipe, toStack(out));

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Extended Workbench", ExtendedCraftingManager.getInstance().getRecipeList(), stack);
        }

        @Override
        public void apply() {
            for (Object o : ExtendedCraftingManager.getInstance().getRecipeList()) {
                if (o instanceof IExtendedRecipe) {
                    IExtendedRecipe r = (IExtendedRecipe) o;
                    if (r.getRecipeOutput() != null && areEqual((ItemStack) r.getRecipeOutput(), stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            ExtendedCraftingManager.getInstance().getRecipeList().remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

}
