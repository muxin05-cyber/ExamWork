package defaultPackage.dto;

import java.util.UUID;

public class CompareRequest {
    private UUID id1;
    private UUID id2;

    public UUID getId1() { return id1; }
    public void setId1(UUID id1) { this.id1 = id1; }

    public UUID getId2() { return id2; }
    public void setId2(UUID id2) { this.id2 = id2; }
}

