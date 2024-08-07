// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.storage;

public class BlockState
{
    private int id;
    private int data;
    
    public BlockState(final int id, final int data) {
        this.id = id;
        this.data = data;
    }
    
    public static BlockState rawToState(final int raw) {
        return new BlockState(raw >> 4, raw & 0xF);
    }
    
    public static int stateToRaw(final BlockState state) {
        return state.getId() << 4 | (state.getData() & 0xF);
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getData() {
        return this.data;
    }
    
    @Override
    public String toString() {
        return "BlockState{id: " + this.id + ", data: " + this.data + "}";
    }
}
