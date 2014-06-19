package cx.gg.antiteajizzle;

import cx.gg.antiteajizzle.AntiTeaJizzle;

import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tom on 19/06/2014.
 */
public class AsyncCheckChunkBedrock extends BukkitRunnable {
    ChunkSnapshot chunk;
    JavaPlugin plugin;

    AsyncCheckChunkBedrock(ChunkSnapshot chunkSnapshot, JavaPlugin plugin) {
        this.chunk = chunkSnapshot;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                //check for holes in bedrock
                if (chunk.getBlockTypeId(x, 1, z)!=7) {
                	Bukkit.getServer().getScheduler().runTask((Plugin)plugin, new FixChunkTask(chunk));
                	return;
                }
                //check for bedrock above y=1
                for(int y = 2; y < 7; y++) {
                    if (chunk.getBlockTypeId(x, y, z)==7) {
                    	Bukkit.getServer().getScheduler().runTask((Plugin)plugin, new FixChunkTask(chunk));
                    	return;
                    }
                }
            }
        }
    }



}
