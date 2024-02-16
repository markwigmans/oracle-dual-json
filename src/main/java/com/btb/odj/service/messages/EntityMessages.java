package com.btb.odj.service.messages;

import java.util.List;

public record EntityMessages(Class<?> type, List<EntityMessage> messages) {}
