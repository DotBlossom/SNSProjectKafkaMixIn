package com.delta.delta.service;

import com.delta.delta.DTO.NotificationsDto;
import com.delta.delta.DTO.PreferenceDataDto;

public interface DataAggToPreferenceService {

    void consumeAgg(NotificationsDto notificationsDto);
    PreferenceDataDto sendPreferData();
}
