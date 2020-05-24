package scripts.justj.api.listener;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;

/**
 * @author Encoded
 */
public class ExperienceObserver extends Thread {

    private boolean isRunning;
    private ExperienceListener listener;
    private Skills.SKILLS[] skills;
    private int[] previousExperience;
    private int[] previousLevels;

    public ExperienceObserver(ExperienceListener listener, Skills.SKILLS... skills) {
        super("Experience Observer Thread");
        this.listener = listener;
        this.skills = skills;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            General.sleep(200);
            checkForExperienceChanges();
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private void checkForExperienceChanges() {
        if (listener == null) {
            shutdown();
            return;
        }

        if (Login.getLoginState() != Login.STATE.INGAME) {
            return;
        }

        int[] currentExperience = getExperience();
        int[] currentLevels = getLevels();

        if (previousExperience == null || previousLevels == null) {
            previousExperience = currentExperience;
            previousLevels = currentLevels;
            return;
        }

        for (int i = 0; i < currentExperience.length; i++) {
            if (currentExperience[i] > previousExperience[i]) {
                listener.experienceGained(skills[i], currentExperience[i] - previousExperience[i]);
            }
            if (currentLevels[i] > previousLevels[i]) {
                listener.levelGained(skills[i], currentLevels[i] - previousLevels[i]);
            }
        }

        previousExperience = currentExperience.clone();
        previousLevels = currentLevels.clone();
    }

    private int[] getExperience() {
        int[] temp = new int[skills.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = skills[i].getXP();
        }
        return temp;
    }

    private int[] getLevels() {
        int[] temp = new int[skills.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = skills[i].getActualLevel();
        }
        return temp;
    }

}
