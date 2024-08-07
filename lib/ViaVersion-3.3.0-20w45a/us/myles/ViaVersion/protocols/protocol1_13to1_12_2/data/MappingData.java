// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data;

import us.myles.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import us.myles.viaversion.libs.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.gson.JsonObject;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import us.myles.ViaVersion.api.data.Mappings;
import com.google.common.collect.BiMap;
import java.util.Map;

public class MappingData extends us.myles.ViaVersion.api.data.MappingData
{
    private final Map<String, Integer[]> blockTags;
    private final Map<String, Integer[]> itemTags;
    private final Map<String, Integer[]> fluidTags;
    private final BiMap<Short, String> oldEnchantmentsIds;
    private final Map<String, String> translateMapping;
    private final Map<String, String> mojangTranslation;
    private final BiMap<String, String> channelMappings;
    private Mappings enchantmentMappings;
    
    public MappingData() {
        super("1.12", "1.13");
        this.blockTags = new HashMap<String, Integer[]>();
        this.itemTags = new HashMap<String, Integer[]>();
        this.fluidTags = new HashMap<String, Integer[]>();
        this.oldEnchantmentsIds = (BiMap<Short, String>)HashBiMap.create();
        this.translateMapping = new HashMap<String, String>();
        this.mojangTranslation = new HashMap<String, String>();
        this.channelMappings = (BiMap<String, String>)HashBiMap.create();
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.blockTags:Ljava/util/Map;
        //     5: aload_2         /* newMappings */
        //     6: ldc             "block_tags"
        //     8: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    11: invokespecial   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lus/myles/viaversion/libs/gson/JsonObject;)V
        //    14: aload_0         /* this */
        //    15: aload_0         /* this */
        //    16: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.itemTags:Ljava/util/Map;
        //    19: aload_2         /* newMappings */
        //    20: ldc             "item_tags"
        //    22: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    25: invokespecial   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lus/myles/viaversion/libs/gson/JsonObject;)V
        //    28: aload_0         /* this */
        //    29: aload_0         /* this */
        //    30: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.fluidTags:Ljava/util/Map;
        //    33: aload_2         /* newMappings */
        //    34: ldc             "fluid_tags"
        //    36: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    39: invokespecial   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lus/myles/viaversion/libs/gson/JsonObject;)V
        //    42: aload_0         /* this */
        //    43: aload_0         /* this */
        //    44: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.oldEnchantmentsIds:Lcom/google/common/collect/BiMap;
        //    47: aload_1         /* oldMappings */
        //    48: ldc             "enchantments"
        //    50: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    53: invokespecial   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.loadEnchantments:(Ljava/util/Map;Lus/myles/viaversion/libs/gson/JsonObject;)V
        //    56: aload_0         /* this */
        //    57: new             Lus/myles/ViaVersion/api/data/Mappings;
        //    60: dup            
        //    61: bipush          72
        //    63: aload_1         /* oldMappings */
        //    64: ldc             "enchantments"
        //    66: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    69: aload_2         /* newMappings */
        //    70: ldc             "enchantments"
        //    72: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //    75: invokespecial   us/myles/ViaVersion/api/data/Mappings.<init>:(ILus/myles/viaversion/libs/gson/JsonObject;Lus/myles/viaversion/libs/gson/JsonObject;)V
        //    78: putfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.enchantmentMappings:Lus/myles/ViaVersion/api/data/Mappings;
        //    81: invokestatic    us/myles/ViaVersion/api/Via.getConfig:()Lus/myles/ViaVersion/api/ViaVersionConfig;
        //    84: invokeinterface us/myles/ViaVersion/api/ViaVersionConfig.isSnowCollisionFix:()Z
        //    89: ifeq            106
        //    92: aload_0         /* this */
        //    93: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lus/myles/ViaVersion/api/data/Mappings;
        //    96: invokevirtual   us/myles/ViaVersion/api/data/Mappings.getOldToNew:()[S
        //    99: sipush          1248
        //   102: sipush          3416
        //   105: sastore        
        //   106: invokestatic    us/myles/ViaVersion/api/Via.getConfig:()Lus/myles/ViaVersion/api/ViaVersionConfig;
        //   109: invokeinterface us/myles/ViaVersion/api/ViaVersionConfig.isInfestedBlocksFix:()Z
        //   114: ifeq            177
        //   117: aload_0         /* this */
        //   118: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lus/myles/ViaVersion/api/data/Mappings;
        //   121: invokevirtual   us/myles/ViaVersion/api/data/Mappings.getOldToNew:()[S
        //   124: astore          oldToNew
        //   126: aload           oldToNew
        //   128: sipush          1552
        //   131: iconst_1       
        //   132: sastore        
        //   133: aload           oldToNew
        //   135: sipush          1553
        //   138: bipush          14
        //   140: sastore        
        //   141: aload           oldToNew
        //   143: sipush          1554
        //   146: sipush          3983
        //   149: sastore        
        //   150: aload           oldToNew
        //   152: sipush          1555
        //   155: sipush          3984
        //   158: sastore        
        //   159: aload           oldToNew
        //   161: sipush          1556
        //   164: sipush          3985
        //   167: sastore        
        //   168: aload           oldToNew
        //   170: sipush          1557
        //   173: sipush          3986
        //   176: sastore        
        //   177: ldc             "channelmappings-1.13.json"
        //   179: invokestatic    us/myles/ViaVersion/api/data/MappingDataLoader.loadFromDataDir:(Ljava/lang/String;)Lus/myles/viaversion/libs/gson/JsonObject;
        //   182: astore          object
        //   184: aload           object
        //   186: ifnull          314
        //   189: aload           object
        //   191: invokevirtual   us/myles/viaversion/libs/gson/JsonObject.entrySet:()Ljava/util/Set;
        //   194: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   199: astore          5
        //   201: aload           5
        //   203: invokeinterface java/util/Iterator.hasNext:()Z
        //   208: ifeq            314
        //   211: aload           5
        //   213: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   218: checkcast       Ljava/util/Map$Entry;
        //   221: astore          entry
        //   223: aload           entry
        //   225: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   230: checkcast       Ljava/lang/String;
        //   233: astore          oldChannel
        //   235: aload           entry
        //   237: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   242: checkcast       Lus/myles/viaversion/libs/gson/JsonElement;
        //   245: invokevirtual   us/myles/viaversion/libs/gson/JsonElement.getAsString:()Ljava/lang/String;
        //   248: astore          newChannel
        //   250: aload           newChannel
        //   252: invokestatic    us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.isValid1_13Channel:(Ljava/lang/String;)Z
        //   255: ifne            297
        //   258: invokestatic    us/myles/ViaVersion/api/Via.getPlatform:()Lus/myles/ViaVersion/api/platform/ViaPlatform;
        //   261: invokeinterface us/myles/ViaVersion/api/platform/ViaPlatform.getLogger:()Ljava/util/logging/Logger;
        //   266: new             Ljava/lang/StringBuilder;
        //   269: dup            
        //   270: invokespecial   java/lang/StringBuilder.<init>:()V
        //   273: ldc             "Channel '"
        //   275: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   278: aload           newChannel
        //   280: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   283: ldc             "' is not a valid 1.13 plugin channel, please check your configuration!"
        //   285: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   288: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   291: invokevirtual   java/util/logging/Logger.warning:(Ljava/lang/String;)V
        //   294: goto            201
        //   297: aload_0         /* this */
        //   298: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.channelMappings:Lcom/google/common/collect/BiMap;
        //   301: aload           oldChannel
        //   303: aload           newChannel
        //   305: invokeinterface com/google/common/collect/BiMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   310: pop            
        //   311: goto            201
        //   314: invokestatic    us/myles/ViaVersion/util/GsonUtil.getGson:()Lus/myles/viaversion/libs/gson/Gson;
        //   317: new             Ljava/io/InputStreamReader;
        //   320: dup            
        //   321: ldc             Lus/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   323: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   326: ldc             "assets/viaversion/data/mapping-lang-1.12-1.13.json"
        //   328: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   331: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   334: new             Lus/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData$1;
        //   337: dup            
        //   338: aload_0         /* this */
        //   339: invokespecial   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData$1.<init>:(Lus/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData;)V
        //   342: invokevirtual   us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData$1.getType:()Ljava/lang/reflect/Type;
        //   345: invokevirtual   us/myles/viaversion/libs/gson/Gson.fromJson:(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   348: checkcast       Ljava/util/Map;
        //   351: astore          translateData
        //   353: new             Ljava/io/InputStreamReader;
        //   356: dup            
        //   357: ldc             Lus/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   359: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   362: ldc             "mojang-translations/en_US.properties"
        //   364: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   367: getstatic       java/nio/charset/StandardCharsets.UTF_8:Ljava/nio/charset/Charset;
        //   370: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
        //   373: astore          reader
        //   375: aconst_null    
        //   376: astore          8
        //   378: aload           reader
        //   380: invokestatic    com/google/common/io/CharStreams.toString:(Ljava/lang/Readable;)Ljava/lang/String;
        //   383: ldc             "\n"
        //   385: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   388: astore          lines
        //   390: aload           reader
        //   392: ifnull          477
        //   395: aload           8
        //   397: ifnull          420
        //   400: aload           reader
        //   402: invokevirtual   java/io/Reader.close:()V
        //   405: goto            477
        //   408: astore          9
        //   410: aload           8
        //   412: aload           9
        //   414: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   417: goto            477
        //   420: aload           reader
        //   422: invokevirtual   java/io/Reader.close:()V
        //   425: goto            477
        //   428: astore          9
        //   430: aload           9
        //   432: astore          8
        //   434: aload           9
        //   436: athrow         
        //   437: astore          10
        //   439: aload           reader
        //   441: ifnull          474
        //   444: aload           8
        //   446: ifnull          469
        //   449: aload           reader
        //   451: invokevirtual   java/io/Reader.close:()V
        //   454: goto            474
        //   457: astore          11
        //   459: aload           8
        //   461: aload           11
        //   463: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   466: goto            474
        //   469: aload           reader
        //   471: invokevirtual   java/io/Reader.close:()V
        //   474: aload           10
        //   476: athrow         
        //   477: aload           lines
        //   479: astore          7
        //   481: aload           7
        //   483: arraylength    
        //   484: istore          8
        //   486: iconst_0       
        //   487: istore          9
        //   489: iload           9
        //   491: iload           8
        //   493: if_icmpge       624
        //   496: aload           7
        //   498: iload           9
        //   500: aaload         
        //   501: astore          line
        //   503: aload           line
        //   505: invokevirtual   java/lang/String.isEmpty:()Z
        //   508: ifeq            514
        //   511: goto            618
        //   514: aload           line
        //   516: ldc_w           "="
        //   519: iconst_2       
        //   520: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   523: astore          keyAndTranslation
        //   525: aload           keyAndTranslation
        //   527: arraylength    
        //   528: iconst_2       
        //   529: if_icmpeq       535
        //   532: goto            618
        //   535: aload           keyAndTranslation
        //   537: iconst_0       
        //   538: aaload         
        //   539: astore          key
        //   541: aload           translateData
        //   543: aload           key
        //   545: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   550: ifne            585
        //   553: aload           keyAndTranslation
        //   555: iconst_1       
        //   556: aaload         
        //   557: ldc_w           "%(\\d\\$)?d"
        //   560: ldc_w           "%$1s"
        //   563: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   566: astore          translation
        //   568: aload_0         /* this */
        //   569: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.mojangTranslation:Ljava/util/Map;
        //   572: aload           key
        //   574: aload           translation
        //   576: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   581: pop            
        //   582: goto            618
        //   585: aload           translateData
        //   587: aload           key
        //   589: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   594: checkcast       Ljava/lang/String;
        //   597: astore          dataValue
        //   599: aload           dataValue
        //   601: ifnull          618
        //   604: aload_0         /* this */
        //   605: getfield        us/myles/ViaVersion/protocols/protocol1_13to1_12_2/data/MappingData.translateMapping:Ljava/util/Map;
        //   608: aload           key
        //   610: aload           dataValue
        //   612: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   617: pop            
        //   618: iinc            9, 1
        //   621: goto            489
        //   624: goto            634
        //   627: astore          e
        //   629: aload           e
        //   631: invokevirtual   java/io/IOException.printStackTrace:()V
        //   634: return         
        //    StackMapTable: 00 15 FB 00 6A FB 00 46 FD 00 17 07 00 48 07 00 89 FE 00 5F 07 00 09 07 00 95 07 00 95 FF 00 10 00 05 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 00 00 FF 00 5D 00 09 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 07 00 0B 07 01 09 07 01 04 07 00 42 00 01 07 00 42 0B FF 00 07 00 09 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 07 00 0B 00 07 01 04 07 00 42 00 01 07 00 42 48 07 00 42 FF 00 13 00 0B 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 07 00 0B 00 07 01 04 07 00 42 00 07 00 42 00 01 07 00 42 0B 04 FF 00 02 00 07 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 07 00 0B 07 01 09 00 00 FE 00 0B 07 01 09 01 01 FC 00 18 07 00 95 FC 00 14 07 01 09 FC 00 31 07 00 95 F8 00 20 FF 00 05 00 06 07 00 02 07 00 48 07 00 48 07 00 48 07 00 48 07 00 0B 00 00 42 07 00 44 06
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  400    405    408    420    Ljava/lang/Throwable;
        //  378    390    428    437    Ljava/lang/Throwable;
        //  378    390    437    477    Any
        //  449    454    457    469    Ljava/lang/Throwable;
        //  428    439    437    477    Any
        //  353    624    627    634    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, @Nullable final JsonObject diffMappings, final String key) {
        if (key.equals("blocks")) {
            return new Mappings(4084, oldMappings.getAsJsonObject("blocks"), newMappings.getAsJsonObject("blockstates"));
        }
        return super.loadFromObject(oldMappings, newMappings, diffMappings, key);
    }
    
