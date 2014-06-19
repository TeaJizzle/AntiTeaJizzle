package cx.gg.antiteajizzle;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import cx.gg.antiteajizzle.AsyncCheckChunkBedrock;

import java.util.HashMap;

public class AntiTeaJizzle extends JavaPlugin implements Listener {
	//Check chunks for dodgy bedrock
    HashMap<Integer,Integer> checkedChunks = new HashMap<Integer,Integer>();

	@EventHandler
	public void onChunkLoad (ChunkLoadEvent event) {
        Integer cX = event.getChunk().getX();
        Integer cZ = event.getChunk().getZ();
        //check if it's overworld
        if (!event.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }
		//return if chunk has already been checked
        if (checkedChunks.containsKey(cX) && checkedChunks.containsValue(cZ)) {
            return;
        }
        //mark chunk as checked
        checkedChunks.put(cX, cZ);
        //run task to check for dodgy bedrock
        ChunkSnapshot chunk = event.getChunk().getChunkSnapshot();
        Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin) this, new AsyncCheckChunkBedrock(chunk));

    }

    public static void fixChunk (ChunkSnapshot chunkSnapshot) {
        World world = Bukkit.getWorld(chunkSnapshot.getWorldName());
        Integer blockX = (chunkSnapshot.getX() * 16);
        Integer blockZ = (chunkSnapshot.getZ() * 16);
        Chunk chunk = world.getChunkAt(blockX,blockZ);
        Boolean unloadChunk = false;
        if (chunk.isLoaded()) {
            chunk.load();
            unloadChunk = true;
        }

        //set all y=1 blocks to bedrock
        Integer bedrockAdded = 0;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                if (!world.getBlockAt(blockX+x, 1, blockZ+z).getType().equals(Material.BEDROCK)) {
                    world.getBlockAt(blockX+x, 1, blockZ+z).setType(Material.BEDROCK);
                    bedrockAdded++;
                }
            }
        }

        //set all bedrock above y=1 to stone
        Integer bedrockRemoved = 0;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = 2; y < 7; y++) {
                    if (world.getBlockAt(blockX + x, y, blockZ + z).getType().equals(Material.BEDROCK)) {
                        world.getBlockAt(blockX + x, y, blockZ + z).setType(Material.STONE);
                        bedrockRemoved++;
                    }
                }
            }
        }

        //if we loaded the chunk to do this, unload it
        if (unloadChunk) {
            chunk.unload(true);
        }

        //log to console
        Bukkit.getLogger().warning("Fixed chunk at "+Integer.toString(blockX)+", "+Integer.toString(blockZ)
                +". Repaired "+Integer.toString(bedrockAdded)+" bedrock blocks at y=1, removed "
                +Integer.toString(bedrockRemoved)+" bedrock blocks above y=1.");
    }
	
	public void onEnable() { 
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
}
