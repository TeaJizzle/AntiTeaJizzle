package cx.gg.antiteajizzle;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import cx.gg.antiteajizzle.AsyncCheckChunkBedrock;



public class AntiTeaJizzle extends JavaPlugin implements Listener {
	//Check chunks for dodgy bedrock

	@EventHandler
	public void onChunkLoad (ChunkLoadEvent event) {
        //check if it's overworld
        if (!event.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return;
        }
        if (event.isNewChunk()) {
        	return;
        }
        //run task to check for dodgy bedrock
        ChunkSnapshot chunk = event.getChunk().getChunkSnapshot();
        Bukkit.getServer().getScheduler().runTask((Plugin) this, new AsyncCheckChunkBedrock(chunk, this));

    }

	
	public void onEnable() { 
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
}
