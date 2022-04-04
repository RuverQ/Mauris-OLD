package com.ruverq.mauris.items.decorations;

import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.*;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.MaurisLootTable;
import com.ruverq.mauris.items.blocks.blocksounds.BlockSounds;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockType;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.utils.DataContainerUtil;
import com.ruverq.mauris.utils.EntityDataContainerUtil;
import lombok.Getter;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.io.File;

public class MaurisDecoration extends MaurisItem implements MaurisPlaceable {

    @Getter
    MaurisDecorationInfo info;

    public MaurisDecoration(MaurisFolder folder, String name, MaurisTextures textures, ItemCharacteristics itemC, boolean generateModel, String model, MaurisBlock maurisBlock, File file, MaurisDecorationInfo info) {
        super(folder, name, textures, itemC, generateModel, model, maurisBlock, file);
        this.info = info;
        this.isPlaceable = true;
    }

    @Override
    public void generate() {
        super.generate();
    }

    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){
        ConfigurationSection decorationSection = cs.getConfigurationSection("decoration");
        if(decorationSection == null) return;

        boolean enabled = decorationSection.getBoolean("enabled", true);
        if(!enabled) return;

        String modelItem = decorationSection.getString("modelItem", "");
        if(modelItem.isEmpty()) return;

        boolean canWalkThrough = decorationSection.getBoolean("canWalkThrough", true);
        boolean gravity = decorationSection.getBoolean("gravity", false);
        boolean selfDrop = decorationSection.getBoolean("selfDrop", true);

        boolean small = decorationSection.getBoolean("small", false);

        String placeSound = decorationSection.getString("sounds.place");
        String stepSound = decorationSection.getString("sounds.step");
        String breakSound = decorationSection.getString("sounds.break");
        String hitSound = decorationSection.getString("sounds.hit");
        String fallSound = decorationSection.getString("sounds.fall");

        BlockSounds sounds = new BlockSounds();
        sounds.setPlaceSound(placeSound);
        sounds.setStepSound(stepSound);
        sounds.setBreakSound(breakSound);
        sounds.setHitSound(hitSound);
        sounds.setFallSound(fallSound);
        sounds.setDefaultSounds(Material.STONE.createBlockData().getSoundGroup());

        MaurisLootTable lootTable = MaurisLootTable.getLootTableByName(decorationSection.getString("lootTable"));
        lootTable = lootTable == null ? MaurisLootTable.fromConfigSection(decorationSection.getConfigurationSection("lootTable")) : lootTable;


        MaurisDecorationInfo info = new MaurisDecorationInfo(modelItem, canWalkThrough, selfDrop, gravity, sounds, lootTable, small);
        mb.setDecorationEnabled(true).setDecorationInfo(info);
    }

    //Actual place
    private void place(Block block, Vector vector){
        MaurisItem model = info.getMaurisItem();
        if(model == null){
            throw new NullArgumentException("Unable to place " + getFullName() + " because model does not exists");
        }

        Location location = block.getLocation();
        location.add(0.5, 0, 0.5);
        location.setDirection(vector);

        ArmorStand stand = (ArmorStand) block.getLocation().getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        EntityDataContainerUtil.setData(stand, "key", getFullName());
        stand.setGravity(info.isGravity());
        stand.setInvisible(true);
        stand.setCollidable(false);
        stand.setPersistent(true);
        stand.setSilent(true);
        stand.setSmall(info.isSmall());
        stand.getEquipment().setHelmet(model.getAsItemStack());
        for (EquipmentSlot value : EquipmentSlot.values()) {
            stand.addEquipmentLock(value, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }

        if(!info.isCanWalkThrough() && !info.isGravity()){
            block.setType(Material.BARRIER);
            DataContainerUtil.setData(block, "key", getFullName());
        }

        info.getSounds().executePlaceSound(block.getLocation());
    }

    @Override
    public void place(Block block, Player player) {
        place(block, player.getLocation().getDirection().multiply(-1));
    }

    @Override
    public void place(Block block) {
        place(block, new Vector());
    }
}
