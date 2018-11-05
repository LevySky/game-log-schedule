package com.spc.schedule.storage.ctr;

import com.spc.common.Pagenation;
import com.spc.schedule.storage.bean.HandleResult;
import com.spc.schedule.storage.bean.LoggerPosition;
import com.spc.schedule.storage.bean.LoggerPositionHistory;
import com.spc.schedule.storage.repo.LoggerPositionHistoryRepository;
import com.spc.schedule.storage.repo.LoggerPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PositionController {

    @Autowired
    LoggerPositionRepository repository;

    @Autowired
    LoggerPositionHistoryRepository historyRepository;

    @PostMapping("/spc/position/edit")
    @ResponseBody
    public HandleResult position(@RequestBody LoggerPosition lp) {
        try {
            repository.save(lp);
            return HandleResult.of(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.of(true, e.getLocalizedMessage());
        }

    }


    @PostMapping("/spc/position/getAll")
    @ResponseBody
    public Page<LoggerPosition> getAllLoggerPosition(@RequestBody Pagenation<LoggerPosition> pagenation) {

        pagenation.getSearch().setPosition(pagenation.getSearch().getPosition() > 0 ? pagenation.getSearch().getPosition() : null);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("fileName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("position")
                ;


        Example<LoggerPosition> ex = Example.of(pagenation.getSearch(), matcher);
        PageRequest pageRequest = PageRequest.of(pagenation.getSkip(), pagenation.getPageSize(), new Sort(Sort.Direction.DESC, "updateTime"));
        repository.findAll();
        Page<LoggerPosition> page = repository.findAll(ex, pageRequest);
        return page;
    }

    @PostMapping("/spc/positionHistory/getAll")
    @ResponseBody
    public Page<LoggerPositionHistory> getHistory(@RequestBody Pagenation<LoggerPositionHistory> pagenation) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("fileName", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<LoggerPositionHistory> ex = Example.of(pagenation.getSearch(), matcher);
        PageRequest pageRequest = PageRequest.of(pagenation.getSkip(), pagenation.getPageSize(), new Sort(Sort.Direction.DESC, "updateTime"));
        Page<LoggerPositionHistory> page = historyRepository.findAll(ex, pageRequest);
        return page;
    }


}
