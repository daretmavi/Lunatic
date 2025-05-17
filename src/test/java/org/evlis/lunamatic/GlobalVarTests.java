package org.evlis.lunamatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GlobalVarTests {

    @Test
    @DisplayName("Verify that all global temporal flags initialize to FALSE:")
    public void allFlagsStartAsFalse() {
        assertEquals(false, GlobalVars.debug);
        assertEquals(false, GlobalVars.bloodMoonToday);
        assertEquals(false, GlobalVars.harvestMoonToday);
        assertEquals(false, GlobalVars.bloodMoonNow);
        assertEquals(false, GlobalVars.harvestMoonNow);
    }
}
