package com.mihir.sse.processors;

import com.mihir.sse.domain.MemoryInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Service
public class MemoryObserverJob {

    public final ApplicationEventPublisher eventPublisher;

    public MemoryObserverJob(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 1000)
    public void printTime() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

        MemoryInfo mi = new MemoryInfo(heap.getUsed(), nonHeap.getUsed());
        this.eventPublisher.publishEvent(mi);
    }
}
