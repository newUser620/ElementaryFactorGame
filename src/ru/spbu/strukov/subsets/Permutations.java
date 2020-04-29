package ru.spbu.strukov.subsets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 06.10.2019 2:19:06
 * @author Alexander Mikhailovich Kovshov
 */
public class Permutations<T> implements Iterable<List<T>> {

    private List<T> objects;
    private int[] objSetNum;

    public Permutations(List<T> objects) {
        this.objects = objects;
        objSetNum = new int[objects.size()];
        for (int i = 0; i < objSetNum.length; i++) {
            objSetNum[i] = i;
        }
    }
    
    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {

            @Override
            public boolean hasNext() {
                if (objSetNum[0] < objSetNum.length) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public List<T> next() {
                ArrayList<T> ret = new ArrayList<>();
                for (int i = 0; i < objSetNum.length; i++) {
                    ret.add(objects.get(objSetNum[i]));
                }
                
                NEXT:
                {
                    int l = objSetNum.length - 2;
                    while (objSetNum[l] > objSetNum[l + 1]) {
                        if (--l < 0) {
                            objSetNum[0] = objSetNum.length;
                            break NEXT;
                        }
                    }
                    int i = objSetNum.length - 1;
                    while (objSetNum[i] < objSetNum[l]) {
                        i--;
                    }
                    int temp = objSetNum[i];
                    objSetNum[i] = objSetNum[l];
                    objSetNum[l] = temp;
                    i = objSetNum.length - 1;
                    l++;
                    while (l < i) {
                        temp = objSetNum[i];
                        objSetNum[i] = objSetNum[l];
                        objSetNum[l] = temp;
                        l++;
                        i--;
                    }
                    
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
        Permutations<Integer> sets = new Permutations(obj);
        for (List<Integer> set : sets) {
            System.out.print("(");
            for (Integer el : set) {
                System.out.print(el);
            }
            System.out.println(")");
        }
//        ArrayList<Integer> ns = new ArrayList<>();
//        sets = new Permutations(obj);
//        for (List<Integer> set : sets) {
//            String s = "";
//            for (Integer el : set) {
//                s += el.toString();
//            }
//            ns.add(new Integer(s));
//            System.out.println(s);
//        }
//        for (int i = 1; i < ns.size(); i++) {
//            System.out.println(ns.get(i));
//            if (ns.get(i) <= ns.get(i - 1))    
//                System.out.println(ns.get(i - 1) + " " + ns.get(i));   
//        }
//        System.out.println(ns.size());
    }

}



/*
0123    0123        0123
0132    0132        0132
0213    0213        0312
0231    0312        0321
0312    0231        3021
0321    0321        3012
1023                3102
1032
1203
1230
1302
1320
2013
2031
2103
2130
2301
2310
3012
3021
3102
3120
3201
3210
*/
