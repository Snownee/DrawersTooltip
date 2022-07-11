package snownee.drawer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
	public static final TagKey<Item> DRAWERS = ItemTags.create(new ResourceLocation("storagedrawers:drawers"));
	public static final TagKey<Item> COMPACTING_DRAWERS = ItemTags.create(new ResourceLocation("drawerstooltip:compacting_drawers"));
}
