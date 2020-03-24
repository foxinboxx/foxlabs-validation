/*
 * Copyright (C) 2012 FoxLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.validation.constraint;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationException;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.common.Checks;

/**
 * This class provides base implementation of the <code>Constraint</code> that
 * uses another constraint to validate elements of a sequence.
 *
 * @author Fox Mulder
 * @param <V> The type of sequence to be validated
 * @param <E> The type of elements
 */
public abstract class SequenceElementConstraint<V, E> extends AbstractValidation<V> implements Constraint<V> {

    /**
     * Constraint to be used to validate elements of a sequence.
     */
    protected final Constraint<E> constraint;

    /**
     * Constructs a new <code>SequenceElementConstraint</code> with the
     * specified constraint of elements.
     *
     * @param constraint Constraint to be used to validate elements of a
     *        sequence.
     * @throws NullPointerException if the specified constraint is
     *         <code>null</code>.
     */
    protected SequenceElementConstraint(Constraint<E> constraint) {
        this.constraint = Checks.checkNotNull(constraint, "constraint");
    }

    /**
     * Returns constraint to be used to validate elements of a sequence.
     *
     * @return Constraint to be used to validate elements of a sequence.
     */
    public final Constraint<E> getConstraint() {
        return constraint;
    }

    /**
     * Appends <code>constraint</code> argument that contains constraint of
     * sequence elements.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("constraint", constraint);
        return true;
    }

    /**
     * Checks whether all elements of the specified sequence conforms to the
     * constraint of sequence elements.
     *
     * @param <T> The type of validated entity.
     * @param sequence Sequence whose elements to be checked.
     * @param context Validation context.
     * @return Sequence with possibly modified elements if constraint of
     *         sequence elements can modify values; unmodified sequence
     *         otherwise.
     * @throws ConstraintViolationException if validation of sequence elements
     *         fails.
     * @see #doValidate(Object, ValidationContext, List)
     */
    @Override
    public <T> V validate(V sequence, ValidationContext<T> context) {
        if (sequence == null)
            return sequence;
        Object index = context.getCurrentIndex();
        ValidationTarget target = context.getCurrentTarget();
        List<ConstraintViolationException> violations = new LinkedList<ConstraintViolationException>();
        try {
            sequence = doValidate(sequence, context, violations);
        } finally {
            context.setCurrentIndex(index);
            context.setCurrentTarget(target);
        }
        if (violations.isEmpty())
            return sequence;
        throw new ConstraintViolationException(this, context, sequence,
                new ValidationException(violations));
    }

    /**
     * Checks whether all elements of the specified sequence conforms to the
     * constraint of sequence elements. This method should not throw
     * <code>ConstraintViolationException</code> and all violations should be
     * stored in the specified list of violations.
     *
     * @param <T> The type of validated entity.
     * @param sequence Sequence whose elements to be checked.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Sequence with possibly modified elements if constraint of
     *         sequence elements can modify values; unmodified sequence
     *         otherwise.
     */
    protected abstract <T> V doValidate(V sequence, ValidationContext<T> context,
            List<ConstraintViolationException> violations);

}
