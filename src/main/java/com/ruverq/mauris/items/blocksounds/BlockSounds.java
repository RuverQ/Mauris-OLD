package com.ruverq.mauris.items.blocksounds;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.SoundGroup;

public class BlockSounds {

    @Getter
    @Setter
    String hitSound;

    @Getter
    @Setter
    String stepSound;

    @Getter
    @Setter
    String breakSound;

    @Getter
    @Setter
    String placeSound;

    @Getter
    @Setter
    String fallSound;

    @Getter
    @Setter
    SoundGroup defaultSounds;

    public void executePlaceSound(Location location){
        if(placeSound == null || placeSound.isEmpty()){
            location.getWorld().playSound(location, defaultSounds.getPlaceSound(), SoundCategory.BLOCKS, 1, 1);
            return;
        }
        location.getWorld().playSound(location, placeSound, SoundCategory.BLOCKS, 1, 1);
    }

    public void executeBreakSound(Location location){
        if(breakSound == null || breakSound.isEmpty()){
            location.getWorld().playSound(location, defaultSounds.getBreakSound(), SoundCategory.BLOCKS, 1, 1);
            return;
        }
        location.getWorld().playSound(location, breakSound, SoundCategory.BLOCKS, 1, 1);
    }

    public void executeStepSound(Location location){
        if(stepSound == null || stepSound.isEmpty()){
            location.getWorld().playSound(location, defaultSounds.getStepSound(), SoundCategory.PLAYERS, 0.2f, 1);
            return;
        }

        location.getWorld().playSound(location, stepSound, SoundCategory.PLAYERS, 0.2f, 1);
    }

    public void executeHitSound(Location location){
        if(hitSound == null || hitSound.isEmpty()){
            location.getWorld().playSound(location, defaultSounds.getHitSound(), SoundCategory.BLOCKS, 1, 1);
            return;
        }

        location.getWorld().playSound(location, hitSound, SoundCategory.BLOCKS, 1, 1);
    }

    public void executeFallSound(Location location){
        if(fallSound == null || fallSound.isEmpty()){
            location.getWorld().playSound(location, defaultSounds.getFallSound(), SoundCategory.BLOCKS, 1, 1);
            return;
        }
        location.getWorld().playSound(location, fallSound, SoundCategory.BLOCKS, 1, 1);
    }


}
