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
        System.out.println("Test setup started");
        server = MockBukkit.mock();
        world = new WorldMock();
        server.addWorld(world);
        entitySpawn = new EntitySpawn();
        GlobalVars.bloodMoonToday = true;
        GlobalVars.bloodMoonNow = true;
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(18200L);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
        GlobalVars.bloodMoonToday = false;
        GlobalVars.bloodMoonNow = false;
    }

    @Test
    void testCreeperSpawnDuringBloodMoonIsCancelled() {
        CreeperMock creeper = new CreeperMock(server, UUID.randomUUID());
        EntitySpawnEvent event = new EntitySpawnEvent(creeper);
        System.out.println("Test creeper 001");
        //entitySpawn.onEntitySpawn(event);
        try {
            entitySpawn.onEntitySpawn(event);
            System.out.println("Test creeper 002");
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Exception thrown during event handler: " + t.getMessage());
        }
        System.out.println("Test creeper 002");
        assertTrue(event.isCancelled(), "Event should be cancelled when a Creeper spawns during Blood Moon");
    }
}