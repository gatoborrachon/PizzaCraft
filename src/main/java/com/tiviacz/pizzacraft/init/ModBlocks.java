package com.tiviacz.pizzacraft.init;

import com.tiviacz.pizzacraft.PizzaCraft;
import com.tiviacz.pizzacraft.blocks.*;
import com.tiviacz.pizzacraft.blocks.crops.DoubleCropBlock;
import com.tiviacz.pizzacraft.blocks.crops.SimpleCropBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PizzaCraft.MODID);

    public static final RegistryObject<Block> PIZZA = registerBlock("pizza", new PizzaBlock(AbstractBlock.Properties.from(Blocks.CAKE)));
    public static final RegistryObject<Block> RAW_PIZZA = registerBlock("raw_pizza", new RawPizzaBlock(AbstractBlock.Properties.from(Blocks.CAKE)));
    public static final RegistryObject<Block> DOUGH = registerBlock("dough", new DoughBlock(AbstractBlock.Properties.from(Blocks.CAKE)));
    public static final RegistryObject<Block> CHEESE_BLOCK = registerBlock("cheese_block", new CheeseBlock(AbstractBlock.Properties.create(Material.CAKE, MaterialColor.YELLOW).hardnessAndResistance(0.5F).sound(SoundType.FUNGUS)));
    public static final RegistryObject<Block> MORTAR_AND_PESTLE = registerBlock("mortar_and_pestle", new MortarAndPestleBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> OVEN = registerBlock("oven", new OvenBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F).notSolid().setLightLevel(blockState -> 9)));

    //Pizza Boards
  /*  public static final RegistryObject<Block> OAK_PIZZA_BOARD = registerBlock("oak_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BIRCH_PIZZA_BOARD = registerBlock("birch_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<Block> SPRUCE_PIZZA_BOARD = registerBlock("spruce_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<Block> JUNGLE_PIZZA_BOARD = registerBlock("jungle_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<Block> ACACIA_PIZZA_BOARD = registerBlock("acacia_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<Block> DARK_OAK_PIZZA_BOARD = registerBlock("dark_oak_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<Block> CRIMSON_PIZZA_BOARD = registerBlock("crimson_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.CRIMSON_PLANKS)));
    public static final RegistryObject<Block> WARPED_PIZZA_BOARD = registerBlock("warped_pizza_board", new PizzaBoard(AbstractBlock.Properties.from(Blocks.WARPED_PLANKS)));
 */
    //Basins
    public static final RegistryObject<Block> GRANITE_BASIN = registerBlock("granite_basin", new BasinBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> DIORITE_BASIN = registerBlock("diorite_basin", new BasinBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> ANDESITE_BASIN = registerBlock("andesite_basin", new BasinBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> BASALT_BASIN = registerBlock("basalt_basin", new BasinBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final RegistryObject<Block> BLACKSTONE_BASIN = registerBlock("blackstone_basin", new BasinBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(1.5F, 6.0F)));

    //Chopping Boards
    public static final RegistryObject<Block> OAK_CHOPPING_BOARD = registerBlock("oak_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)));           ///.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(0.5F).sound(SoundType.WOOD))); //new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> BIRCH_CHOPPING_BOARD = registerBlock("birch_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<Block> SPRUCE_CHOPPING_BOARD = registerBlock("spruce_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<Block> JUNGLE_CHOPPING_BOARD = registerBlock("jungle_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<Block> ACACIA_CHOPPING_BOARD = registerBlock("acacia_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<Block> DARK_OAK_CHOPPING_BOARD = registerBlock("dark_oak_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<Block> CRIMSON_CHOPPING_BOARD = registerBlock("crimson_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.CRIMSON_PLANKS)));
    public static final RegistryObject<Block> WARPED_CHOPPING_BOARD = registerBlock("warped_chopping_board", new ChoppingBoardBlock(AbstractBlock.Properties.from(Blocks.WARPED_PLANKS)));

    //Crops
    public static final RegistryObject<Block> BROCCOLI = registerBlock("broccoli", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.BROCCOLI_SEED.get()));
    public static final RegistryObject<Block> CORNS = registerBlock("corns", new DoubleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.CORN.get()));
    public static final RegistryObject<Block> CUCUMBERS = registerBlock("cucumbers", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.CUCUMBER_SEED.get()));
    public static final RegistryObject<Block> ONIONS = registerBlock("onions", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.ONION.get()));
    public static final RegistryObject<Block> PEPPERS = registerBlock("peppers", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.PEPPER_SEED.get()));
    public static final RegistryObject<Block> PINEAPPLE = registerBlock("pineapple", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.PINEAPPLE_SEED.get()));
    public static final RegistryObject<Block> TOMATOES = registerBlock("tomatoes", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.TOMATO_SEED.get()));


    //public static final RegistryObject<Block> BROCCOLI_CROP = registerBlock("broccoli_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.BROCCOLI_SEED.get()));
    //public static final RegistryObject<Block> CORN_CROP = registerBlock("corn_crop", new DoubleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.CORN_SEED.get()));
    //public static final RegistryObject<Block> CUCUMBER_CROP = registerBlock("cucumber_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.CUCUMBER_SEED.get()));
    //public static final RegistryObject<Block> ONION_CROP = registerBlock("onion_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.ONION_SEED.get()));
    //public static final RegistryObject<Block> PEPPER_CROP = registerBlock("pepper_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.PEPPER_SEED.get()));
    //public static final RegistryObject<Block> PINEAPPLE_CROP = registerBlock("pineapple_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.PINEAPPLE_SEED.get()));
    //public static final RegistryObject<Block> TOMATO_CROP = registerBlock("tomato_crop", new SimpleCropBlock(AbstractBlock.Properties.from(Blocks.WHEAT), () -> ModItems.TOMATO_SEED.get()));

    public static Block[] getChoppingBoards()
    {
        return new Block[] {
                OAK_CHOPPING_BOARD.get(),
                BIRCH_CHOPPING_BOARD.get(),
                SPRUCE_CHOPPING_BOARD.get(),
                JUNGLE_CHOPPING_BOARD.get(),
                ACACIA_CHOPPING_BOARD.get(),
                DARK_OAK_CHOPPING_BOARD.get(),
                CRIMSON_CHOPPING_BOARD.get(),
                WARPED_CHOPPING_BOARD.get()
        };
    }

    public static Block[] getBasins()
    {
        return new Block[] {
                GRANITE_BASIN.get(),
                DIORITE_BASIN.get(),
                ANDESITE_BASIN.get(),
                BASALT_BASIN.get(),
                BLACKSTONE_BASIN.get()
        };
    }

    public static RegistryObject<Block> registerBlock(final String name, Block block)
    {
        return BLOCKS.register(name, () -> block);
    }

/*    private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
    {
        return blockState -> blockState.get(OvenBlock.LIT) ? lightValue : 0;
    } */
}