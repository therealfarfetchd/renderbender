package grondag.renderbender.init;

import static grondag.renderbender.model.ModelBuilder.FULL_BRIGHTNESS;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

import grondag.frex.api.Renderer;
import grondag.frex.api.RendererAccess;
import grondag.frex.api.material.RenderMaterial;
import grondag.frex.api.mesh.MeshBuilder;
import grondag.frex.api.mesh.MutableQuadView;
import grondag.frex.api.mesh.QuadEmitter;
import grondag.frex.api.model.ModelHelper;
import grondag.frex.api.render.RenderContext;
import grondag.frex.api.render.TerrainBlockView;
import grondag.renderbender.model.DynamicRenderer;
import grondag.renderbender.model.MeshTransformer;
import grondag.renderbender.model.ModelBuilder;
import grondag.renderbender.model.SimpleModel;
import grondag.renderbender.model.SimpleUnbakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class BasicModels {

    public static void initialize(HashMap<String, SimpleUnbakedModel> models) {
        models.put("glow", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().emissive(0, true).disableAo(0, true).disableDiffuse(0, true).find(),
                    -1, sprite, 
                    0, 0, 0, 1, 1, 1);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("glow_diffuse", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().emissive(0, true).disableAo(0, true).find(),
                    -1, sprite, 
                    0, 0, 0, 1, 1, 1);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("glow_ao", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().emissive(0, true).disableDiffuse(0, true).find(),
                    -1, sprite, 
                    0, 0, 0, 1, 1, 1);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("glow_shaded", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().emissive(0, true).find(),
                    -1, sprite, 
                    0, 0, 0, 1, 1, 1);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("glow_dynamic", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().find(),
                    -1, sprite, 
                    0, 0, 0, 1, 1, 1);
            return new SimpleModel(mb.builder.build(), glowTransform::get, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("round_hard", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            ModelBuilder.makeIcosahedron(new Vector3f(0.5f, 0.5f, 0.5f), 0.5f, mb.builder.getEmitter(), mb.finder().find(), sprite, false);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("round_soft", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            ModelBuilder.makeIcosahedron(new Vector3f(0.5f, 0.5f, 0.5f), 0.5f, mb.builder.getEmitter(), mb.finder().find(), sprite, true);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("ao_test", new SimpleUnbakedModel(mb -> {
            return new SimpleModel(null, null, mb.getSprite("minecraft:block/quartz_block_side"), ModelHelper.MODEL_TRANSFORM_BLOCK, aoBuilder());
        }));
        
        models.put("shade_test", new SimpleUnbakedModel(mb -> {
            Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            mb.box(mb.finder().find(),
                    -1, sprite,
                    1f/16f, 1f/16f, 1f/16f, 15f/16f, 15f/16f, 15f/16f);
            return new SimpleModel(mb.builder.build(), null, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));
        
        models.put("be_test", new SimpleUnbakedModel(mb -> {
            final Sprite sprite = mb.getSprite("minecraft:block/quartz_block_side");
            final RenderMaterial mat = mb.finder().find();
            final float PIXEL = 1f/16f;
            final QuadEmitter qe = mb.builder.getEmitter();
            int t = 0;
            for(int d = 0; d < 6; d++) {
                Direction face = Direction.byId(d);
                for(int i = 0; i < 14; i++) {
                    float u = PIXEL + PIXEL * i;
                    for(int j = 0; j < 14; j++) {
                        float v = PIXEL + PIXEL * j;
                        qe.tag(t++);
                        qe.material(mat).square(face, u, v, u + PIXEL, v + PIXEL, PIXEL)
                        .spriteColor(0, -1, -1, -1, -1)
                        .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
                    }
                }
            }
            return new SimpleModel(mb.builder.build(), beTestTransform::get, sprite, ModelHelper.MODEL_TRANSFORM_BLOCK, null);
        }));        
    }

    // this is NOT the way to handle this...
    static DynamicRenderer aoBuilder() {
        return new DynamicRenderer() {
            Renderer renderer = RendererAccess.INSTANCE.getRenderer();
            RenderMaterial mat = renderer.materialFinder().disableDiffuse(0, true).find();
            @Override
            public void render(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
                int hash = pos == null ? 8 : pos.hashCode();
                float height = (1 + (hash & 15)) / 16f;
                Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite("minecraft:block/quartz_block_side");
                MeshBuilder builder = renderer.meshBuilder();
                QuadEmitter emitter = builder.getEmitter(); 
                
                for(int d = 0; d < 6; d++) {
                    Direction face = Direction.byId(d);
                    if(face == Direction.UP) {
                        float depth = face == Direction.UP ? 1 - height : 0;
                        for(int i = 0; i < 4; i++) {
                            for(int j = 0; j < 4; j++) {
                                float u = i * .25f;
                                float v = j * .25f;
                                emitter.square(face, u, v, u + .25f, v + .25f, depth)
                                    .material(mat).spriteColor(0, -1, -1, -1, -1)
                                    .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
                            }
                        }
                    } else if(face == Direction.DOWN) {
                        emitter.square(face, 0, 0, 1, 1, 0)
                            .material(mat).spriteColor(0, -1, -1, -1, -1)
                            .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
                    } else {
                        emitter.square(face, 0, 0, 1, height, 0)
                        .material(mat).spriteColor(0, -1, -1, -1, -1)
                        .spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV).emit();
                    }
                }
                context.meshConsumer().accept(builder.build());
            }
        };
    };
    
    static class GlowTransform implements MeshTransformer {
        int topColor;
        int bottomColor;
        int topLight;
        int bottomLight;
        
        @Override
        public boolean transform(MutableQuadView q) {
            for(int i = 0; i < 4; i++) {
                if(MathHelper.equalsApproximate(q.y(i), 0)) {
                    q.spriteColor(i, 0, bottomColor).lightmap(i, bottomLight);
                } else {
                    q.spriteColor(i, 0, topColor).lightmap(i, topLight);
                }
            }
            return true;
        }
        
        @Override
        public GlowTransform prepare(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier) {
            return prep(randomSupplier);
        }

        @Override
        public GlowTransform prepare(ItemStack stack, Supplier<Random> randomSupplier) {
            return prep(randomSupplier);
        }
        
        private GlowTransform prep(Supplier<Random> randomSupplier) {
            Random random = randomSupplier.get();
            topColor = ModelBuilder.randomPastelColor(random);
            bottomColor = ModelBuilder.randomPastelColor(random);
            final boolean topGlow = random.nextBoolean();
            topLight = topGlow ? FULL_BRIGHTNESS : 0;
            bottomLight = topGlow ? 0 : FULL_BRIGHTNESS;
            return this;
        }
    }
    
    static ThreadLocal<MeshTransformer> glowTransform = ThreadLocal.withInitial(GlowTransform::new);
    
    static ThreadLocal<MeshTransformer> beTestTransform = ThreadLocal.withInitial(BeTestTransform::new);

}
