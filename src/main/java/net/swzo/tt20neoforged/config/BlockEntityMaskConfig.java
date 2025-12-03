package net.swzo.tt20neoforged.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swzo.tt20neoforged.util.Mask;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockEntityMaskConfig {
    public static final BlockEntityMaskConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        final Pair<BlockEntityMaskConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(BlockEntityMaskConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    private final ModConfigSpec.ConfigValue<String> type;
    private final ModConfigSpec.ConfigValue<List<? extends String>> blocks;

    private Mask mask;

    public BlockEntityMaskConfig(ModConfigSpec.Builder builder) {
        builder.comment("Block Entity Mask Configuration").push("mask");

        type = builder
                .comment("The type of mask to use. Can be 'whitelist' or 'blacklist'. (Default: whitelist)")
                .define("type", "whitelist");

        blocks = builder
                .comment("A list of blocks to whitelist or blacklist. (Default: [\"*:*\"])")
                .defineList("blocks", Arrays.asList("*:*"), obj -> obj instanceof String);

        builder.pop();
    }

    public Mask getMask() {
        if (this.mask == null) {
            refreshMask();
        }
        return this.mask;
    }

    public void refreshMask() {

        this.mask = new Mask(BuiltInRegistries.BLOCK, this, "blocks");
    }

    public List<String> getBlocks() {

        return new ArrayList<>(this.blocks.get());
    }

    public String getType() {
        return this.type.get();
    }

    public void setType(String type) {
        this.type.set(type);

    }

    public boolean addBlock(String blockId) {
        List<String> currentBlocks = new ArrayList<>(this.getBlocks());
        if (!currentBlocks.contains(blockId)) {
            currentBlocks.add(blockId);
            this.blocks.set(currentBlocks);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeBlock(String blockId) {
        List<String> currentBlocks = new ArrayList<>(this.getBlocks());
        if (currentBlocks.remove(blockId)) {
            this.blocks.set(currentBlocks);
            return true;
        } else {
            return false;
        }
    }

    public void reload() {
        this.refreshMask();
    }

    public List<String> getAsStringList(String key) {
        if ("blocks".equals(key)) {
            return getBlocks();
        }
        return new ArrayList<>();
    }
}