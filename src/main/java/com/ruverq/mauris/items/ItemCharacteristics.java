package com.ruverq.mauris.items;

import com.ruverq.mauris.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ItemCharacteristics {

    Material material;
    String displayName;
    List<String> lore;

    HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    List<ItemFlag> itemFlags = new ArrayList<>();
    HashMap<Attribute, AttributeModifier> attributes = new HashMap<>();

    public void setMaterial(String material){
        if(material == null) return;

        this.material = Material.matchMaterial(material);
    }

    public void addAttribute(Attribute attribute, AttributeModifier attributeModifier){
        attributes.put(attribute, attributeModifier);
    }

    public void addEnchantment(Enchantment enchantment, int level){
        enchantments.put(enchantment, level);
    }

    public void addItemFlag(ItemFlag itemFlag){
        itemFlags.add(itemFlag);
    }

    public void addItemFlags(List<String> itemFlags){
        itemFlags.forEach(this::addItemFlag);
    }

    //FLAG_NAME
    public void addItemFlag(String itemFlag){
        ItemFlag flag = ItemFlag.valueOf(itemFlag);
        addItemFlag(flag);
    }

    public void addEnchantments(List<String> enchantments){
        enchantments.forEach(this::addEnchantment);
    }

    // ENCHANT_NAME LEVEL
    public void addEnchantment(String enchantS){
        if(enchantS == null || enchantS.isEmpty()) return;

        String[] splitted = enchantS.split(" ");
        String enchantName = splitted[0];

        Enchantment enchant = Enchantment.getByName(enchantName.toUpperCase());
        if(enchant == null) return;

        int level = 0;
        if(splitted.length > 1){
            String levelS = splitted[1];
            if(StringUtils.isNumeric(levelS)){
                level = Integer.parseInt(levelS);
            }
        }

        addEnchantment(enchant, level);
    }

    public void addAttributes(List<String> attributes){
        attributes.forEach(this::addAttribute);
    }

    //ATTRIBUTE_NAME AMOUNT OPERATION_TYPE
    public void addAttribute(String attributeS){
        if(attributeS == null || attributeS.isEmpty()) return;
        String[] splitted = attributeS.split(" ");

        String attributeName = splitted[0];
        Attribute attribute = Attribute.valueOf(attributeName.toUpperCase());

        int amount = 0;
        if(splitted.length > 1){
            String levelS = splitted[1];
            if(StringUtils.isNumeric(levelS)){
                amount = Integer.parseInt(levelS);
            }
        }

        AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
        if(splitted.length > 2){
            String operationS = splitted[2];
            operation = AttributeModifier.Operation.valueOf(operationS.toUpperCase());
        }

        AttributeModifier modifier = new AttributeModifier(attributeName, amount, operation);

        addAttribute(attribute, modifier);
    }

}
