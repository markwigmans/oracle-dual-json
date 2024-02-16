package com.btb.odj.service.messages;

public record ProcessedMessage(Class<?> processor, EntityMessages message) {}
