package modtweaker.mods.railcraft.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import modtweaker.mods.railcraft.RailcraftHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.RockCrusher")
public class RockCrusher {
    @ZenMethod
    public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack[] output, double[] chances) {
        IRockCrusherRecipe recipe = RailcraftHelper.getRockCrusherRecipe(toStack(input), matchDamage, matchNBT);
        for (int i = 0; i < output.length; i++)
            recipe.addOutput(toStack(output[i]), (float) chances[i]);
        MineTweakerAPI.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition {
        public Add(IRockCrusherRecipe recipe) {
            super("Rock Crusher", RailcraftHelper.crusher, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((IRockCrusherRecipe) recipe).getInput().getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Rock Crusher", RailcraftHelper.crusher, stack);
        }

        @Override
        public void apply() {
            for (IRockCrusherRecipe r : RailcraftHelper.crusher) {
                if (r.getInput() != null && areEqual(r.getInput(), stack)) {
                    recipe = r;
                    break;
                }
            }

            RailcraftHelper.crusher.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
