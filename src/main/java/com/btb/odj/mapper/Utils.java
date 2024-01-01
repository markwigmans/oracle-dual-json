package com.btb.odj.mapper;

import com.btb.odj.model.Data_AbstractEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    String IDtoString(Data_AbstractEntity source) {
        if (source != null && source.getId() != null) {
            return source.getId().toString();
        } else {
            return null;
        }
    }
}
