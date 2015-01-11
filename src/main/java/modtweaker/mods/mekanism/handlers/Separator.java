package modtweaker.mods.mekanism.handlers;

import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.mods.mekanism.MekanismHelper.toGas;

import java.util.Map;

import mekanism.api.ChemicalPair;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.mekanism.gas.IGasStack;
import modtweaker.mods.mekanism.util.AddMekanismRecipe;
import modtweaker.util.BaseMapRemoval;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Separator")
public class Separator {
    @ZenMethod
    public static void addRecipe(ILiquidStack input, IGasStack gas1, IGasStack gas2) {
        ChemicalPair pair = new ChemicalPair(toGas(gas1), toGas(gas2));
        MineTweakerAPI.apply(new AddMekanismRecipe("ELECTROLYTIC_SEPARATOR", Recipe.ELECTROLYTIC_SEPARATOR.get(), toFluid(input), pair));
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        MineTweakerAPI.apply(new Remove(toFluid(input)));
    }

    private static class Remove extends BaseMapRemoval {
        public Remove(FluidStack stack) {
            super("Electrolytic Separator", Recipe.ELECTROLYTIC_SEPARATOR.get(), stack);
        }

        //We must search through the recipe entries so that we can assign the correct key for removal
        @Override
        public void apply() {
            for (Map.Entry<FluidStack, ChemicalPair> entry : ((Map<FluidStack, ChemicalPair>) map).entrySet()) {
                if (entry.getKey() != null && entry.getKey().isFluidEqual((FluidStack) stack)) {
                    key = entry.getKey();
                    break;
                }
            }

            super.apply();
        }
    }
}
