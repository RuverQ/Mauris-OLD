package com.ruverq.mauris.items.decorations;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.items.blocks.MaurisLoot;
import com.ruverq.mauris.items.blocks.MaurisLootTable;
import com.ruverq.mauris.items.blocks.blocksounds.BlockSounds;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class MaurisDecorationInfo {

    public MaurisItem getMaurisItem() {
        return ItemsLoader.getMaurisItem(maurisItem);
    }

    public String getMaurisItemName() {
        return maurisItem;
    }

    String maurisItem; // Model that will be placed
    boolean canWalkThrough;

    boolean selfDrop;

    boolean gravity;

    BlockSounds sounds;

    MaurisLootTable lootTable;

    boolean small;

}
