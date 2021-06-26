package com.ruverq.mauris.items.blockhardness;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;

import java.util.concurrent.ThreadLocalRandom;

public class BlockMorphUtils {

    public static void playBlockSound(Block block){
        MaurisBlock mb = ItemsLoader.getMaurisBlock(block.getBlockData());

        block.getWorld().playSound(block.getLocation(), mb.getBreakSoundSafe(), SoundCategory.BLOCKS,1, 1f);
    }


    public static void playBlockParticles(Block block){
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 50, 0.24, 0.24, 0.25, 5, block.getBlockData());
    }

}
