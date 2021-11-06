package assignment;
// merges the SortedListInterface and Iterable Interface Into one single interface to constrain generic types
public interface IterableSortedListInterface<T extends Comparable<? super T>> extends SortedListInterface<T>, Iterable<T> {

}
