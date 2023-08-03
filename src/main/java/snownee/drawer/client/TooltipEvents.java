package snownee.drawer.client;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.jaquadro.minecraft.storagedrawers.client.gui.StorageGuiGraphics;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Either;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap.BasicEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent.GatherComponents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import snownee.drawer.ModTags;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
public final class TooltipEvents {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
	private static boolean tagsUpdated;
	public static StorageGuiGraphics storageItemRender;

	@SubscribeEvent
	public static void registerClientTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ClientDrawerTooltip.class, Function.identity());
		storageItemRender = new StorageGuiGraphics(Minecraft.getInstance(), MultiBufferSource.immediate(Tesselator.getInstance().getBuilder()));
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TooltipEvents::onTooltip);
		MinecraftForge.EVENT_BUS.addListener(TooltipEvents::onTagsUpdated);
	}

	private static void onTagsUpdated(TagsUpdatedEvent event) {
		tagsUpdated = true;
	}

	private static void onTooltip(GatherComponents event) {
		if (!tagsUpdated) {
			return;
		}
		List<Object2IntMap.Entry<ItemStack>> contents = getContents(event.getItemStack());
		if (contents.isEmpty()) {
			return;
		}
		List<Either<FormattedText, TooltipComponent>> lines = Lists.newArrayList();
		if (Screen.hasShiftDown()) {
			for (Object2IntMap.Entry<ItemStack> content : contents) {
				lines.add(Either.left(FormattedText.of(content.getKey().getHoverName().getString() + " x" + DECIMAL_FORMAT.format(content.getIntValue()))));
			}
		} else {
			lines.add(Either.right(new ClientDrawerTooltip(contents)));
		}
		event.getTooltipElements().addAll(Math.min(3, event.getTooltipElements().size()), lines);
	}

	public static List<Object2IntMap.Entry<ItemStack>> getContents(ItemStack drawer) {
		CompoundTag tile = drawer.getTagElement("tile");
		if (tile == null || !tile.contains("Drawers")) {
			return Collections.EMPTY_LIST;
		}
		List<Object2IntMap.Entry<ItemStack>> list = Lists.newArrayList();
		if (drawer.is(ModTags.COMPACTING_DRAWERS)) {
			CompoundTag drawersTag = tile.getCompound("Drawers");
			int count = drawersTag.getInt("Count");
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
				list.add(new BasicEntry<>(content, amount));
			}
		} else if (drawer.is(ModTags.DRAWERS)) {
			for (Tag nbt : tile.getList("Drawers", Tag.TAG_COMPOUND)) {
				CompoundTag tag = (CompoundTag) nbt;
				ItemStack content = ItemStack.of(tag.getCompound("Item"));
				if (content.isEmpty()) {
					continue;
				}
				int amount = tag.getInt("Count");
				list.add(new BasicEntry<>(content, amount));
			}
		}
		return list;
	}

}
