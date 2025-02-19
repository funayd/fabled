package studio.magemonkey.fabled.mobcast;

import org.bukkit.Location;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.skills.SkillShot;
import studio.magemonkey.fabled.testutil.MockedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MobCastTest extends MockedTest {

    private SkillShot skill;

    @Override
    public void preInit() {
        loadSkills("Brilliance Strike");
    }

    @BeforeEach
    public void setup() {
        skill = (SkillShot) plugin.getSkill("Brilliance Strike");
    }

    @AfterEach
    public void teardown() {
        world.getEntities().forEach(e -> e.remove());
    }

    @Test
    void passiveCanDamagePassive() {
        Sheep sheep  = world.spawn(new Location(world, 0, 0, 0), Sheep.class);
        Sheep sheep2 = world.spawn(new Location(world, 2, 0, 0), Sheep.class);

        assertEquals(20, sheep2.getHealth());

        skill.cast(sheep, 1, true);
        assertEquals(17, sheep2.getHealth());
    }

    @Test
    void passiveCanDamageHostile() {
        Zombie zombie = world.spawn(new Location(world, 0, 0, 0), Zombie.class);
        Sheep  sheep  = world.spawn(new Location(world, 2, 0, 0), Sheep.class);

        assertEquals(20, zombie.getHealth());

        skill.cast(sheep, 1, true);
        assertEquals(17, zombie.getHealth());
    }

    @Test
    void hostileCanDamagePassive() {
        Sheep  sheep  = world.spawn(new Location(world, 0, 0, 0), Sheep.class);
        Zombie zombie = world.spawn(new Location(world, 2, 0, 0), Zombie.class);

        assertEquals(20, sheep.getHealth());

        skill.cast(zombie, 1, true);
        assertEquals(17, sheep.getHealth());
    }

    @Test
    void hostileCanDamageHostile() {
        Zombie zombie  = world.spawn(new Location(world, 0, 0, 0), Zombie.class);
        Zombie zombie2 = world.spawn(new Location(world, 2, 0, 0), Zombie.class);

        assertEquals(20, zombie2.getHealth());

        skill.cast(zombie, 1, true);
        assertEquals(17, zombie2.getHealth());
    }

    @Test
    void passiveCantBeDamagedWhenPassiveAllyTrue() {
        Fabled.getSettings().setPassiveAlly(true);

        Sheep  sheep  = world.spawn(new Location(world, 0, 0, 0), Sheep.class);
        Zombie zombie = world.spawn(new Location(world, 2, 0, 0), Zombie.class);

        skill.cast(zombie, 1, true);
        assertEquals(20, sheep.getHealth());
    }

}
