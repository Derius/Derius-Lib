package dk.muj.derius.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.Couple;
import com.massivecraft.massivecore.util.MUtil;

public class ItemUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN
	// -------------------------------------------- //
	
	private ItemUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	/*
	private static final Map<Material, Short> DURABILITY_MAP = new HashMap<>(); 
	
		static
		{
			// Leather Items	
			DURABILITY_MAP.put(Material.LEATHER_HELMET, (short) 56);
			DURABILITY_MAP.put(Material.LEATHER_CHESTPLATE, (short) 81);
			DURABILITY_MAP.put(Material.LEATHER_LEGGINGS, (short) 76);
			DURABILITY_MAP.put(Material.LEATHER_BOOTS, (short) 60);
			
			// Wood Items
			DURABILITY_MAP.put(Material.WOOD_SWORD, (short) 60);
			DURABILITY_MAP.put(Material.WOOD_AXE, (short) 60);
			DURABILITY_MAP.put(Material.WOOD_SPADE, (short) 60);
			DURABILITY_MAP.put(Material.WOOD_PICKAXE, (short) 60);
			DURABILITY_MAP.put(Material.WOOD_HOE, (short) 60);

			
			// Gold Items
			DURABILITY_MAP.put(GOLD_HELMET, (short) 78);
			DURABILITY_MAP.put(GOLD_CHESTPLATE, (short) 113);
			DURABILITY_MAP.put(GOLD_LEGGINGS, (short) 106);
			DURABILITY_MAP.put(GOLD_BOOTS, (short) 92);
			DURABILITY_MAP.put(GOLD_SWORD, (short) 33);
			DURABILITY_MAP.put(GOLD_AXE, (short) 33);
			DURABILITY_MAP.put(GOLD_SPADE, (short) 33);
			DURABILITY_MAP.put(GOLD_PICKAXE, (short) 33);
			DURABILITY_MAP.put(GOLD_HOE, (short) 33);

			// Chainmail Items
			DURABILITY_MAP.put(CHAINMAIL_HELMET, (short) 166);
			DURABILITY_MAP.put(CHAINMAIL_CHESTPLATE, (short) 241);
			DURABILITY_MAP.put(CHAINMAIL_LEGGINGS, (short) 226);
			DURABILITY_MAP.put(CHAINMAIL_BOOTS, (short) 196);
			
			// Stone Items
			DURABILITY_MAP.put(STONE_SWORD, (short) 132);
			DURABILITY_MAP.put(STONE_AXE, (short) 132);
			DURABILITY_MAP.put(STONE_SPADE, (short) 132);
			DURABILITY_MAP.put(STONE_PICKAXE, (short) 132);
			DURABILITY_MAP.put(STONE_HOE, (short) 132);
			
			// Iron Items
			DURABILITY_MAP.put(IRON_HELMET, (short) 166);
			DURABILITY_MAP.put(IRON_CHESTPLATE, (short) 241);
			DURABILITY_MAP.put(IRON_LEGGINGS, (short) 226);
			DURABILITY_MAP.put(IRON_BOOTS, (short) 196);
			DURABILITY_MAP.put(IRON_SWORD, (short) 251);
			DURABILITY_MAP.put(IRON_AXE, (short) 251);
			DURABILITY_MAP.put(IRON_SPADE, (short) 251);
			DURABILITY_MAP.put(IRON_PICKAXE, (short) 251);
			DURABILITY_MAP.put(IRON_HOE, (short) 251);
			
			// Diamond Items
			DURABILITY_MAP.put(DIAMOND_HELMET, (short) 364);
			DURABILITY_MAP.put(DIAMOND_CHESTPLATE, (short) 529);
			DURABILITY_MAP.put(DIAMOND_LEGGINGS, (short) 496);
			DURABILITY_MAP.put(DIAMOND_BOOTS, (short) 430);
			DURABILITY_MAP.put(DIAMOND_SWORD, (short) 1562);
			DURABILITY_MAP.put(DIAMOND_AXE, (short) 1562);
			DURABILITY_MAP.put(DIAMOND_SPADE, (short) 1562);
			DURABILITY_MAP.put(DIAMOND_PICKAXE, (short) 1562);
			DURABILITY_MAP.put(DIAMOND_HOE, (short) 1562);

			// Misc Items
			DURABILITY_MAP.put(CARROT_STICK, (short) 26);
			DURABILITY_MAP.put(SHEARS, (short) 239);
			DURABILITY_MAP.put(FLINT_AND_STEEL, (short) 65);
			DURABILITY_MAP.put(FISHING_ROD, (short) 65);
			DURABILITY_MAP.put(BOW, (short) 385);
			
		}
	
	*/
	// -------------------------------------------- //
	// DURABILITY
	// -------------------------------------------- //
	
	/**
	 * Applies the specified amount of damage to a tool/armor
	 * returns true if the item has been broken.
	 * WARNING, the item doesn't break for some weird reason.
	 * @param {Getter<ItemStack>} The item you want to modify,
	 * due to Bukkit untrustable itemstack references, we need to use a getter.
	 * @param {short} the amount of damage you want to apply to it
	 * @return {Couple<ItemStack, Boolean>} 
	 * A The itemstack that was modified.
	 * B true if it is broken now.
	 */
	public static Couple<ItemStack, Boolean> applyDamage(ItemStack item, short damage)
	{
		if (damage < 0) throw new IllegalArgumentException("item damage must be positive");
		// Remember, durability in minecraft counts upwards till it 
		// reaches the maximum. In this case, it destroys the itemstack.
		if (item == null) throw new IllegalArgumentException("item mustn't be null");
		if (item.getType() == Material.AIR) throw new IllegalArgumentException("item mustn't be air");
		
		// Since damage applied after enchantment modification might be a floating point
		// we make it fair and use probabilityRound.
		damage = (short) MUtil.probabilityRound(damage / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1.0));
		
		short newDurability = (short) (damage + item.getDurability());
		short maxDurability = (short) (ItemUtil.maxDurability(item));

		item.setDurability(newDurability);
		
		boolean broke = newDurability >= maxDurability;
		if (broke)
		{
			item.setType(Material.AIR);
			item.setAmount(0);
		}

		return new Couple<>(item, broke);
	}
	
	/**
	 * Reduces an amount of damage for the tool/armor specified
	 * returns true if the item has been fully repaired by it.
	 * @param {ItemStack} The item you want to modify
	 * @param {short} the amount of damage you want to take from it
	 * @return {Couple<ItemStack, Boolean>} 
	 * A The itemstack that was modified.
	 * B true if it is fully repaired now.
	 */
	public static Couple<ItemStack, Boolean> reduceDamage(ItemStack item, short damage)
	{
		if (damage < 0) throw new IllegalArgumentException("item damage must be positive");
		// Remember, durability in minecraft counts upwards till it 
		// reaches the maximum. In this case, it destroys the itemstack.
		if (item == null) throw new IllegalArgumentException("item mustn't be null");
		if (item.getType() == Material.AIR) throw new IllegalArgumentException("item mustn't be air");
		
		short newDurability = (short) (item.getDurability() - damage);
		newDurability = (short) Math.max(0, newDurability);
		item.setDurability(newDurability);
		
		return new Couple<>(item, newDurability == 0);
	}

	public static short maxDurability(ItemStack item)
	{
		Material type = item.getType();
		/*
		if ( ! DURABILITY_MAP.containsKey(type))
		{
			throw new IllegalArgumentException("Derius does not currently provide info for "
				+ Txt.getNicedEnum(type) + "\n if it is a tool, nag the authors at https://github.com/Derius/Derius-Core/issues");
		}
		
		short durability = DURABILITY_MAP.get(type);
		*/
		return type.getMaxDurability();
	}
	
	// -------------------------------------------- //
	// LORE
	// -------------------------------------------- //
	
	/**
	 * Adds the specified lore to the item passed in.
	 * @param {ItemStack} the item you want the lore added to
	 * @param {List<String} the lore you want to add
	 */
	public static void addLore(ItemStack item, List<String> lore)
	{
		if (item == null) return;
		if (lore == null)
		{
			lore = new ArrayList<>(1);
		}
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.hasLore() ? meta.getLore() : lore;
		itemLore.addAll(lore);
		
		meta.setLore(itemLore);
	}
	
	/**
	 * Removes the specified lore to the item passed in.
	 * @param {ItemStack} the item you want the lore added to
	 * @param {List<String} the lore you want to remove
	 */
	public static void removeLore(ItemStack item, List<String> lore)
	{
		if (item == null || lore == null) return;

		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.hasLore() ? meta.getLore() : lore;
		itemLore.removeAll(lore);
		
		meta.setLore(itemLore);
		item.setItemMeta(meta);
	}
	
	// -------------------------------------------- //
	// ENTCHANTMENTS
	// -------------------------------------------- //
	
	/**
	 * Adds enchantments levels to the given item, according to the maps Enchantment and  Integer(level) value
	 * @param {ItemStack} The item you want the enchantment applied to
	 * @param {Map<Enchantment, Integer>} The Map of Enchantments with their levels
	 */
	public static void addEnchantments(ItemStack item, Map<Enchantment, Integer> entchantments)
	{
		if (item == null || entchantments == null) return;
		
		// Save previous enchantment-data of this item
		Map<Enchantment, Integer> itemEntchantsments = item.getEnchantments();
		
		// For each enchantment in the user given map ...
		for (Enchantment ench : entchantments.keySet())
		{
			// get it's level..
			int level = entchantments.get(ench);
			
			// and if the item already has the enchantment we are looking at..
			if (itemEntchantsments.containsKey(ench))
			{
				// Add the level to the Item
				level = level + itemEntchantsments.get(ench);
				
				// remove old enchantment
				item.removeEnchantment(ench);
			}
			
			// Put the level into effect
			item.addEnchantment(ench, level);
		}
	}
	
	/**
	 * Removes enchantments levels to the given item, according to the maps Enchantment and  Integer(level) value
	 * @param {ItemStack} The item you want the enchantment applied to
	 * @param {Map<Enchantment, Integer>} The Map of Enchantments with their levels
	 */
	public static void removeEnchantments(ItemStack item, Map<Enchantment, Integer> entchantments)
	{
		if (item == null) return;
		if (entchantments == null) return;
		
		// Save previous enchantment-data of this item
		Map<Enchantment, Integer> itemEntchantsments= item.getEnchantments();
		
		// For each enchantment in the user given map ...
		for (Enchantment ench : entchantments.keySet())
		{
			// get it's level..
			int level = itemEntchantsments.get(ench);
			
			// and if the item already has the enchantment we are looking at..
			if (itemEntchantsments.containsKey(ench))
			{
				// subtract the level from the Item
				level = level - entchantments.get(ench);
				
				// remove old enchantment
				item.removeEnchantment(ench);
			}
			
			// If the level now is zero, don't  add anything new
			if (level == 0) continue;
			
			// Put the level into effect
			item.addEnchantment(ench, level);
		}
	}
	
	/**
	 * Adds a single enchantment to this item, or adds the level of it to the current one.
	 * @param {ItemStack} The item you want the enchantment applied to
	 * @param {Enchantment} The enchantment you want to add
	 * @param {int} the level amount you want to add
	 */
	public static void addEnchantment(ItemStack item, Enchantment ench, int level)
	{
		if (item == null) return;
		if (ench == null) return;
		if (level == 0) return;
		
		Map<Enchantment, Integer> entchantments = new HashMap<Enchantment, Integer>();
		
		ItemUtil.addEnchantments(item, entchantments);
	}
	
	/**
	 * Removes a single enchantment to this item, or adds the level of it to the current one.
	 * @param {ItemStack} The item you want the enchantment removal applied to
	 * @param {Enchantment} The enchantment you want to remove
	 * @param {int} the level amount you want to remove
	 */
	public static void removeEnchantment(ItemStack item, Enchantment ench, int level)
	{
		if (item == null) return;
		if (ench == null) return;
		if (level == 0) return;
		
		Map<Enchantment, Integer> entchantments = new HashMap<Enchantment, Integer>();
		
		ItemUtil.removeEnchantments(item, entchantments);
	}

}
