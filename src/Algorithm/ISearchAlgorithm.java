package Algorithm;
import java.util.List;

import Node.NodeSearch;

public interface ISearchAlgorithm {
    boolean solve(NodeSearch start, NodeSearch goal);
    List<NodeSearch> getPath();
}

