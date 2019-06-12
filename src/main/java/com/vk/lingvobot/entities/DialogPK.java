package com.vk.lingvobot.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@Slf4j
public class DialogPK implements Serializable {

    private Integer dialogId;
    private Integer state;

    public DialogPK(){}

    public DialogPK(Integer dialogId, Integer state) {
        this.dialogId = dialogId;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DialogPK dialogPK = (DialogPK) o;
        return Objects.equals(dialogId, dialogPK.dialogId) &&
                Objects.equals(state, dialogPK.state);
    }
}

