package com.youthhealth.modules.analytics.service;

// Extension point for future LLM-based advice generation.
public interface LLMAdviceService {
    String generateAdvice(String promptContext);
}
