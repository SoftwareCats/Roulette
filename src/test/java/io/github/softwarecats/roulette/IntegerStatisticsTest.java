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

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntegerStatisticsTest {

    protected final List<Integer> DATA = List.of(9, 8, 5, 9, 9, 4, 5, 8, 10, 7, 8, 8);

    @Test
    public void sum() {
        assertEquals(90, IntegerStatistics.sum(DATA));
    }

    @Test
    public void len() {
        assertEquals(12, IntegerStatistics.len(DATA));
    }

    @Test
    public void mean() {
        assertEquals(7.5, IntegerStatistics.mean(DATA), 0.1);
    }

    @Test
    public void std() {
        assertEquals(1.88293, IntegerStatistics.std(DATA), 0.00001);
    }
}