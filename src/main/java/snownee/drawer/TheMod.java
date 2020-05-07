package snownee.drawer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TheMod.MODID)
public final class TheMod {

    public static final String MODID = "draweringredient";
    public static final String NAME = "Drawer Ingredient";

    public static Logger logger = LogManager.getLogger(NAME);

    public TheMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
    }

    public void setup(FMLCommonSetupEvent event) {
        CraftingHelper.register(new ResourceLocation(MODID, "standard"), DrawerIngredient.Serializer.INSTANCE);
    }

}
