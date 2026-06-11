package defaultPackage.dto;

public class LimitResponse {
    private long dailyLimit;
    private long remaining;
    public LimitResponse(long dailyLimit, long remaining) {
        this.dailyLimit = dailyLimit;
        this.remaining = remaining;
    }
    public long getDailyLimit() { return dailyLimit; }
    public long getRemaining() { return remaining; }
}