package com.tiviacz.pizzacraft.blocks;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.init.ModSounds;
import com.tiviacz.pizzacraft.tileentity.PizzaHungerSystem;
import com.tiviacz.pizzacraft.tileentity.PizzaTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PizzaBlock extends Block
{
    public static final IntegerProperty BITES = BlockStateProperties.BITES_0_6;
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(3.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(5.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(7.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(9.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(11.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.makeCuboidShape(13.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D)};

    public PizzaBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(BITES, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPES[state.get(BITES)];
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) //#TODO add particle for cooked pizza
    {
        if(rand.nextInt(100) > 50 && worldIn.getTileEntity(pos) instanceof PizzaTileEntity)
        {
            if(((PizzaTileEntity)worldIn.getTileEntity(pos)).isFresh())
            {
                //worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.SIZZLING_SOUND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                for(int i = 0; i < 3; i++)
                {
                    worldIn.addParticle(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D, 0D, 0.025D, 0D);
                }
                //worldIn.addParticle(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D, 0D, 0.025D, 0D);
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack itemstack = player.getHeldItem(handIn);

        if(itemstack.getItem() == ModItems.PIZZA_PEEL.get() && worldIn.getTileEntity(pos) instanceof PizzaTileEntity && state.get(BITES) == 0)
        {
            ItemStack stack = asItem().getDefaultInstance();
            ((PizzaTileEntity)worldIn.getTileEntity(pos)).writeToItemStack(stack);

            if(!worldIn.isRemote)
            {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
            }

            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

            return ActionResultType.SUCCESS;
        }

        if(itemstack.isEmpty() && player.isSneaking() && worldIn.getTileEntity(pos) instanceof PizzaTileEntity)
        {
            ((PizzaTileEntity)worldIn.getTileEntity(pos)).openGUI(player, (PizzaTileEntity)worldIn.getTileEntity(pos), pos);
            return ActionResultType.SUCCESS;
        }

        if(this.eatPizza(worldIn, pos, state, player) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }

        if(itemstack.isEmpty())
        {
            return ActionResultType.CONSUME;
        }
        return this.eatPizza(worldIn, pos, state, player);
    }

    private ActionResultType eatPizza(IWorld worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if(tileEntity instanceof PizzaTileEntity)
        {
            tileEntity.requestModelDataUpdate();
        }

        if(!player.canEat(false))
        {
            return ActionResultType.PASS;
        }
        else
        {
            if(tileEntity instanceof PizzaTileEntity)
            {
                player.addStat(Stats.EAT_CAKE_SLICE);
                ((PizzaTileEntity)tileEntity).setHungerAndSaturationRefillment();

                for(Pair<EffectInstance, Float> pair : ((PizzaTileEntity) tileEntity).getEffects())
                {
                    if(!worldIn.isRemote() && pair.getFirst() != null && worldIn.getRandom().nextFloat() < pair.getSecond())
                    {
                        player.addPotionEffect(new EffectInstance(pair.getFirst()));
                    }
                }

                player.getFoodStats().addStats(((PizzaTileEntity)tileEntity).getHungerForSlice(), ((PizzaTileEntity)tileEntity).getSaturationForSlice());
            }

            int i = state.get(BITES);
            if(i < 6)
            {
                worldIn.setBlockState(pos, state.with(BITES, i + 1), 3);
            }
            else
            {
                worldIn.removeBlock(pos, false);
            }

            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(worldIn.getTileEntity(pos) instanceof PizzaTileEntity)
        {
            if(stack.getTag() != null)
            {
                ((PizzaTileEntity)worldIn.getTileEntity(pos)).readFromStack(stack);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        if(world.getTileEntity(pos) instanceof PizzaTileEntity)
        {
            ItemStack stack = this.getBlock().getItem(world, pos, state);
            ((PizzaTileEntity)world.getTileEntity(pos)).writeToItemStack(stack);
            return stack;
        }
        return this.getBlock().getPickBlock(state, target, world, pos, player);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return (7 - blockState.get(BITES)) * 2;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new PizzaTileEntity();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        //if(stack.getTag() != null)
        //{
            ItemStackHandler handler = new ItemStackHandler(9);

            if(stack.getTag() != null)
            {
                handler.deserializeNBT(stack.getTag().getCompound("Inventory"));
            }
            //handler.deserializeNBT(stack.getTag().getCompound("Inventory"));

            for(int i = 0; i < handler.getSlots(); i++)
            {
                if(!handler.getStackInSlot(i).isEmpty())
                {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    TranslationTextComponent translatedText = new TranslationTextComponent(stackInSlot.getTranslationKey());
                    StringTextComponent textComponent = new StringTextComponent(stackInSlot.getCount() > 1 ? stackInSlot.getCount() + "x " : "");
                    tooltip.add(textComponent.append(translatedText).mergeStyle(TextFormatting.BLUE));
                }
            }

            PizzaHungerSystem instance = new PizzaHungerSystem(handler);
            tooltip.add(new StringTextComponent("Restores: " + instance.getHunger() / 6 + " Hunger per Slice").mergeStyle(TextFormatting.BLUE));
        //}
    }
}
