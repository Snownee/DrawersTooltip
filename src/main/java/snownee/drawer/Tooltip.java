package snownee.drawer;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public final class Tooltip {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###");
	private static boolean tagsUpdated;

	private Tooltip() {
	}

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
			CompoundTag tile = stack.getTagElement("tile");
			if (tile == null || !tile.contains("Drawers")) {
				return;
			}
			List<Component> lines = Lists.newArrayList();
			for (Tag nbt : tile.getList("Drawers", Tag.TAG_COMPOUND)) {
				CompoundTag tag = (CompoundTag) nbt;
				ItemStack content = ItemStack.of(tag.getCompound("Item"));
				if (content.isEmpty()) {
					continue;
				}
				int amount = tag.getInt("Count");
				lines.add(new TextComponent(content.getHoverName().getString() + " x" + DECIMAL_FORMAT.format(amount)));
			}
			event.getToolTip().addAll(3, lines);
		} catch (Exception e) {
		}
	}

	private static void compactingDrawer(ItemTooltipEvent event, ItemStack stack) {
		try {
			CompoundTag tile = stack.getTagElement("tile");
			if (tile == null || !tile.contains("Drawers")) {
				return;
			}
			CompoundTag drawersTag = tile.getCompound("Drawers");
			int count = drawersTag.getInt("Count");
			List<Component> lines = Lists.newArrayList();
			for (Tag nbt : drawersTag.getList("Items", Tag.TAG_COMPOUND)) {
				CompoundTag tag = (CompoundTag) nbt;
				ItemStack content = ItemStack.of(tag.getCompound("Item"));
				if (content.isEmpty()) {
					continue;
				}
				int conv = tag.getInt("Conv");
				if (conv <= 0) {
					continue;
				}
				int amount = count / conv;
				if (amount > 0) {
					lines.add(new TextComponent(content.getDisplayName().getString() + " x" + DECIMAL_FORMAT.format(amount)));
				}
			}
			event.getToolTip().addAll(3, lines);
		} catch (Exception e) {
		}
	}

}
