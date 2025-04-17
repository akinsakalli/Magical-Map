public class MyHashMap<E> {
    private static final int DEFAULT_TABLE_SIZE = 101;
    private MyLinkedList<E>[] listsArray;
    private int size;
    public MyHashMap( ) { this( DEFAULT_TABLE_SIZE ); }
    public MyHashMap( int size ) {
        listsArray = new MyLinkedList[ nextPrime( size ) ];
        for( int i = 0; i < listsArray.length; i++ )
            listsArray[i] = new MyLinkedList<>( );
    }
    public void insert( E element ) {
        MyLinkedList<E> list = listsArray[ hash(element) ];
        if(!list.contains(element) ) {
            list.add(element);
            size++;
            if(size > listsArray.length)
                rehash();
        }
    }
    public boolean contains(E element) {
        MyLinkedList<E> list = listsArray[ hash(element) ];
        return list.contains(element);
    }
    private void rehash( ) {
        MyLinkedList<E>[] oldLists = listsArray;
        
        listsArray = new MyLinkedList[ nextPrime( 2 * listsArray.length ) ];
        for( int j = 0; j < listsArray.length; j++ )
            listsArray[j] = new MyLinkedList<>( );

        size = 0;
        for( MyLinkedList<E> list : oldLists ) {
            for (E item : list)
                insert(item);
        }
    }
    private int hash(E element) {
        int hashVal = element.hashCode();

        hashVal %= listsArray.length;
        if( hashVal < 0 )
            hashVal += listsArray.length;

        return hashVal;
    }
    public static boolean isPrime(int number) {
        if (number <= 1)
            return false;
        if (number == 2)
            return true;
        if (number % 2 == 0)
            return false;
        int sqrt = (int) Math.sqrt(number);
        for (int i = 3; i <= sqrt; i += 2) {
            if (number % i == 0)
                return false;
        }
        return true;
    }
    public static int nextPrime(int number) {
        while (!isPrime(number)) {
            number++;
        }
        return number;
    }
}
