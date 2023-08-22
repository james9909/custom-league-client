package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:28
 * Author: Twitter @hawolt
 **/

public class PartyMucJwtDto {
    private final String jwt, channelClaim, domain, targetRegion;

    public PartyMucJwtDto(JSONObject o) {
        this.targetRegion = o.getString("targetRegion");
        this.channelClaim = o.getString("channelClaim");
        this.domain = o.getString("domain");
        this.jwt = o.getString("jwt");
    }

    public String getJwt() {
        return jwt;
    }

    public String getChannelClaim() {
        return channelClaim;
    }

    public String getDomain() {
        return domain;
    }

    public String getTargetRegion() {
        return targetRegion;
    }

    @Override
    public String toString() {
        return "PartyMucJwtDto{" +
                "jwt='" + jwt + '\'' +
                ", channelClaim='" + channelClaim + '\'' +
                ", domain='" + domain + '\'' +
                ", targetRegion='" + targetRegion + '\'' +
                '}';
    }
}
