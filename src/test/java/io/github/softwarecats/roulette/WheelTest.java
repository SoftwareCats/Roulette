/*
 * Copyright © Bowen Wu 2021.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.softwarecats.roulette;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class WheelTest {

    Wheel wheel;

    @Before
    public void setUp() {
        Random rng = new Random(1);
        wheel = new Wheel(rng);
    }

    @Test
    public void addOutcome() {
        Outcome outcome = new Outcome("1", 1);
        Bin bin = (Bin) wheel.getBin(1).clone();

        wheel.addOutcome(1, outcome);
        bin.add(outcome);

        assertEquals(wheel.getBin(1), bin);
    }

    @Test
    public void next() {
        Random rng = new Random(1);

        assertEquals(wheel.next(), wheel.getBin(rng.nextInt(38)));
    }

    @Test
    public void getOutcomes() {
        BinBuilder builder = new BinBuilder();
        builder.buildBins(wheel);

        assertEquals(wheel.getOutcomes("Line").size(), 11);
    }
}