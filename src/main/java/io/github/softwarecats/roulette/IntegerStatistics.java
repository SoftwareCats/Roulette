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

import java.util.List;


/**
 * IntegerStatistics computes several simple descriptive statistics of Integer values in a List.
 * Like the java.util.Math class, this class simply has a bunch of ‘static’ methods.
 */
public class IntegerStatistics {

    /**
     * Returns the sum of elements in the {@link List<Integer>}.
     *
     * @param values the list
     * @return the sum of elements in the list
     */
    public static int sum(List<Integer> values) {
        return values.stream()
                .mapToInt(Integer::valueOf)
                .sum();
    }

    /**
     * Returns the number of elements in the list.  If the list contains
     * more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @param values the list
     * @return the number of elements in the list
     */
    public static int len(List<Integer> values) {
        return values.size();
    }

    /**
     * Computes the mean of the List of Integer values.
     *
     * @param values the list of values we are summarizing
     * @return the mean of the elements in the list
     */
    public static double mean(List<Integer> values) {
        return (double) sum(values) / len(values);
    }

    /**
     * Computes the standard deviation of the List of Integer values.
     *
     * @param values the list of values we are summarizing
     * @return the standard deviation of the elements in the list
     */
    public static double std(List<Integer> values) {
        double mean = mean(values);

        double sum = 0;
        for (Integer value : values) {
            double diff = value - mean;
            sum += diff * diff;
        }

        double variance = sum / (len(values) - 1);

        return Math.sqrt(variance);
    }
}
