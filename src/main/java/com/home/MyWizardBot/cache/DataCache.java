package com.home.MyWizardBot.cache;

import com.home.MyWizardBot.botApi.BotState;
import com.home.MyWizardBot.botApi.handlers.ProfileFormData;

public interface DataCache {
    BotState getUsersCarrentBotState (int userId);
    void setUsersCarrentBotState (int userId,BotState botState);

    ProfileFormData getProfileData(int userId);

    void saveProfileFormData(int userId, ProfileFormData profileFormData);
}