    public static String validateNewChannel(String newId) {
        if (!isValid1_13Channel(newId)) {
            return null;
        }
        final int separatorIndex = newId.indexOf(58);
        if ((separatorIndex == -1 || separatorIndex == 0) && newId.length() <= 10) {
            newId = "minecraft:" + newId;
        }
        return newId;
    }
    
    public static boolean isValid1_13Channel(final String channelId) {
        return channelId.matches("([0-9a-z_.-]+):([0-9a-z_/.-]+)");
    }
    
    private void loadTags(final Map<String, Integer[]> output, final JsonObject newTags) {
        for (final Map.Entry<String, JsonElement> entry : newTags.entrySet()) {
            final JsonArray ids = entry.getValue().getAsJsonArray();
            final Integer[] idsArray = new Integer[ids.size()];
            for (int i = 0; i < ids.size(); ++i) {
                idsArray[i] = ids.get(i).getAsInt();
            }
            output.put(entry.getKey(), idsArray);
        }
    }
    
    private void loadEnchantments(final Map<Short, String> output, final JsonObject enchantments) {
        for (final Map.Entry<String, JsonElement> enchantment : enchantments.entrySet()) {
            output.put(Short.parseShort(enchantment.getKey()), enchantment.getValue().getAsString());
        }
    }
    
    public Map<String, Integer[]> getBlockTags() {
        return this.blockTags;
    }
    
    public Map<String, Integer[]> getItemTags() {
        return this.itemTags;
    }
    
    public Map<String, Integer[]> getFluidTags() {
        return this.fluidTags;
    }
    
    public BiMap<Short, String> getOldEnchantmentsIds() {
        return this.oldEnchantmentsIds;
    }
    
    public Map<String, String> getTranslateMapping() {
        return this.translateMapping;
    }
    
    public Map<String, String> getMojangTranslation() {
        return this.mojangTranslation;
    }
    
    public BiMap<String, String> getChannelMappings() {
        return this.channelMappings;
    }
    
    public Mappings getEnchantmentMappings() {
        return this.enchantmentMappings;
    }
}
