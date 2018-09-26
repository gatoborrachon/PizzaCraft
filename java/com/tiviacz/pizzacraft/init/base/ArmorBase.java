package com.tiviacz.pizzacraft.init.base;

import com.tiviacz.pizzacraft.PizzaCraft;
import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.util.IHasModel;
import com.tiviacz.pizzacraft.util.Reference;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ArmorBase extends ItemArmor implements IHasModel
{
	public static final ArmorMaterial pizza_deliver_set = EnumHelper.addArmorMaterial("pizza_deliver_set", Reference.MODID + ":deliver", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	
	public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(PizzaCraft.PizzaCraftTab);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		PizzaCraft.proxy.registerItemRenderer(this, 0, "inventory");
	}
}

