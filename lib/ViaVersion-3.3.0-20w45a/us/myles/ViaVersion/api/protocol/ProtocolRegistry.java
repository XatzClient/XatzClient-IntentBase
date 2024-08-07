// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

import java.util.concurrent.ThreadFactory;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import us.myles.ViaVersion.protocols.protocol1_16_4to1_16_3.Protocol1_16_4To1_16_3;
import us.myles.ViaVersion.protocols.protocol1_16_3to1_16_2.Protocol1_16_3To1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import us.myles.ViaVersion.protocols.protocol1_16_1to1_16.Protocol1_16_1To1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.ViaVersion.protocols.protocol1_15_2to1_15_1.Protocol1_15_2To1_15_1;
import us.myles.ViaVersion.protocols.protocol1_15_1to1_15.Protocol1_15_1To1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import us.myles.ViaVersion.protocols.protocol1_14_4to1_14_3.Protocol1_14_4To1_14_3;
import us.myles.ViaVersion.protocols.protocol1_14_3to1_14_2.Protocol1_14_3To1_14_2;
import us.myles.ViaVersion.protocols.protocol1_14_2to1_14_1.Protocol1_14_2To1_14_1;
import us.myles.ViaVersion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.protocols.protocol1_13_2to1_13_1.Protocol1_13_2To1_13_1;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.protocols.protocol1_12_2to1_12_1.Protocol1_12_2To1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.Protocol1_12_1To1_12;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
import us.myles.ViaVersion.protocols.protocol1_11_1to1_11.Protocol1_11_1To1_11;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.Protocol1_11To1_10;
import us.myles.ViaVersion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.Protocol1_9_1_2To1_9_3_4;
import us.myles.ViaVersion.protocols.protocol1_9to1_9_1.Protocol1_9To1_9_1;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.Protocol1_9_3To1_9_1_2;
import java.util.Arrays;
import us.myles.ViaVersion.protocols.protocol1_9_1to1_9.Protocol1_9_1To1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.protocols.base.BaseProtocol1_16;
import us.myles.ViaVersion.protocols.base.BaseProtocol1_7;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import us.myles.ViaVersion.protocols.base.BaseProtocol;
import java.util.concurrent.Executor;
import us.myles.ViaVersion.api.data.MappingDataLoader;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import us.myles.viaversion.libs.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Iterator;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.CompletableFuture;
import com.google.common.collect.Range;
import java.util.Set;
import java.util.List;
import us.myles.ViaVersion.api.Pair;
import java.util.Map;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class ProtocolRegistry
{
    public static final Protocol BASE_PROTOCOL;
    public static int SERVER_PROTOCOL;
    public static int maxProtocolPathSize;
    private static final Int2ObjectMap<Int2ObjectMap<Protocol>> registryMap;
    private static final Map<Class<? extends Protocol>, Protocol> protocols;
    private static final Map<Pair<Integer, Integer>, List<Pair<Integer, Protocol>>> pathCache;
    private static final Set<Integer> supportedVersions;
    private static final List<Pair<Range<Integer>, Protocol>> baseProtocols;
    private static final List<Protocol> registerList;
    private static final Object MAPPING_LOADER_LOCK;
    private static Map<Class<? extends Protocol>, CompletableFuture<Void>> mappingLoaderFutures;
    private static ThreadPoolExecutor mappingLoaderExecutor;
    private static boolean mappingsLoaded;
    
    public static void init() {
    }
    
    public static void registerProtocol(final Protocol protocol, final ProtocolVersion supported, final ProtocolVersion output) {
        registerProtocol(protocol, Collections.singletonList(supported.getVersion()), output.getVersion());
    }
    
    public static void registerProtocol(final Protocol protocol, final List<Integer> supported, final int output) {
        if (!ProtocolRegistry.pathCache.isEmpty()) {
            ProtocolRegistry.pathCache.clear();
        }
        ProtocolRegistry.protocols.put(protocol.getClass(), protocol);
        for (final int version : supported) {
            final Int2ObjectMap<Protocol> protocolMap = ProtocolRegistry.registryMap.computeIfAbsent(version, s -> new Int2ObjectOpenHashMap(2));
            protocolMap.put(output, protocol);
        }
        if (Via.getPlatform().isPluginEnabled()) {
            protocol.register(Via.getManager().getProviders());
            refreshVersions();
        }
        else {
            ProtocolRegistry.registerList.add(protocol);
        }
        if (protocol.hasMappingDataToLoad()) {
            if (ProtocolRegistry.mappingLoaderExecutor != null) {
                addMappingLoaderFuture(protocol.getClass(), protocol::loadMappingData);
            }
            else {
                protocol.loadMappingData();
            }
        }
    }
    
    public static void registerBaseProtocol(final Protocol baseProtocol, final Range<Integer> supportedProtocols) {
        ProtocolRegistry.baseProtocols.add(new Pair<Range<Integer>, Protocol>(supportedProtocols, baseProtocol));
        if (Via.getPlatform().isPluginEnabled()) {
            baseProtocol.register(Via.getManager().getProviders());
            refreshVersions();
        }
        else {
            ProtocolRegistry.registerList.add(baseProtocol);
        }
    }
    
    public static void refreshVersions() {
        ProtocolRegistry.supportedVersions.clear();
        ProtocolRegistry.supportedVersions.add(ProtocolRegistry.SERVER_PROTOCOL);
        for (final ProtocolVersion versions : ProtocolVersion.getProtocols()) {
            final List<Pair<Integer, Protocol>> paths = getProtocolPath(versions.getVersion(), ProtocolRegistry.SERVER_PROTOCOL);
            if (paths == null) {
                continue;
            }
            ProtocolRegistry.supportedVersions.add(versions.getVersion());
            for (final Pair<Integer, Protocol> path : paths) {
                ProtocolRegistry.supportedVersions.add(path.getKey());
            }
        }
    }
    
    public static SortedSet<Integer> getSupportedVersions() {
        return Collections.unmodifiableSortedSet(new TreeSet<Integer>(ProtocolRegistry.supportedVersions));
    }
    
    public static boolean isWorkingPipe() {
        for (final Int2ObjectMap<Protocol> map : ProtocolRegistry.registryMap.values()) {
            if (map.containsKey(ProtocolRegistry.SERVER_PROTOCOL)) {
                return true;
            }
        }
        return false;
    }
    
    public static void onServerLoaded() {
        for (final Protocol protocol : ProtocolRegistry.registerList) {
            protocol.register(Via.getManager().getProviders());
        }
        ProtocolRegistry.registerList.clear();
    }
    
    @Nullable
    private static List<Pair<Integer, Protocol>> getProtocolPath(final List<Pair<Integer, Protocol>> current, final int clientVersion, final int serverVersion) {
        if (clientVersion == serverVersion) {
            return null;
        }
        if (current.size() > ProtocolRegistry.maxProtocolPathSize) {
            return null;
        }
        final Int2ObjectMap<Protocol> inputMap = ProtocolRegistry.registryMap.get(clientVersion);
        if (inputMap == null) {
            return null;
        }
        final Protocol protocol = inputMap.get(serverVersion);
        if (protocol != null) {
            current.add(new Pair<Integer, Protocol>(serverVersion, protocol));
            return current;
        }
        List<Pair<Integer, Protocol>> shortest = null;
        for (final Int2ObjectMap.Entry<Protocol> entry : inputMap.int2ObjectEntrySet()) {
            if (entry.getIntKey() == serverVersion) {
                continue;
            }
            final Pair<Integer, Protocol> pair = new Pair<Integer, Protocol>(entry.getIntKey(), entry.getValue());
            if (current.contains(pair)) {
                continue;
            }
            List<Pair<Integer, Protocol>> newCurrent = new ArrayList<Pair<Integer, Protocol>>(current);
            newCurrent.add(pair);
            newCurrent = getProtocolPath(newCurrent, entry.getKey(), serverVersion);
            if (newCurrent == null || (shortest != null && shortest.size() <= newCurrent.size())) {
                continue;
            }
            shortest = newCurrent;
        }
        return shortest;
    }
    
    @Nullable
    public static List<Pair<Integer, Protocol>> getProtocolPath(final int clientVersion, final int serverVersion) {
        final Pair<Integer, Integer> protocolKey = new Pair<Integer, Integer>(clientVersion, serverVersion);
        final List<Pair<Integer, Protocol>> protocolList = ProtocolRegistry.pathCache.get(protocolKey);
        if (protocolList != null) {
            return protocolList;
        }
        final List<Pair<Integer, Protocol>> outputPath = getProtocolPath(new ArrayList<Pair<Integer, Protocol>>(), clientVersion, serverVersion);
        if (outputPath != null) {
            ProtocolRegistry.pathCache.put(protocolKey, outputPath);
        }
        return outputPath;
    }
    
    @Nullable
    public static Protocol getProtocol(final Class<? extends Protocol> protocolClass) {
        return ProtocolRegistry.protocols.get(protocolClass);
    }
    
    public static Protocol getBaseProtocol(final int serverVersion) {
        for (final Pair<Range<Integer>, Protocol> rangeProtocol : Lists.reverse((List)ProtocolRegistry.baseProtocols)) {
            if (rangeProtocol.getKey().contains((Comparable)serverVersion)) {
                return rangeProtocol.getValue();
            }
        }
        throw new IllegalStateException("No Base Protocol for " + serverVersion);
    }
    
    public static boolean isBaseProtocol(final Protocol protocol) {
        for (final Pair<Range<Integer>, Protocol> p : ProtocolRegistry.baseProtocols) {
            if (p.getValue() == protocol) {
                return true;
            }
        }
        return false;
    }
    
    public static void completeMappingDataLoading(final Class<? extends Protocol> protocolClass) throws Exception {
        if (ProtocolRegistry.mappingsLoaded) {
            return;
        }
        final CompletableFuture<Void> future = getMappingLoaderFuture(protocolClass);
        if (future == null) {
            return;
        }
        future.get();
    }
    
    public static boolean checkForMappingCompletion() {
        synchronized (ProtocolRegistry.MAPPING_LOADER_LOCK) {
            if (ProtocolRegistry.mappingsLoaded) {
                return false;
            }
            for (final CompletableFuture<Void> future : ProtocolRegistry.mappingLoaderFutures.values()) {
                if (!future.isDone()) {
                    return false;
                }
            }
            shutdownLoaderExecutor();
            return true;
        }
    }
    
    private static void shutdownLoaderExecutor() {
        Via.getPlatform().getLogger().info("Shutting down mapping loader executor!");
        ProtocolRegistry.mappingsLoaded = true;
        ProtocolRegistry.mappingLoaderExecutor.shutdown();
        ProtocolRegistry.mappingLoaderExecutor = null;
        ProtocolRegistry.mappingLoaderFutures.clear();
        ProtocolRegistry.mappingLoaderFutures = null;
        if (MappingDataLoader.isCacheJsonMappings()) {
            MappingDataLoader.getMappingsCache().clear();
        }
    }
    
    public static void addMappingLoaderFuture(final Class<? extends Protocol> protocolClass, final Runnable runnable) {
        synchronized (ProtocolRegistry.MAPPING_LOADER_LOCK) {
            final CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, ProtocolRegistry.mappingLoaderExecutor).exceptionally(throwable -> {
                Via.getPlatform().getLogger().severe("Error during mapping loading of " + protocolClass.getSimpleName());
                throwable.printStackTrace();
                return null;
            });
            ProtocolRegistry.mappingLoaderFutures.put(protocolClass, future);
        }
    }
    
    public static void addMappingLoaderFuture(final Class<? extends Protocol> protocolClass, final Class<? extends Protocol> dependsOn, final Runnable runnable) {
        synchronized (ProtocolRegistry.MAPPING_LOADER_LOCK) {
            final CompletableFuture<Void> future = getMappingLoaderFuture(dependsOn).whenCompleteAsync((v, throwable) -> runnable.run(), (Executor)ProtocolRegistry.mappingLoaderExecutor).exceptionally(throwable -> {
                Via.getPlatform().getLogger().severe("Error during mapping loading of " + protocolClass.getSimpleName());
                throwable.printStackTrace();
                return null;
            });
            ProtocolRegistry.mappingLoaderFutures.put(protocolClass, future);
        }
    }
    
    @Nullable
    public static CompletableFuture<Void> getMappingLoaderFuture(final Class<? extends Protocol> protocolClass) {
        synchronized (ProtocolRegistry.MAPPING_LOADER_LOCK) {
            if (ProtocolRegistry.mappingsLoaded) {
                return null;
            }
            return ProtocolRegistry.mappingLoaderFutures.get(protocolClass);
        }
    }
    
    static {
        BASE_PROTOCOL = new BaseProtocol();
        ProtocolRegistry.SERVER_PROTOCOL = -1;
        ProtocolRegistry.maxProtocolPathSize = 50;
        registryMap = new Int2ObjectOpenHashMap<Int2ObjectMap<Protocol>>(32);
        protocols = new HashMap<Class<? extends Protocol>, Protocol>();
        pathCache = new ConcurrentHashMap<Pair<Integer, Integer>, List<Pair<Integer, Protocol>>>();
        supportedVersions = new HashSet<Integer>();
        baseProtocols = Lists.newCopyOnWriteArrayList();
        registerList = new ArrayList<Protocol>();
        MAPPING_LOADER_LOCK = new Object();
        ProtocolRegistry.mappingLoaderFutures = new HashMap<Class<? extends Protocol>, CompletableFuture<Void>>();
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Via-Mappingloader-%d").build();
        (ProtocolRegistry.mappingLoaderExecutor = new ThreadPoolExecutor(5, 16, 45L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory)).allowCoreThreadTimeOut(true);
        registerBaseProtocol(ProtocolRegistry.BASE_PROTOCOL, (Range<Integer>)Range.lessThan((Comparable)Integer.MIN_VALUE));
        registerBaseProtocol(new BaseProtocol1_7(), (Range<Integer>)Range.lessThan((Comparable)ProtocolVersion.v1_16.getVersion()));
        registerBaseProtocol(new BaseProtocol1_16(), (Range<Integer>)Range.atLeast((Comparable)ProtocolVersion.v1_16.getVersion()));
        registerProtocol(new Protocol1_9To1_8(), ProtocolVersion.v1_9, ProtocolVersion.v1_8);
        registerProtocol(new Protocol1_9_1To1_9(), Arrays.asList(ProtocolVersion.v1_9_1.getVersion(), ProtocolVersion.v1_9_2.getVersion()), ProtocolVersion.v1_9.getVersion());
        registerProtocol(new Protocol1_9_3To1_9_1_2(), ProtocolVersion.v1_9_3, ProtocolVersion.v1_9_2);
        registerProtocol(new Protocol1_9To1_9_1(), ProtocolVersion.v1_9, ProtocolVersion.v1_9_2);
        registerProtocol(new Protocol1_9_1_2To1_9_3_4(), Arrays.asList(ProtocolVersion.v1_9_1.getVersion(), ProtocolVersion.v1_9_2.getVersion()), ProtocolVersion.v1_9_3.getVersion());
        registerProtocol(new Protocol1_10To1_9_3_4(), ProtocolVersion.v1_10, ProtocolVersion.v1_9_3);
        registerProtocol(new Protocol1_11To1_10(), ProtocolVersion.v1_11, ProtocolVersion.v1_10);
        registerProtocol(new Protocol1_11_1To1_11(), ProtocolVersion.v1_11_1, ProtocolVersion.v1_11);
        registerProtocol(new Protocol1_12To1_11_1(), ProtocolVersion.v1_12, ProtocolVersion.v1_11_1);
        registerProtocol(new Protocol1_12_1To1_12(), ProtocolVersion.v1_12_1, ProtocolVersion.v1_12);
        registerProtocol(new Protocol1_12_2To1_12_1(), ProtocolVersion.v1_12_2, ProtocolVersion.v1_12_1);
        registerProtocol(new Protocol1_13To1_12_2(), ProtocolVersion.v1_13, ProtocolVersion.v1_12_2);
        registerProtocol(new Protocol1_13_1To1_13(), ProtocolVersion.v1_13_1, ProtocolVersion.v1_13);
        registerProtocol(new Protocol1_13_2To1_13_1(), ProtocolVersion.v1_13_2, ProtocolVersion.v1_13_1);
        registerProtocol(new Protocol1_14To1_13_2(), ProtocolVersion.v1_14, ProtocolVersion.v1_13_2);
        registerProtocol(new Protocol1_14_1To1_14(), ProtocolVersion.v1_14_1, ProtocolVersion.v1_14);
        registerProtocol(new Protocol1_14_2To1_14_1(), ProtocolVersion.v1_14_2, ProtocolVersion.v1_14_1);
        registerProtocol(new Protocol1_14_3To1_14_2(), ProtocolVersion.v1_14_3, ProtocolVersion.v1_14_2);
        registerProtocol(new Protocol1_14_4To1_14_3(), ProtocolVersion.v1_14_4, ProtocolVersion.v1_14_3);
        registerProtocol(new Protocol1_15To1_14_4(), ProtocolVersion.v1_15, ProtocolVersion.v1_14_4);
        registerProtocol(new Protocol1_15_1To1_15(), ProtocolVersion.v1_15_1, ProtocolVersion.v1_15);
        registerProtocol(new Protocol1_15_2To1_15_1(), ProtocolVersion.v1_15_2, ProtocolVersion.v1_15_1);
        registerProtocol(new Protocol1_16To1_15_2(), ProtocolVersion.v1_16, ProtocolVersion.v1_15_2);
        registerProtocol(new Protocol1_16_1To1_16(), ProtocolVersion.v1_16_1, ProtocolVersion.v1_16);
        registerProtocol(new Protocol1_16_2To1_16_1(), ProtocolVersion.v1_16_2, ProtocolVersion.v1_16_1);
        registerProtocol(new Protocol1_16_3To1_16_2(), ProtocolVersion.v1_16_3, ProtocolVersion.v1_16_2);
        registerProtocol(new Protocol1_16_4To1_16_3(), ProtocolVersion.v1_16_4, ProtocolVersion.v1_16_3);
        registerProtocol(new Protocol1_17To1_16_4(), ProtocolVersion.v1_17, ProtocolVersion.v1_16_4);
    }
}
