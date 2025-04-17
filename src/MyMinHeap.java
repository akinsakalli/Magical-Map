public class MyMinHeap<E extends Comparable<? super E>> {
    private static final int DEFAULT_CAPACITY = 10;
    private int currentSize;
    private E[] array;
    public MyMinHeap( ) {
        this( DEFAULT_CAPACITY );
    }
    public MyMinHeap( int capacity ) {
        currentSize = 0;
        array = (E[])(new Comparable[ capacity + 1 ]);
    }
    public void insert(E element) {
        if( currentSize == array.length - 1 )
            enlargeArray( array.length * 2 + 1 );

        // Percolate up algorithm
        int hole = ++currentSize;
        for( array[ 0 ] = element; element.compareTo( array[ hole / 2 ] ) < 0; hole /= 2 ) {
            array[ hole ] = array[ hole / 2 ];
        }
        array[ hole ] = element;
    }
    public boolean contains(E element) {
        for (int i = 1; i < currentSize + 1; i++) {
            if (array[i].equals(element))
                return true;
        }
        return false;
    }
    public void percolateUp(E element) {
        for (int i = 1; i < currentSize + 1; i++) {
            if (array[i].equals(element)) {
                percolateUp(i);
            }
        }
    }
    private void percolateUp(int hole) {
        E tmp = array[hole];

        for(array[0] = tmp; tmp.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2]; // Move the parent down
        }

        array[hole] = tmp;
    }
    private void enlargeArray( int newSize ) {
        E[] old = array;
        array = (E []) new Comparable[ newSize ];
        for( int i = 0; i < old.length; i++ )
            array[ i ] = old[ i ];
    }
    public E findMin( ) {
        if( isEmpty( ) )
            return null;
        return array[ 1 ];
    }
    public E deleteMin( ) {
        if( isEmpty( ) )
            return null;

        E minItem = findMin( );
        array[ 1 ] = array[currentSize];
        currentSize--;
        percolateDown( 1 );

        return minItem;
    }
    public boolean isEmpty( ) { return currentSize == 0; }
    private void percolateDown( int hole ) {
        int child;
        E tmp = array[ hole ];

        for( ; hole * 2 <= currentSize; hole = child ) {
            child = hole * 2;
            if( child != currentSize &&
                    array[ child + 1 ].compareTo( array[ child ] ) < 0 )
                child++;
            if( array[ child ].compareTo( tmp ) < 0 )
                array[ hole ] = array[ child ];
            else
                break;
        }
        array[ hole ] = tmp;
    }
}