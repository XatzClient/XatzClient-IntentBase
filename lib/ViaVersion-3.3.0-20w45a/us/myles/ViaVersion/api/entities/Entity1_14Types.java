// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import us.myles.ViaVersion.api.Via;

public class Entity1_14Types
{
    public static EntityType getTypeFromId(final int typeID) {
        final Optional<EntityType> type = EntityType.findById(typeID);
        if (!type.isPresent()) {
            Via.getPlatform().getLogger().severe("Could not find 1.14 type id " + typeID);
            return EntityType.ENTITY;
        }
        return type.get();
    }
    
    public enum EntityType implements us.myles.ViaVersion.api.entities.EntityType
    {
        ENTITY(-1), 
        AREA_EFFECT_CLOUD(0, EntityType.ENTITY), 
        END_CRYSTAL(17, EntityType.ENTITY), 
        EVOKER_FANGS(21, EntityType.ENTITY), 
        EXPERIENCE_ORB(23, EntityType.ENTITY), 
        EYE_OF_ENDER(24, EntityType.ENTITY), 
        FALLING_BLOCK(25, EntityType.ENTITY), 
        FIREWORK_ROCKET(26, EntityType.ENTITY), 
        ITEM(34, EntityType.ENTITY), 
        LLAMA_SPIT(39, EntityType.ENTITY), 
        TNT(58, EntityType.ENTITY), 
        SHULKER_BULLET(63, EntityType.ENTITY), 
        FISHING_BOBBER(101, EntityType.ENTITY), 
        LIVINGENTITY(-1, EntityType.ENTITY), 
        ARMOR_STAND(1, EntityType.LIVINGENTITY), 
        PLAYER(100, EntityType.LIVINGENTITY), 
        ABSTRACT_INSENTIENT(-1, EntityType.LIVINGENTITY), 
        ENDER_DRAGON(18, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_CREATURE(-1, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_AGEABLE(-1, EntityType.ABSTRACT_CREATURE), 
        VILLAGER(84, EntityType.ABSTRACT_AGEABLE), 
        WANDERING_TRADER(88, EntityType.ABSTRACT_AGEABLE), 
        ABSTRACT_ANIMAL(-1, EntityType.ABSTRACT_AGEABLE), 
        DOLPHIN(13, EntityType.ABSTRACT_INSENTIENT), 
        CHICKEN(8, EntityType.ABSTRACT_ANIMAL), 
        COW(10, EntityType.ABSTRACT_ANIMAL), 
        MOOSHROOM(49, EntityType.COW), 
        PANDA(52, EntityType.ABSTRACT_INSENTIENT), 
        PIG(54, EntityType.ABSTRACT_ANIMAL), 
        POLAR_BEAR(57, EntityType.ABSTRACT_ANIMAL), 
        RABBIT(59, EntityType.ABSTRACT_ANIMAL), 
        SHEEP(61, EntityType.ABSTRACT_ANIMAL), 
        TURTLE(77, EntityType.ABSTRACT_ANIMAL), 
        FOX(27, EntityType.ABSTRACT_ANIMAL), 
        ABSTRACT_TAMEABLE_ANIMAL(-1, EntityType.ABSTRACT_ANIMAL), 
        CAT(6, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        OCELOT(50, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        WOLF(93, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        ABSTRACT_PARROT(-1, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        PARROT(53, EntityType.ABSTRACT_PARROT), 
        ABSTRACT_HORSE(-1, EntityType.ABSTRACT_ANIMAL), 
        CHESTED_HORSE(-1, EntityType.ABSTRACT_HORSE), 
        DONKEY(12, EntityType.CHESTED_HORSE), 
        MULE(48, EntityType.CHESTED_HORSE), 
        LLAMA(38, EntityType.CHESTED_HORSE), 
        TRADER_LLAMA(75, EntityType.CHESTED_HORSE), 
        HORSE(31, EntityType.ABSTRACT_HORSE), 
        SKELETON_HORSE(66, EntityType.ABSTRACT_HORSE), 
        ZOMBIE_HORSE(95, EntityType.ABSTRACT_HORSE), 
        ABSTRACT_GOLEM(-1, EntityType.ABSTRACT_CREATURE), 
        SNOW_GOLEM(69, EntityType.ABSTRACT_GOLEM), 
        IRON_GOLEM(85, EntityType.ABSTRACT_GOLEM), 
        SHULKER(62, EntityType.ABSTRACT_GOLEM), 
        ABSTRACT_FISHES(-1, EntityType.ABSTRACT_CREATURE), 
        COD(9, EntityType.ABSTRACT_FISHES), 
        PUFFERFISH(55, EntityType.ABSTRACT_FISHES), 
        SALMON(60, EntityType.ABSTRACT_FISHES), 
        TROPICAL_FISH(76, EntityType.ABSTRACT_FISHES), 
        ABSTRACT_MONSTER(-1, EntityType.ABSTRACT_CREATURE), 
        BLAZE(4, EntityType.ABSTRACT_MONSTER), 
        CREEPER(11, EntityType.ABSTRACT_MONSTER), 
        ENDERMITE(20, EntityType.ABSTRACT_MONSTER), 
        ENDERMAN(19, EntityType.ABSTRACT_MONSTER), 
        GIANT(29, EntityType.ABSTRACT_MONSTER), 
        SILVERFISH(64, EntityType.ABSTRACT_MONSTER), 
        VEX(83, EntityType.ABSTRACT_MONSTER), 
        WITCH(89, EntityType.ABSTRACT_MONSTER), 
        WITHER(90, EntityType.ABSTRACT_MONSTER), 
        RAVAGER(98, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_ILLAGER_BASE(-1, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_EVO_ILLU_ILLAGER(-1, EntityType.ABSTRACT_ILLAGER_BASE), 
        EVOKER(22, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        ILLUSIONER(33, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        VINDICATOR(86, EntityType.ABSTRACT_ILLAGER_BASE), 
        PILLAGER(87, EntityType.ABSTRACT_ILLAGER_BASE), 
        ABSTRACT_SKELETON(-1, EntityType.ABSTRACT_MONSTER), 
        SKELETON(65, EntityType.ABSTRACT_SKELETON), 
        STRAY(74, EntityType.ABSTRACT_SKELETON), 
        WITHER_SKELETON(91, EntityType.ABSTRACT_SKELETON), 
        GUARDIAN(30, EntityType.ABSTRACT_MONSTER), 
        ELDER_GUARDIAN(16, EntityType.GUARDIAN), 
        SPIDER(72, EntityType.ABSTRACT_MONSTER), 
        CAVE_SPIDER(7, EntityType.SPIDER), 
        ZOMBIE(94, EntityType.ABSTRACT_MONSTER), 
        DROWNED(15, EntityType.ZOMBIE), 
        HUSK(32, EntityType.ZOMBIE), 
        ZOMBIE_PIGMAN(56, EntityType.ZOMBIE), 
        ZOMBIE_VILLAGER(96, EntityType.ZOMBIE), 
        ABSTRACT_FLYING(-1, EntityType.ABSTRACT_INSENTIENT), 
        GHAST(28, EntityType.ABSTRACT_FLYING), 
        PHANTOM(97, EntityType.ABSTRACT_FLYING), 
        ABSTRACT_AMBIENT(-1, EntityType.ABSTRACT_INSENTIENT), 
        BAT(3, EntityType.ABSTRACT_AMBIENT), 
        ABSTRACT_WATERMOB(-1, EntityType.ABSTRACT_INSENTIENT), 
        SQUID(73, EntityType.ABSTRACT_WATERMOB), 
        SLIME(67, EntityType.ABSTRACT_INSENTIENT), 
        MAGMA_CUBE(40, EntityType.SLIME), 
        ABSTRACT_HANGING(-1, EntityType.ENTITY), 
        LEASH_KNOT(37, EntityType.ABSTRACT_HANGING), 
        ITEM_FRAME(35, EntityType.ABSTRACT_HANGING), 
        PAINTING(51, EntityType.ABSTRACT_HANGING), 
        ABSTRACT_LIGHTNING(-1, EntityType.ENTITY), 
        LIGHTNING_BOLT(99, EntityType.ABSTRACT_LIGHTNING), 
        ABSTRACT_ARROW(-1, EntityType.ENTITY), 
        ARROW(2, EntityType.ABSTRACT_ARROW), 
        SPECTRAL_ARROW(71, EntityType.ABSTRACT_ARROW), 
        TRIDENT(82, EntityType.ABSTRACT_ARROW), 
        ABSTRACT_FIREBALL(-1, EntityType.ENTITY), 
        DRAGON_FIREBALL(14, EntityType.ABSTRACT_FIREBALL), 
        FIREBALL(36, EntityType.ABSTRACT_FIREBALL), 
        SMALL_FIREBALL(68, EntityType.ABSTRACT_FIREBALL), 
        WITHER_SKULL(92, EntityType.ABSTRACT_FIREBALL), 
        PROJECTILE_ABSTRACT(-1, EntityType.ENTITY), 
        SNOWBALL(70, EntityType.PROJECTILE_ABSTRACT), 
        ENDER_PEARL(79, EntityType.PROJECTILE_ABSTRACT), 
        EGG(78, EntityType.PROJECTILE_ABSTRACT), 
        POTION(81, EntityType.PROJECTILE_ABSTRACT), 
        EXPERIENCE_BOTTLE(80, EntityType.PROJECTILE_ABSTRACT), 
        MINECART_ABSTRACT(-1, EntityType.ENTITY), 
        CHESTED_MINECART_ABSTRACT(-1, EntityType.MINECART_ABSTRACT), 
        CHEST_MINECART(42, EntityType.CHESTED_MINECART_ABSTRACT), 
        HOPPER_MINECART(45, EntityType.CHESTED_MINECART_ABSTRACT), 
        MINECART(41, EntityType.MINECART_ABSTRACT), 
        FURNACE_MINECART(44, EntityType.MINECART_ABSTRACT), 
        COMMAND_BLOCK_MINECART(43, EntityType.MINECART_ABSTRACT), 
        TNT_MINECART(47, EntityType.MINECART_ABSTRACT), 
        SPAWNER_MINECART(46, EntityType.MINECART_ABSTRACT), 
        BOAT(5, EntityType.ENTITY);
        
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
