package com.hawolt.client.resources.ledge;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.inventory.InventoryServiceLedge;
import com.hawolt.client.resources.ledge.leagues.LeagueLedge;
import com.hawolt.client.resources.ledge.loot.LootLedge;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.perks.PerksLedge;
import com.hawolt.client.resources.ledge.store.StoreLedge;
import com.hawolt.client.resources.ledge.summoner.SummonerLedge;
import com.hawolt.client.resources.ledge.teambuilder.TeamBuilderLedge;
import com.hawolt.virtual.leagueclient.client.VirtualLeagueClient;
import com.hawolt.yaml.ConfigValue;
import com.hawolt.yaml.YamlWrapper;

/**
 * Created: 19/01/2023 16:03
 * Author: Twitter @hawolt
 **/

public class LedgeEndpoint {
    private final InventoryServiceLedge inventoryServiceLedge;
    private final TeamBuilderLedge teamBuilderLedge;
    private final SummonerLedge summonerLedge;
    private final PartiesLedge partiesLedge;
    private final LeagueLedge leagueLedge;
    private final StoreLedge storeLedge;
    private final PerksLedge perksLedge;
    private final LootLedge lootLedge;

    public LedgeEndpoint(LeagueClient client) {
        VirtualLeagueClient virtualLeagueClient = client.getVirtualLeagueClient();
        YamlWrapper wrapper = virtualLeagueClient.getYamlWrapper();
        this.lootLedge = new LootLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.storeLedge = new StoreLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.perksLedge = new PerksLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.leagueLedge = new LeagueLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.partiesLedge = new PartiesLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.summonerLedge = new SummonerLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.teamBuilderLedge = new TeamBuilderLedge(client, wrapper.get(ConfigValue.LEDGE));
        this.inventoryServiceLedge = new InventoryServiceLedge(client, wrapper.get(ConfigValue.LEDGE));
    }

    public InventoryServiceLedge getInventoryService() {
        return inventoryServiceLedge;
    }

    public SummonerLedge getSummoner() {
        return summonerLedge;
    }

    public PartiesLedge getParties() {
        return partiesLedge;
    }

    public LeagueLedge getLeague() {
        return leagueLedge;
    }

    public TeamBuilderLedge getTeamBuilder() {
        return teamBuilderLedge;
    }

    public LootLedge getLoot() {
        return lootLedge;
    }

    public StoreLedge getStore() {
        return storeLedge;
    }

    public PerksLedge getPerks() {
        return perksLedge;
    }
}
