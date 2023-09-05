package com.hawolt.ui.chat.friendlist;

import com.hawolt.client.misc.SortOrder;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Created: 05/09/2023 01:59
 * Author: Twitter @hawolt
 **/

public class ChatListComparator implements Comparator<ChatSidebarFriend> {
    private final ChatListComparatorType type;
    private final SortOrder sortOrder;

    public ChatListComparator(ChatListComparatorType type, SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.type = type;
    }

    @Override
    public int compare(ChatSidebarFriend o1, ChatSidebarFriend o2) {
        switch (type) {
            case NAME -> {
                return compareValues(o1, o2, o -> o.getProvidedUsername().toLowerCase());
            }
            case STATUS -> {
                return compareValues(o1, o2, o -> o.getConnectionStatus().ordinal());
            }
        }
        return -1;
    }

    public <R extends Comparable<R>> int compareValues(ChatSidebarFriend o1, ChatSidebarFriend o2, Function<ChatSidebarFriend, R> getValue) {
        R val1 = getValue.apply(o1);
        R val2 = getValue.apply(o2);
        return switch (this.sortOrder) {
            case DESCENDING -> val2.compareTo(val1);
            case ASCENDING -> val1.compareTo(val2);
        };
    }
}
