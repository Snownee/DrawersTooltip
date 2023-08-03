package snownee.drawer;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@Mod(DrawersTooltip.ID)
@EventBusSubscriber(bus = Bus.MOD)
public final class DrawersTooltip {

	public static final String ID = "drawerstooltip";
	public static final Logger logger = LogUtils.getLogger();

	@SubscribeEvent
	public static void registerRecipeSerialziers(RegisterEvent event) {
		if (event.getRegistryKey() == Registries.BLOCK) {
			CraftingHelper.register(new ResourceLocation(ID, "standard"), DrawerIngredient.Serializer.INSTANCE);
		}
	}

}
