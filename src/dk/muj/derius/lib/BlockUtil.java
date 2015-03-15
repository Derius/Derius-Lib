package dk.muj.derius.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public final class BlockUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN
	// -------------------------------------------- //
	
	private BlockUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// SURROUNDING BLOCKS
	// -------------------------------------------- //
	
	/**
	 * Gets all blocks relative to specified block. With all BlockFaces
	 * @param {Block} Block to get relatives from
	 * @return {Set<Block>} the blocks relative to the specified block
	 */
	public static Set<Block> getSurroundingBlocks(final Block source)
	{
		Set<Block> ret = new HashSet<>();
		
		for (BlockFace face : BlockFace.values())
		{
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
	/**
	 * Gets all blocks relative to specified block. With specified BlockFaces
	 * @param {Block} Block to get relatives from
	 * @param {Collection<BlockFace>} BlockFaces to get from
	 * @return {Set<Block>} the blocks relative to the specified block.
	 */
	public static Set<Block> getSurroundingBlocksWith(final Block source, Collection<BlockFace> faces)
	{
		Set<Block> ret = new HashSet<>();
		
		for (BlockFace face : faces)
		{
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
	/**
	 * Gets all blocks relative to specified block. Without specified BlockFaces
	 * @param {Block} Block to get relatives from
	 * @param {Collection<BlockFace>} BlockFaces not to get from
	 * @return {Set<Block>} the blocks relative to the specified block.
	 */
	public static Set<Block> getSurroundingBlocksWithout(final Block source, Collection<BlockFace> faces)
	{
		Set<Block> ret = new HashSet<>();
		
		for (BlockFace face : BlockFace.values())
		{
			if (faces.contains(face)) continue;
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
	// -------------------------------------------- //
	// IS LOG/LEAVE
	// -------------------------------------------- //
	
	public static boolean isLogOrLeave(Material compared)
	{
		return isLog(compared) || isLeave(compared);
	}
	public static boolean isLogOrLeave(BlockState compared) { return isLogOrLeave(compared.getType()); }
	public static boolean isLogOrLeave(Block compared) { return isLogOrLeave(compared.getState()); }
	
	public static boolean isLog(Material compared)
	{
		return compared == Material.LOG || compared == Material.LOG_2;
	}
	public static boolean isLog(BlockState compared) { return isLog(compared.getType()); }
	public static boolean isLog(Block compared) { return isLog(compared.getState()); }

	
	public static boolean isLeave(Material compared)
	{
		return compared == Material.LEAVES || compared == Material.LEAVES_2;
	}
	public static boolean isLeave(BlockState compared) { return isLeave(compared.getType()); }
	public static boolean isLeave(Block compared) { return isLeave(compared.getState()); }
	
	// -------------------------------------------- //
	// IS WOOD TYPE
	// -------------------------------------------- //
	
	// The real logic.
	@SuppressWarnings("deprecation")
	public static boolean isWoodType(BlockState compared, int version, int data)
	{
		return getWoodVersion(compared) == version && (compared.getData().getData() % 4) == data;
	}
	
	// Oak
	public static final int OAK_DATA = 0;
	public static boolean isOak(BlockState compared) { return isWoodType(compared, 1, OAK_DATA); }
	public static boolean isOak(Block compared) { return isOak(compared.getState()); }
	
	// Spruce
	public static final int SPRUCE_DATA = 1;
	public static boolean isSpruce(BlockState compared) { return isWoodType(compared, 1, SPRUCE_DATA); }
	public static boolean isSpruce(Block compared) { return isSpruce(compared.getState()); }
	
	// Birch
	public static final int BIRCH_DATA = 2;
	public static boolean isBirch(BlockState compared) { return isWoodType(compared, 1, BIRCH_DATA); }
	public static boolean isBirch(Block compared) { return isBirch(compared.getState()); }
	
	// Jungle
	public static final int JUNGLE_DATA = 3;
	public static boolean isJungle(BlockState compared) { return isWoodType(compared, 1, JUNGLE_DATA); }
	public static boolean isJungle(Block compared) { return isJungle(compared.getState()); }
	
	// Acacia
	public static final int ACACIA_DATA = 0;
	public static boolean isAcacia(BlockState compared) { return isWoodType(compared, 2, ACACIA_DATA); }
	public static boolean isAcacia(Block compared) { return isAcacia(compared.getState()); }
	
	// Dark oak
	public static final int DARK_OAK_DATA = 1;
	public static boolean isDarkOak(BlockState compared) { return isWoodType(compared, 2, DARK_OAK_DATA); }
	public static boolean isDarkOak(Block compared) { return isDarkOak(compared.getState()); }
	
	// -------------------------------------------- //
	// WOOD TYPE
	// -------------------------------------------- //
	
	/**
	 * Gets the wood type (1 or 2)
	 * Not related to BlockUtil.isSameWoodType
	 * @return
	 * 1 if it is oak, birch, spruce or jungle
	 * 2 if it is dark oak or acacia.
	 */
	public static int getWoodVersion(Material compared)
	{
		if (compared == Material.LOG || compared == Material.LEAVES) return 1;
		if (compared == Material.LOG_2 || compared == Material.LEAVES_2) return 2;
		throw new IllegalArgumentException("passed material is not a log or leave");
	}

	public static int getWoodVersion(BlockState compared)
	{
		return getWoodVersion(compared.getType());
	}
	
	
	public static int getWoodVersion(Block compared)
	{
		return getWoodVersion(compared.getState());
	}
	
	public static boolean isSameWoodType(BlockState b1, BlockState b2)
	{
		int d1 = b1.getData().getData();
		int d2 = b2.getData().getData();
		
		// Sometimes data is stored by adding 4, 8 or 12 to the data value.
		// We don't care about that extra information.
		d1 = d1 % 4;
		d2 = d2 % 4;
		
		return getWoodVersion(b1) == getWoodVersion(b2) && d1 == d2;
	}
	
}
