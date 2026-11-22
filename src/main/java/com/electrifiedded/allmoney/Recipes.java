package com.electrifiedded.allmoney;

import static com.electrifiedded.allmoney.AllMoneyMod.*;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** Recipes of the 1.7 CraftingManager, 1:1. */
final class Recipes {
    private static int n;

    private Recipes() {}

    private static ResourceLocation id() {
        return new ResourceLocation(Reference.MOD_ID, "r" + (n++));
    }

    private static void shaped(Object out, int count, Object... args) {
        GameRegistry.addShapedRecipe(id(), null, stack(out, count), args);
    }

    private static void shapeless(Object out, int count, Object... in) {
        Ingredient[] ing = new Ingredient[in.length];
        for (int i = 0; i < in.length; i++) {
            ing[i] = in[i] instanceof net.minecraft.block.Block
                    ? Ingredient.fromStacks(new ItemStack((net.minecraft.block.Block) in[i], 1, 32767))
                    : Ingredient.fromItem((Item) in[i]);
        }
        GameRegistry.addShapelessRecipe(id(), null, stack(out, count), ing);
    }

    private static ItemStack stack(Object o, int count) {
        return o instanceof net.minecraft.block.Block ? new ItemStack((net.minecraft.block.Block) o, count) : new ItemStack((Item) o, count);
    }

    static void register() {
        Item gold = Items.GOLD_INGOT;
        shaped(sword, 1, " AA", "GDA", "OG ", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick, 'G', gold);
        shaped(sword, 1, "AA ", "ADG", " GO", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick, 'G', gold);
        shaped(pickaxe, 1, "DAD", "AOA", " O ", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(axe, 1, "AAD", " AO", "  O", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(axe, 1, "DAA", "OA ", "O  ", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(shovel, 1, " AA", " DA", "O  ", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(shovel, 1, "AA ", "AD ", "  O", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(hoe, 1, "AAD", "  O", "  O", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(hoe, 1, "DAA", "O  ", "O  ", 'A', americanIngot, 'D', diamondEntropy, 'O', obsidianStick);
        shaped(helmet, 1, "ADA", "A A", 'A', americanIngot, 'D', diamondEntropy);
        shaped(chest, 1, "GDG", "ADA", "AAA", 'G', gold, 'A', americanIngot, 'D', diamondEntropy);
        shaped(legs, 1, "AAA", "D D", "A A", 'A', americanIngot, 'D', diamondEntropy);
        shaped(boots, 1, "A A", "D D", 'A', americanIngot, 'D', diamondEntropy);
        shaped(sheetCotton, 6, " X ", "XAX", " X ", 'X', Items.PAPER, 'A', Blocks.WOOL);
        shaped(sheetLinen, 2, " X ", "XAX", " X ", 'X', Items.STRING, 'A', Items.PAPER);
        shaped(dollar1, 8, "CCC", "LGL", "CCC", 'C', sheetCotton, 'L', sheetLinen, 'G', gold);
        shaped(americanDust, 1, "GIG", "ISI", "GIG", 'G', Items.GOLD_NUGGET, 'I', Items.IRON_INGOT, 'S', stack100);
        shaped(blackDust, 3, " A ", "LDL", " A ", 'L', Items.LAVA_BUCKET, 'D', Items.DIAMOND, 'A', americanIngot);

        Item[] bills = {dollar1, dollar2, dollar5, dollar10, dollar20, dollar50, dollar100};
        Object[] stacks = {stack1, stack2, stack5, stack10, stack20, stack50, stack100};
        for (int i = 0; i < bills.length; i++) shaped(stacks[i], 1, "XXX", "XXX", "XXX", 'X', bills[i]);
        shaped(muricaBlock, 1, "XXX", "XXX", "XXX", 'X', americanIngot);

        shapeless(bacon, 2, dollar5, Items.COOKED_PORKCHOP);
        shapeless(dollar2, 1, dollar1, dollar1);
        shapeless(dollar5, 1, dollar1, dollar1, dollar1, dollar1, dollar1);
        shapeless(dollar10, 1, dollar5, dollar5);
        shapeless(dollar10, 1, dollar2, dollar2, dollar2, dollar2, dollar2);
        shapeless(dollar20, 1, dollar10, dollar10);
        shapeless(dollar20, 1, dollar5, dollar5, dollar5, dollar5);
        shapeless(dollar50, 1, dollar10, dollar10, dollar10, dollar10, dollar10);
        shapeless(dollar100, 1, dollar20, dollar20, dollar20, dollar20, dollar20);
        shapeless(dollar100, 1, dollar50, dollar50);
        shapeless(dollar2, 50, dollar100);
        shapeless(dollar1, 50, dollar50);
        shapeless(dollar1, 20, dollar20);
        shapeless(dollar1, 10, dollar10);
        shapeless(dollar1, 5, dollar5);
        shapeless(dollar1, 2, dollar2);
        for (int i = 0; i < bills.length; i++) shapeless(bills[i], 9, stacks[i]);
        shapeless(americanIngot, 9, muricaBlock);
        shapeless(Items.STRING, 4, Blocks.WOOL);
        shapeless(obsidianStick, 2, Blocks.OBSIDIAN);
        shapeless(dollarSeed, 1, dollar1);

        GameRegistry.addSmelting(americanDust, new ItemStack(americanIngot), 120.0F);
        GameRegistry.addSmelting(moneyOre, new ItemStack(dollar5), 30.0F);
        GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 20.0F);
        GameRegistry.addSmelting(blackDust, new ItemStack(diamondEntropy), 150.0F);
        GameRegistry.addSmelting(Items.EGG, new ItemStack(eggs), 15.0F);
        GameRegistry.addSmelting(dollarSeed, new ItemStack(dollar1), 15.0F);
    }
}
