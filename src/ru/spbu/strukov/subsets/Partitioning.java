package ru.spbu.strukov.subsets;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created on 03.10.2019 20:57:30
 * @author Alexander Mikhailovich Kovshov
 */
public class Partitioning<T> implements Iterable<ArrayList<ArrayList<T>>> {
    
    private ArrayList<T> objects;
    private int[] objSetNum;
    
    public Partitioning(ArrayList<T> objects) {
        this.objects = objects;
        objSetNum = new int[objects.size()];
    }

    @Override
    public Iterator<ArrayList<ArrayList<T>>> iterator() {
        return new Iterator<ArrayList<ArrayList<T>>>() {

            @Override
            public boolean hasNext() {
                if (objSetNum[0] == 0) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public ArrayList<ArrayList<T>> next() {
                ArrayList<ArrayList<T>> ret = new ArrayList<>();
                for (int k = 0; k < objSetNum.length; k++) {
                        ArrayList<T> set = new ArrayList<>();
                    for (int i = 0; i < objSetNum.length; i++) {
                        if (objSetNum[i] == k) {
                            set.add(objects.get(i));
                        }
                    }
                    if (set.isEmpty()) break;
                    ret.add(set);
                }
                
                int l = 0;
                int max = 0;
                for (int i = 1; i < objSetNum.length; i++) {
                    if (objSetNum[i] <= max) {
                        l = i;
                    } else {
                        max = objSetNum[i];
                    }
                }
                objSetNum[l] += 1;
                for (int i = l + 1; i < objSetNum.length; i++) {
                    objSetNum[i] = 0;
                }
                return ret;
            }
        };
    }

    public static void main(String[] args) {
        ArrayList<Integer> obj = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            obj.add(i);
        }
        Partitioning<Integer> partitions = new Partitioning<>(obj);
        for (ArrayList<ArrayList<Integer>> partition : partitions) {
            for (ArrayList<Integer> set : partition) {
                System.out.print("(");
                for (Integer el : set) {
                    System.out.print(el);
                }
                System.out.print(")");
            }
            System.out.println("");
        }
    }

}
