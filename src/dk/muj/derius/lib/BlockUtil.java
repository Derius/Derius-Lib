package dk.muj.derius.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
		Set<Block> ret = new HashSet<Block>();
		
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
		Set<Block> ret = new HashSet<Block>();
		
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
		Set<Block> ret = new HashSet<Block>();
		
		for (BlockFace face : BlockFace.values())
		{
			if (faces.contains(face)) continue;
			ret.add(source.getRelative(face));
		}
		
		return ret;
	}
	
}
