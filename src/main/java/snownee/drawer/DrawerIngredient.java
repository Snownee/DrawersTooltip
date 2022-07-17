package snownee.drawer;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class DrawerIngredient extends Ingredient {

	private final Ingredient ingredient;
	private final int amount;

	private DrawerIngredient(Ingredient ingredient, int amount) {
		super(Stream.of(new DrawerList(ingredient, amount)));
		this.ingredient = ingredient;
		this.amount = amount;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public Serializer getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public boolean test(ItemStack stack) {
		if (!stack.is(ModTags.DRAWERS)) {
			return false;
		}
		try {
			CompoundTag tile = stack.getTagElement("tile");
			if (tile == null || !tile.contains("Drawers")) {
				return false;
			}
			for (Tag nbt : tile.getList("Drawers", Tag.TAG_COMPOUND)) {
				CompoundTag tag = (CompoundTag) nbt;
				int amount = tag.getInt("Count");
				if (amount < this.amount) {
					continue;
				}
				ItemStack content = ItemStack.of(tag.getCompound("Item"));
				if (ingredient.test(content)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public enum Serializer implements IIngredientSerializer<DrawerIngredient> {
		INSTANCE;

		@Override
		public DrawerIngredient parse(FriendlyByteBuf buffer) {
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int amount = buffer.readVarInt();
			return new DrawerIngredient(ingredient, amount);
		}

		@Override
		public DrawerIngredient parse(JsonObject json) {
			Ingredient ingredient = CraftingHelper.getIngredient(GsonHelper.getAsJsonObject(json, "content"));
			int amount = GsonHelper.getAsInt(json, "amount");
			return new DrawerIngredient(ingredient, amount);
		}

		@Override
		public void write(FriendlyByteBuf buffer, DrawerIngredient ingredient) {
			ingredient.ingredient.toNetwork(buffer);
			buffer.writeVarInt(ingredient.amount);
		}
	}

	private static class DrawerList implements Ingredient.Value {

		public static final Item ITEM = ForgeRegistries.ITEMS.getValue(new ResourceLocation("storagedrawers", "oak_full_drawers_1"));
		private final Ingredient ingredient;
		private final int amount;

		private DrawerList(Ingredient ingredient, int amount) {
			this.ingredient = ingredient;
			this.amount = amount;
		}

		@Override
		@SuppressWarnings("boxing")
		public Collection<ItemStack> getItems() {
			CompoundTag itemTag = new CompoundTag();
			ItemStack[] stacks = ingredient.getItems();
			if (stacks.length > 0) {
				itemTag.putInt("Count", amount);
				itemTag.put("Item", stacks[0].serializeNBT());
			}
			CompoundTag tag = new CompoundTag();
			tag.put("tile", new CompoundTag());
			tag.getCompound("tile").put("Drawers", new ListTag());
			tag.getCompound("tile").getList("Drawers", Tag.TAG_COMPOUND).add(itemTag);

			List<ItemStack> list = Lists.newArrayList();

			//for (Item item : ModTags.DRAWERS.getAllElements()) {
			ItemStack stack = new ItemStack(ITEM);
			stack.setTag(tag);
			list.add(stack);
			//}

			if (list.size() == 0) {
				list.add(new ItemStack(Blocks.BARRIER).setHoverName(Component.literal("Empty Tag: " + ModTags.DRAWERS.location())));
			}
			return list;
		}

		@Override
		public JsonObject serialize() {
			return new JsonObject();
		}
	}
}
