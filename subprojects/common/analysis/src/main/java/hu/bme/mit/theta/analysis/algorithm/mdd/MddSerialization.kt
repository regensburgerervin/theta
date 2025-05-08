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

package hu.bme.mit.theta.analysis.algorithm.mdd

import hu.bme.mit.delta.java.mdd.MddHandle
import hu.bme.mit.delta.java.mdd.MddVariable
import hu.bme.mit.theta.analysis.algorithm.mdd.expressionnode.LitExprConverter
import hu.bme.mit.theta.core.decl.Decl
import java.io.File
import java.util.*

class Assignment {
    var mddVariable: MddVariable
    var value: Int

    constructor(mddVariable: MddVariable, value: Int) {
        this.mddVariable = mddVariable
        this.value = value
    }
}

fun toValuation(assignments: Stack<Assignment>): Map<MddVariable, Int> {
    val map = mutableMapOf<MddVariable, Int>()
    assignments.stream().forEach { ass ->
        map.put(ass.mddVariable, ass.value)
    }
    return map
}

fun collect(node: MddHandle): Set<Map<MddVariable, Int>> {
    val assignments = Stack<Assignment>()
    val valuations = mutableSetOf<Map<MddVariable, Int>>()

    collect(node, assignments, valuations)

    return valuations
}

fun collect(
    node: MddHandle,
    assignments: Stack<Assignment>,
    valuations: MutableSet<Map<MddVariable, Int>>) {

    if (node.isTerminal) {
        valuations.add(toValuation(assignments))
    } else {
        if (!node.defaultValue().isTerminalZero) {

            collect(node.defaultValue() as MddHandle, assignments, valuations)

        } else {
            val cursor = node.cursor()
            while (cursor.moveNext()) {
                assert(cursor.value() != null)

                assignments.push(
                    Assignment(
                        node.variableHandle.variable.get(),
                        cursor.key()))

                collect(cursor.value(), assignments, valuations)

                assignments.pop()
            }
        }
    }
}

fun escapeCsv(value: String): String {
    val needsQuoting = value.contains(",") || value.contains("\"") || value.contains("\n")
    val escaped = value.replace("\"", "\"\"")
    return if (needsQuoting) "\"$escaped\"" else escaped
}

@JvmOverloads
fun serialyzeValuations(valuations: Set<Map<MddVariable, Int>>, fileName: String = "output.csv") {
    if (valuations.isEmpty()) return

    val variables = valuations.first().keys.sortedBy { (it.getTraceInfo() as Decl<*>).name }

    val builder = StringBuilder()

    builder.append(variables.joinToString(",") { escapeCsv((it.traceInfo as Decl<*>).name) })
    builder.append("\n")

    for (valuation in valuations) {
        val row = variables.map { LitExprConverter.toLitExpr(valuation[it]!!, (it.getTraceInfo() as Decl<*>).type)?.toString() ?: "" }
        builder.append(row.joinToString(",") { escapeCsv(it) })
        builder.append("\n")
    }

    File(fileName).writeText(builder.toString())
}