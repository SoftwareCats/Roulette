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

import java.util.*;

/**
 * Wheel contains the 38 individual bins on a Roulette wheel, plus a random number generator. It can select
 * a Bin at random, simulating a spin of the Roulette wheel.
 */
public class Wheel {

    protected final Map<String, Outcome> ALL_OUTCOMES = new HashMap<>();
    /**
     * Contains the individual Bin instances.
     * This is always a 'new List( 38 )'.
     */
    private final List<Bin> BINS;
    /**
     * The random number generator to use to select a Bin from the bins collection.
     * This is not always simply ‘new java.util.Random()’. For testing, we would
     * inject a non-random random number generator in place of the system random number generator.
     */
    private final Random RNG;

    /**
     * Create a wheel that will use a the default random number generator. The java.util.Random will be
     * used. This will define the various bins and outcomes using an instance of BinBuilder.
     */
    public Wheel() {
        this(new Random());
    }

    /**
     * Create a wheel with the given random number generator. This will define the various bins and outcomes
     * using an instance of BinBuilder.
     *
     * @param rng a “random” number generator. For testing, this may
     *            be a non-random number generator
     */
    public Wheel(Random rng) {
        List<Bin> bins = new ArrayList<>();
        for (int i = 0; i < 38; i++) {
            bins.add(new Bin());
        }
        this.BINS = bins;

        this.RNG = rng;
    }

    public Map<String, Outcome> getAllOutcomes() {
        return ALL_OUTCOMES;
    }

    /**
     * Adds the given Outcome to the Bin with the given number.
     *
     * @param bin     bin number, in the range zero to 37 inclusive
     * @param outcome the Outcome to add to this Bin
     */
    public void addOutcome(int bin, Outcome outcome) {
        // Update Bin
        BINS.get(bin).add(outcome);

        // Update Map of all possible Outcomes
        ALL_OUTCOMES.put(outcome.toString(), outcome);
    }

    /**
     * Generates a random number between 0 and 37, and returns the randomly selected Bin.
     *
     * @return a Bin selected at random from the wheel
     */
    public Bin next() {
        return BINS.get(RNG.nextInt(38));
    }

    /**
     * Generates a random number between 0 and 37, and returns the randomly selected Bin.
     *
     * @param bin bin number, in the range zero to 37 inclusive
     * @return the requested Bin
     */
    public Bin getBin(int bin) {
        return BINS.get(bin);
    }

    /**
     * Get all outcomes previously added to the wheel that contains the queried name.
     *
     * @param name the name the matching outcomes must contain
     * @return set of Outcomes with the queried name
     */
    public List<Outcome> getOutcomes(String name) {
        List<Outcome> result = new ArrayList<>();
        for (Map.Entry<String, Outcome> entry : ALL_OUTCOMES.entrySet()) {
            if (entry.getKey().contains(name)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }
}
