package com.hawolt.ui.chat;

import com.hawolt.ui.chat.profile.ChatStatus;

/**
 * Created: 08/08/2023 19:22
 * Author: Twitter @hawolt
 **/

public interface ChatStatusCallback {
    void onChatStatus(ChatStatus status);
}
