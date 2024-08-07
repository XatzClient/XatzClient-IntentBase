// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.rewriters;

import java.util.Iterator;
import us.myles.viaversion.libs.fastutil.ints.IntList;
import us.myles.viaversion.libs.fastutil.ints.IntArrayList;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import us.myles.ViaVersion.api.protocol.Protocol;

public class TagRewriter
{
    private static final int[] EMPTY_ARRAY;
    private final Protocol protocol;
    private final IdRewriteFunction entityRewriter;
    private final List<TagData> newBlockTags;
    private final List<TagData> newItemTags;
    private final List<TagData> newEntityTags;
    
    public TagRewriter(final Protocol protocol, @Nullable final IdRewriteFunction entityRewriter) {
        this.newBlockTags = new ArrayList<TagData>();
        this.newItemTags = new ArrayList<TagData>();
        this.newEntityTags = new ArrayList<TagData>();
        this.protocol = protocol;
        this.entityRewriter = entityRewriter;
    }
    
    public void addEmptyTag(final RegistryType tagType, final String id) {
        this.getNewTags(tagType).add(new TagData(id, TagRewriter.EMPTY_ARRAY));
    }
    
    public void addEmptyTags(final RegistryType tagType, final String... ids) {
        final List<TagData> tagList = this.getNewTags(tagType);
        for (final String id : ids) {
            tagList.add(new TagData(id, TagRewriter.EMPTY_ARRAY));
        }
    }
    
    public void addTag(final RegistryType tagType, final String id, final int... oldIds) {
        final List<TagData> newTags = this.getNewTags(tagType);
        final IdRewriteFunction rewriteFunction = this.getRewriter(tagType);
        for (int i = 0; i < oldIds.length; ++i) {
            final int oldId = oldIds[i];
            oldIds[i] = rewriteFunction.rewrite(oldId);
        }
        newTags.add(new TagData(id, oldIds));
    }
    
    public void register(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                final MappingData mappingData;
                final MappingData mappingData2;
                final MappingData mappingData3;
                int fluidTagsSize;
                int i;
                this.handler(wrapper -> {
                    mappingData = TagRewriter.this.protocol.getMappingData();
                    TagRewriter.this.handle(wrapper, id -> (mappingData2 != null) ? Integer.valueOf(mappingData2.getNewBlockId(id)) : null, TagRewriter.this.newBlockTags);
                    TagRewriter.this.handle(wrapper, id -> (mappingData3 != null) ? Integer.valueOf(mappingData3.getNewItemId(id)) : null, TagRewriter.this.newItemTags);
                    if (TagRewriter.this.entityRewriter != null || !TagRewriter.this.newEntityTags.isEmpty()) {
                        for (fluidTagsSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < fluidTagsSize; ++i) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        }
                        TagRewriter.this.handle(wrapper, TagRewriter.this.entityRewriter, TagRewriter.this.newEntityTags);
                    }
                });
            }
        });
    }
    
    private void handle(final PacketWrapper wrapper, final IdRewriteFunction rewriteFunction, final List<TagData> newTags) throws Exception {
        final int tagsSize = wrapper.read((Type<Integer>)Type.VAR_INT);
        wrapper.write(Type.VAR_INT, (newTags != null) ? (tagsSize + newTags.size()) : tagsSize);
        for (int i = 0; i < tagsSize; ++i) {
            wrapper.passthrough(Type.STRING);
            final int[] ids = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
            if (rewriteFunction != null) {
                final IntList idList = new IntArrayList(ids.length);
                for (final int id : ids) {
                    final int mappedId = rewriteFunction.rewrite(id);
                    if (mappedId != -1) {
                        idList.add(mappedId);
                    }
                }
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, idList.toArray(TagRewriter.EMPTY_ARRAY));
            }
            else {
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, ids);
            }
        }
        if (newTags != null && !newTags.isEmpty()) {
            for (final TagData tag : newTags) {
                wrapper.write(Type.STRING, tag.identifier);
                wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, tag.entries);
            }
        }
    }
    
    private List<TagData> getNewTags(final RegistryType tagType) {
        switch (tagType) {
            case BLOCK: {
                return this.newBlockTags;
            }
            case ITEM: {
                return this.newItemTags;
            }
            case ENTITY: {
                return this.newEntityTags;
            }
            default: {
                return null;
            }
        }
    }
    
    @Nullable
    private IdRewriteFunction getRewriter(final RegistryType tagType) {
        switch (tagType) {
            case BLOCK: {
                return (this.protocol.getMappingData().getBlockMappings() != null) ? (id -> this.protocol.getMappingData().getNewBlockId(id)) : null;
            }
            case ITEM: {
                return (this.protocol.getMappingData().getItemMappings() != null) ? (id -> this.protocol.getMappingData().getNewItemId(id)) : null;
            }
            case ENTITY: {
                return this.entityRewriter;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        EMPTY_ARRAY = new int[0];
    }
    
    private static final class TagData
    {
        private final String identifier;
        private final int[] entries;
        
        private TagData(final String identifier, final int[] entries) {
            this.identifier = identifier;
            this.entries = entries;
        }
    }
}
