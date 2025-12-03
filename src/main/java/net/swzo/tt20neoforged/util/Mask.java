package net.swzo.tt20neoforged.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.swzo.tt20neoforged.config.BlockEntityMaskConfig;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mask {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final BlockEntityMaskConfig config;
    private final MaskType maskType;
    private final Registry<?> registry;
    private final RegistryIndex index;
    private final Set<ResourceLocation> entries;

    public Mask(Registry<?> registry, BlockEntityMaskConfig config, String maskKey) {
        this.config = config;

        String typeStr = config.getType();
        if (typeStr == null || typeStr.isEmpty()) {
            LOGGER.warn("(TT20) Mask type is missing, defaulting to 'whitelist'.");
            this.maskType = MaskType.WHITELIST;
        } else {

            try {
                this.maskType = MaskType.fromString(typeStr);
            } catch (IllegalArgumentException e) {
                LOGGER.error("(TT20) Invalid mask type '{}', defaulting to WHITELIST", typeStr);
                throw e; 
            }
        }

        this.registry = registry;
        this.index = RegistryIndex.getIndex(this.registry);
        this.entries = new HashSet<>();

        List<String> maskArray = config.getAsStringList(maskKey); 

        if (maskArray == null || maskArray.isEmpty()) {
            LOGGER.error("(TT20) Mask list '{}' is empty or missing.", maskKey);
        } else {
            for (String element : maskArray) {
                this.entries.addAll(this.manageEntry(element));
            }
        }
    }

    public List<ResourceLocation> manageEntry(String entry) {
        String[] split = entry.split(":");
        if (split.length != 2) {
            LOGGER.error("(TT20) '{}' is not a valid identifier. Correct format is <namespace>:<path>", entry);
            return new ArrayList<>();
        } else if (split[0].equals("*") && split[1].equals("*")) {
            return this.index.getIdentifiers();
        } else if (!split[0].equals("*") && !split[1].equals("*")) {
            return List.of(ResourceLocation.tryBuild(split[0], split[1]));
        } else if (split[0].equals("*") && !split[1].equals("*")) {
            return this.index.getPathIndex().getOrDefault(split[1], new ArrayList<>());
        } else {
            return !split[0].equals("*") && split[1].equals("*")
                    ? this.index.getNamespaceIndex().getOrDefault(split[0], new ArrayList<>())
                    : null;
        }
    }

    public Registry<?> getRegistry() {
        return this.registry;
    }

    public BlockEntityMaskConfig getConfig() {
        return this.config;
    }

    public boolean matches(ResourceLocation identifier) {
        return this.entries.contains(identifier);
    }

    public boolean isOkay(ResourceLocation identifier) {
        if (this.maskType == MaskType.WHITELIST) {
            return this.entries.contains(identifier);
        } else {
            return !this.entries.contains(identifier);
        }
    }
}