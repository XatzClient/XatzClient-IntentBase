// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.api;

public interface ViaRewindConfig
{
    CooldownIndicator getCooldownIndicator();
    
    boolean isReplaceAdventureMode();
    
    boolean isReplaceParticles();
    
    public enum CooldownIndicator
    {
        TITLE, 
        ACTION_BAR, 
        BOSS_BAR, 
        DISABLED;
    }
}
