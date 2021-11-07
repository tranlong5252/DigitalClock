package cz.perwin.digitalclock;

public class AfterDone implements Runnable {
    private final DigitalClock i;

    public AfterDone(DigitalClock i) {
        this.i = i;
    }

    public void run() {
        this.i.getClocks();
        this.i.getConsole().info("[DigitalClock] Loaded " + this.i.getClocksL().size() + " clock(s).");
        if (i.runStart())
            this.i.runTasks();
    }
}
