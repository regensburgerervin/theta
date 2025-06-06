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
package hu.bme.mit.theta.frontend.transformation.model.types.complex.integer.clonglong;

import hu.bme.mit.theta.frontend.ParseContext;
import hu.bme.mit.theta.frontend.transformation.model.types.complex.integer.CInteger;
import hu.bme.mit.theta.frontend.transformation.model.types.simple.CSimpleType;

public abstract class CLongLong extends CInteger {

    private static final int RANK = 50;

    protected CLongLong(CSimpleType origin, ParseContext parseContext) {
        super(origin, parseContext);
        rank = RANK;
    }

    public <T, R> R accept(CComplexTypeVisitor<T, R> visitor, T param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getTypeName() {
        return "longlong";
    }

    @Override
    public CInteger getSignedVersion() {
        return new CSignedLongLong(null, parseContext);
    }

    @Override
    public CInteger getUnsignedVersion() {
        return new CUnsignedLongLong(null, parseContext);
    }
}
