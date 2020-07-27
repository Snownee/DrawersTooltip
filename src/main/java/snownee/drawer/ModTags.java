package snownee.drawer;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public final class ModTags {
    public static final Tag<Item> DRAWERS = new ItemTags.Wrapper(new ResourceLocation("storagedrawers", "drawers"));
    public static final Tag<Item> COMPACTING_DRAWERS = new ItemTags.Wrapper(new ResourceLocation("drawerstooltip", "compacting_drawers"));
}
