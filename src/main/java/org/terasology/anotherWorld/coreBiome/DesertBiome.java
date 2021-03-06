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
package org.terasology.anotherWorld.coreBiome;

import org.terasology.anotherWorld.Biome;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class DesertBiome implements Biome {
    public static final String ID = "AnotherWorld:Desert";
    // Hot and dry, and preferably flat
    private SweetSpot sweetSpot = new DefaultSweetSpot(0f, 0.4f, 1f, 0.4f, 0f, 0.2f, 0f, 0f);

    @Override
    public String getBiomeId() {
        return ID;
    }

    @Override
    public String getBiomeName() {
        return "Desert";
    }

    @Override
    public String getBiomeParent() {
        return null;
    }

    @Override
    public float getRarity() {
        return 0.4f;
    }

    @Override
    public SweetSpot getSweetSpot() {
        return sweetSpot;
    }

    @Override
    public float getFog() {
        return 0.1f;
    }
}
