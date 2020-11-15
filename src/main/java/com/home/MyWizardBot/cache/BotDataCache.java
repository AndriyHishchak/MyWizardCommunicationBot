package com.home.MyWizardBot.cache;

import com.home.MyWizardBot.botApi.BotState;
import com.home.MyWizardBot.botApi.handlers.ProfileFormData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotDataCache implements DataCache {

   private final Map<Integer,BotState> usersBotStates = new HashMap<>();
   private final Map<Integer,ProfileFormData> profileForms = new HashMap<>();

    @Override
    public BotState getUsersCarrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.ASK_DESTINY;
        }
        return botState;
    }

    @Override
    public ProfileFormData getProfileData(int userId) {
        ProfileFormData profileFormData = profileForms.get(userId);
        if (profileFormData == null) {
            profileFormData = new ProfileFormData();
        }
        return profileFormData;
    }
    @Override
    public void setUsersCarrentBotState(int userId, BotState botState) {
         usersBotStates.put(userId,botState);
    }

    @Override
    public void saveProfileFormData(int userId, ProfileFormData profileFormData) {
        profileForms.put(userId,profileFormData);
    }
}
