package snownee.drawer;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.util.Constants;

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
    public boolean test(ItemStack stack) {
        if (!ModTags.DRAWERS.contains(stack.getItem())) {
            return false;
        }
        try {
            CompoundNBT tile = stack.getChildTag("tile");
            if (tile == null || !tile.contains("Drawers")) {
                return false;
            }
            for (INBT nbt : tile.getList("Drawers", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT tag = (CompoundNBT) nbt;
                int amount = tag.getInt("Count");
                if (amount < this.amount) {
                    continue;
                }
                ItemStack content = ItemStack.read(tag.getCompound("Item"));
                if (ingredient.test(content)) {
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }

    public enum Serializer implements IIngredientSerializer<DrawerIngredient> {
        INSTANCE;

        @Override
        public DrawerIngredient parse(PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            int amount = buffer.readVarInt();
            return new DrawerIngredient(ingredient, amount);
        }

        @Override
        public DrawerIngredient parse(JsonObject json) {
            Ingredient ingredient = CraftingHelper.getIngredient(JSONUtils.getJsonObject(json, "content"));
            int amount = JSONUtils.getInt(json, "amount");
            return new DrawerIngredient(ingredient, amount);
        }

        @Override
        public void write(PacketBuffer buffer, DrawerIngredient ingredient) {
            ingredient.ingredient.write(buffer);
            buffer.writeVarInt(ingredient.amount);
        }
    }

    private static class DrawerList implements Ingredient.IItemList {

        private final Ingredient ingredient;
        private final int amount;

        private DrawerList(Ingredient ingredient, int amount) {
            this.ingredient = ingredient;
            this.amount = amount;
        }

        @Override
        @SuppressWarnings("boxing")
        public Collection<ItemStack> getStacks() {
            CompoundNBT itemTag = new CompoundNBT();
            ItemStack[] stacks = ingredient.getMatchingStacks();
            if (stacks.length > 0) {
                itemTag.putInt("Count", amount);
                itemTag.put("Item", stacks[0].serializeNBT());
            }
            CompoundNBT tag = new CompoundNBT();
            tag.put("tile", new CompoundNBT());
            tag.getCompound("tile").put("Drawers", new ListNBT());
            tag.getCompound("tile").getList("Drawers", Constants.NBT.TAG_COMPOUND).add(itemTag);

            List<ItemStack> list = Lists.newArrayList();

            for (Item item : ModTags.DRAWERS.getAllElements()) {
                ItemStack stack = new ItemStack(item);
                stack.setTag(tag);
                list.add(stack);
            }

            if (list.size() == 0 && !net.minecraftforge.common.ForgeConfig.SERVER.treatEmptyTagsAsAir.get()) {
                list.add(new ItemStack(net.minecraft.block.Blocks.BARRIER).setDisplayName(new net.minecraft.util.text.StringTextComponent("Empty Tag: " + ModTags.DRAWERS.getId().toString())));
            }
            return list;
        }

        @Override
        public JsonObject serialize() {
            return new JsonObject();
        }
    }
}
