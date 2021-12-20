package snownee.drawer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;

public final class ModTags {
	public static final Named<Item> DRAWERS = ItemTags.createOptional(new ResourceLocation("storagedrawers:drawers"));
	public static final Named<Item> COMPACTING_DRAWERS = ItemTags.createOptional(new ResourceLocation("drawerstooltip:compacting_drawers"));
}
