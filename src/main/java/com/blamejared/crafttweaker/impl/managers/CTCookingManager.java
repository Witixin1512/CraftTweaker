package com.blamejared.crafttweaker.impl.managers;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.ICookingRecipeManager;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("crafttweaker.api.CookingManager")
public class CTCookingManager implements ICookingRecipeManager {
    
    @ZenCodeGlobals.Global("cooking")
    public static final CTCookingManager INSTANCE = new CTCookingManager();
    
    private CTCookingManager() {
    }
    
    @Override
    public AbstractCookingRecipe makeRecipe(String name, IItemStack output, IIngredient input, float xp, int cookTime) {
        return new CampfireCookingRecipe(new ResourceLocation(CraftTweaker.MODID, name), "", input.asVanillaIngredient(), output.getInternal(), xp, cookTime);
    }
    
    @Override
    public IRecipeType getRecipeType() {
        return IRecipeType.CAMPFIRE_COOKING;
    }
    
}
