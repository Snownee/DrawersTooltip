package snownee.drawer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@Mod(TheMod.ID)
@EventBusSubscriber(bus = Bus.MOD)
public final class TheMod {

	public static final String ID = "drawerstooltip";
	public static final String NAME = "Drawers Tooltip";
	public static final Logger logger = LogManager.getLogger(NAME);

	@SubscribeEvent
	public static void registerRecipeSerialziers(RegisterEvent event) {
		if (event.getRegistryKey() == Registries.BLOCK) {
			CraftingHelper.register(new ResourceLocation(ID, "standard"), DrawerIngredient.Serializer.INSTANCE);
		}
	}

}
