/*
 * Copyright 1997-2018 Optimatika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.ann;

import static org.ojalgo.constant.PrimitiveMath.*;
import static org.ojalgo.function.PrimitiveFunction.*;

import java.util.function.UnaryOperator;

import org.ojalgo.access.Access1D;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.random.Normal;

final class Layer implements UnaryOperator<Access1D<Double>> {

    private UnaryFunction<Double> myActivator;
    private final PrimitiveDenseStore myBias;
    private final PrimitiveDenseStore myOutput;
    private final PrimitiveDenseStore myWeights;

    Layer(int numberOfInputs, int numberOfOutputs, UnaryFunction<Double> activator) {

        super();

        myWeights = PrimitiveDenseStore.FACTORY.makeZero(numberOfInputs, numberOfOutputs);
        myBias = PrimitiveDenseStore.FACTORY.makeZero(1, numberOfOutputs);
        myOutput = PrimitiveDenseStore.FACTORY.makeZero(1, numberOfOutputs);

        myActivator = activator;
    }

    public Access1D<Double> apply(Access1D<Double> input) {
        myWeights.premultiply(input).operateOnMatching(ADD, myBias).operateOnAll(myActivator).supplyTo(myOutput);
        return myOutput;
    }

    UnaryFunction<Double> getActivator() {
        return myActivator;
    }

    void initialise() {

        Normal generator = new Normal(ONE / myWeights.countRows(), HALF);

        myWeights.fillAll(generator);
    }

    void setActivator(UnaryFunction<Double> activator) {
        myActivator = activator;
    }

    void setWeight(int input, int output, double weight) {
        myWeights.set(input, output, weight);
    }

    void setBias(int output, double bias) {
        myBias.set(output, bias);
    }

}
