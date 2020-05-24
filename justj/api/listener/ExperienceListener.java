package scripts.justj.api.listener;

import org.tribot.api2007.Skills;

public interface ExperienceListener {

    void experienceGained(Skills.SKILLS skill, int amount);

    void levelGained(Skills.SKILLS skill, int amount);

}
