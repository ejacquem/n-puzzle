package Node;
import java.util.List;

public interface State {
    List<State> neighbors();
    int heuristic(State goal);
    boolean isGoal(State goal);
    void print();
}