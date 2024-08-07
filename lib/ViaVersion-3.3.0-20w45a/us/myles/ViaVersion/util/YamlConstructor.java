// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.util;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class YamlConstructor extends SafeConstructor
{
    public YamlConstructor() {
        this.yamlClassConstructors.put(NodeId.mapping, new SafeConstructor.ConstructYamlMap((SafeConstructor)this));
        this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
    }
    
    class Map extends SafeConstructor.ConstructYamlMap
    {
        Map() {
            super((SafeConstructor)YamlConstructor.this);
        }
        
        public Object construct(final Node node) {
            final Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap((java.util.Map<?, ?>)o);
            }
            return o;
        }
    }
    
    class ConstructYamlOmap extends SafeConstructor.ConstructYamlOmap
    {
        ConstructYamlOmap() {
            super((SafeConstructor)YamlConstructor.this);
        }
        
        public Object construct(final Node node) {
            final Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap((java.util.Map<?, ?>)o);
            }
            return o;
        }
    }
}
