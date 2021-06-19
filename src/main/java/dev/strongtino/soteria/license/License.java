package dev.strongtino.soteria.license;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class License {

    @SerializedName("_id")
    private final String key;

    private final String user;
    private final String software;

    private final long createdAt;

    private long revokedAt;
    private long duration;

    private boolean active;

    public License(String key, String user, String product, long duration) {
        this(key, user, product, System.currentTimeMillis(), 0L, duration, true);
    }

    public boolean isExpired() {
        return duration != Long.MAX_VALUE && System.currentTimeMillis() > createdAt + duration;
    }

    public void revoke() {
        active = false;
        revokedAt = System.currentTimeMillis();
    }
}
