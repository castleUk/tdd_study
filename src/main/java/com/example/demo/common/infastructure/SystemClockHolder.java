package com.example.demo.common.infastructure;

import com.example.demo.common.service.port.ClockHolder;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {
	@Override
	public long millis() {
		return Clock.systemUTC()
		            .millis();
	}
}
