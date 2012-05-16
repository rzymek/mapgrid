package bob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class BobMaps {
	static int[][]  scenos = {
			{8},
			{1,10},
			{3,4},
			{3,4,8,10},
			{5,7},
			{1,3},
			{1,2,6,9},
			{1,3,5,6,9},
			{6,8,9,2},
			{6,2,9,1},
			{1,3},
			{9,6},
			{1,3,9,6},
			{1,6},
			{2,4,6,9},
			{1},
			{5,7},
			{4,5,9,7,2}
	};
	
	public static void main(String[] args) {
		int[] have={1,2,3,4,5,8};
		for(int i=0;i<scenos.length;++i){
			int[] s = scenos[i];
			String ok="";
			for (int m : s) {
				if(contains(have, m))
					continue;
				else
					ok+=m+" ";
			}
			System.out.println("Sceno "+(i+1)+": missing "+ok);
		}
	}
	
	private static boolean contains(int[] s, int h) {
		for (int i : s) {
			if(i == h)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static void main1(String[] args) {		
		Set<Integer>[] usedTogether = (Set<Integer>[]) new Set<?>[11];
		Set<Integer>[] canBeOne = (Set<Integer>[]) new Set<?>[11];
		for(int i=1;i<usedTogether.length;++i) {
			usedTogether[i] = new TreeSet<Integer>();
			canBeOne[i] = new TreeSet<Integer>();
		}
		for (int[] sceno: scenos) {
			for (int map1 : sceno) {
				for (int map2 : sceno) {
					usedTogether[map1].add(map2);
					usedTogether[map2].add(map1);
				}				
			}
		}
		for(int i=1;i<usedTogether.length;++i) {
			for(int j=1;j<usedTogether.length;++j) {
				if(!usedTogether[j].contains(i)) {
					canBeOne[i].add(j);
					canBeOne[j].add(i);
				}
			}
		}		
		for(int i=1;i<usedTogether.length;++i) {
			System.out.println(i+": "+usedTogether[i]);
		}		
		System.out.println();
		for(int i=1;i<canBeOne.length;++i) {
			System.out.println(i+":"+canBeOne[i]);
		}
		System.out.println();
		int[] maps = new int[11];
		for(int i=1;i<canBeOne.length;++i) {
			if(maps[i] != 0)
				continue;
			if(canBeOne[i].isEmpty()) {
				dump(maps);
				throw new RuntimeException("Failed on "+i);
			}
			Iterator<Integer> iterator = canBeOne[i].iterator();
			Integer next = iterator.next();
			maps[i] = next;
			maps[next] = i;
			iterator.remove();
			boolean remove = canBeOne[next].remove(i);
			if(!remove) {
				dump(maps);
				throw new RuntimeException("failed to remove "+i+" from "+next);
			}
		}
		
		dump(maps);
	}

	private static void dump(int[] maps) {
		for (int i = 1; i < maps.length; i++) {
			System.out.println(i+" = "+maps[i]);
		}
	}	
}
/*
1:4
2:3
3:2
4:1
5:8
6:[7]
7:[3, 6]
8:5
9:10[]
10:9[6, 7]

 */
