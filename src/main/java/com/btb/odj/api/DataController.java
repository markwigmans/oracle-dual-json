package com.btb.odj.api;

import com.btb.odj.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;

    @GetMapping("/{count}")
    public void createRaces(@PathVariable int count) {
        CompletableFuture<Void> dataset = dataService.createDataset(count);
        dataset.whenComplete((r, ex) -> {
            log.info("Data created");
            if (ex != null) {
                log.error("Exception", ex);
            }
        });
    }

    @GetMapping("/sync")
    public void syncData() {
        dataService.syncData();
    }
}
