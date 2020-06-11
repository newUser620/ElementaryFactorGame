package ru.spbu.user.subsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Subsets<T> implements Iterable<List<T>> {

    private List<T> objects;
    private int[] objSetNum;

    public Subsets(List<T> objects) {
        this.objects = objects;
        objSetNum = new int[objects.size()];
        Arrays.fill(objSetNum, -1);
        objSetNum[objSetNum.length - 1] = 0;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {

            @Override
            public boolean hasNext() {
                if (objSetNum[0] < 1) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public List<T> next() {
                ArrayList<T> ret = new ArrayList<>();
                for (int i = 0; i < objSetNum.length; i++) {
                    if (objSetNum[i] != -1) {
                        ret.add(objects.get(objSetNum[i]));
                    }
                }

                int l = objSetNum.length - 1;
                for (;;) {
                    if (objSetNum[l]++ < l) {
                        break;
                    } else {
                        if (--l < 0) {
                            return ret;
                        }
                    }
                }
                for (int i = l + 1; i < objSetNum.length; i++) {
                    objSetNum[i] = objSetNum[i - 1] + 1;
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
        Subsets<Integer> sets = new Subsets(obj);
        for (List<Integer> set : sets) {
            System.out.print("(");
            for (Integer el : set) {
                System.out.print(el);
            }
            System.out.println(")");
        }

    }

}

/*
---0
---1
---2
---3
--01
--02
--03
--12
--13
--23
-012
-013
-023
-123
0123
*/

