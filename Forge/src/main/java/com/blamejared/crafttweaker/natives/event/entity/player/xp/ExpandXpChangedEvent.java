package com.blamejared.crafttweaker.natives.event.entity.player.xp;


import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import net.minecraftforge.event.entity.player.PlayerXpEvent.XpChange;
import org.openzen.zencode.java.ZenCodeType;

/**
 * This event is fired when the player's xp count is changed. If the amount is
 * positive the xp is being added. If the xp is negative, the xp is being
 * removed. This event takes place before the xp has changed, allowing you to
 * change the amount, or negate the change entirely.
 *
 * @docParam this event
 * @docEvent canceled the xp will not change
 */
@ZenRegister
@Document("forge/api/event/entity/player/xp/XpChangeEvent")
@NativeTypeRegistration(value = XpChange.class, zenCodeName = "crafttweaker.api.event.entity.player.xp.XpChangeEvent")
public class ExpandXpChangedEvent {
    
    /**
     * Gets the amount of xp that the player's xp counter is being changed by.
     *
     * @return The amount of xp that the player's xp counter is being changed
     * by.
     */
    @ZenCodeType.Method
    @ZenCodeType.Getter("xp")
    public static int getXp(XpChange internal) {
        
        return internal.getAmount();
    }
    
    /**
     * Sets the amount of xp to change the player's xp counter by.
     *
     * @param amount The amount of xp that the player's xp counter should be
     *               changed by.
     *
     * @docParam amount 15
     */
    @ZenCodeType.Method
    @ZenCodeType.Setter("xp")
    public static void setXp(XpChange internal, int amount) {
        
        internal.setAmount(amount);
    }
    
}