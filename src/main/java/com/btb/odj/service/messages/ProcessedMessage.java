package com.btb.odj.service.messages;

public record ProcessedMessage(Class<?> processor, EntityMessage message) {}
