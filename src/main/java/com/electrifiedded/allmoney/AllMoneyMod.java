package com.electrifiedded.allmoney;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.electrifiedded.archaiclib.original.OriginalModAssets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
        dependencies = "required-after:archaiclib")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class AllMoneyMod {
    public static final String ORIGINAL_NAME = "Its_All_About_Money_Mod_1.7.10.jar (It's All About the Money by Freakin_Sweet)";
    public static final String ORIGINAL_URL = "https://www.planetminecraft.com/mod/itsallaboutthemoney-mc1710/";

    static final List<Item> ITEMS = new ArrayList<>();
    static final List<Block> BLOCKS = new ArrayList<>();

    public static final CreativeTabs TAB = new CreativeTabs("AllThings") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(dollar1);
        }
    };

    // 1.7 tool / armour materials (durability -1 = never breaks)
    static final Item.ToolMaterial ENT_BASE = EnumHelper.addToolMaterial("ALLMONEY_ENTROPY", 3, -1, 1000.0F, 3.0F, 30);
    static final Item.ToolMaterial ENT_SWORD = EnumHelper.addToolMaterial("ALLMONEY_ENTROPY_SWORD", 3, -1, 50.0F, 16.0F, 30);
    // 1.12 order: boots, legs, chest, helmet (1.7 was helmet, chest, legs, boots = 6,16,12,6)
    static final ItemArmor.ArmorMaterial ENT_ARMOR = EnumHelper.addArmorMaterial("ALLMONEY_ENTROPY", "allmoney:entropy",
            -1, new int[]{6, 12, 16, 6}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F);

    public static Item dollar1, dollar2, dollar5, dollar10, dollar20, dollar50, dollar100;
    public static Item sheetLinen, sheetCotton, americanDust, americanIngot, obsidianStick, blackDust, diamondEntropy, dollarSeed;
    public static Item bacon, eggs;
    public static Item sword, pickaxe, axe, shovel, hoe, helmet, chest, legs, boots;
    public static Block stack1, stack2, stack5, stack10, stack20, stack50, stack100, muricaBlock, moneyOre, dollarCrop;

    static {
        stack1 = stack("stack1", "Stack1", 1.6F);
        stack2 = stack("stack2", "Stack2", 3.3F);
        stack5 = stack("stack5", "Stack5", 8.3F);
        stack10 = stack("stack10", "Stack10", 16.6F);
        stack20 = stack("stack20", "Stack20", 33.3F);
        stack50 = stack("stack50", "Stack50", 83.3F);
        stack100 = stack("stack100", "Stack100", 166.6F);
        muricaBlock = block(new Block(Material.ROCK) {{ setHarvestLevel("pickaxe", 3); }}, "murica_block", "MuricaBlock",
                5.0F, 200.0F, SoundType.METAL);
        moneyOre = block(new Block(Material.IRON) {
            { setHarvestLevel("pickaxe", 2); }
            @Override public Item getItemDropped(IBlockState s, Random r, int fortune) { return dollar1; }
            @Override public int quantityDropped(Random r) { return 1; }
            @Override public int quantityDroppedWithBonus(int fortune, Random r) {
                if (fortune > 0) return Math.max(0, r.nextInt(fortune + 2) - 1) + 1;
                return 1;
            }
        }, "money_ore", "MoneyOre", 5.0F, 5.0F, SoundType.STONE);
        dollarCrop = new BlockCrops() {
            @Override protected Item getSeed() { return dollarSeed; }
            @Override protected Item getCrop() { return dollar1; }
        };
        dollarCrop.setRegistryName(Reference.MOD_ID, "dollar_crop").setTranslationKey("DollarCrop");
        BLOCKS.add(dollarCrop);

        dollar1 = item(new Item(), "dollar1", "Dollar1");
        dollar2 = item(new Item(), "dollar2", "Dollar2");
        dollar5 = item(new Item(), "dollar5", "Dollar5");
        dollar10 = item(new Item(), "dollar10", "Dollar10");
        dollar20 = item(new Item(), "dollar20", "Dollar20");
        dollar50 = item(new Item(), "dollar50", "Dollar50");
        dollar100 = item(new Item(), "dollar100", "Dollar100");
        sheetLinen = item(new Item(), "sheet_linen", "SheetLinen");
        sheetCotton = item(new Item(), "sheet_cotton", "SheetCotton");
        americanDust = item(new Item(), "american_dust", "AmericanDust");
        americanIngot = item(new Item(), "american_ingot", "AmericanIngot");
        obsidianStick = item(new Item(), "obsidian_stick", "ObsidianStick");
        blackDust = item(new Item(), "black_dust", "BlackDust");
        diamondEntropy = item(new Item(), "diamond_entropy", "DiamondEntropy");
        dollarSeed = item(new ItemSeeds(dollarCrop, Blocks.FARMLAND), "dollar_seed", "DollarSeed");
        bacon = item(new ItemFood(12, true), "bacon", "Bacon");
        eggs = item(new ItemFood(6, true), "eggs", "Eggs");

        sword = item(new ItemSword(ENT_SWORD), "entropy_sword", "EntropySword");
        pickaxe = item(new ItemPickaxe(ENT_BASE) {}, "entropy_pickaxe", "EntropyPickaxe");
        axe = item(new ItemAxe(ENT_BASE, 9.0F, -3.0F) {}, "entropy_axe", "EntropyAxe");
        shovel = item(new ItemSpade(ENT_BASE), "entropy_shovel", "EntropyShovel");
        hoe = item(new ItemHoe(ENT_BASE), "entropy_hoe", "EntropyHoe");
        helmet = item(new EntropyArmor(EntityEquipmentSlot.HEAD), "entropy_helmet", "EntropyHelmet");
        chest = item(new EntropyArmor(EntityEquipmentSlot.CHEST), "entropy_chestplate", "EntropyChestplate");
        legs = item(new EntropyArmor(EntityEquipmentSlot.LEGS), "entropy_leggings", "EntropyLeggings");
        boots = item(new EntropyArmor(EntityEquipmentSlot.FEET), "entropy_boots", "EntropyBoots");
        for (Item t : new Item[]{sword, pickaxe, axe, shovel, hoe}) t.setMaxStackSize(1);
    }

    static class EntropyArmor extends ItemArmor {
        EntropyArmor(EntityEquipmentSlot slot) { super(ENT_ARMOR, 0, slot); }
        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
            return armorType == EntityEquipmentSlot.LEGS ? "allmoney:textures/armor/entropy_armorlower.png"
                    : "allmoney:textures/armor/entropy_armorupper.png";
        }
    }

    private static Block stack(String id, String key, float resistance) {
        return block(new Block(Material.CLOTH), id, key, 0.2F, resistance, SoundType.PLANT);
    }

    private static Block block(Block b, String id, String key, float hardness, float resistance, SoundType sound) {
        b.setHardness(hardness);
        b.setResistance(resistance);
        b.setSoundType(sound);
        b.setRegistryName(Reference.MOD_ID, id).setTranslationKey(key);
        b.setCreativeTab(TAB);
        BLOCKS.add(b);
        ITEMS.add(new ItemBlock(b).setRegistryName(b.getRegistryName()));
        return b;
    }

    private static Item item(Item i, String id, String key) {
        i.setRegistryName(Reference.MOD_ID, id).setTranslationKey(key).setCreativeTab(TAB);
        ITEMS.add(i);
        return i;
    }

    public AllMoneyMod() {
        // our lower-case file names -> the original's mixed-case ones
        String[][] al = {
                {"textures/items/american_dust.png", "textures/items/AmericanDust.png"},
                {"textures/items/american_ingot.png", "textures/items/AmericanIngot.png"},
                {"textures/items/obsidian_stick.png", "textures/items/ObsidianStick.png"},
                {"textures/items/black_dust.png", "textures/items/blackdust.png"},
                {"textures/armor/entropy_armorupper.png", "textures/armor/entropy_ArmorUpper.png"},
                {"textures/armor/entropy_armorlower.png", "textures/armor/entropy_ArmorLower.png"},
        };
        for (String[] a : al) OriginalModAssets.alias(Reference.MOD_ID, a[0], a[1]);
        OriginalModAssets.hide(Reference.MOD_ID, "lang/en_us.lang"); // we ship our own
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        OriginalModAssets.require(Reference.MOD_ID, Reference.MOD_ID, ORIGINAL_NAME, ORIGINAL_URL, Reference.MOD_ID);
        GameRegistry.registerWorldGenerator(new OreGen(), 1);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Recipes.register();
    }

    /** 1.7 MoneyOreGen: overworld, 20 tries per chunk, y 5..39, veins 10..19 in stone */
    static class OreGen implements IWorldGenerator {
        @Override
        public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider prov) {
            if (world.provider.getDimension() != 0) return;
            IBlockState ore = moneyOre.getDefaultState();
            for (int i = 0; i < 20; i++) {
                BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), 5 + random.nextInt(35), chunkZ * 16 + random.nextInt(16));
                new WorldGenMinable(ore, 10 + random.nextInt(10), BlockMatcher.forBlock(Blocks.STONE)).generate(world, random, pos);
            }
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        for (Block b : BLOCKS) e.getRegistry().register(b);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for (Item i : ITEMS) e.getRegistry().register(i);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent e) {
        for (Item i : ITEMS) {
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
        }
    }
}
