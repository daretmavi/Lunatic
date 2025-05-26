package org.evlis.lunamatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.bukkit.Difficulty;
import org.evlis.lunamatic.events.EntitySpawn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import java.util.Map;

public class GlobalVarTests {

    private ServerMock server;
    private WorldMock world;

    @BeforeEach
    void setUp() {
        System.out.println("Begin GlobalVar test setup");
        server = MockBukkit.mock();
        world = new WorldMock();
        server.addWorld(world);
        world.setTime(18200L);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Verify that all global temporal flags initialize to FALSE:")
    public void allFlagsStartAsFalse() {
        assertEquals(false, GlobalVars.debug);
        for (Map.Entry<String, GlobalVars.CurrentMoonState> entry : GlobalVars.currentMoonStateMap.entrySet()) {
            String worldName = entry.getKey();
            GlobalVars.CurrentMoonState state = entry.getValue();
            assertFalse(state.isBloodMoonToday());
            assertFalse(state.isHarvestMoonToday());
            assertFalse(state.isBloodMoonNow());
            assertFalse(state.isHarvestMoonNow());
        }
    }
}
