/*******************************************************************************
 * Copyright 2019 grondag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.renderbender.model;

import java.util.Random;
import java.util.function.Supplier;

import grondag.frex.api.render.RenderContext.QuadTransform;
import grondag.frex.api.render.TerrainBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface MeshTransformer extends QuadTransform {
    MeshTransformer prepare(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier);
    
    MeshTransformer prepare(ItemStack stack, Supplier<Random> randomSupplier);
}
