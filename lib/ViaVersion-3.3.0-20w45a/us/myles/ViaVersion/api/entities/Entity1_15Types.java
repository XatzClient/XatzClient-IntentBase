// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import us.myles.ViaVersion.api.Via;

public class Entity1_15Types
{
    public static EntityType getTypeFromId(final int typeID) {
        final Optional<EntityType> type = EntityType.findById(typeID);
        if (!type.isPresent()) {
            Via.getPlatform().getLogger().severe("Could not find 1.15 type id " + typeID);
            return EntityType.ENTITY;
        }
        return type.get();
    }
    
    public enum EntityType implements us.myles.ViaVersion.api.entities.EntityType
    {
        ENTITY(-1), 
        AREA_EFFECT_CLOUD(0, EntityType.ENTITY), 
        END_CRYSTAL(18, EntityType.ENTITY), 
        EVOKER_FANGS(22, EntityType.ENTITY), 
        EXPERIENCE_ORB(24, EntityType.ENTITY), 
        EYE_OF_ENDER(25, EntityType.ENTITY), 
        FALLING_BLOCK(26, EntityType.ENTITY), 
        FIREWORK_ROCKET(27, EntityType.ENTITY), 
        ITEM(35, EntityType.ENTITY), 
        LLAMA_SPIT(40, EntityType.ENTITY), 
        TNT(59, EntityType.ENTITY), 
        SHULKER_BULLET(64, EntityType.ENTITY), 
        FISHING_BOBBER(102, EntityType.ENTITY), 
        LIVINGENTITY(-1, EntityType.ENTITY), 
        ARMOR_STAND(1, EntityType.LIVINGENTITY), 
        PLAYER(101, EntityType.LIVINGENTITY), 
        ABSTRACT_INSENTIENT(-1, EntityType.LIVINGENTITY), 
        ENDER_DRAGON(19, EntityType.ABSTRACT_INSENTIENT), 
        BEE(4, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_CREATURE(-1, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_AGEABLE(-1, EntityType.ABSTRACT_CREATURE), 
        VILLAGER(85, EntityType.ABSTRACT_AGEABLE), 
        WANDERING_TRADER(89, EntityType.ABSTRACT_AGEABLE), 
        ABSTRACT_ANIMAL(-1, EntityType.ABSTRACT_AGEABLE), 
        DOLPHIN(14, EntityType.ABSTRACT_INSENTIENT), 
        CHICKEN(9, EntityType.ABSTRACT_ANIMAL), 
        COW(11, EntityType.ABSTRACT_ANIMAL), 
        MOOSHROOM(50, EntityType.COW), 
        PANDA(53, EntityType.ABSTRACT_INSENTIENT), 
        PIG(55, EntityType.ABSTRACT_ANIMAL), 
        POLAR_BEAR(58, EntityType.ABSTRACT_ANIMAL), 
        RABBIT(60, EntityType.ABSTRACT_ANIMAL), 
        SHEEP(62, EntityType.ABSTRACT_ANIMAL), 
        TURTLE(78, EntityType.ABSTRACT_ANIMAL), 
        FOX(28, EntityType.ABSTRACT_ANIMAL), 
        ABSTRACT_TAMEABLE_ANIMAL(-1, EntityType.ABSTRACT_ANIMAL), 
        CAT(7, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        OCELOT(51, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        WOLF(94, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        ABSTRACT_PARROT(-1, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        PARROT(54, EntityType.ABSTRACT_PARROT), 
        ABSTRACT_HORSE(-1, EntityType.ABSTRACT_ANIMAL), 
        CHESTED_HORSE(-1, EntityType.ABSTRACT_HORSE), 
        DONKEY(13, EntityType.CHESTED_HORSE), 
        MULE(49, EntityType.CHESTED_HORSE), 
        LLAMA(39, EntityType.CHESTED_HORSE), 
        TRADER_LLAMA(76, EntityType.CHESTED_HORSE), 
        HORSE(32, EntityType.ABSTRACT_HORSE), 
        SKELETON_HORSE(67, EntityType.ABSTRACT_HORSE), 
        ZOMBIE_HORSE(96, EntityType.ABSTRACT_HORSE), 
        ABSTRACT_GOLEM(-1, EntityType.ABSTRACT_CREATURE), 
        SNOW_GOLEM(70, EntityType.ABSTRACT_GOLEM), 
        IRON_GOLEM(86, EntityType.ABSTRACT_GOLEM), 
        SHULKER(63, EntityType.ABSTRACT_GOLEM), 
        ABSTRACT_FISHES(-1, EntityType.ABSTRACT_CREATURE), 
        COD(10, EntityType.ABSTRACT_FISHES), 
        PUFFERFISH(56, EntityType.ABSTRACT_FISHES), 
        SALMON(61, EntityType.ABSTRACT_FISHES), 
        TROPICAL_FISH(77, EntityType.ABSTRACT_FISHES), 
        ABSTRACT_MONSTER(-1, EntityType.ABSTRACT_CREATURE), 
        BLAZE(5, EntityType.ABSTRACT_MONSTER), 
        CREEPER(12, EntityType.ABSTRACT_MONSTER), 
        ENDERMITE(21, EntityType.ABSTRACT_MONSTER), 
        ENDERMAN(20, EntityType.ABSTRACT_MONSTER), 
        GIANT(30, EntityType.ABSTRACT_MONSTER), 
        SILVERFISH(65, EntityType.ABSTRACT_MONSTER), 
        VEX(84, EntityType.ABSTRACT_MONSTER), 
        WITCH(90, EntityType.ABSTRACT_MONSTER), 
        WITHER(91, EntityType.ABSTRACT_MONSTER), 
        RAVAGER(99, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_ILLAGER_BASE(-1, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_EVO_ILLU_ILLAGER(-1, EntityType.ABSTRACT_ILLAGER_BASE), 
        EVOKER(23, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        ILLUSIONER(34, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        VINDICATOR(87, EntityType.ABSTRACT_ILLAGER_BASE), 
        PILLAGER(88, EntityType.ABSTRACT_ILLAGER_BASE), 
        ABSTRACT_SKELETON(-1, EntityType.ABSTRACT_MONSTER), 
        SKELETON(66, EntityType.ABSTRACT_SKELETON), 
        STRAY(75, EntityType.ABSTRACT_SKELETON), 
        WITHER_SKELETON(92, EntityType.ABSTRACT_SKELETON), 
        GUARDIAN(31, EntityType.ABSTRACT_MONSTER), 
        ELDER_GUARDIAN(17, EntityType.GUARDIAN), 
        SPIDER(73, EntityType.ABSTRACT_MONSTER), 
        CAVE_SPIDER(8, EntityType.SPIDER), 
        ZOMBIE(95, EntityType.ABSTRACT_MONSTER), 
        DROWNED(16, EntityType.ZOMBIE), 
        HUSK(33, EntityType.ZOMBIE), 
        ZOMBIE_PIGMAN(57, EntityType.ZOMBIE), 
        ZOMBIE_VILLAGER(97, EntityType.ZOMBIE), 
        ABSTRACT_FLYING(-1, EntityType.ABSTRACT_INSENTIENT), 
        GHAST(29, EntityType.ABSTRACT_FLYING), 
        PHANTOM(98, EntityType.ABSTRACT_FLYING), 
        ABSTRACT_AMBIENT(-1, EntityType.ABSTRACT_INSENTIENT), 
        BAT(3, EntityType.ABSTRACT_AMBIENT), 
        ABSTRACT_WATERMOB(-1, EntityType.ABSTRACT_INSENTIENT), 
        SQUID(74, EntityType.ABSTRACT_WATERMOB), 
        SLIME(68, EntityType.ABSTRACT_INSENTIENT), 
        MAGMA_CUBE(41, EntityType.SLIME), 
        ABSTRACT_HANGING(-1, EntityType.ENTITY), 
        LEASH_KNOT(38, EntityType.ABSTRACT_HANGING), 
        ITEM_FRAME(36, EntityType.ABSTRACT_HANGING), 
        PAINTING(52, EntityType.ABSTRACT_HANGING), 
        ABSTRACT_LIGHTNING(-1, EntityType.ENTITY), 
        LIGHTNING_BOLT(100, EntityType.ABSTRACT_LIGHTNING), 
        ABSTRACT_ARROW(-1, EntityType.ENTITY), 
        ARROW(2, EntityType.ABSTRACT_ARROW), 
        SPECTRAL_ARROW(72, EntityType.ABSTRACT_ARROW), 
        TRIDENT(83, EntityType.ABSTRACT_ARROW), 
        ABSTRACT_FIREBALL(-1, EntityType.ENTITY), 
        DRAGON_FIREBALL(15, EntityType.ABSTRACT_FIREBALL), 
        FIREBALL(37, EntityType.ABSTRACT_FIREBALL), 
        SMALL_FIREBALL(69, EntityType.ABSTRACT_FIREBALL), 
        WITHER_SKULL(93, EntityType.ABSTRACT_FIREBALL), 
        PROJECTILE_ABSTRACT(-1, EntityType.ENTITY), 
        SNOWBALL(71, EntityType.PROJECTILE_ABSTRACT), 
        ENDER_PEARL(80, EntityType.PROJECTILE_ABSTRACT), 
        EGG(79, EntityType.PROJECTILE_ABSTRACT), 
        POTION(82, EntityType.PROJECTILE_ABSTRACT), 
        EXPERIENCE_BOTTLE(81, EntityType.PROJECTILE_ABSTRACT), 
        MINECART_ABSTRACT(-1, EntityType.ENTITY), 
        CHESTED_MINECART_ABSTRACT(-1, EntityType.MINECART_ABSTRACT), 
        CHEST_MINECART(43, EntityType.CHESTED_MINECART_ABSTRACT), 
        HOPPER_MINECART(46, EntityType.CHESTED_MINECART_ABSTRACT), 
        MINECART(42, EntityType.MINECART_ABSTRACT), 
        FURNACE_MINECART(45, EntityType.MINECART_ABSTRACT), 
        COMMAND_BLOCK_MINECART(44, EntityType.MINECART_ABSTRACT), 
        TNT_MINECART(48, EntityType.MINECART_ABSTRACT), 
        SPAWNER_MINECART(47, EntityType.MINECART_ABSTRACT), 
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
