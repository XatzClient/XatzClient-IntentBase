// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

public final class ScoreComponent extends BaseComponent
{
    private String name;
    private String objective;
    private String value;
    
    public ScoreComponent(final String name, final String objective) {
        this.value = "";
        this.setName(name);
        this.setObjective(objective);
    }
    
    public ScoreComponent(final ScoreComponent original) {
        super(original);
        this.value = "";
        this.setName(original.getName());
        this.setObjective(original.getObjective());
        this.setValue(original.getValue());
    }
    
    @Override
    public ScoreComponent duplicate() {
        return new ScoreComponent(this);
    }
    
    protected void toPlainText(final StringBuilder builder) {
        builder.append(this.value);
        super.toPlainText(builder);
    }
    
    protected void toLegacyText(final StringBuilder builder) {
        this.addFormat(builder);
        builder.append(this.value);
        super.toLegacyText(builder);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setObjective(final String objective) {
        this.objective = objective;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "ScoreComponent(name=" + this.getName() + ", objective=" + this.getObjective() + ", value=" + this.getValue() + ")";
    }
    
    public ScoreComponent(final String name, final String objective, final String value) {
        this.value = "";
        this.name = name;
        this.objective = objective;
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScoreComponent)) {
            return false;
        }
        final ScoreComponent other = (ScoreComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0075: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0075;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0075;
            }
            return false;
        }
        final Object this$objective = this.getObjective();
        final Object other$objective = other.getObjective();
        Label_0112: {
            if (this$objective == null) {
                if (other$objective == null) {
                    break Label_0112;
                }
            }
            else if (this$objective.equals(other$objective)) {
                break Label_0112;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ScoreComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $objective = this.getObjective();
        result = result * 59 + (($objective == null) ? 43 : $objective.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
}
