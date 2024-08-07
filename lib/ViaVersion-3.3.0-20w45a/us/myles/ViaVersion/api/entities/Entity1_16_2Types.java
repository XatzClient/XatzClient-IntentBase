// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import us.myles.ViaVersion.api.Via;

public class Entity1_16_2Types
{
    public static EntityType getTypeFromId(final int typeID) {
        final Optional<EntityType> type = EntityType.findById(typeID);
        if (!type.isPresent()) {
            Via.getPlatform().getLogger().severe("Could not find 1.16.2 type id " + typeID);
            return EntityType.ENTITY;
        }
        return type.get();
    }
    
    public enum EntityType implements us.myles.ViaVersion.api.entities.EntityType
    {
        ENTITY(-1), 
        AREA_EFFECT_CLOUD(0, EntityType.ENTITY), 
        END_CRYSTAL(18, EntityType.ENTITY), 
        EVOKER_FANGS(23, EntityType.ENTITY), 
        EXPERIENCE_ORB(24, EntityType.ENTITY), 
        EYE_OF_ENDER(25, EntityType.ENTITY), 
        FALLING_BLOCK(26, EntityType.ENTITY), 
        FIREWORK_ROCKET(27, EntityType.ENTITY), 
        ITEM(37, EntityType.ENTITY), 
        LLAMA_SPIT(43, EntityType.ENTITY), 
        TNT(64, EntityType.ENTITY), 
        SHULKER_BULLET(71, EntityType.ENTITY), 
        FISHING_BOBBER(107, EntityType.ENTITY), 
        LIVINGENTITY(-1, EntityType.ENTITY), 
        ARMOR_STAND(1, EntityType.LIVINGENTITY), 
        PLAYER(106, EntityType.LIVINGENTITY), 
        ABSTRACT_INSENTIENT(-1, EntityType.LIVINGENTITY), 
        ENDER_DRAGON(19, EntityType.ABSTRACT_INSENTIENT), 
        BEE(4, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_CREATURE(-1, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_AGEABLE(-1, EntityType.ABSTRACT_CREATURE), 
        VILLAGER(93, EntityType.ABSTRACT_AGEABLE), 
        WANDERING_TRADER(95, EntityType.ABSTRACT_AGEABLE), 
        ABSTRACT_ANIMAL(-1, EntityType.ABSTRACT_AGEABLE), 
        DOLPHIN(13, EntityType.ABSTRACT_INSENTIENT), 
        CHICKEN(9, EntityType.ABSTRACT_ANIMAL), 
        COW(11, EntityType.ABSTRACT_ANIMAL), 
        MOOSHROOM(53, EntityType.COW), 
        PANDA(56, EntityType.ABSTRACT_INSENTIENT), 
        PIG(59, EntityType.ABSTRACT_ANIMAL), 
        POLAR_BEAR(63, EntityType.ABSTRACT_ANIMAL), 
        RABBIT(66, EntityType.ABSTRACT_ANIMAL), 
        SHEEP(69, EntityType.ABSTRACT_ANIMAL), 
        TURTLE(91, EntityType.ABSTRACT_ANIMAL), 
        FOX(28, EntityType.ABSTRACT_ANIMAL), 
        ABSTRACT_TAMEABLE_ANIMAL(-1, EntityType.ABSTRACT_ANIMAL), 
        CAT(7, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        OCELOT(54, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        WOLF(100, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        ABSTRACT_PARROT(-1, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        PARROT(57, EntityType.ABSTRACT_PARROT), 
        ABSTRACT_HORSE(-1, EntityType.ABSTRACT_ANIMAL), 
        CHESTED_HORSE(-1, EntityType.ABSTRACT_HORSE), 
        DONKEY(14, EntityType.CHESTED_HORSE), 
        MULE(52, EntityType.CHESTED_HORSE), 
        LLAMA(42, EntityType.CHESTED_HORSE), 
        TRADER_LLAMA(89, EntityType.CHESTED_HORSE), 
        HORSE(33, EntityType.ABSTRACT_HORSE), 
        SKELETON_HORSE(74, EntityType.ABSTRACT_HORSE), 
        ZOMBIE_HORSE(103, EntityType.ABSTRACT_HORSE), 
        ABSTRACT_GOLEM(-1, EntityType.ABSTRACT_CREATURE), 
        SNOW_GOLEM(77, EntityType.ABSTRACT_GOLEM), 
        IRON_GOLEM(36, EntityType.ABSTRACT_GOLEM), 
        SHULKER(70, EntityType.ABSTRACT_GOLEM), 
        ABSTRACT_FISHES(-1, EntityType.ABSTRACT_CREATURE), 
        COD(10, EntityType.ABSTRACT_FISHES), 
        PUFFERFISH(65, EntityType.ABSTRACT_FISHES), 
        SALMON(68, EntityType.ABSTRACT_FISHES), 
        TROPICAL_FISH(90, EntityType.ABSTRACT_FISHES), 
        ABSTRACT_MONSTER(-1, EntityType.ABSTRACT_CREATURE), 
        BLAZE(5, EntityType.ABSTRACT_MONSTER), 
        CREEPER(12, EntityType.ABSTRACT_MONSTER), 
        ENDERMITE(21, EntityType.ABSTRACT_MONSTER), 
        ENDERMAN(20, EntityType.ABSTRACT_MONSTER), 
        GIANT(30, EntityType.ABSTRACT_MONSTER), 
        SILVERFISH(72, EntityType.ABSTRACT_MONSTER), 
        VEX(92, EntityType.ABSTRACT_MONSTER), 
        WITCH(96, EntityType.ABSTRACT_MONSTER), 
        WITHER(97, EntityType.ABSTRACT_MONSTER), 
        RAVAGER(67, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_PIGLIN(-1, EntityType.ABSTRACT_MONSTER), 
        PIGLIN(60, EntityType.ABSTRACT_PIGLIN), 
        PIGLIN_BRUTE(61, EntityType.ABSTRACT_PIGLIN), 
        HOGLIN(32, EntityType.ABSTRACT_ANIMAL), 
        STRIDER(83, EntityType.ABSTRACT_ANIMAL), 
        ZOGLIN(101, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_ILLAGER_BASE(-1, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_EVO_ILLU_ILLAGER(-1, EntityType.ABSTRACT_ILLAGER_BASE), 
        EVOKER(22, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        ILLUSIONER(35, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        VINDICATOR(94, EntityType.ABSTRACT_ILLAGER_BASE), 
        PILLAGER(62, EntityType.ABSTRACT_ILLAGER_BASE), 
        ABSTRACT_SKELETON(-1, EntityType.ABSTRACT_MONSTER), 
        SKELETON(73, EntityType.ABSTRACT_SKELETON), 
        STRAY(82, EntityType.ABSTRACT_SKELETON), 
        WITHER_SKELETON(98, EntityType.ABSTRACT_SKELETON), 
        GUARDIAN(31, EntityType.ABSTRACT_MONSTER), 
        ELDER_GUARDIAN(17, EntityType.GUARDIAN), 
        SPIDER(80, EntityType.ABSTRACT_MONSTER), 
        CAVE_SPIDER(8, EntityType.SPIDER), 
        ZOMBIE(102, EntityType.ABSTRACT_MONSTER), 
        DROWNED(16, EntityType.ZOMBIE), 
        HUSK(34, EntityType.ZOMBIE), 
        ZOMBIFIED_PIGLIN(105, EntityType.ZOMBIE), 
        ZOMBIE_VILLAGER(104, EntityType.ZOMBIE), 
        ABSTRACT_FLYING(-1, EntityType.ABSTRACT_INSENTIENT), 
        GHAST(29, EntityType.ABSTRACT_FLYING), 
        PHANTOM(58, EntityType.ABSTRACT_FLYING), 
        ABSTRACT_AMBIENT(-1, EntityType.ABSTRACT_INSENTIENT), 
        BAT(3, EntityType.ABSTRACT_AMBIENT), 
        ABSTRACT_WATERMOB(-1, EntityType.ABSTRACT_INSENTIENT), 
        SQUID(81, EntityType.ABSTRACT_WATERMOB), 
        SLIME(75, EntityType.ABSTRACT_INSENTIENT), 
        MAGMA_CUBE(44, EntityType.SLIME), 
        ABSTRACT_HANGING(-1, EntityType.ENTITY), 
        LEASH_KNOT(40, EntityType.ABSTRACT_HANGING), 
        ITEM_FRAME(38, EntityType.ABSTRACT_HANGING), 
        PAINTING(55, EntityType.ABSTRACT_HANGING), 
        ABSTRACT_LIGHTNING(-1, EntityType.ENTITY), 
        LIGHTNING_BOLT(41, EntityType.ABSTRACT_LIGHTNING), 
        ABSTRACT_ARROW(-1, EntityType.ENTITY), 
        ARROW(2, EntityType.ABSTRACT_ARROW), 
        SPECTRAL_ARROW(79, EntityType.ABSTRACT_ARROW), 
        TRIDENT(88, EntityType.ABSTRACT_ARROW), 
        ABSTRACT_FIREBALL(-1, EntityType.ENTITY), 
        DRAGON_FIREBALL(15, EntityType.ABSTRACT_FIREBALL), 
        FIREBALL(39, EntityType.ABSTRACT_FIREBALL), 
        SMALL_FIREBALL(76, EntityType.ABSTRACT_FIREBALL), 
        WITHER_SKULL(99, EntityType.ABSTRACT_FIREBALL), 
        PROJECTILE_ABSTRACT(-1, EntityType.ENTITY), 
        SNOWBALL(78, EntityType.PROJECTILE_ABSTRACT), 
        ENDER_PEARL(85, EntityType.PROJECTILE_ABSTRACT), 
        EGG(84, EntityType.PROJECTILE_ABSTRACT), 
        POTION(87, EntityType.PROJECTILE_ABSTRACT), 
        EXPERIENCE_BOTTLE(86, EntityType.PROJECTILE_ABSTRACT), 
        MINECART_ABSTRACT(-1, EntityType.ENTITY), 
        CHESTED_MINECART_ABSTRACT(-1, EntityType.MINECART_ABSTRACT), 
        CHEST_MINECART(46, EntityType.CHESTED_MINECART_ABSTRACT), 
        HOPPER_MINECART(49, EntityType.CHESTED_MINECART_ABSTRACT), 
        MINECART(45, EntityType.MINECART_ABSTRACT), 
        FURNACE_MINECART(48, EntityType.MINECART_ABSTRACT), 
        COMMAND_BLOCK_MINECART(47, EntityType.MINECART_ABSTRACT), 
        TNT_MINECART(51, EntityType.MINECART_ABSTRACT), 
        SPAWNER_MINECART(50, EntityType.MINECART_ABSTRACT), 
        BOAT(6, EntityType.ENTITY);
        
        private static final Map<Integer, EntityType> TYPES;
        private final int id;
        private final EntityType parent;
        
        private EntityType(final int id) {
            this.id = id;
            this.parent = null;
        }
        
        private EntityType(final int id, final EntityType parent) {
            this.id = id;
            this.parent = parent;
        }
        
        @Override
        public int getId() {
            return this.id;
        }
        
        @Override
        public EntityType getParent() {
            return this.parent;
        }
        
        public static Optional<EntityType> findById(final int id) {
            if (id == -1) {
                return Optional.empty();
            }
            return Optional.ofNullable(EntityType.TYPES.get(id));
        }
        
        static {
            TYPES = new HashMap<Integer, EntityType>();
            for (final EntityType type : values()) {
                EntityType.TYPES.put(type.id, type);
            }
        }
    }
}
