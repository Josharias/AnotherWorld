/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.anotherWorld.decorator;

import com.google.common.base.Predicate;
import org.terasology.anotherWorld.ChunkDecorator;
import org.terasology.anotherWorld.decorator.structure.Structure;
import org.terasology.anotherWorld.decorator.structure.StructureDefinition;
import org.terasology.anotherWorld.decorator.structure.VeinsStructureDefinition;
import org.terasology.anotherWorld.generation.SeedFacet;
import org.terasology.anotherWorld.util.PDist;
import org.terasology.math.Vector3i;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;

import java.util.Collection;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class CaveDecorator implements ChunkDecorator {
    private Predicate<Block> blockFilter;
    private PDist caveFrequency;
    private PDist mainCaveRadius;
    private PDist mainCaveYLevel;
    private PDist tunnelLength;
    private PDist tunnelRadius;
    private StructureDefinition caveDefinition;

    public CaveDecorator(Predicate<Block> blockFilter, PDist caveFrequency, PDist mainCaveRadius, PDist mainCaveYLevel,
                         PDist tunnelLength, PDist tunnelRadius) {
        this.blockFilter = blockFilter;
        this.caveFrequency = caveFrequency;
        this.mainCaveRadius = mainCaveRadius;
        this.mainCaveYLevel = mainCaveYLevel;
        this.tunnelLength = tunnelLength;
        this.tunnelRadius = tunnelRadius;

        caveDefinition = new VeinsStructureDefinition(caveFrequency,
                new VeinsStructureDefinition.VeinsBlockProvider() {
                    @Override
                    public Block getClusterBlock(float distanceFromCenter) {
                        return BlockManager.getAir();
                    }

                    @Override
                    public Block getBranchBlock() {
                        return BlockManager.getAir();
                    }
                }, mainCaveRadius, mainCaveYLevel, new PDist(4f, 1f), new PDist(0f, 0.1f), tunnelLength,
                new PDist(1000f, 0f), new PDist(0f, 0f), new PDist(0.25f, 0f), new PDist(5f, 0f), new PDist(0.5f, 0.5f),
                tunnelRadius, new PDist(1f, 0f), new PDist(1f, 0f));
    }

    @Override
    public void initialize() {
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SeedFacet seedFacet = chunkRegion.getFacet(SeedFacet.class);
        Structure.StructureCallback callback = new StructureCallbackImpl(chunk);

        Collection<Structure> structures = caveDefinition.generateStructures(chunk, seedFacet.getSeed(), chunkRegion);
        for (Structure structure : structures) {
            structure.generateStructure(callback);
        }
    }

    private final class StructureCallbackImpl implements Structure.StructureCallback {
        private CoreChunk chunk;

        private StructureCallbackImpl(CoreChunk chunk) {
            this.chunk = chunk;
        }

        @Override
        public boolean canReplace(int x, int y, int z) {
            boolean validCoords = (x >= 0 && y >= 1 && z >= 0
                    && x < chunk.getChunkSizeX() && y < chunk.getChunkSizeY() && z < chunk.getChunkSizeZ());
            return validCoords && blockFilter.apply(chunk.getBlock(x, y, z));

        }

        @Override
        public void replaceBlock(Vector3i position, float force, Block block) {
            if (canReplace(position.x, position.y, position.z)) {
                chunk.setBlock(position, block);
            }
        }
    }
}
