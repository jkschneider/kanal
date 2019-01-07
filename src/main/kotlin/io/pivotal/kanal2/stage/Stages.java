package io.pivotal.kanal2.stage;

import io.pivotal.kanal.model.ExpressionPrecondition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Stages {
    public static StageGraph checkPreconditions(String name, String... expressions) {
        return StageGraph.of(new Stage.CheckPreconditions(name, Arrays.stream(expressions)
                .map(ExpressionPrecondition::new)
                .collect(Collectors.toList())));
    }

    public static StageGraph deploy(String name) {
        return StageGraph.of(Stage.DeployServerGroup.builder()
                .name(name)
                .build());
    }

    public static StageGraph destroy(String name, CloudProvider provider, String credentials, List<String> regions, String condition) {
        return StageGraph.of(Stage.DestroyService.builder()
                .name(name)
                .cloudProvider(provider.code())
                .credentials(credentials)
                .regions(regions)
                .condition(condition)
                .build());
    }

    public static StageGraph parallel(StageGraph... stages) {
        return parallel(Arrays.asList(stages));
    }

    public static StageGraph parallel(Iterable<StageGraph> stages) {
        return StageGraph.of(new Stage.Parallel(stages));
    }

    public static StageGraph of(Stage stage) {
        return StageGraph.of(stage);
    }
}
