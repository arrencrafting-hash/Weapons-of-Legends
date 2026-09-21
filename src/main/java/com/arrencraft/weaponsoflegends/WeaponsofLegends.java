package com.arrencraft.weaponsoflegends;

import com.arrencraft.weaponsoflegends.artifacts.kingslayers.Kingslayers;
import com.arrencraft.weaponsoflegends.config.Config;
import com.arrencraft.weaponsoflegends.progression.forge.LegendaryForgeBlock;
import com.arrencraft.weaponsoflegends.progression.forge.LegendaryForgeBlockEntity;
import com.arrencraft.weaponsoflegends.progression.forge.LegendaryForgePartBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WeaponsofLegends.MODID)
public class WeaponsofLegends {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "weapons_of_legends";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "weapons_of_legends" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredBlock<LegendaryForgeBlock> LEGENDARY_FORGE = BLOCKS.registerBlock(
            "legendary_forge",
            LegendaryForgeBlock::new,
            BlockBehaviour.Properties.of()
    );

    public static final DeferredBlock<LegendaryForgePartBlock> LEGENDARY_FORGE_PART =
            BLOCKS.registerBlock(
                    "legendary_forge_part",
                    LegendaryForgePartBlock::new,
                    BlockBehaviour.Properties.of()
            );

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final Supplier<BlockEntityType<LegendaryForgeBlockEntity>> LEGENDARY_FORGE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "legendary_forge",
                    () -> BlockEntityType.Builder.of(
                            LegendaryForgeBlockEntity::new,
                            LEGENDARY_FORGE.get()
                    ).build(null)
            );
    // Create a Deferred Register to hold Items which will all be registered under the "weapons_of_legends" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<BlockItem> LEGENDARY_FORGE_ITEM = ITEMS.registerSimpleBlockItem(LEGENDARY_FORGE);
    public static final DeferredItem<Kingslayers> KINGSLAYERS =
            ITEMS.registerItem("kingslayers", Kingslayers::new);
    public static final DeferredItem<BlockItem> LEGENDARY_FORGE_PART_ITEM = ITEMS.registerSimpleBlockItem(LEGENDARY_FORGE_PART);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "weapons_of_legends" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WEAPONS_OF_LEGENDS_TAB =
            CREATIVE_MODE_TABS.register("weapons_of_legends", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.weapons_of_legends"))
                            .icon(() -> KINGSLAYERS.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(KINGSLAYERS.get());
                                output.accept(LEGENDARY_FORGE_ITEM.get());
                                output.accept(LEGENDARY_FORGE_PART_ITEM.get());
                            })
                            .build()
            );
    // Creates a new Block with the id "weapons_of_legends:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "weapons_of_legends:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "weapons_of_legends:example_id", nutrition 1 and saturation 2

    // Creates a creative tab with the id "weapons_of_legends:example_tab" for the example item, that is placed after the combat tab

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public WeaponsofLegends(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (WeaponsofLegends) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {

        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
