package com.btb.odj.api;

import com.btb.odj.service.RequestService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/driver/points/{points}")
    public Map<String, List<?>> findDriversWithMoreThan(@PathVariable int points) {
        return requestService.findDriversWithMoreThan(points);
    }
}
