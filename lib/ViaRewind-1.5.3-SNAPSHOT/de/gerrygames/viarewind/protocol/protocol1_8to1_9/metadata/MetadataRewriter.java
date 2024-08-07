// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata;

import java.util.Iterator;
import de.gerrygames.viarewind.ViaRewind;
import us.myles.ViaVersion.api.minecraft.EulerAngle;
import us.myles.ViaVersion.api.minecraft.Vector;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.metadata.MetaIndex;
import java.util.UUID;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import java.util.Collection;
import java.util.ArrayList;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.entities.Entity1_10Types;

public class MetadataRewriter
{
    public static void transform(final Entity1_10Types.EntityType type, final List<Metadata> list) {
        for (final Metadata entry : new ArrayList<Metadata>(list)) {
            final MetaIndex metaIndex = MetaIndex1_8to1_9.searchIndex(type, entry.getId());
            try {
                if (metaIndex == null) {
                    throw new Exception("Could not find valid metadata");
                }
                if (metaIndex.getOldType() == MetaType1_8.NonExistent || metaIndex.getNewType() == MetaType1_9.Discontinued) {
                    list.remove(entry);
                }
                else {
                    final Object value = entry.getValue();
                    entry.setMetaType((MetaType)metaIndex.getOldType());
                    entry.setId(metaIndex.getIndex());
                    switch (metaIndex.getNewType()) {
                        case Byte: {
                            if (metaIndex.getOldType() == MetaType1_8.Byte) {
                                entry.setValue(value);
                            }
                            if (metaIndex.getOldType() == MetaType1_8.Int) {
                                entry.setValue((Object)(int)value);
                                break;
                            }
                            break;
                        }
                        case OptUUID: {
                            if (metaIndex.getOldType() != MetaType1_8.String) {
                                list.remove(entry);
                                break;
                            }
                            final UUID owner = (UUID)value;
                            if (owner == null) {
                                entry.setValue((Object)"");
                                break;
                            }
                            entry.setValue((Object)owner.toString());
                            break;
                        }
                        case BlockID: {
                            list.remove(entry);
                            list.add(new Metadata(metaIndex.getIndex(), (MetaType)MetaType1_8.Short, (Object)((Integer)value).shortValue()));
                            break;
                        }
                        case VarInt: {
                            if (metaIndex.getOldType() == MetaType1_8.Byte) {
                                entry.setValue((Object)((Integer)value).byteValue());
                            }
                            if (metaIndex.getOldType() == MetaType1_8.Short) {
                                entry.setValue((Object)((Integer)value).shortValue());
                            }
                            if (metaIndex.getOldType() == MetaType1_8.Int) {
                                entry.setValue(value);
                                break;
                            }
                            break;
                        }
                        case Float: {
                            entry.setValue(value);
                            break;
                        }
                        case String: {
                            entry.setValue(value);
                            break;
                        }
                        case Boolean: {
                            if (metaIndex == MetaIndex.AGEABLE_AGE) {
                                entry.setValue((Object)(byte)(value ? -1 : 0));
                                break;
                            }
                            entry.setValue((Object)(byte)(((boolean)value) ? 1 : 0));
                            break;
                        }
                        case Slot: {
                            entry.setValue((Object)ItemRewriter.toClient((Item)value));
                            break;
                        }
                        case Position: {
                            final Vector vector = (Vector)value;
                            entry.setValue((Object)vector);
                            break;
                        }
                        case Vector3F: {
                            final EulerAngle angle = (EulerAngle)value;
                            entry.setValue((Object)angle);
                            break;
                        }
                        case Chat: {
                            entry.setValue(value);
                            break;
                        }
                        default: {
                            ViaRewind.getPlatform().getLogger().warning("[Out] Unhandled MetaDataType: " + metaIndex.getNewType());
                            list.remove(entry);
                            break;
                        }
                    }
                    if (metaIndex.getOldType().getType().getOutputClass().isAssignableFrom(entry.getValue().getClass())) {
                        continue;
                    }
                    list.remove(entry);
                }
            }
            catch (Exception e) {
                list.remove(entry);
            }
        }
    }
}
