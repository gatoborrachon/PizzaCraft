package com.tiviacz.pizzacraft.blocks;

import com.tiviacz.pizzacraft.PizzaCraft;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.tileentity.ChoppingBoardTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

public class ChoppingBoardBlock extends HorizontalBlock
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public ChoppingBoardBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch(state.get(HORIZONTAL_FACING))
        {
            case NORTH:
            case SOUTH:
                return Block.makeCuboidShape(2.0D, 0.0D, 4.0D, 14.0D, 0.75D, 12.0D);
            default:
                return Block.makeCuboidShape(4.0D, 0.0D, 2.0D, 12.0D, 0.75D, 14.0D);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(worldIn.getTileEntity(pos) instanceof ChoppingBoardTileEntity)
        {
            ChoppingBoardTileEntity choppingBoardTile = (ChoppingBoardTileEntity)worldIn.getTileEntity(pos);
            ItemStack itemHeld = player.getHeldItem(handIn);
            ItemStack itemOffhand = player.getHeldItemOffhand();

            // Placing items on the board. It should prefer off-hand placement, unless it's a BlockItem (since it never passes to off-hand...)
            if(choppingBoardTile.isEmpty())
            {
                if(!itemOffhand.isEmpty() && handIn == Hand.MAIN_HAND && !(itemHeld.getItem() instanceof BlockItem))
                {
                    return ActionResultType.PASS; // main-hand passes to off-hand
                }

                if(itemHeld.isEmpty())
                {
                    return ActionResultType.PASS;
                }

                else if(choppingBoardTile.addItem(itemHeld))
                {
                    if(!player.isCreative())
                    {
                        player.setHeldItem(handIn, choppingBoardTile.getInventory().insertItem(0, itemHeld, false));
                    }
                    else
                    {
                        choppingBoardTile.getInventory().insertItem(0, itemHeld.copy(), false);
                    }
                    worldIn.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.7F, 0.8F + worldIn.rand.nextFloat());
                    return ActionResultType.SUCCESS;
                }
            }
            // Processing the item with the held tool
            else if(!itemHeld.isEmpty())
            {
                if(choppingBoardTile.canChop(itemHeld))
                {
                    choppingBoardTile.chop(itemHeld, player);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
            // Removing the board's item
            else if(handIn == Hand.MAIN_HAND && !choppingBoardTile.removeItem().isEmpty())
            {
                if(!player.isCreative())
                {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), choppingBoardTile.getInventory().extractItem(0, 64, false));
                }
                else
                {
                    choppingBoardTile.getInventory().extractItem(0, 64, false);
                }
                worldIn.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.7F, 0.8F + worldIn.rand.nextFloat());
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;

     /*   if(worldIn.getTileEntity(pos) instanceof ChoppingBoardTileEntity)
        {
            ChoppingBoardTileEntity tile = (ChoppingBoardTileEntity)worldIn.getTileEntity(pos);

            if(tile.canChop(stack))
            {
                tile.chop(player, handIn);
                return ActionResultType.SUCCESS;
            }

            if(tile.canInsert(stack))
            {
                tile.getInventory().insertItem(0, stack, false);
                worldIn.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.7F, 0.8F + worldIn.rand.nextFloat());
                return ActionResultType.SUCCESS;
            }

            if(tile.canExtract(player, handIn))
            {
                tile.getInventory().extractItem(0, 64, false);
                return ActionResultType.SUCCESS;
            }
        ItemStack stack = player.getHeldItem(handIn);

        if(worldIn.getTileEntity(pos) instanceof ChoppingBoardTileEntity && handIn == Hand.MAIN_HAND)
        {
            ChoppingBoardTileEntity tile = (ChoppingBoardTileEntity)worldIn.getTileEntity(pos);

            if(tile.canChop(stack) && canHit(state.get(FACING), hit, pos, 0))
            {
                tile.chop(player, handIn);
                if(!worldIn.isRemote)
                {
                    stack.damageItem(1, player, e -> e.sendBreakAnimation(handIn));
                }
                return ActionResultType.SUCCESS;
            }

            if(tile.insert(stack, 0, canHit(state.get(FACING), hit, pos, 0)))
            {
             //   worldIn.playSound(player, pos.getX() + 0.5, pos.getY() + 0.3D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.7F, 0.8F + worldIn.rand.nextFloat());
                worldIn.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.7F, 0.8F + worldIn.rand.nextFloat());
                return ActionResultType.SUCCESS;
            }

            if(tile.extract(0, canHit(state.get(FACING), hit, pos, 0), player, handIn) || tile.extract(1, canHit(state.get(FACING), hit, pos, 1), player, handIn))
            {
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS; */
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            if(world.getTileEntity(pos) instanceof ChoppingBoardTileEntity)
            {
                IItemHandler inventory = ((ChoppingBoardTileEntity)world.getTileEntity(pos)).getInventory();

                for(int i = 0; i < inventory.getSlots(); i++)
                {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        if(worldIn.getTileEntity(pos) instanceof ChoppingBoardTileEntity)
        {
            ItemStack storedStack = ((ChoppingBoardTileEntity)worldIn.getTileEntity(pos)).getStoredStack();
            return !storedStack.isEmpty() ? 15 : 0;
        }
        return 0;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) { builder.add(FACING); }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new ChoppingBoardTileEntity();
    }

    @Mod.EventBusSubscriber(modid = PizzaCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ToolCarvingEvent
    {
        @SubscribeEvent
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event)
        {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            PlayerEntity player = event.getPlayer();
            ItemStack heldItem = player.getHeldItemMainhand();
            TileEntity tile = world.getTileEntity(event.getPos());

            if(player.isSecondaryUseActive() && !heldItem.isEmpty() && tile instanceof ChoppingBoardTileEntity)
            {
                if(heldItem.getItem() == ModItems.KNIFE.get() || heldItem.getItem() instanceof TieredItem || heldItem.getItem() instanceof TridentItem || heldItem.getItem() instanceof ShearsItem)
                {
                    boolean success = ((ChoppingBoardTileEntity)tile).carveToolOnBoard(player.abilities.isCreativeMode ? heldItem.copy() : heldItem);

                    if(success)
                    {
                        world.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                        event.setCanceled(true);
                        event.setCancellationResult(ActionResultType.SUCCESS);
                    }
                }
            }
        }
    }
}