/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.reactive.executor.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.vertx.deployment.VertxBuildItem;
import org.apache.camel.quarkus.core.Flags;
import org.apache.camel.quarkus.core.deployment.CamelReactiveExecutorBuildItem;
import org.apache.camel.quarkus.core.deployment.CamelServiceFilterBuildItem;
import org.apache.camel.quarkus.reactive.executor.ReactiveExecutorRecorder;

public class BuildProcessor {
    /*
     * The reactive executor is programmatically configured by the extension thus
     * we can safely prevent camel-quarkus-core to instantiate a default instance.
     */
    @BuildStep
    void serviceFilter(BuildProducer<CamelServiceFilterBuildItem> filterBuildItems) {
        filterBuildItems.produce(
            new CamelServiceFilterBuildItem(si -> si.path.endsWith("META-INF/services/org/apache/camel/reactive-executor"))
        );
    }

    @Record(value = ExecutionTime.RUNTIME_INIT, optional = true)
    @BuildStep(onlyIf = Flags.MainEnabled.class)
    CamelReactiveExecutorBuildItem reactiveExecutor(ReactiveExecutorRecorder recorder, VertxBuildItem vertx) {
        return new CamelReactiveExecutorBuildItem(recorder.createReactiveExecutor(vertx.getVertx()));
    }
}
