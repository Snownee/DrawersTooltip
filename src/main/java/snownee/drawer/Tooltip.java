package snownee.drawer;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public final class Tooltip {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###");
    private static boolean tagsUpdated;

    private Tooltip() {}

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        tagsUpdated = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onTooltip(ItemTooltipEvent event) {
        if (!tagsUpdated) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (ModTags.DRAWERS.contains(stack.getItem())) {
            normalDrawer(event, stack);
        } else if (ModTags.COMPACTING_DRAWERS.contains(stack.getItem())) {
            compactingDrawer(event, stack);
        }
    }

    private static void normalDrawer(ItemTooltipEvent event, ItemStack stack) {
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
                lines.add(new StringTextComponent(content.getDisplayName().getString() + " x" + DECIMAL_FORMAT.format(amount)));
            }
            event.getToolTip().addAll(3, lines);
        } catch (Exception e) {}
    }

    private static void compactingDrawer(ItemTooltipEvent event, ItemStack stack) {
        try {
            CompoundNBT tile = stack.getChildTag("tile");
            if (tile == null || !tile.contains("Drawers")) {
                return;
            }
            CompoundNBT drawersTag = tile.getCompound("Drawers");
            int count = drawersTag.getInt("Count");
            List<ITextComponent> lines = Lists.newArrayList();
            for (INBT nbt : drawersTag.getList("Items", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT tag = (CompoundNBT) nbt;
                ItemStack content = ItemStack.read(tag.getCompound("Item"));
                if (content.isEmpty()) {
                    continue;
                }
                int conv = tag.getInt("Conv");
                if (conv <= 0) {
                    continue;
                }
                int amount = count / conv;
                if (amount > 0) {
                    lines.add(new StringTextComponent(content.getDisplayName().getString() + " x" + DECIMAL_FORMAT.format(amount)));
                }
            }
            event.getToolTip().addAll(3, lines);
        } catch (Exception e) {}
    }

}
