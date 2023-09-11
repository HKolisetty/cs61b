import edu.princeton.cs.algs4.In;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    @DisplayName("ArrayDeque has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    void to_list_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        assertThat(arr1.toList()).isEmpty();
    }
    @Test
    void add_first_only() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addFirst(2);
        arr1.addFirst(1);
        arr1.addFirst(0);
        assertThat(arr1.toList()).containsExactly(0,1,2);
    }
    @Test
    void add_last_only() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        assertThat(arr1.toList()).containsExactly(0,1,2);
    }
    @Test
    void add_last_then_first() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.addFirst(0);
        assertThat(arr1.toList()).containsExactly(0,1,2);
    }
    @Test
    void add_first_then_last() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addFirst(1);
        arr1.addFirst(0);
        arr1.addLast(2);
        assertThat(arr1.toList()).containsExactly(0,1,2);
    }
    @Test
    void add_first_to_resize() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 9; i >= 0; i--) {
            arr1.addFirst(i);
        }
        assertThat(arr1.toList()).containsExactly(0,1,2,3,4,5,6,7,8,9);
    }
    @Test
    void add_last_to_resize() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            arr1.addLast(i);
        }
        assertThat(arr1.toList()).containsExactly(0,1,2,3,4,5,6,7,8,9);
    }
    @Test
    void get_normal() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        assertThat(arr1.get(0)).isEqualTo(0);
    }
    @Test
    void get_oob_large() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        assertThat(arr1.get(43)).isNull();
    }
    @Test
    void get_oob_negative() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        assertThat(arr1.get(-2)).isNull();
    }
    @Test
    void is_empty_true() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        assertThat(arr1.isEmpty()).isTrue();
    }
    @Test
    void is_empty_false() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        assertThat(arr1.isEmpty()).isFalse();
    }
    @Test
    void size() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        assertThat(arr1.size()).isEqualTo(3);
    }
    @Test
    void size_after_remove_to_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.removeLast();
        assertThat(arr1.size()).isEqualTo(0);
    }
    @Test
    void size_after_remove_from_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        assertThat(arr1.size()).isEqualTo(0);
    }
    @Test
    void remove_first() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeFirst();
        assertThat(arr1.toList()).containsExactly(1,2);
    }
    @Test
    void remove_last() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeLast();
        assertThat(arr1.toList()).containsExactly(0,1);
    }
    @Test
    void remove_first_to_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeFirst();
        arr1.removeFirst();
        arr1.removeFirst();
        assertThat(arr1.toList()).isEmpty();
    }
    @Test
    void remove_first_to_one() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeFirst();
        arr1.removeFirst();
        assertThat(arr1.toList()).containsExactly(2);
    }
    @Test
    void remove_last_to_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeLast();
        arr1.removeLast();
        arr1.removeLast();
        assertThat(arr1.toList()).isEmpty();
    }
    @Test
    void remove_last_to_one() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.removeLast();
        arr1.removeLast();
        assertThat(arr1.toList()).containsExactly(0);
    }
    @Test
    void add_first_after_remove_to_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.removeLast();
        arr1.addFirst(1);
        assertThat(arr1.toList()).containsExactly(1);
    }
    @Test
    void add_last_after_remove_to_empty() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.removeLast();
        arr1.addLast(1);
        assertThat(arr1.toList()).containsExactly(1);
    }
    @Test
    void add_last_remove_last() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            arr1.addLast(i);
        }
        for (int i = 0; i < 17; i++) {
            arr1.removeLast();
        }
        assertThat(arr1.toList()).containsExactly(0,1,2);
    }
    @Test
    void add_first_remove_last() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            arr1.addFirst(i);
        }
        for (int i = 0; i < 17; i++) {
            arr1.removeLast();
        }
        for (int i = 0; i < 2; i++) {
            arr1.addFirst(i);
        }
        assertThat(arr1.toList()).containsExactly(1,0,19,18,17);
    }
    @Test
    void add_first_remove_first() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 0; i < 60; i++) {
            arr1.addFirst(i);
        }
        for (int i = 0; i < 57; i++) {
            arr1.removeFirst();
        }
        assertThat(arr1.toList()).containsExactly(2,1,0);
    }
    @Test
    void add_last_remove_first() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            arr1.addLast(i);
        }
        for (int i = 0; i < 17; i++) {
            arr1.removeFirst();
        }
        assertThat(arr1.toList()).containsExactly(19,18,17);
    }
    @Test
    void myget() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addFirst(0);
        arr1.addLast(1);
        arr1.removeLast();
        arr1.addLast(3);
        arr1.removeLast();
        arr1.addLast(5);
        arr1.addLast(6);
        arr1.addFirst(7);
        assertThat(arr1.get(0)).isEqualTo(7);
    }
    @Test
    void myget2() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addLast(1);
        arr1.addLast(2);
        arr1.addLast(3);
        arr1.addLast(4);
        arr1.addLast(5);
        arr1.removeFirst();
        arr1.addLast(7);
        arr1.addLast(8);
        arr1.addLast(9);
        assertThat(arr1.toList()).containsExactly(1,2,3,4,5,7,8,9);
    }
    @Test
    void myget3() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.addFirst(2);
        arr1.addFirst(3);
        arr1.removeLast();
        arr1.addLast(6);
        arr1.removeLast();
        arr1.removeFirst();
        arr1.addFirst(10);
        arr1.removeLast();
        arr1.addFirst(13);
        arr1.removeLast();
        arr1.addFirst(15);
        arr1.removeFirst();
        assertThat(arr1.get(0)).isEqualTo(13);
    }
    @Test
    void myget4() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addLast(0);
        arr1.removeFirst();
        arr1.addLast(2);
        arr1.removeFirst();
        arr1.addLast(4);
        arr1.addFirst(5);
        arr1.addFirst(6);
        arr1.removeLast();
        arr1.addLast(9);
        arr1.addLast(10);
        arr1.addLast(11);
        arr1.removeLast();
        arr1.addFirst(13);
        arr1.addFirst(14);
        arr1.addFirst(15);
        arr1.addFirst(16);
        arr1.addLast(18);
        arr1.addFirst(19);
        arr1.removeLast();
        arr1.addFirst(21);
        arr1.removeFirst();
        assertThat(arr1.removeLast()).isEqualTo(10);
    }
    @Test
    void myget5() {
        Deque<Integer> arr1 = new ArrayDeque<>();
        arr1.addFirst(0);
        arr1.addFirst(1);
        arr1.removeLast();
        arr1.addFirst(3);
        arr1.removeFirst();
        arr1.removeFirst();
        arr1.addFirst(7);
        arr1.addLast(8);
        arr1.addFirst(10);
        arr1.addFirst(11);
        arr1.addFirst(12);
        arr1.removeFirst();
        arr1.addFirst(14);
        arr1.addFirst(15);
        arr1.addLast(16);
        arr1.addFirst(17);
        arr1.removeLast();
        arr1.addFirst(19);
        arr1.addLast(20);
        arr1.addLast(21);
        arr1.addLast(22);
        assertThat(arr1.get(10)).isEqualTo(22);
    }
}
