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
package hu.bme.mit.theta.solver.smtlib.solver.interpolation;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.solver.Interpolant;
import hu.bme.mit.theta.solver.ItpMarker;
import java.util.Map;

public class SmtLibInterpolant implements Interpolant {

    private final Map<ItpMarker, Expr<BoolType>> itpMap;

    public SmtLibInterpolant(final Map<ItpMarker, Expr<BoolType>> itpMap) {
        this.itpMap = itpMap;
    }

    @Override
    public Expr<BoolType> eval(final ItpMarker marker) {
        checkNotNull(marker);
        final Expr<BoolType> itpExpr = itpMap.get(marker);
        checkNotNull(itpExpr);
        return itpExpr;
    }
}
