package com.whattobake.api.Interfaces;

public interface InsertRequestDto<UpdateRequest,Id> {
    UpdateRequest toUpdateRequest(Id id);
}
