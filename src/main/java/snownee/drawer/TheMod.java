package snownee.drawer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@Mod(TheMod.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public final class TheMod {

	public static final String MODID = "drawerstooltip";
	public static final String NAME = "Drawers Tooltip";
	public static final Logger logger = LogManager.getLogger(NAME);

	@SubscribeEvent
	public static void registerRecipeSerialziers(RegisterEvent event) {
		if (event.getRegistryKey() == Registry.BLOCK_REGISTRY) {
			CraftingHelper.register(new ResourceLocation(MODID, "standard"), DrawerIngredient.Serializer.INSTANCE);
		}
	}

}
