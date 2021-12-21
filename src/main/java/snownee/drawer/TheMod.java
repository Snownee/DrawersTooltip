package snownee.drawer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod(TheMod.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public final class TheMod {

	public static final String MODID = "drawerstooltip";
	public static final String NAME = "Drawers Tooltip";
	public static final Logger logger = LogManager.getLogger(NAME);

	@SubscribeEvent //ModBus, can't use addListener due to nested genetics.
	public static void registerRecipeSerialziers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		CraftingHelper.register(new ResourceLocation(MODID, "standard"), DrawerIngredient.Serializer.INSTANCE);
	}

}
