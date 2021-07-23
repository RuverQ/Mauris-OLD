package com.ruverq.mauris.crafts;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Recipe;

import java.io.File;

public class MaurisRecipe {

    @Getter
    @Setter
    File file;

    @Getter
    @Setter
    String discoverBy;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    Recipe recipe;

}
