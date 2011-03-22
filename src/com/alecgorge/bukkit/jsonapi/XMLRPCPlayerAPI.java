package com.alecgorge.bukkit.jsonapi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Shamelessly stolen from sk89...
 * @author sk89q
 */
public class XMLRPCPlayerAPI {
    /**
     * Error code reported when a player cannot be found by his/her name.
     */
    private static final int PLAYER_NOT_FOUND = 1;
	private JSONApi etc;

    public XMLRPCPlayerAPI(JSONApi plugin) {
    	this.etc = plugin;
	}

	/**
     * Gets a player by name.
     *
     * @param name
     * @return
     * @throws APIException
     */
    public Player getPlayerByName(String name) throws APIException {
    	Player player = etc.getServer().getPlayer(name);
    	if(player == null) {
    		throw new APIException(PLAYER_NOT_FOUND, "Name does not exist");
    	}
    	return player;
    }

    /**
     * Get the map containing a player's information.
     *
     * @param player
     * @return
     */
    private Map<String,Object> getPlayerInfoMap(Player player) {
        // TODO Find replacement methods for the ones commented out.
    	Map<String,Object> kv = new HashMap<String,Object>();
        kv.put("name", player.getName());
        //kv.put("isAdmin", player.getAdmin());
        //kv.put("canBuild", player.canBuild());
        //kv.put("canIgnoreRestrictions", player.canIgnoreRestrictions());
        //kv.put("canModifyWorld", player.canModifyWorld());
        //kv.put("isMuted", player.isMuted());
        //kv.put("color", player.getColor());
        kv.put("health", player.getHealth());
        kv.put("ip", player.getAddress().toString().substring(1));//getIP());
        kv.put("itemInHand", player.getItemInHand().getTypeId());
        kv.put("x", player.getLocation().getX());
        kv.put("y", player.getLocation().getY());
        kv.put("z", player.getLocation().getZ());
        kv.put("yaw", (double)player.getLocation().getPitch());
        kv.put("pitch", (double)player.getLocation().getYaw());
        kv.put("world", player.getLocation().getWorld().getName());
        //kv.put("prefix", player.getPrefix());
        return kv;
    }

    /**
     * Gets a list of players and information about them.
     *
     * @return
     */
    public List<Map> getPlayers() {
        List<Map> players = new ArrayList<Map>();

        for (Player player : etc.getServer().getOnlinePlayers()) {
            players.add(getPlayerInfoMap(player));
        }

        return players;
    }

    /**
     * Gets a list of player names.
     *
     * @return
     */
    public List<String> getPlayerNames() {
        List<String> players = new ArrayList<String>();

        for (Player player : etc.getServer().getOnlinePlayers()) {
            players.add(player.getName());
        }

        return players;
    }

    /**
     * Get information about a player.
     *
     * @param name
     * @return
     * @throws APIException
     */
    public Map<String,Object> getPlayerInfo(String name) throws APIException {
        Player player = getPlayerByName(name);
        return getPlayerInfoMap(player);
    }

    /**
     * Give a player an item.
     *
     * @param name
     * @param itemID
     * @param amount
     * @return
     * @throws APIException
     * @return
     */
    public boolean giveItem(String name, int itemID, int amount)
            throws APIException {
        Player player = getPlayerByName(name);
        
        ItemStack stack = new ItemStack(itemID);
        stack.setAmount(amount);
        player.getInventory().addItem(stack);
        
        return true;
    }

    /**
     * Give a player an item drop.
     *
     * @param name
     * @param itemID
     * @param amount
     * @return
     * @throws APIException
     * @return
     */
    public boolean giveItemDrop(String name, int itemID, int amount)
            throws APIException {
        Player player = getPlayerByName(name);
        
        ItemStack stack = new ItemStack(itemID);
        stack.setAmount(amount);
        
        // DONE Check that giving a player a drop does mean dropping it on their location.
        player.getWorld().dropItem(player.getLocation(), stack);
       
        return true;
    }

