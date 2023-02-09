package com.whattobake.api.Dto.SseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PbAction {
    private com.whattobake.api.Enum.PbAction action;
    private Map<String,Object> record;
}
