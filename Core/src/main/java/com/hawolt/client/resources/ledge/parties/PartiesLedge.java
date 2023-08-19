package com.hawolt.client.resources.ledge.parties;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.client.resources.ledge.LedgeEndpoint;
import com.hawolt.client.resources.ledge.parties.objects.PartiesRegistration;
import com.hawolt.client.resources.ledge.parties.objects.PartyException;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyAction;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyRole;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyType;
import com.hawolt.client.resources.ledge.parties.objects.data.PositionPreference;
import com.hawolt.client.resources.ledge.parties.objects.invitation.PartyInvitation;
import com.hawolt.client.resources.ledge.summoner.objects.Summoner;
import com.hawolt.generic.Constant;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.virtual.leagueclient.authentication.Userinfo;
import com.hawolt.virtual.leagueclient.client.Authentication;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created: 19/01/2023 16:04
 * Author: Twitter @hawolt
 **/

public class PartiesLedge extends AbstractLedgeEndpoint {

    private PartiesRegistration current;

    public PartiesLedge(LeagueClient client, String base) {
        super(client, base);
    }

    public String getGameClientVersion() {
        String leagueClientVersion = gameVersionSupplier.getVersionValue("League of Legends.exe");
        int majorIndex = leagueClientVersion.indexOf('.');
        String major = leagueClientVersion.substring(0, majorIndex);
        int minorIndex = leagueClientVersion.indexOf('.', majorIndex + 1);
        String minor = leagueClientVersion.substring(majorIndex + 1, minorIndex);
        String remainder = leagueClientVersion.substring(minorIndex + 1).replaceAll("\\.", "");
        return String.format(
                "%s.%s.%s+branch.releases-%s-%s.code.public.content.release",
                major,
                minor,
                remainder,
                major,
                minor
        );
    }

    public PartiesRegistration register() throws IOException {
        JSONObject object = new JSONObject();
        object.put("accountId", userInformation.getOriginalAccountId());
        object.put("createdAt", 0L);
        object.put("currentParty", JSONObject.NULL);
        object.put("eligibilityHash", 0L);
        object.put("parties", JSONObject.NULL);
        object.put("platformId", platform.name());
        object.put("puuid", userInformation.getSub());
        JSONObject registration = new JSONObject();
        registration.put("gameClientVersion", getGameClientVersion());
        registration.put("inventoryToken", JSONObject.NULL);
        registration.put("inventoryTokens", new JSONArray());
        LedgeEndpoint ledge = client.getLedge();
        registration.put("rankedOverviewToken", ledge.getLeague().getRankedOverviewToken());
        registration.put("simpleInventoryToken", ledge.getInventoryService().getInventoryToken());
        registration.put("summonerToken", ledge.getSummoner().getSummonerToken());
        registration.put("userInfoToken", ((Userinfo) virtualLeagueClient.get(Authentication.USERINFO)).get("lol.userinfo_token", true));
        object.put("registration", registration);
        object.put("serverUtcMillis", 0L);
        object.put("summonerId", userInformation.getUserInformationLeagueAccount().getSummonerId());
        object.put("version", 0L);
        String uri = String.format("%s/%s/v%s/players/%s",
                base,
                name(),
                version(),
                userInformation.getSub()
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create(object.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartyInvitation invite(Summoner summoner) throws IOException, PartyException {
        return invite(current, summoner.getPUUID());
    }

    public PartyInvitation invite(PartiesRegistration registration, Summoner summoner) throws IOException, PartyException {
        return invite(registration, summoner.getPUUID());
    }

    public PartyInvitation invite(Summoner... summoners) throws IOException, PartyException {
        return invite(current, Arrays.stream(summoners).map(Summoner::getPUUID).toArray(String[]::new));
    }

    public PartyInvitation invite(PartiesRegistration registration, Summoner... summoners) throws IOException, PartyException {
        return invite(Arrays.stream(summoners).map(Summoner::getPUUID).toArray(String[]::new));
    }

    public PartyInvitation invite(String... puuids) throws IOException, PartyException {
        return invite(current, puuids);
    }

    public PartyInvitation invite(PartiesRegistration registration, String... puuids) throws IOException, PartyException {
        if (registration == null) throw new PartyException();
        String uri = String.format("%s/%s/v%s/parties/%s/invite",
                base,
                name(),
                version(),
                current.getFirstPartyId()
        );
        JSONArray array = new JSONArray();
        for (String puuid : puuids) {
            array.put(puuid);
        }
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(array.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return new PartyInvitation(o);
            }
        }
    }

    public PartiesRegistration gamemode(String partyId, long maxPartySize, long maxTeamSize, long queueId) throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/gamemode",
                base,
                name(),
                version(),
                partyId
        );
        JSONObject object = new JSONObject();
        object.put("allowSpectators", JSONObject.NULL);
        object.put("botDifficulty", JSONObject.NULL);
        object.put("customsSettings", JSONObject.NULL);
        object.put("gameCustomization", new JSONObject());
        object.put("gameType", "");
        object.put("gameTypeConfigId", JSONObject.NULL);
        object.put("mapId", JSONObject.NULL);
        object.put("maxPartySize", maxPartySize);
        object.put("maxTeamSize", maxTeamSize);
        object.put("queueId", queueId);
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create(object.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration partytype(String partyId, PartyType type) throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/partytype",
                base,
                name(),
                version(),
                partyId
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create(type.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration leave(String partyId, PartyRole role) throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/members/%s/role",
                base,
                name(),
                version(),
                partyId,
                userInformation.getSub()
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create(role.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration setQueueAction(String partyId, PartyAction action) throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/members/%s/%s",
                base,
                name(),
                version(),
                partyId,
                userInformation.getSub(),
                action.name().toLowerCase() + "Action"
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .post(RequestBody.create(new byte[0], Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration metadata(String partyId, PositionPreference first, PositionPreference second) throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/members/%s/metadata",
                base,
                name(),
                version(),
                partyId,
                userInformation.getSub()
        );
        JSONObject object = new JSONObject();
        object.put("championSelection", JSONObject.NULL);
        object.put("properties", JSONObject.NULL);
        object.put("skinSelection", JSONObject.NULL);
        JSONArray array = new JSONArray();
        array.put(first.name());
        array.put(second.name());
        object.put("positionPref", array);
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create(object.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration ready() throws IOException {
        String uri = String.format("%s/%s/v%s/parties/%s/members/%s/ready",
                base,
                name(),
                version(),
                current.getFirstPartyId(),
                userInformation.getSub()
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json")
                .put(RequestBody.create("true", Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject o = new JSONObject(plain);
                return (current = new PartiesRegistration(o));
            }
        }
    }

    public PartiesRegistration getCurrentRegistration() {
        return current;
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public String name() {
        return "parties-ledge";
    }

    @Override
    public String rcp() {
        return "rcp-be-lol-lobby";
    }

}
