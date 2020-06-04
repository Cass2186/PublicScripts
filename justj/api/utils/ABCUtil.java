package scripts.justj.api.utils;

import org.tribot.api.interfaces.Clickable;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api.util.abc.preferences.WalkingPreference;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.types.RSObject;
import scripts.justj.api.JustLogger;

import java.util.Arrays;
import java.util.Optional;

/**
 * Original author: Encoded
 */
public class ABCUtil extends org.tribot.api.util.abc.ABCUtil {

    private static final JustLogger LOGGER = new JustLogger(ABCUtil.class);

    private static double reactionSleepModifier = 0.6;

    private static ABCUtil instance = new ABCUtil();
    private int runActivation;
    private int eatAt;
    private int eatModifier = 15;

    private ABCUtil() {
        super();
        runActivation = generateRunActivation();
        eatAt = super.generateEatAtHP() + eatModifier;
    }

    public static ABCUtil getInstance() {
        return instance;
    }

    public static void setReactionSleepModifier(double modifier) {
        if (modifier > 1.0) {
            modifier = 1.0;
        } else if (modifier < 0) {
            modifier = 0;
        }
        reactionSleepModifier = modifier;
    }

    public static void terminate() {
        getInstance().close();
    }

    public static boolean shouldActivateRun() {
        return Game.getRunEnergy() >= getInstance().runActivation && !Game.isRunOn();
    }

    public static void activateRun() {
        if (Options.setRunEnabled(true)) {
            getInstance().runActivation = getInstance().generateRunActivation();
        }
    }

    public static void performRunActivation() {
        if (shouldActivateRun()) {
            activateRun();
        }
    }

    public static boolean shouldEat() {
        return Combat.getHPRatio() <= getInstance().eatAt;
    }

    public static void generateEatAtPercentage() {
        getInstance().eatAt = getInstance().generateEatAtHP() + getInstance().eatModifier;

        LOGGER.info(String.format("Next eat at %s%%", getInstance().eatAt));
    }

    public static OpenBankPreference getOpenBankPreference() {
        return getInstance().generateOpenBankPreference();
    }

    public static WalkingPreference getWalkingPreference(int distance) {
        return getInstance().generateWalkingPreference(distance);
    }

    public static Positionable getNextTarget(Positionable[] positionables) {
        return getInstance().selectNextTarget(positionables);
    }

    public static boolean shouldHoverNext() {
        return getInstance().shouldHover();
    }

    public static boolean shouldOpenNextMenu() {
        return getInstance().shouldOpenMenu();
    }

    public static void waitReactionTime(long waitingTime) {
        waitReactionTime(waitingTime, false, false);
    }

    public static void waitReactionTime(long waitingTime, boolean waitingFixed) {
        waitReactionTime(waitingTime, waitingFixed, false);
    }

    public static void waitReactionTime(long waitingTime, boolean waitingFixed, boolean underAttack) {
        getInstance().setProperties(waitingTime, waitingFixed, underAttack);
        LOGGER.debug("Waiting Time: " + waitingTime + " Reaction Time: " + reactionSleepModifier  );
        int reactionTime = (int) (getInstance().generateReactionTime() * reactionSleepModifier);
        getInstance().sleep(reactionTime);
    }

    public static void generateTrackers(long waitingTime, boolean waitingFixed) {
        generateTrackers(waitingTime, waitingFixed, false);
    }

    public static void generateTrackers(long waitingTime, boolean waitingFixed, boolean underAttack) {
        getInstance().setProperties(waitingTime, waitingFixed, underAttack);
        getInstance().generateTrackers();
    }

    public static void performActions() {
        performXPCheck();
        performCheckTabs();
        performRotateCamera();
        performExamineEntity();
        performMoveMouse();
        performRightClick();
        performLeaveGame();
        performPickupMouse();
    }

    public static void performXPCheck() {
        if (getInstance().shouldCheckXP()) {
            LOGGER.info("Checking XP");
            getInstance().checkXP();
        }
    }

    public static void performCheckTabs() {
        if (getInstance().shouldCheckTabs()) {
            LOGGER.info("Checking Tabs");
            getInstance().checkTabs();
        }
    }

    public static void performRotateCamera() {
        if (getInstance().shouldRotateCamera()) {
            LOGGER.info("Rotating Camera");
            getInstance().rotateCamera();
        }
    }

    public static void performExamineEntity() {
        if (getInstance().shouldExamineEntity()) {
            LOGGER.info("Examine entity");
            getInstance().examineEntity();
        }
    }

    public static void performMoveMouse() {
        if (getInstance().shouldMoveMouse()) {
            LOGGER.info("Moving mouse");
            getInstance().moveMouse();
        }
    }

    public static void performRightClick() {
        if (getInstance().shouldRightClick()) {
            LOGGER.info("Right clicking");
            getInstance().rightClick();
        }
    }

    public static void performLeaveGame() {
        if (getInstance().shouldLeaveGame()) {
            LOGGER.info("Leaving game");
            getInstance().leaveGame();
        }
    }

    public static void performPickupMouse() {
        if (getInstance().shouldPickupMouse()) {
            LOGGER.info("Picking up mouse");
            getInstance().pickupMouse();
        }
    }

    public static void performHoverNext(Clickable next) {
        if (ABCUtil.shouldHoverNext()) {
            LOGGER.info("Hovering next");
            Optional.ofNullable(next).ifPresent(Clickable::hover);
        }
    }


    private void setProperties(long waitingTime, boolean waitingFixed, boolean underAttack) {
        boolean hovering = shouldHover();
        boolean menuOpen = hovering && shouldOpenMenu();
        ABCProperties properties = getProperties();
        properties.setWaitingTime(Math.toIntExact(waitingTime));
        properties.setMenuOpen(menuOpen);
        properties.setHovering(hovering);
        properties.setUnderAttack(underAttack);
        properties.setWaitingFixed(waitingFixed);
    }

    private void sleep(int reactionTime) {
        if (getProperties().isWaitingFixed()) {
            LOGGER.info("Waiting time is fixed; not waiting reaction time until TRiLeZ gathers more data.");
            return;
        }
        LOGGER.info("Waiting for ABC2 reaction time: " + org.tribot.api.Timing.msToString(reactionTime) + " (" + reactionTime + "ms)");
        try {
            super.sleep(reactionTime);
        } catch (InterruptedException e) {
            LOGGER.info("ABC2 Sleep Interrupted.");
        }
    }

}
