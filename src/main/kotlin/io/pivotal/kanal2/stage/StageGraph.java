package io.pivotal.kanal2.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

public class StageGraph {
    private final List<Stage> stages;
    private final Map<Stage, List<Stage>> stageDependencies;

    public StageGraph(List<Stage> stages, Map<Stage, List<Stage>> stageDependencies) {
        this.stages = stages;
        this.stageDependencies = stageDependencies;
    }

    public StageGraph checkPreconditions(String name, String... expressions) {
        return concat(Stages.checkPreconditions(name, expressions));
    }

    public StageGraph deploy(String name) {
        return concat(Stages.deploy(name));
    }

    public StageGraph destroyService(String name, CloudProvider provider, String credentials, List<String> regions, String condition) {
        return concat(Stages.destroy(name, provider, credentials, regions, condition));
    }

    public StageGraph parallel(StageGraph... stages) {
        return concat(Stages.parallel(stages));
    }

    public StageGraph parallel(Iterable<StageGraph> stages) {
        return concat(Stages.parallel(stages));
    }

    public StageGraph andThen(Stage stage) {
        return concat(StageGraph.of(stage));
    }

    public StageGraph concat(StageGraph sg) {
        if (sg.stages.isEmpty())
            return this;
        if (stages.isEmpty())
            return sg;

        List<Stage> sumStages = new ArrayList<>(stages.size() + sg.stages.size());
        Map<Stage, List<Stage>> sumStageDependencies = new HashMap<>(stageDependencies);

        sumStageDependencies.put(sg.first(), singletonList(last()));
        return new StageGraph(sumStages, sumStageDependencies);
    }

    private Stage first() {
        return stages.iterator().next();
    }

    private Stage last() {
        return stages.get(stages.size() - 1);
    }

    public static StageGraph of(Stage stage) {
        return new StageGraph(singletonList(stage), emptyMap());
    }
}
