package io.pivotal.kanal2.stage;

import io.pivotal.kanal.model.Condition;
import io.pivotal.kanal.model.ExpressionCondition;
import io.pivotal.kanal.model.Precondition;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

public abstract class Stage {
    private static class NamedStage extends Stage {
        private final String name;

        protected NamedStage(String name) {
            this.name = name;
        }
    }

    private static class ProviderStage extends NamedStage {
        private final String cloudProvider;

        private ProviderStage(String name, String cloudProvider) {
            super(name);
            this.cloudProvider = cloudProvider;
        }
    }

    public static class DeployService extends NamedStage {
        @Builder(toBuilder = true)
        public DeployService(String name) {
            super(name);
        }
    }

    public static class DeployServerGroup extends NamedStage {
        @Builder(toBuilder = true)
        public DeployServerGroup(String name) {
            super(name);
        }
    }

    public static class Wait extends NamedStage {
        @Builder(toBuilder = true)
        public Wait(String name) {
            super(name);
        }
    }

    public static class ManualJudgment extends NamedStage {
        @Builder(toBuilder = true)
        public ManualJudgment(String name) {
            super(name);
        }
    }

    public static class Canary extends NamedStage {
        @Builder(toBuilder = true)
        public Canary(String name) {
            super(name);
        }
    }

    public static class Webhook extends NamedStage {
        @Builder(toBuilder = true)
        private Webhook(String name) {
            super(name);
        }
    }

    public static class DestroyService extends ProviderStage {
        String credentials;
        List<String> regions;
        Condition condition;

        @Builder(toBuilder = true)
        public DestroyService(String name, String cloudProvider, String credentials, List<String> regions, String condition) {
            super(name, cloudProvider);
            this.credentials = credentials;
            this.regions = regions;
            this.condition = new ExpressionCondition(condition);
        }
    }

    public static class CheckPreconditions extends NamedStage {
        private final List<Precondition> preconditions;

        @Builder(toBuilder = true)
        public CheckPreconditions(String name, List<Precondition> preconditions) {
            super(name);
            this.preconditions = preconditions;
        }
    }

    static class Parallel extends Stage {
        private final Iterable<StageGraph> stages;

        public Parallel(Iterable<StageGraph> stages) {
            this.stages = stages;
        }
    }
}
