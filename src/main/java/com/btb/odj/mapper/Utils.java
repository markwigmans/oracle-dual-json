package com.btb.odj.mapper;

import com.btb.odj.model.jpa.J_AbstractEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    String IDtoString(J_AbstractEntity source) {
        if (source != null && source.getId() != null) {
            return source.getId().toString();
        } else {
            return null;
        }
    }
}
