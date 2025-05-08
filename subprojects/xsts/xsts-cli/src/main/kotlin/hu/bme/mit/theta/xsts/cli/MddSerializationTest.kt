import hu.bme.mit.delta.java.mdd.GraphvizSerializer
import hu.bme.mit.delta.java.mdd.JavaMddFactory
import hu.bme.mit.delta.java.mdd.*
import hu.bme.mit.delta.mdd.LatticeDefinition
import hu.bme.mit.delta.mdd.MddBuilder
import hu.bme.mit.theta.analysis.algorithm.mdd.collect
import hu.bme.mit.theta.analysis.algorithm.mdd.serialyzeValuations
import java.util.*
import java.util.List
import java.io.File


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
fun main() {

    val mddGraph: MddGraph<Boolean> = JavaMddFactory.getDefault().createMddGraph(LatticeDefinition.forSets())
    val variableOrder: MddVariableOrder = JavaMddFactory.getDefault().createMddVariableOrder(mddGraph)

    // MddVariableDescriptor.create(traceInfo, domainSize) - domainSize is 0 if unbounded
    val z: MddVariable = variableOrder.createOnTop(MddVariableDescriptor.create("z", 0))
    val y: MddVariable = variableOrder.createOnTop(MddVariableDescriptor.create("y", 0))
    val x: MddVariable = variableOrder.createOnTop(MddVariableDescriptor.create("x", 0))

    val z_y_x: MddSignature = variableOrder.defaultSetSignature

    val node1 = MddBuilder<Boolean>(z_y_x).build(List.of(
        arrayOf(1, 0, 4),
        arrayOf(2, 0, 4),
        arrayOf(4, 0, 6),
        arrayOf(4, 0, 0),
        arrayOf(4, 0, 4)
    ), true) as MddHandle

    val valuations = collect(node1)
    println(valuations)
    serialyzeValuations(valuations)

    // Generate GraphViz DOT representation (reduced = false)...
    println(GraphvizSerializer.serialize(node1 as MddHandle, false))


}