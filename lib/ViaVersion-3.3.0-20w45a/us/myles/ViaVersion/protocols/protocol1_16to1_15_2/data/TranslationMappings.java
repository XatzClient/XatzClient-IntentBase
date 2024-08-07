// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.data;

import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.gson.JsonElement;
import java.util.HashMap;
import us.myles.ViaVersion.api.protocol.Protocol;
import java.util.Map;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;

public class TranslationMappings extends ComponentRewriter
{
    private final Map<String, String> mappings;
    
    public TranslationMappings(final Protocol protocol) {
        super(protocol);
        (this.mappings = new HashMap<String, String>()).put("block.minecraft.flowing_water", "Flowing Water");
        this.mappings.put("block.minecraft.flowing_lava", "Flowing Lava");
        this.mappings.put("block.minecraft.bed", "Bed");
        this.mappings.put("block.minecraft.bed.not_valid", "Your home bed was missing or obstructed");
        this.mappings.put("block.minecraft.bed.set_spawn", "Respawn point set");
        this.mappings.put("block.minecraft.two_turtle_eggs", "Two Turtle Eggs");
        this.mappings.put("block.minecraft.three_turtle_eggs", "Three Turtle Eggs");
        this.mappings.put("block.minecraft.four_turtle_eggs", "Four Turtle Eggs");
        this.mappings.put("block.minecraft.banner", "Banner");
        this.mappings.put("block.minecraft.wall_banner", "Wall Banner");
        this.mappings.put("item.minecraft.zombie_pigman_spawn_egg", "Zombie Pigman Spawn Egg");
        this.mappings.put("item.minecraft.skeleton_skull", "Skeleton Skull");
        this.mappings.put("item.minecraft.wither_skeleton_skull", "Wither Skeleton Skull");
        this.mappings.put("item.minecraft.zombie_head", "Zombie Head");
        this.mappings.put("item.minecraft.creeper_head", "Creeper Head");
        this.mappings.put("item.minecraft.dragon_head", "Dragon Head");
        this.mappings.put("entity.minecraft.zombie_pigman", "Zombie Pigman");
        this.mappings.put("death.fell.accident.water", "%1$s fell out of the water");
        this.mappings.put("death.attack.netherBed.message", "%1$s was killed by %2$s");
        this.mappings.put("death.attack.netherBed.link", "Intentional Game Design");
        this.mappings.put("advancements.husbandry.break_diamond_hoe.title", "Serious Dedication");
        this.mappings.put("advancements.husbandry.break_diamond_hoe.description", "Completely use up a diamond hoe, and then reevaluate your life choices");
        this.mappings.put("biome.minecraft.nether", "Nether");
    }
    
    @Override
    public void processText(final JsonElement element) {
        super.processText(element);
        if (element == null || !element.isJsonObject()) {
            return;
        }
        final JsonObject object = element.getAsJsonObject();
        final JsonObject score = object.getAsJsonObject("score");
        if (score == null || object.has("text")) {
            return;
        }
        final JsonPrimitive value = score.getAsJsonPrimitive("value");
        if (value != null) {
            object.remove("score");
            object.add("text", value);
        }
    }
    
    @Override
    protected void handleTranslate(final JsonObject object, final String translate) {
        final String mappedTranslation = this.mappings.get(translate);
        if (mappedTranslation != null) {
            object.addProperty("translate", mappedTranslation);
        }
    }
}
