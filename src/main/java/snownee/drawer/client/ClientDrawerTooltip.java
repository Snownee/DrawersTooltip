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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int p_194047_) {
        int i = contents.size();
        int j = 1;
        boolean flag = false;
        int k = 0;

        for (int l = 0; l < j; ++l) {
            for (int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                renderSlot(j1, k1, k++, flag, font, poseStack, TooltipEvents.storageItemRender, p_194047_);
            }
        }

        drawBorder(x, y, i, j, poseStack, p_194047_);
    }

    private void renderSlot(int x, int y, int slot, boolean p_194030_, Font font, PoseStack poseStack, StorageRenderItem itemRenderer, int p_194034_) {
        if (slot >= contents.size()) {
            blit(poseStack, x, y, p_194034_, p_194030_ ? Texture.BLOCKED_SLOT : Texture.SLOT);
        } else {
            Entry<ItemStack> entry = contents.get(slot);
            //ItemStack itemstack = ItemStackHelper.encodeItemStack(entry.getKey(), entry.getIntValue());
            ItemStack itemstack = entry.getKey();
            itemstack.setCount(entry.getIntValue());
            blit(poseStack, x, y, p_194034_, Texture.SLOT);
            itemRenderer.overrideStack = itemstack;
            itemRenderer.renderAndDecorateItem(null, itemstack, x + 1, y + 1, slot);
            itemRenderer.blitOffset += 500;
            itemRenderer.renderGuiItemDecorations(font, itemstack, x + 1, y + 1, null);
            itemRenderer.blitOffset -= 500;
            //            if (p_194029_ == 0) {
            //                AbstractContainerScreen.renderSlotHighlight(p_194032_, p_194027_ + 1, p_194028_ + 1, p_194034_);
            //            }
        }
    }

    private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, PoseStack p_194024_, int p_194025_) {
        blit(p_194024_, p_194020_, p_194021_, p_194025_, Texture.BORDER_CORNER_TOP);
        blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_, p_194025_, Texture.BORDER_CORNER_TOP);

        for (int i = 0; i < p_194022_; ++i) {
            blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_, p_194025_, Texture.BORDER_HORIZONTAL_TOP);
            blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for (int j = 0; j < p_194023_; ++j) {
            blit(p_194024_, p_194020_, p_194021_ + j * 20 + 1, p_194025_, Texture.BORDER_VERTICAL);
            blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + j * 20 + 1, p_194025_, Texture.BORDER_VERTICAL);
        }

        blit(p_194024_, p_194020_, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_CORNER_BOTTOM);
        blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + p_194023_ * 20, p_194025_, Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(PoseStack p_194036_, int p_194037_, int p_194038_, int p_194039_, Texture p_194040_) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(p_194036_, p_194037_, p_194038_, p_194039_, p_194040_.x, p_194040_.y, p_194040_.w, p_194040_.h, 128, 128);
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
