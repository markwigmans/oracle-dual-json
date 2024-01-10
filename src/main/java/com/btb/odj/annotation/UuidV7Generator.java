package com.btb.odj.annotation;

import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(com.btb.odj.util.UuidV7Generator.class)
@ValueGenerationType(generatedBy = com.btb.odj.util.UuidV7Generator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface UuidV7Generator {}
