package cx.gg.antiteajizzle;

import cx.gg.antiteajizzle.AntiTeaJizzle;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.Biome;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tom on 19/06/2014.
 */
public class AsyncCheckChunkBedrock extends BukkitRunnable {
    ChunkSnapshot chunk;

    AsyncCheckChunkBedrock(ChunkSnapshot chunkSnapshot) {
        this.chunk = chunkSnapshot;
    }

    @Override
    public void run() {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                //check for holes in bedrock
                if (chunk.getBlockTypeId(x, 1, z)==0) {
                    AntiTeaJizzle.fixChunk(chunk);
                }
                //check for bedrock above y=1
                for(int y = 2; y < 7; y++) {
                    if (chunk.getBlockTypeId(x, y, z)==4) {
                        AntiTeaJizzle.fixChunk(chunk);
                    }
                }
            }
        }
    }



}
