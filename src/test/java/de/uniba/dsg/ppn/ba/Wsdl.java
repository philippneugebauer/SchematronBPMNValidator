package de.uniba.dsg.ppn.ba;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.uniba.dsg.bpmn.ValidationResult;
import de.uniba.dsg.bpmn.Violation;
import de.uniba.dsg.ppn.ba.helper.BpmnValidationException;

public class Wsdl extends TestCase {

    @Test
    public void testConstraintImportedWsdlFail() throws BpmnValidationException {
        ValidationResult result = verifyInValidResult(
                createFile("wsdl-fail.bpmn"), 4);
        Violation v = result.getViolations().get(0);
        assertTrue(v.getMessage().contains("cvc-complex-type.2.4.c:"));
        assertTrue(v.getMessage().contains("xs:schema"));
        assertViolation(v, 22);
        v = result.getViolations().get(1);
        assertTrue(v.getMessage().contains("cvc-complex-type.2.4.a:"));
        assertTrue(v.getMessage().contains("DAMAGEDinterface"));
        assertViolation(v, 40);
        v = result.getViolations().get(2);
        assertTrue(v.getMessage().contains("cvc-complex-type.3.2.2:"));
        assertTrue(v.getMessage().contains("DAMinterface"));
        assertTrue(v.getMessage().contains("service"));
        assertViolation(v, 72);
        v = result.getViolations().get(3);
        assertTrue(v.getMessage().contains("cvc-complex-type.4:"));
        assertTrue(v.getMessage().contains("interface"));
        assertTrue(v.getMessage().contains("service"));
        assertViolation(v, 72);
    }

    @Test
    public void testConstraintImportedWsdlSuccess()
            throws BpmnValidationException {
        verifyValidResult(createFile("wsdl-success.bpmn"));
    }

    private void assertViolation(Violation v, int line) {
        assertEquals("", v.getxPath());
        assertEquals("wsdl2primer-fail.wsdl", v.getFileName());
        assertEquals(line, v.getLine());
    }

    @Override
    protected String getExtNumber() {
        return "wsdl";
    }
}
