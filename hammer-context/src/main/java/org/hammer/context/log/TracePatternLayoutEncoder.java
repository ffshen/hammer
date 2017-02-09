package org.hammer.context.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class TracePatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {
    @Override
    public void start() {
        TracePatternLayout patternLayout = new TracePatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }
}
