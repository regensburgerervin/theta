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
package hu.bme.mit.theta.core.type.fptype;

import static hu.bme.mit.theta.core.type.booltype.BoolExprs.Bool;
import static hu.bme.mit.theta.core.utils.TypeUtils.castFp;
import static hu.bme.mit.theta.core.utils.TypeUtils.checkAllTypesEqual;

import hu.bme.mit.theta.core.model.Valuation;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.abstracttype.LtExpr;
import hu.bme.mit.theta.core.type.booltype.BoolLitExpr;
import hu.bme.mit.theta.core.type.booltype.BoolType;

public final class FpLtExpr extends LtExpr<FpType> {

    private static final int HASH_SEED = 1745;
    private static final String OPERATOR_LABEL = "<";

    private FpLtExpr(final Expr<FpType> leftOp, final Expr<FpType> rightOp) {
        super(leftOp, rightOp);
        checkAllTypesEqual(leftOp, rightOp);
    }

    public static FpLtExpr of(final Expr<FpType> leftOp, final Expr<FpType> rightOp) {
        return new FpLtExpr(leftOp, rightOp);
    }

    public static FpLtExpr create(final Expr<?> leftOp, final Expr<?> rightOp) {
        final Expr<FpType> newLeftOp = castFp(leftOp);
        final Expr<FpType> newRightOp = castFp(rightOp);
        return FpLtExpr.of(newLeftOp, newRightOp);
    }

    @Override
    public BoolType getType() {
        return Bool();
    }

    @Override
    public BoolLitExpr eval(final Valuation val) {
        final FpLitExpr leftOpVal = (FpLitExpr) getLeftOp().eval(val);
        final FpLitExpr rightOpVal = (FpLitExpr) getRightOp().eval(val);

        return leftOpVal.lt(rightOpVal);
    }

    @Override
    public FpLtExpr with(final Expr<FpType> leftOp, final Expr<FpType> rightOp) {
        if (leftOp == getLeftOp() && rightOp == getRightOp()) {
            return this;
        } else {
            return FpLtExpr.of(leftOp, rightOp);
        }
    }

    @Override
    public FpLtExpr withLeftOp(final Expr<FpType> leftOp) {
        return with(leftOp, getRightOp());
    }

    @Override
    public FpLtExpr withRightOp(final Expr<FpType> rightOp) {
        return with(getLeftOp(), rightOp);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            final FpLtExpr that = (FpLtExpr) obj;
            return this.getLeftOp().equals(that.getLeftOp())
                    && this.getRightOp().equals(that.getRightOp());
        } else {
            return false;
        }
    }

    @Override
    protected int getHashSeed() {
        return HASH_SEED;
    }

    @Override
    public String getOperatorLabel() {
        return OPERATOR_LABEL;
    }
}
