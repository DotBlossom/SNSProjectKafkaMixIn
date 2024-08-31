package com.delta.delta.DTO;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PreferenceDataDto {

    private String eventType;
    //private List<String> postFileNames;
    private List<Long> postIdToPrefer;

}
