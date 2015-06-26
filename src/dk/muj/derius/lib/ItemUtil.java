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

	@Deprecated
	/**
	 * Use Material#getMaxDurability() instead.
	 * @param item
	 * @return
	 */
	public static short maxDurability(ItemStack item)
	{
		return item.getType().getMaxDurability();
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
			int level = 0;
			
			// and if the item already has the enchantment we are looking at..
			if (itemEntchantsments.containsKey(ench))
			{
				// get it's level..
				level = itemEntchantsments.get(ench);
				
				// subtract the level from the Item
				level -= entchantments.get(ench);
				
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
