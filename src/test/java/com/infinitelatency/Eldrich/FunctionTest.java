/* Licensed under Apache-2.0 */
package com.infinitelatency.Eldrich;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class FunctionTest {
    @Test
    public void functionObeysEquals() {
        EqualsVerifier.forClass(Function.class).verify();
    }

}