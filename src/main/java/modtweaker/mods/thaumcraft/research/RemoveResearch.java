package modtweaker.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class RemoveResearch implements IUndoableAction {
    String key;
    String tab;
    ResearchItem removed;

    public RemoveResearch(String victim) {
        key = victim;
        tab = ThaumcraftHelper.getResearchTab(key);
    }

    @Override
    public void apply() {
        if (tab != null) {
            removed = ResearchCategories.researchCategories.get(tab).research.get(key);
            ResearchCategories.researchCategories.get(tab).research.remove(key);
        }
    }

    @Override
    public String describe() {
        return "Removing Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return tab != null && removed != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.put(tab, removed);
    }

    @Override
    public String describeUndo() {
        return "Restoring Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}