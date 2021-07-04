package com.ruverq.mauris.items.blockhardness;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;

public class BlockMorphUtils {

    public static void playBlockSound(Block block){
        MaurisBlock mb = ItemsLoader.getMaurisBlock(block.getBlockData());
        mb.getSounds().executeBreakSound(block.getLocation());
    }


    public static void playBlockParticles(Block block){

        Location center = block.getLocation().add(0.5, 0.5, 0.5);
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, center, 50, 0.25, 0.25, 0.25, 10000, block.getBlockData());
    }

}
