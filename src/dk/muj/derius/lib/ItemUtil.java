package dk.muj.derius.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

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
	
	private static Map<Material, Short> durabilityMap = MUtil.map(
			// I cast one, and suddenly all of it is ok, the java compiler is smart
			// Leather Items				
			Material.LEATHER_HELMET,		(short) 56,
			Material.LEATHER_CHESTPLATE,	81,
			Material.LEATHER_LEGGINGS,		76,
			Material.LEATHER_BOOTS,			60,
			
			// Wood Items
			Material.WOOD_SWORD,			60,
			Material.WOOD_AXE,				60,
			Material.WOOD_SPADE,			60,
			Material.WOOD_PICKAXE,			60,
			Material.WOOD_HOE,				60,
			
			// Gold Items
			Material.GOLD_HELMET,			78,
			Material.GOLD_CHESTPLATE,		113,
			Material.GOLD_LEGGINGS,			106,
			Material.GOLD_BOOTS,			92,
			Material.GOLD_SWORD,			33,
			Material.GOLD_AXE,				33,
			Material.GOLD_SPADE,			33,
			Material.GOLD_PICKAXE,			33,
			Material.GOLD_HOE,				33,
			
			// Chainmail Items
			Material.CHAINMAIL_HELMET,		166,
			Material.CHAINMAIL_CHESTPLATE,	241,
			Material.CHAINMAIL_LEGGINGS,	226,
			Material.CHAINMAIL_BOOTS,		196,
			
			// Stone Items
			Material.STONE_SWORD,			 132,
			Material.STONE_AXE,				 132,
			Material.STONE_SPADE,			 132,
			Material.STONE_PICKAXE,			 132,
			Material.STONE_HOE,				 132,
			
			// Iron Items
			Material.IRON_HELMET,			166,
			Material.IRON_CHESTPLATE,		241,
			Material.IRON_LEGGINGS,			226,
			Material.IRON_BOOTS,			196,
			Material.IRON_SWORD,			251,
			Material.IRON_AXE,				251,
			Material.IRON_SPADE,			251,
			Material.IRON_PICKAXE,			251,
			Material.IRON_HOE,				251,
			
			// Diamond Items
			Material.DIAMOND_HELMET,		364,
			Material.DIAMOND_CHESTPLATE,	529,
			Material.DIAMOND_LEGGINGS,		496,
			Material.DIAMOND_BOOTS,			430,
			Material.DIAMOND_SWORD,			1562,
			Material.DIAMOND_AXE,			1562,
			Material.DIAMOND_SPADE,			1562,
			Material.DIAMOND_PICKAXE,		1562,
			Material.DIAMOND_HOE,			1562,

			// Misc Items
			Material.CARROT_STICK,			26,
			Material.SHEARS,				239,
			Material.FLINT_AND_STEEL,		65,
			Material.FISHING_ROD,			65,
			Material.BOW,					385
			
			);
	
	// -------------------------------------------- //
	// DURABILITY
	// -------------------------------------------- //
	
	/**
	 * Applies the specified amount of damage to a tool/armor
	 * returns true if the item has been broken.
	 * @param {ItemStack} The item you want to modify
	 * @param {short} the amount of damage you want to apply to it
	 * @return {boolean} true if broken afterwards
	 */
	public static boolean applyDamage(ItemStack item, short damage)
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
		short maxDurability = ItemUtil.maxDurability(item);
		
		item.setDurability(newDurability);
		
		return newDurability >= maxDurability;
	}
	
	/**
	 * Reduces an amount of damage for the tool/armor specified
	 * returns true if the item has been fully repaired by it.
	 * @param {ItemStack} The item you want to modify
	 * @param {short} the amount of damage you want to take from it
	 * @return {boolean} whether it is fully repaired or not
	 */
	public static boolean reduceDamage(ItemStack item, short reduce)
	{
		if (item == null || item.getType() == Material.AIR) return false;
		if (reduce < 0)
		{
			reduce = (short) -reduce;
		}
		
		short newDurability = (short) (item.getDurability() - reduce);
		if (newDurability >= 0) newDurability = 0;
		item.setDurability(newDurability);
		
		return newDurability <= 0;
	}

	public static short maxDurability(ItemStack item)
	{
		Material type = item.getType();
		if ( ! ItemUtil.durabilityMap.containsKey(type))
		{
			throw new IllegalArgumentException("Derius does not currently provide info for "
				+ Txt.getNicedEnum(type) + " nag the authors at https://github.com/Derius/Derius-Core/issues");
		}
		
		int durability = ItemUtil.durabilityMap.get(type);
		return (short) durability;
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
