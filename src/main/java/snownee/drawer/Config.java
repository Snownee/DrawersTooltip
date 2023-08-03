package snownee.drawer;

import snownee.kiwi.config.KiwiConfig;
import snownee.kiwi.config.KiwiConfig.ConfigType;

@KiwiConfig(type = ConfigType.CLIENT)
public class Config {
	public static boolean drawBorder = true;
	public static boolean drawSlot = true;
}
