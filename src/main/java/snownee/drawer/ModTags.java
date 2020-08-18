package snownee.drawer;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public final class ModTags {
    public static final INamedTag<Item> DRAWERS = ItemTags.createOptional(new ResourceLocation("storagedrawers:drawers"));
    public static final INamedTag<Item> COMPACTING_DRAWERS = ItemTags.createOptional(new ResourceLocation("drawerstooltip:compacting_drawers"));
}
