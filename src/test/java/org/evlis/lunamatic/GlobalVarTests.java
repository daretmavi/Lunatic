package org.evlis.lunamatic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Difficulty;
import org.evlis.lunamatic.events.EntitySpawn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

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
        assertEquals(false, GlobalVars.bloodMoonToday);
        assertEquals(false, GlobalVars.harvestMoonToday);
        assertEquals(false, GlobalVars.bloodMoonNow);
        assertEquals(false, GlobalVars.harvestMoonNow);
    }

}
