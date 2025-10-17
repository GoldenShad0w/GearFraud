package goldenshadow.gearviewspoofer.client;

public enum Quality {
    PERFECT("Perfect"),
    DEFECTIVE("Defective"),
    DEFAULT("Default");

    private final String displayName;

    Quality(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