    /**
     * Set a position of a player.
     *
     * @param name
     * @param x
     * @param y
     * @param z
     * @throws APIException
     * @return
     */
    public boolean teleportTo(String name, Integer x, Integer y, Integer z)
            throws APIException {
        Player player = getPlayerByName(name);

        Location newLocation = player.getLocation().clone();
        newLocation.setX(x);
        newLocation.setY(y);
        newLocation.setZ(z);

        player.teleportTo(newLocation);
        return true;
    }

    public boolean teleportTo(String name, Integer x, Integer y, Integer z, String world)
            throws APIException {
        Player player = getPlayerByName(name);

        Location newLocation = player.getLocation().clone();
        newLocation.setX(x);
        newLocation.setY(y);
        newLocation.setZ(z);
        newLocation.setWorld(etc.getServer().getWorld(world));

        player.teleportTo(newLocation);
        return true;
    }

    public String replaceColors(String s)
    {
        String msg = s;
        Pattern p = Pattern.compile("&c[0-9a-fA-F]");
        Matcher m = p.matcher(s);
        while(m.find())
        {
            msg = m.replaceAll("§\0");
        }

        return msg;
    }

    /**
     * Set a position of a player.
     *
     * @param name
     * @param x
     * @param y
     * @param z
     * @param pitch
     * @param yaw
     * @throws APIException
     * @return
     */
    public boolean teleportTo(String name, double x, double y, double z,
            double pitch, double yaw, String world)
            throws APIException {
        Player player = getPlayerByName(name);
        
        Location location = new Location(etc.getServer().getWorld(world), x, y, z);
        location.setPitch((float)pitch);
        location.setYaw((float)yaw);
        
        player.teleportTo(location);
        etc.getLogger().info("[JsonAPI] teleport : "+name+" to "+x+","+y+","+z);
        return true;
    }

    /**
     * Toggles a player's mute status.
     *
     * @param name
     * @return
     * @throws APIException
     */
    public boolean toggleMute(String name) throws APIException {
    	return false;
    	// TODO Figure out the replacement for toggleMute
    	//Player player = getPlayerByName(name);
        //return player.toggleMute();
    }

    /**
     * Kick a player.
     *
     * @param name
     * @param reason
     * @throws APIException
     * @return
     */
    public boolean kick(String name, String reason)
            throws APIException {
    	Player player = getPlayerByName(name);
        player.kickPlayer(reason);
        etc.getLogger().info("[JsonAPI] kick : "+name+", "+reason);
        return true;
    }

    /**
     * Send a player a message.
     *
     * @param name
     * @param msg
     * @throws APIException
     * @return
     */
    public boolean sendMessage(String name, String msg)
            throws APIException {
        Player player = getPlayerByName(name);
        String m = msg.replace("%c", "§");
        player.sendMessage(m);
        etc.getLogger().info("[JsonAPI] sendMessage : "+m);
        return true;
    }

    /**
     * Broadcast a message.
     *
     * @param msg
     * @return
     */
    public boolean broadcastMessage(String msg) {
        String m = msg.replace("%c", "§");
        for (Player player : etc.getServer().getOnlinePlayers()) {
            player.sendMessage(m);
        }
        etc.getLogger().info("[JsonAPI] broadcastMessage : "+m);
        return true;
    }

