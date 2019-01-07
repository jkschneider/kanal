package io.pivotal.kanal2.stage;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static io.pivotal.kanal2.stage.CloudProvider.CloudFoundry;
import static java.util.Collections.singletonList;
import static java.util.stream.IntStream.range;

class StagesTest {
    @Test
    void usingStagesDotDestroy() {
        Stages
                .checkPreconditions("preconditions", "${true}", "${2 < 1}")
                .parallel(range(1, 4)
                        .mapToObj(n -> Stages.destroy(
                                "Destroy Service " + n + " Before",
                                CloudFoundry,
                                "${trigger['parameters']['account'] }",
                                singletonList("${trigger['parameters']['region'] }"),
                                "${" +
                                        "trigger['parameters']['destroyServicesBefore']=='true' && " +
                                        "trigger['parameters']['serviceName" + n + "']!='none' && " +
                                        "trigger['parameters']['serviceName" + n + "']!=\"\"" +
                                        "}"))
                        .collect(Collectors.toList())
                );
    }

    @Test
    void usingDestroyServiceBuilder() {
        Stages
                .checkPreconditions("preconditions", "${true}", "${2 < 1}")
                .parallel(range(1, 4)
                        .mapToObj(n -> Stages.of(Stage.DestroyService.builder()
                                .name("Destroy Service " + n + " Before")
                                .cloudProvider(CloudFoundry.code())
                                .credentials("${trigger['parameters']['account'] }")
                                .regions(singletonList("${trigger['parameters']['region'] }"))
                                .condition("${" +
                                        "trigger['parameters']['destroyServicesBefore']=='true' && " +
                                        "trigger['parameters']['serviceName" + n + "']!='none' && " +
                                        "trigger['parameters']['serviceName" + n + "']!=\"\"" +
                                        "}"
                                )
                                .build()))
                        .collect(Collectors.toList())
                );
    }
}

