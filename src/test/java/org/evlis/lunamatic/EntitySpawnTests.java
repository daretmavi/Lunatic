package org.evlis.lunamatic;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.CreeperMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import org.bukkit.Difficulty;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.events.EntitySpawn;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EntitySpawnTests {

    private ServerMock server;
    private WorldMock world;
    private EntitySpawn entitySpawn;

    @BeforeEach
    void setUp() {
        System.out.println("CreeperSpawn test setup started");
        server = MockBukkit.mock();
        world = new WorldMock();
        String worldName = world.getName();
        server.addWorld(world);
        entitySpawn = new EntitySpawn();
        GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(true);
        GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonNow(true);
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(18200L);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @Disabled("Disabled due to unimplemented RegionScheduler in MockBukkit")
    void testCreeperSpawnDuringBloodMoonIsCancelled() {
        CreeperMock creeper = new CreeperMock(server, UUID.randomUUID());
        EntitySpawnEvent event = new EntitySpawnEvent(creeper);
        //entitySpawn.onEntitySpawn(event);
        try {
            entitySpawn.onEntitySpawn(event);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Exception thrown during event handler: " + t.getMessage());
        }
        assertTrue(event.isCancelled(), "Event should be cancelled when a Creeper spawns during Blood Moon");
    }
}