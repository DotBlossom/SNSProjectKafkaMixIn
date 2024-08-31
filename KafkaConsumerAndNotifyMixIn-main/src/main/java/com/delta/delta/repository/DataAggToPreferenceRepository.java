package com.delta.delta.repository;

import com.delta.delta.DTO.PreferenceDataDto;

import java.util.HashMap;

public interface DataAggToPreferenceRepository {

    boolean save(HashMap<Long, Long> hashMap);
    PreferenceDataDto sendPreferData();
}
