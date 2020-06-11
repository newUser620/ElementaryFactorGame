/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spbu.user.Permutations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Permutations<T> implements Iterable<List<T>> {

    PermutationGenerator pGenerator;
    T[] elements;
    int[] indices;

    public Permutations(List<T> list) {
        pGenerator = new PermutationGenerator(list.size());
        elements = (T[]) list.toArray();
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {

            int pos = 0;

            @Override
            public boolean hasNext() {
                return pGenerator.hasMore();
            }

            @Override
            public List<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                indices = pGenerator.getNext();
                List<T> permutation = new ArrayList<>();
                for (int i = 0; i < indices.length; i++) {
                    permutation.add(elements[indices[i]]);
                }
                return permutation;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
