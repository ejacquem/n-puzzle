import java.util.List;

import Node.NodeSearch;

public interface SearchAlgorithm {
    // SearchAlgorithm(NodeSearch start, NodeSearch goal);
    boolean solve();
    List<NodeSearch> getPath();
}

