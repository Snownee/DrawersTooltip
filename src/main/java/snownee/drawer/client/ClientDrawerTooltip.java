package snownee.drawer.client;

import java.util.List;

import com.jaquadro.minecraft.storagedrawers.client.renderer.StorageRenderItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
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
	public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer) {
		int i = contents.size();
		int j = 1;
		boolean flag = false;
		int k = 0;

		for (int l = 0; l < j; ++l) {
			for (int i1 = 0; i1 < i; ++i1) {
				int j1 = x + i1 * 18 + 1;
				int k1 = y + l * 20 + 1;
				renderSlot(j1, k1, k++, flag, font, poseStack, TooltipEvents.storageItemRender);
			}
		}

		if (Config.drawBorder) {
			drawBorder(x, y, i, j, poseStack);
		}
	}

	private void renderSlot(int x, int y, int slot, boolean p_194030_, Font font, PoseStack poseStack, StorageRenderItem itemRenderer) {
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
			itemRenderer.renderAndDecorateItem(poseStack, itemstack, x + 1, y + 1, slot);
			//			itemRenderer.blitOffset += 500;
			itemRenderer.renderGuiItemDecorations(poseStack, font, itemstack, x + 1, y + 1, null);
			//			itemRenderer.blitOffset -= 500;
		}
	}

	private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, PoseStack p_194024_) {
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

	private void blit(PoseStack p_276033_, int p_276062_, int p_276063_, Texture p_276044_) {
		RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
		GuiComponent.blit(p_276033_, p_276062_, p_276063_, 0, (float) p_276044_.x, (float) p_276044_.y, p_276044_.w, p_276044_.h, 128, 128);
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
