package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:53
 * Author: Twitter @hawolt
 **/

public class GatekeeperRestriction {
    private final long accountId, remainingMillis;
    private final String reason;
    private final int queueId;

    public GatekeeperRestriction(JSONObject o) {
        this.remainingMillis = o.getLong("remainingMillis");
        this.accountId = o.getLong("accountId");
        this.reason = o.getString("reason");
        this.queueId = o.getInt("queueId");
    }

    public long getAccountId() {
        return accountId;
    }

    public long getRemainingMillis() {
        return remainingMillis;
    }

    public String getReason() {
        return reason;
    }

    public int getQueueId() {
        return queueId;
    }

    @Override
    public String toString() {
        return "GatekeeperRestriction{" +
                "accountId=" + accountId +
                ", remainingMillis=" + remainingMillis +
                ", reason='" + reason + '\'' +
                ", queueId=" + queueId +
                '}';
    }
}
