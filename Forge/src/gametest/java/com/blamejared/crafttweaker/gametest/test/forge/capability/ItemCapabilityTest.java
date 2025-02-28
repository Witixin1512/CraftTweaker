package com.blamejared.crafttweaker.gametest.test.forge.capability;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.gametest.CraftTweakerGameTest;
import com.blamejared.crafttweaker.gametest.api.event.TestEvent;
import com.blamejared.crafttweaker.gametest.framework.ScriptBuilder;
import com.blamejared.crafttweaker.gametest.framework.annotation.CraftTweakerGameTestHolder;
import com.blamejared.crafttweaker.gametest.framework.annotation.TestModifier;
import com.blamejared.crafttweaker.gametest.logger.appender.GameTestLoggerAppender;
import com.blamejared.crafttweaker.impl.script.scriptrun.GameTestScriptRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;

@CraftTweakerGameTestHolder
public class ItemCapabilityTest implements CraftTweakerGameTest {
    
    @GameTest(template = "crafttweaker:chest_with_item")
    @TestModifier(implicitSuccession = true)
    public void testItemHandler(GameTestHelper helper, ScriptBuilder builder) {
        
        builder.file("capability/item_handler_test.zs");
        
        GameTestLoggerAppender.QueryableLog log = GameTestScriptRunner.runScripts(helper, builder);
        log.assertNoErrors();
        log.assertNoWarnings();
        MinecraftForge.EVENT_BUS.post(new TestEvent("testItemHandler", helper.getLevel(), helper.absolutePos(BlockPos.ZERO), helper.makeMockPlayer()));
        log.assertOutput(2, IItemStack.of(new ItemStack(Items.DIAMOND, 2)).getCommandString());
        log.assertOutput(3, IItemStack.of(new ItemStack(Items.DIRT)).getCommandString());
    }
    
}
