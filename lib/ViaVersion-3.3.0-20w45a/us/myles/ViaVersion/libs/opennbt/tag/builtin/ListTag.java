// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.opennbt.tag.builtin;

import java.io.DataOutput;
import us.myles.viaversion.libs.opennbt.tag.TagCreateException;
import java.io.IOException;
import us.myles.viaversion.libs.opennbt.tag.TagRegistry;
import java.io.DataInput;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ListTag extends Tag implements Iterable<Tag>
{
    private Class<? extends Tag> type;
    private List<Tag> value;
    
    public ListTag(final String name) {
        super(name);
        this.type = null;
        this.value = new ArrayList<Tag>();
    }
    
    public ListTag(final String name, final Class<? extends Tag> type) {
        this(name);
        this.type = type;
    }
    
    public ListTag(final String name, final List<Tag> value) throws IllegalArgumentException {
        this(name);
        this.setValue(value);
    }
    
    @Override
    public List<Tag> getValue() {
        return new ArrayList<Tag>(this.value);
    }
    
    public void setValue(final List<Tag> value) throws IllegalArgumentException {
        this.type = null;
        this.value.clear();
        for (final Tag tag : value) {
            this.add(tag);
        }
    }
    
    public Class<? extends Tag> getElementType() {
        return this.type;
    }
    
    public boolean add(final Tag tag) throws IllegalArgumentException {
        if (tag == null) {
            return false;
        }
        if (this.type == null) {
            this.type = tag.getClass();
        }
        else if (tag.getClass() != this.type) {
            throw new IllegalArgumentException("Tag type cannot differ from ListTag type.");
        }
        return this.value.add(tag);
    }
    
    public boolean remove(final Tag tag) {
        return this.value.remove(tag);
    }
    
    public <T extends Tag> T get(final int index) {
        return (T)this.value.get(index);
    }
    
    public int size() {
        return this.value.size();
    }
    
    @Override
    public Iterator<Tag> iterator() {
        return this.value.iterator();
    }
    
    @Override
    public void read(final DataInput in) throws IOException {
        this.type = null;
        this.value.clear();
        final int id = in.readUnsignedByte();
        if (id != 0) {
            this.type = TagRegistry.getClassFor(id);
            if (this.type == null) {
                throw new IOException("Unknown tag ID in ListTag: " + id);
            }
        }
        for (int count = in.readInt(), index = 0; index < count; ++index) {
            Tag tag = null;
            try {
                tag = TagRegistry.createInstance(id, "");
            }
            catch (TagCreateException e) {
                throw new IOException("Failed to create tag.", e);
            }
            tag.read(in);
            this.add(tag);
        }
    }
    
    @Override
    public void write(final DataOutput out) throws IOException {
        if (this.type == null) {
            out.writeByte(0);
        }
        else {
            final int id = TagRegistry.getIdFor(this.type);
            if (id == -1) {
                throw new IOException("ListTag contains unregistered tag class.");
            }
            out.writeByte(id);
        }
        out.writeInt(this.value.size());
        for (final Tag tag : this.value) {
            tag.write(out);
        }
    }
    
    @Override
    public ListTag clone() {
        final List<Tag> newList = new ArrayList<Tag>();
        for (final Tag value : this.value) {
            newList.add(value.clone());
        }
        return new ListTag(this.getName(), newList);
    }
}
