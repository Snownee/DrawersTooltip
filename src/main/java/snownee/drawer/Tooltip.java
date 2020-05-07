package snownee.drawer;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TheMod.MODID, value = Dist.CLIENT)
public final class Tooltip {

    private Tooltip() {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!ModTags.DRAWERS.contains(stack.getItem())) {
            return;
        }
        try {
            CompoundNBT tile = stack.getChildTag("tile");
            if (tile == null || !tile.contains("Drawers")) {
                return;
            }
            List<ITextComponent> lines = Lists.newArrayList();
            for (INBT nbt : tile.getList("Drawers", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT tag = (CompoundNBT) nbt;
                ItemStack content = ItemStack.read(tag.getCompound("Item"));
                if (content.isEmpty()) {
                    continue;
                }
                int amount = tag.getInt("Count");
                lines.add(new StringTextComponent(content.getDisplayName().getString() + " x" + amount));
            }
            event.getToolTip().addAll(3, lines);
        } catch (Exception e) {}
    }

}
