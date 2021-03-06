package grondag.renderbender.init;

import static grondag.renderbender.init.BasicBlocks.ITEM_FUNCTION_STANDARD;
import static grondag.renderbender.init.BasicBlocks.register;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.block.BiomeColors;
import net.minecraft.client.render.block.FoliageColorHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class ExtendedBlocks {
    private static final Block LAYERS_BLOCK = new Block(FabricBlockSettings
            .of(Material.STONE).strength(1, 1)
            .materialColor(MaterialColor.FOLIAGE).build());

    private static final Block SHADER_BLOCK = new Block(FabricBlockSettings
            .of(Material.STONE).strength(1, 1)
            .materialColor(MaterialColor.CYAN).build());

    private static final Block CONDITIONAL_BLOCK = new Block(FabricBlockSettings
            .of(Material.GLASS).strength(1, 1)
            .materialColor(MaterialColor.CYAN).build()) {

        @Override
        public boolean isFullBoundsCubeForCulling(BlockState blockState_1) {
            return false;
        }

        @Override
        public boolean canSuffocate(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
            return false;
        }

        @Override
        public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
            return false;
        }

        @Override
        public boolean allowsSpawning(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityType<?> entityType_1) {
            return false;
        }

        @Environment(EnvType.CLIENT)
        @Override
        public boolean skipRenderingSide(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
            return blockState_2.getBlock() == this ? true : super.skipRenderingSide(blockState_1, blockState_2, direction_1);
        }

        @Override
        public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
            return true;
        }

        @Override
        public int getLightSubtracted(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
            return 0;
        }
    };

    public static Item CONDITION_ITEM;

    public static void initialize() {
        register(LAYERS_BLOCK, "layers", ITEM_FUNCTION_STANDARD);

        ColorProviderRegistry.BLOCK.register((blockState, extendedBlockView, pos, colorIndex) -> {
            return extendedBlockView != null && pos != null ? BiomeColors.foliageColorAt(extendedBlockView, pos) : FoliageColorHandler.getDefaultColor();
        }, LAYERS_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, colorIndex) -> {
            return FoliageColorHandler.getDefaultColor();
        }, LAYERS_BLOCK);

        register(SHADER_BLOCK, "shader", ITEM_FUNCTION_STANDARD);

        CONDITION_ITEM = register(CONDITIONAL_BLOCK, "conditional", ITEM_FUNCTION_STANDARD);
    }
}
