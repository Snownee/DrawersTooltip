package snownee.drawer;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public final class ModTags {
    public static final INamedTag<Item> DRAWERS = ItemTags.makeWrapperTag("storagedrawers:drawers");
    public static final INamedTag<Item> COMPACTING_DRAWERS = ItemTags.makeWrapperTag("drawerstooltip:compacting_drawers");
}
