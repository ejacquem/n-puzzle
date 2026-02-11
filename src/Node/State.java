package Node;
import java.util.List;

// you need to override equals(Object o) and hashCode() for implementation
public interface State {
    List<State> neighbors();
    int heuristic(State goal);
    void print();
}