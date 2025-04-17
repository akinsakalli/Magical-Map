import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<AnyType> implements Iterable<AnyType> {
    private static class Node<AnyType> {
        public AnyType element;
        public Node<AnyType> next;
        public Node( AnyType element, Node<AnyType> next ) {
            this.element = element;
            this.next = next;
        }
    }
    private Node<AnyType> head;
    private Node<AnyType> tail;
    public MyLinkedList() {
        head = null;
        tail = null;
    }
    public boolean contains(AnyType element) {
        if (head == null)
            return false;
        Node<AnyType> node = head;
        while (node != null) {
            if (node.element.equals(element)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }
    public void add(AnyType element) {
        if (head == null) {
            head = new Node<>(element, null);
            tail = head;
        }
        else if (head == tail) {
            tail = new Node<>(element, null);
            head.next = tail;
        }
        else {
            tail.next = new Node<>(element, null);
            tail = tail.next;
        }
    }

    @Override
    public Iterator<AnyType> iterator() {
        return new MyLinkedListIterator();
    }
    private class MyLinkedListIterator implements Iterator<AnyType> {
        private Node<AnyType> current;
        public MyLinkedListIterator() {
            current = head;
        }
        @Override
        public boolean hasNext() {
            return current != null;
        }
        @Override
        public AnyType next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            AnyType element = current.element;
            current = current.next;
            return element;
        }
    }

}
