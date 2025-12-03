package net.swzo.tt20neoforged.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryIndex {

    private static final Map<Registry<?>, RegistryIndex> indexes = new HashMap<>();

    private final Registry<?> registry;
    private final List<ResourceLocation> identifiers;
    private final List<String> namespaces;
    private final List<String> paths;
    private final Map<String, List<ResourceLocation>> namespaceIndex;
    private final Map<String, List<ResourceLocation>> pathIndex;

    private RegistryIndex(Registry<?> registry) {
        this.registry = registry;
        this.identifiers = new ArrayList<>();
        this.namespaces = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.namespaceIndex = new HashMap<>();
        this.pathIndex = new HashMap<>();

        for (ResourceLocation key : registry.keySet()) {
            String namespace = key.getNamespace();
            String path = key.getPath();

            if (!this.namespaces.contains(namespace)) {
                this.namespaces.add(namespace);
                this.namespaceIndex.put(namespace, new ArrayList<>());
            }

            if (!this.paths.contains(path)) {
                this.paths.add(path);
                this.pathIndex.put(path, new ArrayList<>());
            }

            this.identifiers.add(key);
            this.namespaceIndex.get(namespace).add(key);
            this.pathIndex.get(path).add(key);
        }
    }

    public Registry<?> getRegistry() {
        return this.registry;
    }

    public List<ResourceLocation> getIdentifiers() {
        return this.identifiers;
    }

    public HashMap<String, List<ResourceLocation>> getNamespaceIndex() {
        return new HashMap<>(this.namespaceIndex);
    }

    public HashMap<String, List<ResourceLocation>> getPathIndex() {
        return new HashMap<>(this.pathIndex);
    }

    public List<String> getNamespaces() {
        return new ArrayList<>(this.namespaces);
    }

    public List<String> getPaths() {
        return new ArrayList<>(this.paths);
    }

    public static RegistryIndex getIndex(Registry<?> registry) {
        if (indexes.containsKey(registry)) {
            return indexes.get(registry);
        } else {
            RegistryIndex newIndex = new RegistryIndex(registry);
            indexes.put(registry, newIndex);
            return newIndex;
        }
    }
}