package snownee.drawer.client;

import java.util.List;

import com.jaquadro.minecraft.storagedrawers.client.gui.StorageGuiGraphics;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import snownee.drawer.Config;

public class ClientDrawerTooltip implements TooltipComponent, ClientTooltipComponent {

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

	private final List<Entry<ItemStack>> contents;

	public ClientDrawerTooltip(List<Entry<ItemStack>> contents) {
		this.contents = contents;
	}

	@Override
	public int getHeight() {
		return 20 + 6;
	}

	@Override
	public int getWidth(Font p_169952_) {
		return contents.size() * 18 + 2;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		int i = contents.size();
		int j = 1;
		boolean flag = false;
		int k = 0;

		StorageGuiGraphics itemRenderer = TooltipEvents.storageItemRender;
		itemRenderer.pose().pushPose();
		itemRenderer.pose().mulPoseMatrix(guiGraphics.pose().last().pose());

		for (int l = 0; l < j; ++l) {
			for (int i1 = 0; i1 < i; ++i1) {
				int j1 = x + i1 * 18 + 1;
				int k1 = y + l * 20 + 1;
				renderSlot(j1, k1, k++, flag, font, guiGraphics, itemRenderer);
			}
		}

		if (Config.drawBorder) {
			drawBorder(x, y, i, j, guiGraphics);
		}

		itemRenderer.pose().popPose();
	}

	private void renderSlot(int x, int y, int slot, boolean p_194030_, Font font, GuiGraphics poseStack, StorageGuiGraphics itemRenderer) {
		if (slot >= contents.size()) {
			if (Config.drawSlot) {
				blit(poseStack, x, y, p_194030_ ? Texture.BLOCKED_SLOT : Texture.SLOT);
			}
		} else {
			Entry<ItemStack> entry = contents.get(slot);
			//ItemStack itemstack = ItemStackHelper.encodeItemStack(entry.getKey(), entry.getIntValue());
			ItemStack itemstack = entry.getKey();
			itemstack.setCount(entry.getIntValue());
			if (Config.drawSlot) {
				blit(poseStack, x, y, Texture.SLOT);
			}
			itemRenderer.overrideStack = itemstack;
			itemRenderer.renderItem(itemstack, x + 1, y + 1, slot);
			itemRenderer.renderItemDecorations(font, itemstack, x + 1, y + 1, null);
		}
	}

	private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, GuiGraphics p_194024_) {
		blit(p_194024_, p_194020_, p_194021_, Texture.BORDER_CORNER_TOP);
		blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_, Texture.BORDER_CORNER_TOP);

		for (int i = 0; i < p_194022_; ++i) {
			blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_, Texture.BORDER_HORIZONTAL_TOP);
			blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_ + p_194023_ * 20, Texture.BORDER_HORIZONTAL_BOTTOM);
		}

		for (int j = 0; j < p_194023_; ++j) {
			blit(p_194024_, p_194020_, p_194021_ + j * 20 + 1, Texture.BORDER_VERTICAL);
			blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + j * 20 + 1, Texture.BORDER_VERTICAL);
		}

		blit(p_194024_, p_194020_, p_194021_ + p_194023_ * 20, Texture.BORDER_CORNER_BOTTOM);
		blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + p_194023_ * 20, Texture.BORDER_CORNER_BOTTOM);
	}

	private void blit(GuiGraphics p_281273_, int p_282428_, int p_281897_, Texture p_281917_) {
		p_281273_.blit(TEXTURE_LOCATION, p_282428_, p_281897_, 0, (float) p_281917_.x, (float) p_281917_.y, p_281917_.w, p_281917_.h, 128, 128);
	}

	enum Texture {
		SLOT(0, 0, 18, 20),
		BLOCKED_SLOT(0, 40, 18, 20),
		BORDER_VERTICAL(0, 18, 1, 20),
		BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
		BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
		BORDER_CORNER_TOP(0, 20, 1, 1),
		BORDER_CORNER_BOTTOM(0, 60, 1, 1);

		public final int x;
		public final int y;
		public final int w;
		public final int h;

		Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
			x = p_169928_;
			y = p_169929_;
			w = p_169930_;
			h = p_169931_;
		}
	}
}
