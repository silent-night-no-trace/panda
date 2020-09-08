package com.style.registry.auto;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author leon
 * @date 2020-07-13 16:50:12
 */
public class CustomSelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{CustomBeanConfiguration.class.getName()};
	}
}
