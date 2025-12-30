package com.example.learning_jpa.config;


import com.example.learning_jpa.entity.Stall;
import com.example.learning_jpa.enums.StallSize;
import com.example.learning_jpa.enums.StallStatus;
import com.example.learning_jpa.repository.StallDetailsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StallInitializer {

    @Autowired
    StallDetailsRepository stallRepository;


    @Value("${stall.count.small}")
    private int small;
    @Value("${stall.count.medium}")
    private int medium;
    @Value("${stall.count.large}")
    private int large;

    @PostConstruct
    public void init() {

        if (stallRepository.count() > 0) return;

        create("A", StallSize.SMALL, small);
        create("B", StallSize.MEDIUM, medium);
        create("C", StallSize.LARGE, large);
    }

    private void create(String prefix, StallSize size, int count) {
        for (int i = 1; i <= count; i++) {
            Stall stall = new Stall();
            stall.setStallCode(prefix + i);
            stall.setSize(size);
            stall.setStatus(StallStatus.AVAILABLE);
            stallRepository.save(stall);
        }
    }


}
