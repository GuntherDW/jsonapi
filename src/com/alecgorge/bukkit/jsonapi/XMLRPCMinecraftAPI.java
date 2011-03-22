package com.alecgorge.bukkit.jsonapi;

import org.bukkit.block.Block;






/**
 * Shamelessly stolen from sk89...
 * @author sk89q
 */
public class XMLRPCMinecraftAPI {
    private JSONApi etc;
    
    public XMLRPCMinecraftAPI(JSONApi plugin){
    	this.etc = plugin;
    }
	
	/**
     * Thrown when a bad block index is supplied.
     */
    @SuppressWarnings("unused")
	private static final int BAD_BLOCK_INDEX = 10;
    
    /**
     * Gets the block at a location.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getBlockID(int x, int y, int z) {
        Block block = etc.getServer().getWorlds().get(0).getBlockAt(x, y, z);
    	return block.getTypeId();
    }

    /**
     * Gets a cuboid. A byte array is returned with the index of each byte
     * defined to be y * width * length + z * width + x. The value of each
     * byte is the block ID. Note that index 0 is min X, min Y, min Z, and it
     * has no relevance with the x1, y1, z1 parameters.
     * 
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return
     */
    public byte[] getCuboidIDs(int x1, int y1, int z1, int x2, int y2, int z2) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        int width = Math.abs(maxX - minX) + 1;
        int height = Math.abs(maxY - minY) + 1;
        int length = Math.abs(maxZ - minZ) + 1;

        byte[] data = new byte[width * height * length];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int index = y * width * length + z * width + x;
                    data[index] =
                            (byte)getBlockID(x + minX, y + minY, z + minZ);
                }
            }
        }
        return data;
    }


    /**
     * Sets the block at a location.
     *
     * @param x
     * @param y
     * @param z
     * @param id
     * @return
     */
    public boolean setBlockID(int x, int y, int z, int id) {
    	Block block = etc.getServer().getWorlds().get(0).getBlockAt(x, y, z);
    	return block.setTypeId(id);
    }

    /**
     * Gets the highest block at a certain location.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getHighestBlockY(int x, int z) {
        return etc.getServer().getWorlds().get(0).getHighestBlockYAt(x, z);
    }

    /**
     * Set time.
     *
     * @return
     */
    public int getTime() {
        return (int)etc.getServer().getWorlds().get(0).getTime();
    }

    /**
     * Set time.
     *
     * @param time
     * @return
     */
    public boolean setTime(int time) {
        etc.getServer().getWorlds().get(0).setTime(time);
        return true;
    }

    /**
     * Set relative time.
     *
     * @return
     */
    public int getRelativeTime() {
        long time = (getTime() % 24000);
        // Java modulus is stupid.
        if (time < 0) {
            time += 24000;
        }
        return (int)time;
    }

    /**
     * Set relative time.
     *
     * @param time
     * @return
     */
    public boolean setRelativeTime(int time) {
        long margin = (time-getTime()) % 24000;
        // Java modulus is stupid.
        if (margin < 0) {
            margin += 24000;
        }

        return setTime((int)(getTime()+margin));
    }
}