    /**
     * Get a player's inventory.
     *
     * @param name
     * @throws APIException
     */
    public Map<Integer,Map<String,Integer>> getInventory(String name)
            throws APIException {
        Player player = getPlayerByName(name);
        PlayerInventory  inventory = player.getInventory();

        HashMap<Integer, Map<String,Integer>> out =
                new HashMap<Integer,Map<String,Integer>>();

        int count = 0;
        for(ItemStack item : inventory.getContents()) {
            Map<String,Integer> kv = new HashMap<String,Integer>();
            kv.put("itemID", item.getTypeId());
            kv.put("amount", item.getAmount());
            out.put(count, kv);
            count++;
        }
        
        Map<String,Integer> kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getBoots().getTypeId());
        kv.put("amount", (kv.get("itemID") > 0 ? 1 : 0));
        out.put(100, kv);
        
        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getLeggings().getTypeId());
        kv.put("amount", (kv.get("itemID") > 0 ? 1 : 0));
        out.put(101, kv);
        
        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getChestplate().getTypeId());
        kv.put("amount", (kv.get("itemID") > 0 ? 1 : 0));
        out.put(102, kv);
        
        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getHelmet().getTypeId());
        kv.put("amount", (kv.get("itemID") > 0 ? 1 : 0));
        out.put(103, kv);
        

        return out;
    }

    /**
     * Get a player's inventory.
     *
     * @param name
     * @throws APIException
     */
    public Map<Integer,Map<String,Integer>> getInventoryDurability(String name)
            throws APIException {
        Player player = getPlayerByName(name);
        PlayerInventory  inventory = player.getInventory();

        HashMap<Integer, Map<String,Integer>> out =
                new HashMap<Integer,Map<String,Integer>>();

        int count = 0;
        for(ItemStack item : inventory.getContents()) {
            Map<String,Integer> kv = new HashMap<String,Integer>();
            kv.put("itemID", item.getTypeId());
            kv.put("durability", Integer.valueOf(item.getDurability()));
            out.put(count, kv);
            count++;
        }

        Map<String,Integer> kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getBoots().getTypeId());
        // kv.put("amount", (kv.get("itemID") > 0 ? 1 : 0));
        kv.put("durability", Integer.valueOf(inventory.getBoots().getDurability()));
        out.put(100, kv);

        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getLeggings().getTypeId());
        kv.put("durability", Integer.valueOf(inventory.getLeggings().getDurability()));
        out.put(101, kv);

        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getChestplate().getTypeId());
        kv.put("durability", Integer.valueOf(inventory.getChestplate().getDurability()));
        out.put(102, kv);

        kv = new HashMap<String,Integer>();
        kv.put("itemID", inventory.getHelmet().getTypeId());
        kv.put("durability", Integer.valueOf(inventory.getHelmet().getDurability()));
        out.put(103, kv);


        return out;
    }

    /**
     * Remove the item in a player's inventory slot.
     *
     * @param name
     * @param slot
     * @return
     * @throws APIException
     */
    public boolean removeInventorySlot(String name, int slot)
            throws APIException {
    	Player player = getPlayerByName(name);
        player.getInventory().setItem(slot, new ItemStack(0, 0));
        return true;
    }

    /**
     * Remove some items from a player's inventory.
     * 
     * @param name
     * @param itemID
     * @param amount
     * @return
     * @throws APIException
     */
    public boolean removeInventoryItem(String name, int itemID, int amount)
            throws APIException {
    	Player player = getPlayerByName(name);
        Inventory inventory = player.getInventory();
    	inventory.removeItem(new ItemStack(itemID, amount));
        return true;
    }

    /**
     * Get a list of warps accessible by a player.
     *
     * @param name
     * @return
     * @throws APIException
     */
    public String[] getAccessibleWarps(String name)
            throws APIException {
    	// TODO No warp support yet
        //Player player = getPlayerByName(name);
        //return etc.getDataSource().getWarpNames(player).split(",");
    	return new String[0];
    }

    /**
     * Get a list of accessible kits.
     * 
     * @param name
     * @return
     * @throws APIException
     */
    public String[] getAccessibleKits(String name)
            throws APIException {
    	// TODO No kit support yet.
    	//Player player = getPlayerByName(name);
        //return etc.getDataSource().getKitNames(player).split(",");
    	return new String[0];
    }

    public void reloadPermissions()
    {
        etc.reloadPermissions();
    }
}
