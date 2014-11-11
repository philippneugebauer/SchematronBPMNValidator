package de.uniba.dsg.ppn.ba;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uniba.dsg.bpmn.ValidationResult;
import de.uniba.dsg.bpmn.Violation;
import de.uniba.dsg.ppn.ba.helper.BpmnValidationException;

public class Ext102 extends TestCase {

    @Test
    public void testConstraintFail() throws BpmnValidationException {
        ValidationResult result = verifyInValidResult(createFile("Fail.bpmn"),
                2);
        Violation v = result.getViolations().get(0);
        assertEquals(
                "A message flow must connect ’InteractionNodes’ from different Pools",
                v.getMessage());
        assertEquals("//bpmn:messageFlow[0]", v.getxPath());
        assertEquals(7, v.getLine());
        v = result.getViolations().get(1);
        assertEquals("A Start Event MUST NOT be a source for a message flow",
                v.getMessage());
        assertEquals("//bpmn:messageFlow[@sourceRef][0]", v.getxPath());
        assertEquals(7, v.getLine());
    }

    @Override
    protected String getExtNumber() {
        return "102";
    }
}
