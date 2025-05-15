/*
 *  Copyright 2025 Budapest University of Technology and Economics
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package hu.bme.mit.theta.xsts.analysis;

import static org.junit.Assert.assertTrue;

import hu.bme.mit.theta.analysis.algorithm.SafetyResult;
import hu.bme.mit.theta.analysis.algorithm.mdd.MddCex;
import hu.bme.mit.theta.analysis.algorithm.mdd.MddChecker.IterationStrategy;
import hu.bme.mit.theta.analysis.algorithm.mdd.MddProof;
import hu.bme.mit.theta.analysis.algorithm.mdd.MddSerializationKt;
import hu.bme.mit.theta.common.logging.ConsoleLogger;
import hu.bme.mit.theta.common.logging.Logger;
import hu.bme.mit.theta.solver.SolverPool;
import hu.bme.mit.theta.solver.z3legacy.Z3LegacySolverFactory;
import hu.bme.mit.theta.xsts.XSTS;
import hu.bme.mit.theta.xsts.analysis.mdd.XstsMddChecker;
import hu.bme.mit.theta.xsts.dsl.XstsDslManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collection;

public class XstsMddCheckerTest {

    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {

                    //                { "src/test/resources/model/trafficlight.xsts",
                    // "src/test/resources/property/green_and_red.prop", true},

                    {
                        "src/test/resources/model/trafficlight_v2.xsts",
                        "src/test/resources/property/green_and_red.prop",
                        true
                    },

                });
    }

    public static void runTestWithIterationStrategy(
            String filePath, String propPath, boolean safe, IterationStrategy iterationStrategy)
            throws Exception {
        final Logger logger = new ConsoleLogger(Logger.Level.SUBSTEP);

        XSTS xsts;
        try (InputStream inputStream =
                new SequenceInputStream(
                        new FileInputStream(filePath), new FileInputStream(propPath))) {
            xsts = XstsDslManager.createXsts(inputStream);
        }

        final SafetyResult<MddProof, MddCex> status;
        try (var solverPool = new SolverPool(Z3LegacySolverFactory.getInstance())) {
            final XstsMddChecker checker =
                    XstsMddChecker.create(xsts, solverPool, logger, iterationStrategy);
            status = checker.check(null);
            var statespace = status.getProof().getMdd();
            var valuations = MddSerializationKt.collect(statespace);
            MddSerializationKt.serializeValuations(valuations);
        }

        if (safe) {
            assertTrue(status.isSafe());
        } else {
            assertTrue(status.isUnsafe());
        }
    }
}
