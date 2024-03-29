/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Usuario
 */

/**

 * Class to sort the indexes of an array based upon their values. Note the array or Collection passed

 * into the constructor is not itself sorted. 

 * doubles, 

 * @author G, Cope

 *

 */

public class IndexSorter<T extends Comparable<T>> implements Comparator<Integer>{

	

	private final T[] values;

	

	private final Integer[] indexes;

	

	/**

	 * Constructs a new IndexSorter based upon the parameter array.

	 * @param d

	 */

	public IndexSorter(T[] d){

		this.values = d;

		indexes = new Integer[this.values.length];

		for ( int i = 0; i < indexes.length; i++ ){

			indexes[i] = i;

		}

	}

	

	/**

	 * Constructs a new IndexSorter based upon the parameter List.

	 * @param d

	 */

	public IndexSorter(List<T> d){

		this.values = (T[])d.toArray();

		for ( int i = 0; i < values.length; i++ ){

			values[i] = d.get(i);

		}

		indexes = new Integer[this.values.length];

		for ( int i = 0; i < indexes.length; i++ ){

			indexes[i] = i;

		}

	}



	/**

	 * Sorts the underlying index array based upon the values provided in the constructor. The underlying value array is not sorted. 

	 */

	public void sort(){

		Arrays.sort(indexes, this);
                             

	}	

	

	/**

	 * Retrieves the indexes of the array. The returned array is sorted if this object has been sorted.

	 * @return The array of indexes.

	 */

	public Integer[] getIndexes(){

		return indexes;

	}

	

	/**

	 * Compares the two values at index arg0 and arg0

	 * @param arg0 The first index

	 * @param arg1 The second index

	 * @return The result of calling compareTo on T objects at position arg0 and arg1

	 */

	public int compare(Integer arg0, Integer arg1) {

		T d1 = values[arg0];

		T d2 = values[arg1];

		return d1.compareTo(d2);

	}
       


}